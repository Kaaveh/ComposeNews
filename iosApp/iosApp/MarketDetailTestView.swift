import Shared
import SwiftUI

/// Renders `MarketDetailContractState` from the real ViewModel, to check the bridge. #372 replaces it
/// with the detail screen.
struct MarketDetailTestView: View {
    let market: MarketModel
    // Created in `.task`, not here: SwiftUI evaluates a `@State` initial value on every rebuild of the struct.
    @State private var model: MarketDetailModel?

    var body: some View {
        List {
            if let state = model?.state {
                LabeledContent("Market", value: describe(state.market) { "\($0.name), \($0.currentPrice) USD" })
                LabeledContent("Chart", value: describe(state.marketChart) { "\($0.prices.count) prices" })
                LabeledContent("Detail", value: describe(state.marketDetail) { "rank \($0.marketCapRank)" })
                LabeledContent("Range", value: state.chartRange.label)
            }
        }
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

    private func describe<T: AnyObject>(_ data: LoadableData<T>, _ loaded: (T) -> String) -> String {
        switch onEnum(of: data) {
        case .initial: "Initial"
        case .loading: "Loading"
        case .loaded(let value): loaded(value.data!)
        case .error(let value): "Error: \(value.error)"
        }
    }
}
