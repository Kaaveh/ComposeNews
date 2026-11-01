import Shared
import SwiftUI

/// Markets and Favorites, the same two destinations as Android's `NavigationSuiteScaffold`: a tab bar on
/// iPhone, a sidebar on iPad.
struct RootView: View {
    enum Destination { case markets, favorites }

    @State private var destination = Destination.markets

    var body: some View {
        TabView(selection: $destination) {
            Tab("Markets", systemImage: "chart.line.uptrend.xyaxis", value: .markets) {
                MarketsTab()
            }
            Tab("Favorites", systemImage: "heart", value: .favorites) {
                FavoritesTab()
            }
        }
        .tabViewStyle(.sidebarAdaptable)
    }
}

private struct MarketsTab: View {
    // Created in `.task`, not here: SwiftUI evaluates a `@State` initial value on every rebuild of the struct.
    @State private var model: MarketListModel?

    var body: some View {
        MarketSplitView(
            title: "Markets",
            list: model?.state.marketList,
            empty: ContentUnavailableView("No markets", systemImage: "chart.line.uptrend.xyaxis"),
            refresh: model.map { model in { model.send(MarketListContractEvent.OnRefresh.shared) } }
        )
        .equatable()
        .task {
            guard model == nil else { return }
            let model = MarketListModel()
            model.send(MarketListContractEvent.OnGetMarketList.shared)
            self.model = model
        }
    }
}

private struct FavoritesTab: View {
    @State private var model: FavoriteListModel?

    var body: some View {
        MarketSplitView(
            title: "Favorites",
            list: model?.state.favoriteList,
            empty: ContentUnavailableView(
                "No favorites",
                systemImage: "heart",
                description: Text("Markets you favorite show up here.")
            ),
            refresh: nil
        )
        .equatable()
        .task {
            guard model == nil else { return }
            let model = FavoriteListModel()
            model.send(FavoriteListContractEvent.OnGetFavoriteList.shared)
            self.model = model
        }
    }
}

/// List | detail, like Android's `ListDetailPaneScaffold`: a stack on iPhone, both panes on iPad.
/// #372 replaces the plain rows and the test detail view.
private struct MarketSplitView<T: AnyObject>: View, Equatable {
    let title: String
    let list: LoadableData<T>?
    let empty: ContentUnavailableView<Label<Text, Image>, Text?, EmptyView>
    /// Also the error's retry button; `nil` for a list that only reads the database.
    let refresh: (() -> Void)?

    // The id, not the market: a sync or a favorite toggle replaces the MarketModel, which then equals no row.
    @State private var selectedID: String?

    /// Redraw only for a new list. A sync's `isRefreshing` or a 429's countdown changes the rest of the state
    /// every second but keeps the same `LoadableData`.
    static func == (lhs: Self, rhs: Self) -> Bool {
        lhs.list === rhs.list && (lhs.refresh == nil) == (rhs.refresh == nil)
    }

    private var markets: [MarketModel] { list?.data as? [MarketModel] ?? [] }

    var body: some View {
        let selected = markets.first { $0.id == selectedID }
        NavigationSplitView {
            content
                .navigationTitle(title)
        } detail: {
            if let selected {
                MarketDetailTestView(market: selected).id(selected.id)
            } else {
                ContentUnavailableView("Select a market", systemImage: "chart.line.uptrend.xyaxis")
            }
        }
        // A market that left the list (un-favorited) closes its detail.
        .onChange(of: selectedID != nil && selected == nil) { _, gone in
            if gone { selectedID = nil }
        }
    }

    @ViewBuilder private var content: some View {
        switch list.map({ onEnum(of: $0) }) {
        case nil, .initial, .loading:
            ProgressView()
        case .error(let error):
            ContentUnavailableView {
                Label("Couldn't load markets", systemImage: "wifi.exclamationmark")
            } description: {
                Text(describe(error.error))
            } actions: {
                if let refresh { Button("Try again", action: refresh) }
            }
        case .loaded where markets.isEmpty:
            empty
        case .loaded:
            if let refresh {
                marketList.refreshable { refresh() }
            } else {
                marketList
            }
        }
    }

    private var marketList: some View {
        List(markets, id: \.id, selection: $selectedID) { market in
            NavigationLink(value: market.id) {
                LabeledContent(market.name, value: market.currentPrice, format: priceFormat(market.currentPrice))
            }
        }
    }

    private func describe(_ error: KtorErrors) -> String {
        switch onEnum(of: error) {
        case .rateLimited(let limit): "Too many requests. Try again in \(limit.retryAfterSeconds) seconds."
        case .apiError(let api): "The server answered \(api.code)."
        case .exceptionError: "Check your connection and try again."
        }
    }
}

/// Android's `formatPrice`: two decimals from $1 up, up to six below it, so $0.0000123 doesn't show as $0.00.
private func priceFormat(_ price: Double) -> FloatingPointFormatStyle<Double>.Currency {
    // Decide on the rounded value, so 0.9999996 is $1.00 rather than $1.
    let small = (price * 1_000_000).rounded() < 1_000_000
    return .currency(code: "USD").precision(.fractionLength(small ? 0...6 : 2...2))
}
