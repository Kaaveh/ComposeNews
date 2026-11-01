import Observation
import Shared

/// Owns one screen's ViewModel. Dropping it clears the ViewModel, which runs `onCleared()` and cancels
/// `viewModelScope`.
@MainActor
@Observable
final class ScreenModel<State, Event> {
    private(set) var state: State

    @ObservationIgnored private let onEvent: (Event) -> Void
    @ObservationIgnored private let owner: ViewModelOwner
    @ObservationIgnored private var observation: Task<Void, Never>?

    init(_ viewModel: ViewModel, state flow: SkieSwiftStateFlow<State>, onEvent: @escaping (Event) -> Void) {
        self.onEvent = onEvent
        owner = ViewModelOwner(viewModel: viewModel)
        state = flow.value
        // viewModelScope runs on the main queue, so the collection stays on the main actor.
        observation = Task { [weak self] in
            for await state in flow {
                self?.state = state
            }
        }
    }

    deinit {
        observation?.cancel()
        owner.clear()
    }

    func send(_ event: Event) {
        onEvent(event)
    }
}

typealias MarketListModel = ScreenModel<MarketListContractState, MarketListContractEvent>
typealias FavoriteListModel = ScreenModel<FavoriteListContractState, FavoriteListContractEvent>
typealias MarketDetailModel = ScreenModel<MarketDetailContractState, MarketDetailContractEvent>

extension ScreenModel where State == MarketListContractState, Event == MarketListContractEvent {
    convenience init(viewModel: MarketListViewModel = marketListViewModel()) {
        self.init(viewModel, state: viewModel.state, onEvent: viewModel.event(event:))
    }
}

extension ScreenModel where State == FavoriteListContractState, Event == FavoriteListContractEvent {
    convenience init(viewModel: FavoriteListViewModel = favoriteListViewModel()) {
        self.init(viewModel, state: viewModel.state, onEvent: viewModel.event(event:))
    }
}

extension ScreenModel where State == MarketDetailContractState, Event == MarketDetailContractEvent {
    convenience init(viewModel: MarketDetailViewModel = marketDetailViewModel()) {
        self.init(viewModel, state: viewModel.state, onEvent: viewModel.event(event:))
    }
}
