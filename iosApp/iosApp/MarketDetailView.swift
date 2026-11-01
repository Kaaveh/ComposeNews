import Charts
import Shared
import SwiftUI

/// One market: header, the edge-to-edge price chart for the picked range, and Market Data. Back and Share are
/// the toolbar's glass buttons; the favorite toggle is a glass capsule at the bottom on iPhone and sits next to
/// Share on iPad.
struct MarketDetailView: View {
    let market: MarketModel
    /// The split view shows both columns (iPad): the favorite sits in the toolbar and the tab bar stays.
    let wide: Bool
    // Created in `.task`, not here: SwiftUI evaluates a `@State` initial value on every rebuild of the struct.
    @State private var model: MarketDetailModel?

    var body: some View {
        let state = model?.state
        // The ViewModel's copy follows the database's favorite flag; until it exists, the list's.
        let shown = state?.market.data ?? market
        let favorite = FavoriteToggle(isFavorite: shown.isFavorite, name: shown.name) {
            model?.send(MarketDetailContractEvent.OnFavoriteClick(market: shown))
        }
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                MarketHeader(market: shown, state: state)
                    .padding(.horizontal)
                if let state {
                    PriceChartSection(chart: state.marketChart, range: state.chartRange) { range in
                        model?.send(MarketDetailContractEvent.OnChartRangeSelect(range: range))
                    }
                    MarketDataSection(detail: state.marketDetail)
                        .padding(.horizontal)
                }
            }
            .padding(.vertical)
        }
        .background(Color(.systemGroupedBackground))
        .safeAreaInset(edge: .bottom) {
            if !wide {
                favorite.controlSize(.large)
            }
        }
        .toolbar {
            ToolbarItem {
                ShareLink(item: shareText(shown))
            }
            if wide {
                ToolbarItem { favorite }
            }
        }
        .toolbarVisibility(wide ? .automatic : .hidden, for: .tabBar)
        .navigationBarTitleDisplayMode(.inline)
        .task {
            // Once per view, so coming back to the screen doesn't fetch again.
            guard model == nil else { return }
            let model = MarketDetailModel()
            model.send(MarketDetailContractEvent.SetMarket(market: market))
            model.send(MarketDetailContractEvent.GetMarketChart(marketId: market.id))
            model.send(MarketDetailContractEvent.GetMarketDetail(marketId: market.id))
            self.model = model
        }
        // A sync or a favorite toggle brings a new copy of the same market.
        .onChange(of: market) { _, market in
            model?.send(MarketDetailContractEvent.SetMarket(market: market))
        }
    }
}

/// Android's `shareText` in the locale's number formats: `Bitcoin (BTC): $67,412.80, ▲ 2.14% (24h)` and the
/// CoinGecko page (Android always writes `67,412.80 $` and `2.14 %`).
func shareText(_ market: MarketModel) -> String {
    "\(market.name) (\(market.symbol.uppercased())): \(formatPrice(market.currentPrice)), "
        + "\(trendText(market.priceChangePercentage24h)) (24h)\n"
        + "https://www.coingecko.com/en/coins/\(market.id)"
}

/// `In Favorites` on accent-tinted glass when saved, `Add to Favorites` on clear glass when not. One button
/// whose glass changes, so a tap morphs it and VoiceOver focus stays on it.
private struct FavoriteToggle: View {
    let isFavorite: Bool
    let name: String
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Label(
                isFavorite ? "In Favorites" : "Add to Favorites",
                systemImage: isFavorite ? "heart.fill" : "heart"
            )
            .fontWeight(.semibold)
            .foregroundStyle(isFavorite ? Color.white : Color.accentColor)
        }
        .buttonStyle(.glass(isFavorite ? .regular.tint(.accentColor) : .regular))
        .accessibilityLabel(isFavorite ? Text("Remove \(name) from favorites") : Text("Add \(name) to favorites"))
        .sensoryFeedback(.selection, trigger: isFavorite)
    }
}

/// Logo, name, `BTC · Rank #1`, the price, and the change over the picked range in a capsule.
private struct MarketHeader: View {
    let market: MarketModel
    let state: MarketDetailContractState?
    @ScaledMetric(relativeTo: .largeTitle) private var priceSize: CGFloat = 44

    var body: some View {
        let range = state?.chartRange ?? .day
        let rank = market.marketCapRank?.int32Value ?? state?.marketDetail.data?.marketCapRank
        // Until the 1D chart answers, its change is the 24h change the list already has.
        let change = state?.rangeChange?.doubleValue ?? (range == .day ? market.priceChangePercentage24h : nil)
        VStack(alignment: .leading, spacing: 6) {
            HStack(spacing: 10) {
                MarketLogo(url: market.imageUrl, size: 32)
                Text(market.name)
                    .font(.title3.weight(.semibold))
                    .accessibilityAddTraits(.isHeader)
                Group {
                    if let rank {
                        Text("\(market.symbol.uppercased()) · Rank #\(rank)")
                    } else {
                        Text(market.symbol.uppercased())
                    }
                }
                .foregroundStyle(.secondary)
            }
            Text(market.currentPrice, format: priceFormat(market.currentPrice))
                .font(.system(size: priceSize, weight: .bold))
                .monospacedDigit()
                .minimumScaleFactor(0.5)
                .lineLimit(1)
            HStack(spacing: 6) {
                if let change {
                    TrendChange(change: change)
                        .fontWeight(.semibold)
                        .foregroundStyle(trendColor(change >= 0))
                }
                Text(rangeLabel(range))
                    .foregroundStyle(.secondary)
            }
        }
    }
}

/// The period next to the change, `Today`; `short` is the range button, `1D`. A Swift copy of `ChartRange`'s
/// `label` and `shortLabel`, so the String Catalog can translate them.
private func rangeLabel(_ range: MarketDetailContractChartRange, short: Bool = false) -> LocalizedStringKey {
    switch range {
    case .day: short ? "1D" : "Today"
    case .week: short ? "1W" : "Past week"
    case .month: short ? "1M" : "Past month"
    case .year: short ? "1Y" : "Past year"
    }
}

private let chartHeight: CGFloat = 188

/// The price line for the range, edge to edge, and the 1D / 1W / 1M / 1Y picker under it.
private struct PriceChartSection: View {
    let chart: LoadableData<MarketChart>
    let range: MarketDetailContractChartRange
    let onRangeSelect: (MarketDetailContractChartRange) -> Void

    var body: some View {
        VStack(spacing: 12) {
            switch onEnum(of: chart) {
            case .initial, .loading:
                Rectangle()
                    .fill(.quaternary)
                    .frame(height: chartHeight + 44)
                    .accessibilityLabel(Text("Loading chart"))
            case .error(let error):
                Text(errorMessage(error.error))
                    .foregroundStyle(.secondary)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal)
                    .frame(maxWidth: .infinity, minHeight: chartHeight + 44)
            case .loaded(let loaded):
                PriceChart(points: loaded.data?.prices.compactMap(PricePoint.init) ?? [])
            }
            Picker("Range", selection: Binding(get: { range }, set: onRangeSelect)) {
                ForEach(MarketDetailContractChartRange.allCases, id: \.self) { range in
                    Text(rangeLabel(range, short: true)).tag(range)
                }
            }
            .pickerStyle(.segmented)
            .padding(.horizontal)
        }
    }
}

private struct PricePoint {
    let date: Date
    let price: Double

    init?(_ pair: KotlinPair<KotlinLong, KotlinDouble>) {
        guard let millis = pair.first?.int64Value, let price = pair.second?.doubleValue else { return nil }
        date = Date(timeIntervalSince1970: Double(millis) / 1000)
        self.price = price
    }
}

/// The price line with the scrubbed point's price and time above it. It starts on the latest price; a touch
/// or drag moves the marker, as on Android.
private struct PriceChart: View {
    let points: [PricePoint]
    @State private var selectedDate: Date?

    var body: some View {
        if let first = points.first, let last = points.last, points.count >= 2 {
            let prices = points.map(\.price)
            let lowest = prices.min() ?? first.price
            let highest = prices.max() ?? first.price
            // A flat series (a stablecoin's day) would give the y scale no height.
            let pad = highest > lowest ? 0 : max(abs(lowest) * 0.01, 0.01)
            let low = lowest - pad
            let high = highest + pad
            let color = trendColor(last.price >= first.price)
            let selected = selectedDate.map { date in
                points.min { abs($0.date.timeIntervalSince(date)) < abs($1.date.timeIntervalSince(date)) } ?? last
            } ?? last
            VStack(alignment: .leading, spacing: 4) {
                HStack {
                    Text(selected.price, format: priceFormat(selected.price))
                        .font(.headline)
                        .monospacedDigit()
                    Spacer()
                    Text(formatChartTime(selected.date))
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                }
                .accessibilityElement(children: .combine)
                .padding(.horizontal)
                Chart {
                    // By position: CoinGecko can send two points with one timestamp.
                    ForEach(Array(points.enumerated()), id: \.offset) { _, point in
                        AreaMark(
                            x: .value("Time", point.date),
                            yStart: .value("Price", low),
                            yEnd: .value("Price", point.price)
                        )
                        .interpolationMethod(.catmullRom)
                        .foregroundStyle(.linearGradient(
                            colors: [color.opacity(0.25), color.opacity(0)],
                            startPoint: .top,
                            endPoint: .bottom
                        ))
                        LineMark(x: .value("Time", point.date), y: .value("Price", point.price))
                            .interpolationMethod(.catmullRom)
                            .foregroundStyle(color)
                    }
                    RuleMark(x: .value("Time", selected.date))
                        .foregroundStyle(.secondary)
                        .lineStyle(StrokeStyle(lineWidth: 1, dash: [4, 4]))
                    PointMark(x: .value("Time", selected.date), y: .value("Price", selected.price))
                        .foregroundStyle(color)
                }
                .chartXSelection(value: $selectedDate)
                .chartXAxis(.hidden)
                .chartYScale(domain: low...high)
                // Edge to edge: the price labels sit inside the plot instead of taking a gutter.
                .chartYAxis {
                    AxisMarks(position: .trailing, values: .automatic(desiredCount: 3)) {
                        AxisGridLine(stroke: StrokeStyle(lineWidth: 0.5, dash: [4, 4]))
                        AxisValueLabel(format: compactUSD, anchor: .bottomTrailing)
                            .offset(x: -16, y: -2)
                    }
                }
                .frame(height: chartHeight)
            }
        } else {
            Text("No chart data")
                .foregroundStyle(.secondary)
                .padding(.horizontal)
                .frame(maxWidth: .infinity, minHeight: chartHeight + 44)
        }
    }
}

/// `Market Data`: Market Cap, High 24h, Low 24h and Rank, as grouped rows.
private struct MarketDataSection: View {
    let detail: LoadableData<MarketDetail>

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Market Data")
                .font(.title2.bold())
                .accessibilityAddTraits(.isHeader)
                .padding(.horizontal, 4)
            switch onEnum(of: detail) {
            case .error(let error):
                Text(errorMessage(error.error))
                    .foregroundStyle(.secondary)
            case .loaded(let loaded):
                if let detail = loaded.data {
                    rows(Self.values(detail))
                }
            case .initial, .loading:
                rows(Self.placeholder)
                    .redacted(reason: .placeholder)
                    .accessibilityElement(children: .ignore)
                    .accessibilityLabel(Text("Loading market data"))
            }
        }
    }

    private static let placeholder = ["$1.23T", "$12,345.67", "$12,345.67", "#1"]

    /// The four values in row order; `—` when CoinGecko sent no market data.
    private static func values(_ detail: MarketDetail) -> [String] {
        let data = detail.marketData
        return [
            data?.marketCapUSD.map { Double($0.int64Value).formatted(compactUSD) } ?? "—",
            data.map { formatPrice($0.high24hUSD) } ?? "—",
            data.map { formatPrice($0.low24hUSD) } ?? "—",
            "#\(data?.marketCapRank ?? detail.marketCapRank)",
        ]
    }

    private static let labels: [LocalizedStringKey] = ["Market Cap", "High 24h", "Low 24h", "Rank"]

    private func rows(_ values: [String]) -> some View {
        VStack(spacing: 0) {
            ForEach(values.indices, id: \.self) { index in
                if index > 0 { Divider() }
                LabeledContent(Self.labels[index]) {
                    Text(values[index])
                        .fontWeight(.semibold)
                        .foregroundStyle(.primary)
                        .monospacedDigit()
                }
                .frame(minHeight: 50)
            }
        }
        .padding(.horizontal, 16)
        .background(Color(.secondarySystemGroupedBackground), in: .rect(cornerRadius: 26))
    }
}
