import Foundation
import Shared
import Testing
@testable import ComposeNews

/// The hosted app has already run `initKoin()`, so these use the real Koin-built `FavoriteListViewModel`.
@MainActor
struct ScreenModelTests {
    @Test func startsFromTheViewModelState() {
        let viewModel = favoriteListViewModel()

        let model = FavoriteListModel(viewModel: viewModel)

        #expect(model.state == viewModel.state.value)
    }

    @Test func sentEventsReachTheViewModelAndStateFollows() async throws {
        let model = FavoriteListModel()

        model.send(FavoriteListContractEvent.OnSortSelect(sort: .price))

        #expect(model.currentState.sort == .price)
        // `state` catches up once the observation task runs on the main actor.
        for _ in 0..<100 where model.state.sort != .price {
            try await Task.sleep(for: .milliseconds(10))
        }
        #expect(model.state.sort == .price)
    }

    @Test func droppingTheModelClearsTheViewModel() {
        let viewModel = favoriteListViewModel()
        let closeable = CloseableSpy()
        viewModel.addCloseable(closeable: closeable)
        var model: FavoriteListModel? = FavoriteListModel(viewModel: viewModel)
        #expect(model != nil && !closeable.closed)

        model = nil

        #expect(closeable.closed)
    }
}

/// `ViewModel.clear()` closes its closeables, so this sees the store being cleared.
private final class CloseableSpy: NSObject, KotlinAutoCloseable {
    private(set) var closed = false

    func close() {
        closed = true
    }
}
