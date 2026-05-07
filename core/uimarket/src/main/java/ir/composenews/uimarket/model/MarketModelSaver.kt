package ir.composenews.uimarket.model

import androidx.compose.runtime.saveable.Saver
import kotlinx.serialization.json.Json

val MarketModelSaver: Saver<MarketModel?, String> =
    Saver(
        save = { value -> value?.let { Json.encodeToString(MarketModel.serializer(), it) }.orEmpty() },
        restore = { json -> if (json.isEmpty()) null else Json.decodeFromString(MarketModel.serializer(), json) },
    )
