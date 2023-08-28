package ir.composenews.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Chart(
    val prices: List<Pair<Int, Double>>,
)
