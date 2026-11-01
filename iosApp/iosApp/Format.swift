import Foundation
import Shared

/// Android's `formatPrice`: two decimals from $1 up, up to six below it, so $0.0000123 doesn't show as $0.00.
func priceFormat(_ price: Double) -> FloatingPointFormatStyle<Double>.Currency {
    // Decide on the rounded value, so 0.9999996 is $1.00 rather than $1.
    let small = (price * 1_000_000).rounded() < 1_000_000
    return .currency(code: "USD").precision(.fractionLength(small ? 0...6 : 2...2))
}

func formatPrice(_ price: Double) -> String {
    price.formatted(priceFormat(price))
}

/// Three significant figures with a T, B, M or K suffix: `$1.33T`, `$255B`, `$67.4K`.
let compactUSD = FloatingPointFormatStyle<Double>.Currency(code: "USD")
    .notation(.compactName)
    .precision(.significantDigits(3))

/// The size of a percent change, without its sign: `2.14%`. The sign is the arrow next to it.
func formatPercent(_ change: Double) -> String {
    (abs(change) / 100).formatted(.percent.precision(.fractionLength(2)))
}

/// When a chart point was priced: `Today, 14:20`, `Yesterday, 23:05`, `Oct 24, 14:00` this year, `Mar 3, 2025` before.
func formatChartTime(_ date: Date) -> String {
    let calendar = Calendar.current
    let time = date.formatted(date: .omitted, time: .shortened)
    if calendar.isDateInToday(date) { return String(localized: "Today, \(time)") }
    if calendar.isDateInYesterday(date) { return String(localized: "Yesterday, \(time)") }
    if calendar.isDate(date, equalTo: .now, toGranularity: .year) {
        return date.formatted(.dateTime.month(.abbreviated).day().hour().minute())
    }
    return date.formatted(.dateTime.month(.abbreviated).day().year())
}

/// The retry button after a failed sync: `Try again`, or `Try again in 0:42` while a 429 runs out.
func formatRetry(_ retryInSeconds: Int32) -> String {
    guard retryInSeconds > 0 else { return String(localized: "Try again") }
    let left = Duration.seconds(Int64(retryInSeconds)).formatted(.time(pattern: .minuteSecond))
    return String(localized: "Try again in \(left)")
}

func errorMessage(_ error: KtorErrors) -> String {
    switch onEnum(of: error) {
    case .rateLimited(let limit): String(localized: "Too many requests. Try again in \(limit.retryAfterSeconds) seconds.")
    case .apiError(let api): String(localized: "The server answered \(api.code).")
    case .exceptionError: String(localized: "Check your connection and try again.")
    }
}
