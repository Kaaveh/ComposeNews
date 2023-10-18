package ir.composenews.domain.util

sealed interface OrderType {
    data object Ascending : OrderType
    data object Descending : OrderType
}