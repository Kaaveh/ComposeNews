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
        MarketSplitView(title: "Markets", markets: model?.state.marketList.data as? [MarketModel] ?? [])
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
        MarketSplitView(title: "Favorites", markets: model?.state.favoriteList.data as? [MarketModel] ?? [])
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
private struct MarketSplitView: View {
    let title: String
    let markets: [MarketModel]

    @State private var selected: MarketModel?

    var body: some View {
        NavigationSplitView {
            List(markets, id: \.id, selection: $selected) { market in
                NavigationLink(value: market) {
                    LabeledContent(market.name, value: market.currentPrice, format: .currency(code: "USD"))
                }
            }
            .navigationTitle(title)
        } detail: {
            if let selected {
                MarketDetailTestView(market: selected).id(selected.id)
            } else {
                ContentUnavailableView("Select item from left", systemImage: "sidebar.left")
            }
        }
    }
}
