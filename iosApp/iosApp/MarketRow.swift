import Charts
import Shared
import SwiftUI

/// One market in a list: logo, symbol and name, 7-day sparkline, price, 24h change and, when `onFavoriteClick`
/// is set, the favorite toggle (Favorites removes by swipe instead).
struct MarketRow: View {
    let market: MarketModel
    var onFavoriteClick: (() -> Void)?

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
                TrendChange(change: market.priceChangePercentage24h)
                    .font(.footnote.weight(.semibold))
            }
            if let onFavoriteClick {
                FavoriteButton(isFavorite: market.isFavorite, name: market.name, action: onFavoriteClick)
                    // Its own tap target, not the row's navigation.
                    .buttonStyle(.borderless)
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

func trendColor(_ up: Bool) -> Color {
    up ? .green : .red
}

/// An arrow and the percent change in the trend color, so the trend never relies on color alone.
struct TrendChange: View {
    let change: Double

    var body: some View {
        let up = change >= 0
        let percent = formatPercent(change)
        HStack(spacing: 2) {
            Image(systemName: up ? "arrow.up" : "arrow.down")
            Text(percent).monospacedDigit()
        }
        .foregroundStyle(trendColor(up))
        .accessibilityElement(children: .ignore)
        .accessibilityLabel(up ? Text("Up \(percent)") : Text("Down \(percent)"))
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

struct FavoriteButton: View {
    let isFavorite: Bool
    let name: String
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Image(systemName: isFavorite ? "heart.fill" : "heart")
                .foregroundStyle(isFavorite ? Color.red : Color.secondary)
        }
        .accessibilityLabel(isFavorite ? Text("Remove \(name) from favorites") : Text("Add \(name) to favorites"))
        .sensoryFeedback(.selection, trigger: isFavorite)
    }
}
