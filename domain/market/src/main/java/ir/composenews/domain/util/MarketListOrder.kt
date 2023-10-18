package ir.composenews.domain.util

sealed class MarketListOrder(val orderType: OrderType) {
    class Symbol(orderType: OrderType) : MarketListOrder(orderType)
    class Name(orderType: OrderType) : MarketListOrder(orderType)
    class Price(orderType: OrderType) : MarketListOrder(orderType)

    fun copy(orderType: OrderType): MarketListOrder = when (this) {
        is Name -> Name(orderType)
        is Price -> Price(orderType)
        is Symbol -> Symbol(orderType)
    }
}