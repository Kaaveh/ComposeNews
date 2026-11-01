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
                MarketsTab { destination = .favorites }
            }
            Tab("Favorites", systemImage: "heart", value: .favorites) {
                FavoritesTab { destination = .markets }
            }
        }
        .tabViewStyle(.sidebarAdaptable)
    }
}

private struct MarketsTab: View {
    let openFavorites: () -> Void
    // Created in `.task`, not here: SwiftUI evaluates a `@State` initial value on every rebuild of the struct.
    @State private var model: MarketListModel?

    var body: some View {
        let state = model?.state
        // The top mover can be outside the filtered list.
        MarketSplitView(title: "Markets") { id in
            market(withID: id, in: state?.marketList) ?? state?.topMover.flatMap { $0.id == id ? $0 : nil }
        } content: { selectedID, wide in
            if let model {
                MarketListView(model: model, selectedID: selectedID, wide: wide, openFavorites: openFavorites)
            } else {
                ProgressView()
            }
        }
        .task {
            guard model == nil else { return }
            let model = MarketListModel()
            model.send(MarketListContractEvent.OnGetMarketList.shared)
            self.model = model
        }
    }
}

private struct FavoritesTab: View {
    let browseMarkets: () -> Void
    @State private var model: FavoriteListModel?

    var body: some View {
        MarketSplitView(title: "Favorites") { id in
            market(withID: id, in: model?.state.favoriteList)
        } content: { selectedID, _ in
            if let model {
                FavoriteListView(model: model, selectedID: selectedID, browseMarkets: browseMarkets)
            } else {
                ProgressView()
            }
        }
        .task {
            guard model == nil else { return }
            let model = FavoriteListModel()
            model.send(FavoriteListContractEvent.OnGetFavoriteList.shared)
            self.model = model
        }
    }
}

/// List | detail, like Android's `ListDetailPaneScaffold`: a stack on iPhone, both panes on iPad. The list gets
/// the selection and whether the detail sits beside it (`wide`).
private struct MarketSplitView<Content: View>: View {
    let title: LocalizedStringKey
    /// The market the selection opens; nil once it leaves the list.
    let market: (_ id: String) -> MarketModel?
    @ViewBuilder let content: (_ selectedID: Binding<String?>, _ wide: Bool) -> Content

    // The id, not the market: a sync or a favorite toggle replaces the MarketModel, which then equals no row.
    @State private var selectedID: String?
    // Read here: inside the split view the list column is compact even on iPad.
    @Environment(\.horizontalSizeClass) private var sizeClass

    var body: some View {
        let selected = selectedID.flatMap(market)
        NavigationSplitView {
            content($selectedID, sizeClass == .regular)
                .navigationTitle(title)
        } detail: {
            if let selected {
                MarketDetailView(market: selected).id(selected.id)
            } else {
                ContentUnavailableView("Select a market", systemImage: "chart.line.uptrend.xyaxis")
            }
        }
        // A market that left the list (un-favorited, filtered out) closes its detail.
        .onChange(of: selectedID != nil && selected == nil) { _, gone in
            if gone { selectedID = nil }
        }
    }
}

/// Searches the NSArray as it is: bridging all 250 markets to `[MarketModel]` would run on every state tick.
private func market(withID id: String, in list: LoadableData<NSArray>?) -> MarketModel? {
    list?.data?.first { ($0 as? MarketModel)?.id == id } as? MarketModel
}
