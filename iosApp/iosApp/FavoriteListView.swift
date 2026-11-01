import Shared
import SwiftUI

/// The saved markets, sorted by name, price or 24h change; a swipe to the left (or Edit) removes one.
struct FavoriteListView: View {
    let model: FavoriteListModel
    @Binding var selectedID: String?
    let browseMarkets: () -> Void
    // Owned here so removing the last favorite can leave edit mode: its Done button goes with the list.
    @State private var editMode = EditMode.inactive

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
                        Label("No Favorites Yet", systemImage: "heart")
                    } description: {
                        Text("Open a market and tap Add to Favorites to keep it here.")
                    } actions: {
                        Button("Browse Markets", action: browseMarkets)
                            .buttonStyle(.glassProminent)
                            .controlSize(.large)
                    }
                } else {
                    list(state, favorites: favorites)
                }
            }
        }
        .navigationSubtitle(state.count > 0 ? Text("\(state.count) markets") : Text(verbatim: ""))
        .toolbar {
            if state.count > 0 {
                ToolbarItem { EditButton() }
            }
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
        .environment(\.editMode, $editMode)
        .onChange(of: state.count == 0) { _, empty in
            if empty { editMode = .inactive }
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
                .onDelete { offsets in
                    for index in offsets {
                        model.send(FavoriteListContractEvent.OnFavoriteClick(market: favorites[index]))
                    }
                }
            } footer: {
                Text("Swipe left on a market to remove it from Favorites.")
            }
        }
    }
}
