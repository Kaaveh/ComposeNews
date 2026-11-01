import Shared
import SwiftUI

/// The saved markets, sorted by name, price or 24h change; a swipe to the left removes one.
struct FavoriteListView: View {
    let model: FavoriteListModel
    @Binding var selectedID: String?
    let browseMarkets: () -> Void

    var body: some View {
        let state = model.state
        Group {
            switch onEnum(of: state.favoriteList) {
            case .initial, .loading:
                ProgressView()
            // Room doesn't fail here today; kept so a future error isn't a blank screen.
            case .error(let error):
                ContentUnavailableView(
                    "Couldn't load favorites",
                    systemImage: "exclamationmark.triangle",
                    description: Text(errorMessage(error.error))
                )
            case .loaded(let loaded):
                let favorites = loaded.data as? [MarketModel] ?? []
                if favorites.isEmpty {
                    ContentUnavailableView {
                        Label("No favorites yet", systemImage: "heart")
                    } description: {
                        Text("Tap the heart on any market to keep it here, ready at a glance.")
                    } actions: {
                        Button("Browse markets", action: browseMarkets)
                            .buttonStyle(.borderedProminent)
                    }
                } else {
                    list(state, favorites: favorites)
                }
            }
        }
        .toolbar {
            ToolbarItem {
                Menu {
                    Picker("Sort by", selection: Binding(
                        get: { state.sort },
                        set: { model.send(FavoriteListContractEvent.OnSortSelect(sort: $0)) }
                    )) {
                        Text("Name").tag(FavoriteListContractSort.name)
                        Text("Price").tag(FavoriteListContractSort.price)
                        Text("24h change").tag(FavoriteListContractSort.change24h)
                    }
                } label: {
                    Label("Sort favorites by name, price or 24h change", systemImage: "arrow.up.arrow.down")
                }
            }
        }
    }

    private func list(_ state: FavoriteListContractState, favorites: [MarketModel]) -> some View {
        List(selection: $selectedID) {
            Section {
                ForEach(favorites, id: \.id) { market in
                    NavigationLink(value: market.id) {
                        MarketRow(market: market)
                    }
                    // A full swipe removes, like Android's; VoiceOver lists it as an action.
                    .swipeActions(edge: .trailing) {
                        Button(role: .destructive) {
                            model.send(FavoriteListContractEvent.OnFavoriteClick(market: market))
                        } label: {
                            Label("Remove", systemImage: "heart.slash")
                        }
                    }
                }
            } header: {
                Text("\(state.count) saved · swipe left to remove")
            } footer: {
                Label("Favorites stay in sync in the background, even offline.", systemImage: "bell")
            }
        }
    }
}
