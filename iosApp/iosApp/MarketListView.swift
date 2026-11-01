import Charts
import Shared
import SwiftUI

/// The Markets list: sort, top mover, filter and the synced top 250. `wide` means the detail sits beside the
/// list, which then leaves out the top mover, as Android's pane does.
struct MarketListView: View {
    let model: MarketListModel
    @Binding var selectedID: String?
    let wide: Bool
    let openFavorites: () -> Void

    var body: some View {
        let state = model.state
        Group {
            switch onEnum(of: state.marketList) {
            case .initial, .loading:
                MarketListSkeleton()
            case .error(let error):
                MarketErrorPane(
                    error: error.error,
                    retryInSeconds: state.retryInSeconds,
                    onRefresh: refresh,
                    openFavorites: openFavorites
                )
            case .loaded:
                list(state)
            }
        }
        .toolbar {
            ToolbarItem {
                if state.marketList.isLoading {
                    // The canvas's glass loader: the toolbar item renders as a glass circle.
                    ProgressView()
                        .accessibilityLabel(Text("Loading markets"))
                } else {
                    sortMenu(state)
                }
            }
        }
        .overlay(alignment: .bottom) {
            if let error = state.syncError {
                SyncErrorBanner(message: errorMessage(error)) {
                    model.send(MarketListContractEvent.OnSyncErrorShown.shared)
                }
                .id(ObjectIdentifier(error))
            }
        }
        .animation(.default, value: state.syncError == nil)
    }

    private func list(_ state: MarketListContractState) -> some View {
        List(selection: $selectedID) {
            if let mover = state.topMover, !wide {
                Section {
                    TopMoverCard(market: mover) { selectedID = mover.id }
                        .listRowInsets(EdgeInsets())
                        .listRowBackground(Color.clear)
                }
            }
            Section {
                Picker("Filter", selection: Binding(
                    get: { state.filter },
                    set: { model.send(MarketListContractEvent.OnFilterSelect(filter: $0)) }
                )) {
                    Text("All").tag(MarketListContractFilter.all)
                    Text("Gainers").tag(MarketListContractFilter.gainers)
                    Text("Losers").tag(MarketListContractFilter.losers)
                }
                .pickerStyle(.segmented)
                .listRowBackground(Color.clear)
                .listRowInsets(EdgeInsets())
            }
            Section {
                // `count` on the NSArray: no bridging of the list on every countdown tick.
                if (state.marketList.data?.count ?? 0) == 0 {
                    Group {
                        if state.filter == .all {
                            Text("No markets")
                        } else {
                            Text("No markets match this filter")
                        }
                    }
                    .foregroundStyle(.secondary)
                }
                MarketRows(list: state.marketList)
                    .equatable()
            } header: {
                Text(headerTitle(state.header))
                    .font(.title2.bold())
                    .foregroundStyle(.primary)
                    .textCase(nil)
                    .accessibilityAddTraits(.isHeader)
            }
        }
        .refreshable { await refreshAndWait() }
    }

    private func sortMenu(_ state: MarketListContractState) -> some View {
        Menu {
            Picker("Sort by", selection: Binding(
                get: { state.sort },
                set: { model.send(MarketListContractEvent.OnSortSelect(sort: $0)) }
            )) {
                Label("Market cap", systemImage: "chart.pie").tag(MarketListContractSort.marketCap)
                Label("Volume", systemImage: "chart.bar").tag(MarketListContractSort.volume)
            }
        } label: {
            Label("Sort markets", systemImage: "arrow.up.arrow.down")
        }
    }

    private func refresh() {
        model.send(MarketListContractEvent.OnRefresh.shared)
    }

    /// `.refreshable` keeps its spinner until this returns. The ViewModel sets `isRefreshing` synchronously, so
    /// `currentState` (not `state`, which catches up a hop later) already has it; during a 429 countdown it stays false.
    private func refreshAndWait() async {
        refresh()
        // ponytail: polls; an awaitable sync from the ViewModel if this ever matters.
        while model.currentState.isRefreshing {
            guard (try? await Task.sleep(for: .milliseconds(100))) != nil else { return }
        }
    }

    private func headerTitle(_ header: MarketListContractHeader) -> LocalizedStringKey {
        switch header {
        case .byMarketCap: "Top 250 · By market cap"
        case .byVolume: "Top 250 · By volume"
        case .gainers: "Top 250 gainers · 24h"
        case .losers: "Top 250 losers · 24h"
        }
    }
}

/// The rows, redrawn only for a new list: a sync's `isRefreshing` or a 429's countdown changes the rest of the
/// state every second but keeps the same `LoadableData`.
private struct MarketRows: View, Equatable {
    let list: LoadableData<NSArray>

    static func == (lhs: Self, rhs: Self) -> Bool {
        lhs.list === rhs.list
    }

    var body: some View {
        ForEach(list.data as? [MarketModel] ?? [], id: \.id) { market in
            NavigationLink(value: market.id) {
                MarketRow(market: market)
            }
        }
    }
}

/// The biggest 24h gainer of all synced markets, whatever the filter: a dark card with the 7-day trend as a
/// gradient chart along its bottom. A button rather than a `NavigationLink`: the mover is often a row too, and two
/// rows with one selection tag both highlight.
private struct TopMoverCard: View {
    let market: MarketModel
    let onOpen: () -> Void
    @ScaledMetric(relativeTo: .largeTitle) private var priceSize: CGFloat = 34

    var body: some View {
        Button(action: onOpen) {
            VStack(alignment: .leading, spacing: 10) {
                HStack {
                    Text("Top Mover in Top 250 Today")
                        .font(.footnote.weight(.semibold))
                        .foregroundStyle(.white.opacity(0.78))
                    Spacer(minLength: 8)
                    TrendChange(change: market.priceChangePercentage24h)
                        .font(.footnote.weight(.semibold))
                        .padding(.horizontal, 10)
                        .padding(.vertical, 4)
                        .glassEffect(in: .capsule)
                }
                HStack(spacing: 12) {
                    MarketLogo(url: market.imageUrl, size: 44)
                    VStack(alignment: .leading) {
                        Text(market.name).font(.title2.bold()).lineLimit(1)
                        Text(market.symbol.uppercased())
                            .font(.subheadline)
                            .foregroundStyle(.white.opacity(0.72))
                    }
                }
                Text(market.currentPrice, format: priceFormat(market.currentPrice))
                    .font(.system(size: priceSize, weight: .bold))
                    .monospacedDigit()
                    .padding(.bottom, 40)
            }
            .foregroundStyle(.white)
            .padding(EdgeInsets(top: 18, leading: 18, bottom: 12, trailing: 18))
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(alignment: .bottom) {
                if let up = market.isTrendingUp7d?.boolValue {
                    MoverChart(points: market.sparkline7d.map(\.doubleValue), up: up)
                        .frame(height: 96)
                }
            }
            .background(Color(red: 0.11, green: 0.11, blue: 0.12))
            .clipShape(.rect(cornerRadius: 28))
            // The card is dark in both modes, so its glass and trend colors resolve as in dark mode.
            .environment(\.colorScheme, .dark)
            .contentShape(.rect)
        }
        .buttonStyle(.plain)
        .accessibilityElement(children: .combine)
        .accessibilityHint(Text("Opens \(market.name) details"))
    }
}

/// The mover's 7-day prices as a line over a fading fill.
private struct MoverChart: View {
    let points: [Double]
    let up: Bool

    var body: some View {
        let color = trendColor(up)
        Chart(Array(points.enumerated()), id: \.offset) { point in
            AreaMark(x: .value("Time", point.offset), y: .value("Price", point.element))
                .foregroundStyle(.linearGradient(
                    colors: [color.opacity(0.45), color.opacity(0)],
                    startPoint: .top,
                    endPoint: .bottom
                ))
            LineMark(x: .value("Time", point.offset), y: .value("Price", point.element))
                .foregroundStyle(color)
                .lineStyle(StrokeStyle(lineWidth: 2))
        }
        .chartXAxis(.hidden)
        .chartYAxis(.hidden)
        .chartYScale(domain: .automatic(includesZero: false))
        .chartPlotStyle { $0.padding(.top, 8) }
        .accessibilityHidden(true)
    }
}

/// Nothing stored and the sync failed: a plain error, or a 429 whose `Try again` waits out the countdown.
private struct MarketErrorPane: View {
    let error: KtorErrors
    let retryInSeconds: Int32
    let onRefresh: () -> Void
    let openFavorites: () -> Void

    private var rateLimited: Bool {
        if case .rateLimited = onEnum(of: error) { return true }
        return false
    }

    var body: some View {
        let waiting = retryInSeconds > 0
        ContentUnavailableView {
            Label {
                rateLimited ? Text("Too many requests") : Text("Couldn't load markets")
            } icon: {
                Image(systemName: rateLimited ? "hourglass" : "icloud.slash")
            }
        } description: {
            if !rateLimited {
                Text("Prices couldn't load. Check your connection and try again.")
            } else if waiting {
                Text("You can try again when the timer ends.")
            } else {
                Text("You can try again now.")
            }
        } actions: {
            Button(action: onRefresh) {
                Label(formatRetry(retryInSeconds), systemImage: waiting ? "timer" : "arrow.clockwise")
                    .monospacedDigit()
            }
            .buttonStyle(.glass)
            .controlSize(.large)
            .disabled(waiting)
            Button("Open saved favorites", action: openFavorites)
        }
    }
}

/// The first load: placeholder rows in the shape of the real ones.
private struct MarketListSkeleton: View {
    private static let placeholder = MarketModel(
        id: "",
        name: "Placeholder",
        symbol: "pholder",
        currentPrice: 12_345.67,
        priceChangePercentage24h: 1.23,
        imageUrl: "",
        isFavorite: false,
        marketCapRank: nil,
        totalVolume: nil,
        sparkline7d: []
    )

    var body: some View {
        List(0..<8, id: \.self) { _ in
            MarketRow(market: Self.placeholder)
        }
        .redacted(reason: .placeholder)
        .disabled(true)
        .accessibilityElement(children: .ignore)
        .accessibilityLabel(Text("Loading markets"))
    }
}

/// A failed sync while stored markets stay on screen, like Android's snackbar: shown for a few seconds, then
/// `onShown`, so coming back to the screen doesn't show it again.
private struct SyncErrorBanner: View {
    let message: String
    let onShown: () -> Void

    var body: some View {
        Label(message, systemImage: "exclamationmark.triangle")
            .font(.subheadline)
            .padding(.horizontal, 16)
            .padding(.vertical, 12)
            .background(.regularMaterial, in: .capsule)
            .padding()
            .transition(.move(edge: .bottom).combined(with: .opacity))
            .task {
                AccessibilityNotification.Announcement(message).post()
                // Cancelled (a newer error, a tab switch) means not shown: clearing then could drop the newer one.
                guard (try? await Task.sleep(for: .seconds(4))) != nil else { return }
                onShown()
            }
    }
}
