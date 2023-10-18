package ir.composenews.marketlist.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.composenews.designsystem.component.DefaultRadioButton
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.domain.util.MarketListOrder
import ir.composenews.domain.util.OrderType


@Composable
fun MarketListOrderSection(
    modifier: Modifier = Modifier,
    marketListOrder: MarketListOrder = MarketListOrder.Price(OrderType.Descending),
    onOrderChange: (MarketListOrder) -> Unit,
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {


            DefaultRadioButton(text = "Name", selected = marketListOrder is MarketListOrder.Name) {
                onOrderChange(
                    MarketListOrder.Name(marketListOrder.orderType)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                DefaultRadioButton(
                    text = "Symbol",
                    selected = marketListOrder is MarketListOrder.Symbol
                ) {
                    onOrderChange(
                        MarketListOrder.Symbol(marketListOrder.orderType)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    DefaultRadioButton(
                        text = "Price",
                        selected = marketListOrder is MarketListOrder.Price
                    ) {
                        onOrderChange(
                            MarketListOrder.Price(marketListOrder.orderType)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {


            DefaultRadioButton(
                text = "Ascending",
                selected = marketListOrder.orderType is OrderType.Ascending
            ) {
                onOrderChange(
                    marketListOrder.copy(OrderType.Ascending)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                DefaultRadioButton(
                    text = "Descending",
                    selected = marketListOrder.orderType is OrderType.Descending
                ) {
                    onOrderChange(
                        marketListOrder.copy(OrderType.Descending)
                    )
                }

            }
        }
    }
}

@Preview
@Composable
private fun MarketListOrderSectionPrev() {
    ComposeNewsTheme {
        Surface {
            MarketListOrderSection(onOrderChange = {})
        }
    }
}

