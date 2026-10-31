package ir.composenews.marketdetail

import android.content.Context
import android.content.Intent
import ir.composenews.designsystem.component.formatPercent
import ir.composenews.designsystem.component.formatUsdPrice
import ir.composenews.uimarket.model.MarketModel
import java.util.Locale

/** `Bitcoin (BTC): 67,412.80 $, ▲ 2.14 % (24h)` and the CoinGecko page. */
internal fun shareText(market: MarketModel): String =
    with(market) {
        val arrow = if (priceChangePercentage24h >= 0) "▲" else "▼"
        val change = formatPercent(priceChangePercentage24h)
        "$name (${symbol.uppercase(
            Locale.US,
        )}): ${formatUsdPrice(currentPrice)}, $arrow $change (24h)\n" +
            "https://www.coingecko.com/en/coins/$id"
    }

internal fun Context.shareMarket(market: MarketModel) {
    val send =
        Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, shareText(market))
    startActivity(Intent.createChooser(send, null))
}
