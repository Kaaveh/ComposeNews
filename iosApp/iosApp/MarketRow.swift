import Charts
import Shared
import SwiftUI

/// One market in a list: logo, symbol and name, 7-day sparkline, price and the 24h change chip. As in the
/// Liquid Glass canvas the row has no heart: a market is saved from its detail.
struct MarketRow: View {
    let market: MarketModel

    var body: some View {
        HStack(spacing: 12) {
            MarketLogo(url: market.imageUrl, size: 44)
            VStack(alignment: .leading) {
                Text(market.symbol.uppercased())
                    .font(.headline)
                Text(market.name)
                    .font(.subheadline)
                    .foregroundStyle(.secondary)
                    .lineLimit(1)
            }
            Spacer(minLength: 8)
            if let up = market.isTrendingUp7d?.boolValue {
                VStack(spacing: 1) {
                    Sparkline(points: market.sparkline7d.map(\.doubleValue), up: up)
                        .frame(width: 52, height: 22)
                    Text("7d")
                        .font(.caption2)
                        .foregroundStyle(.secondary)
                }
                .accessibilityElement(children: .ignore)
                .accessibilityLabel(up ? Text("7-day trend up") : Text("7-day trend down"))
            }
            VStack(alignment: .trailing) {
                Text(market.currentPrice, format: priceFormat(market.currentPrice))
                    .font(.body.weight(.semibold))
                    .monospacedDigit()
                TrendChip(change: market.priceChangePercentage24h)
            }
        }
    }
}

struct MarketLogo: View {
    let url: String
    let size: CGFloat

    var body: some View {
        AsyncImage(url: URL(string: url)) { image in
            image.resizable().scaledToFit()
        } placeholder: {
            Circle().fill(.quaternary)
        }
        .frame(width: size, height: size)
        .clipShape(.circle)
        .accessibilityHidden(true)
    }
}

/// Trend text and lines: the `TrendUp` / `TrendDown` color sets, darker in light mode for 4.5:1 contrast.
func trendColor(_ up: Bool) -> Color {
    Color(up ? "TrendUp" : "TrendDown")
}

/// `▲ 2.14%` / `▼ 1.37%`: the triangle carries the sign, so the trend never relies on color alone.
func trendText(_ change: Double) -> String {
    "\(change >= 0 ? "▲" : "▼") \(formatPercent(change))"
}

private func trendAccessibilityLabel(_ change: Double) -> Text {
    let percent = formatPercent(change)
    return change >= 0 ? Text("Up \(percent)") : Text("Down \(percent)")
}

/// The change as text; the caller picks its color.
struct TrendChange: View {
    let change: Double

    var body: some View {
        Text(trendText(change))
            .monospacedDigit()
            .accessibilityLabel(trendAccessibilityLabel(change))
    }
}

/// The change in white on a filled chip, like the Stocks app. The fill stays the light-mode trend color in dark
/// mode too: white on the dark-mode green would fall under 4.5:1.
struct TrendChip: View {
    let change: Double

    var body: some View {
        TrendChange(change: change)
            .font(.footnote.weight(.semibold))
            .foregroundStyle(.white)
            .frame(minWidth: 48, alignment: .trailing)
            .padding(.horizontal, 8)
            .padding(.vertical, 2)
            .background(trendColor(change >= 0), in: .rect(cornerRadius: 7))
            .environment(\.colorScheme, .light)
    }
}

struct Sparkline: View {
    let points: [Double]
    let up: Bool

    var body: some View {
        Chart(Array(points.enumerated()), id: \.offset) { point in
            LineMark(x: .value("Time", point.offset), y: .value("Price", point.element))
                .lineStyle(StrokeStyle(lineWidth: 1.5))
        }
        .foregroundStyle(trendColor(up))
        .chartXAxis(.hidden)
        .chartYAxis(.hidden)
        .chartYScale(domain: .automatic(includesZero: false))
        .accessibilityHidden(true)
    }
}
