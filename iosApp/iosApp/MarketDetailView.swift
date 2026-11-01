import Charts
import Shared
import SwiftUI

/// One market: header, the price chart for the picked range, and Market Data. The toolbar has Share and the
/// favorite toggle.
struct MarketDetailView: View {
    let market: MarketModel
    // Created in `.task`, not here: SwiftUI evaluates a `@State` initial value on every rebuild of the struct.
    @State private var model: MarketDetailModel?

    var body: some View {
        let state = model?.state
        // The ViewModel's copy follows the database's favorite flag; until it exists, the list's.
        let shown = state?.market.data ?? market
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                MarketHeader(market: shown, state: state)
                if let state {
                    PriceChartCard(chart: state.marketChart, range: state.chartRange) { range in
                        model?.send(MarketDetailContractEvent.OnChartRangeSelect(range: range))
                    }
                    MarketDataSection(detail: state.marketDetail)
                }
            }
            .padding()
        }
        .toolbar {
            ToolbarItemGroup {
                ShareLink(item: shareText(shown))
                FavoriteButton(isFavorite: shown.isFavorite, name: shown.name) {
                    model?.send(MarketDetailContractEvent.OnFavoriteClick(market: shown))
                }
            }
        }
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
private func shareText(_ market: MarketModel) -> String {
    let arrow = market.priceChangePercentage24h >= 0 ? "▲" : "▼"
    let change = formatPercent(market.priceChangePercentage24h)
    return "\(market.name) (\(market.symbol.uppercased())): \(formatPrice(market.currentPrice)), \(arrow) \(change) (24h)\n"
        + "https://www.coingecko.com/en/coins/\(market.id)"
}

/// Logo, name, `BTC · Rank #1`, the price, and the change over the picked range in a capsule.
private struct MarketHeader: View {
    let market: MarketModel
    let state: MarketDetailContractState?

    var body: some View {
        let range = state?.chartRange ?? .day
        let rank = market.marketCapRank?.int32Value ?? state?.marketDetail.data?.marketCapRank
        // Until the 1D chart answers, its change is the 24h change the list already has.
        let change = state?.rangeChange?.doubleValue ?? (range == .day ? market.priceChangePercentage24h : nil)
        VStack(alignment: .leading, spacing: 6) {
            HStack(spacing: 12) {
                MarketLogo(url: market.imageUrl, size: 48)
                VStack(alignment: .leading) {
                    Text(market.name)
                        .font(.title2.bold())
                        .accessibilityAddTraits(.isHeader)
                    Group {
                        if let rank {
                            Text("\(market.symbol.uppercased()) · Rank #\(rank)")
                        } else {
                            Text(market.symbol.uppercased())
                        }
                    }
                    .font(.subheadline)
                    .foregroundStyle(.secondary)
                }
            }
            Text(market.currentPrice, format: priceFormat(market.currentPrice))
                .font(.largeTitle.bold())
                .monospacedDigit()
                .padding(.top, 10)
            HStack(spacing: 8) {
                if let change {
                    TrendChange(change: change)
                        .font(.subheadline.weight(.semibold))
                        .padding(.horizontal, 10)
                        .padding(.vertical, 4)
                        .background(trendColor(change >= 0).opacity(0.15), in: .capsule)
                }
                Text(rangeLabel(range))
                    .font(.subheadline)
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

/// The price line for the range and the 1D / 1W / 1M / 1Y picker under it.
private struct PriceChartCard: View {
    let chart: LoadableData<MarketChart>
    let range: MarketDetailContractChartRange
    let onRangeSelect: (MarketDetailContractChartRange) -> Void

    var body: some View {
        VStack(spacing: 12) {
            switch onEnum(of: chart) {
            case .initial, .loading:
                RoundedRectangle(cornerRadius: 16)
                    .fill(.quaternary)
                    .frame(height: chartHeight + 44)
                    .accessibilityLabel(Text("Loading chart"))
            case .error(let error):
                Text(errorMessage(error.error))
                    .foregroundStyle(.secondary)
                    .multilineTextAlignment(.center)
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
        }
        .padding(12)
        .background(Color(.secondarySystemBackground), in: .rect(cornerRadius: 28))
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
                .chartYAxis {
                    AxisMarks(position: .trailing, values: .automatic(desiredCount: 3)) {
                        AxisGridLine(stroke: StrokeStyle(lineWidth: 0.5, dash: [4, 4]))
                        AxisValueLabel(format: compactUSD)
                    }
                }
                .frame(height: chartHeight)
            }
        } else {
            Text("No chart data")
                .foregroundStyle(.secondary)
                .frame(maxWidth: .infinity, minHeight: chartHeight + 44)
        }
    }
}

/// `Market Data`: Market Cap, Rank, High 24h and Low 24h, in a 2×2 grid.
private struct MarketDataSection: View {
    let detail: LoadableData<MarketDetail>

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Market Data")
                .font(.title3.bold())
                .accessibilityAddTraits(.isHeader)
            switch onEnum(of: detail) {
            case .error(let error):
                Text(errorMessage(error.error))
                    .foregroundStyle(.secondary)
            case .loaded(let loaded):
                if let detail = loaded.data {
                    tiles(Self.values(detail))
                }
            case .initial, .loading:
                tiles(Self.placeholder)
                    .redacted(reason: .placeholder)
                    .accessibilityElement(children: .ignore)
                    .accessibilityLabel(Text("Loading market data"))
            }
        }
    }

    private static let placeholder = ["$1.23T", "#1", "$12,345.67", "$12,345.67"]

    /// The four values in tile order; `—` when CoinGecko sent no market data.
    private static func values(_ detail: MarketDetail) -> [String] {
        let data = detail.marketData
        return [
            data.map { Double($0.marketCapUSD).formatted(compactUSD) } ?? "—",
            "#\(data?.marketCapRank ?? detail.marketCapRank)",
            data.map { formatPrice($0.high24hUSD) } ?? "—",
            data.map { formatPrice($0.low24hUSD) } ?? "—",
        ]
    }

    private func tiles(_ values: [String]) -> some View {
        Grid(horizontalSpacing: 8, verticalSpacing: 8) {
            GridRow {
                StatTile(label: "Market Cap", systemImage: "chart.pie.fill", tint: .accentColor, value: values[0])
                StatTile(label: "Rank", systemImage: "medal", tint: .secondary, value: values[1])
            }
            GridRow {
                StatTile(label: "High 24h", systemImage: "arrow.up.right", tint: trendColor(true), value: values[2])
                StatTile(label: "Low 24h", systemImage: "arrow.down.right", tint: trendColor(false), value: values[3])
            }
        }
    }
}

private struct StatTile: View {
    let label: LocalizedStringKey
    let systemImage: String
    let tint: Color
    let value: String

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Label {
                Text(label).foregroundStyle(.secondary)
            } icon: {
                Image(systemName: systemImage).foregroundStyle(tint)
            }
            .font(.subheadline)
            Text(value)
                .font(.title3.weight(.semibold))
                .monospacedDigit()
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding()
        .background(Color(.secondarySystemBackground), in: .rect(cornerRadius: 20))
        .accessibilityElement(children: .combine)
    }
}
