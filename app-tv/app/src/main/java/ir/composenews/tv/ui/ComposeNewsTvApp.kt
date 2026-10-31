@file:Suppress("ktlint:standard:function-naming")

package ir.composenews.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults
import androidx.tv.material3.Tab
import androidx.tv.material3.TabDefaults
import androidx.tv.material3.TabRow
import androidx.tv.material3.TabRowDefaults
import androidx.tv.material3.Text
import ir.composenews.apptv.ui.TvMarketsRoute
import ir.composenews.designsystem.R
import androidx.compose.material3.MaterialTheme as PhoneMaterialTheme

private enum class TvTab(
    val label: String,
    val icon: Int,
    val selectedIcon: Int,
) {
    Markets("Markets", R.drawable.ic_finance_mode, R.drawable.ic_finance_mode_filled),
    Favorites("Favorites", R.drawable.ic_favorite, R.drawable.ic_favorite_filled),
}

@Composable
fun ComposeNewsTvApp() {
    var tab by rememberSaveable { mutableStateOf(TvTab.Markets) }
    Surface(
        modifier = Modifier.fillMaxSize(),
        colors = SurfaceDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
    ) {
        Box {
            TvMarketsRoute(
                favorites = tab == TvTab.Favorites,
                onSelectTab = { tab = if (it) TvTab.Favorites else TvTab.Markets },
            )
            // Over the screen rather than inside it: a tab switch replaces the screen, and the focused tab with it.
            Box(modifier = Modifier.padding(start = 48.dp, top = 24.dp)) {
                TvTabs(selected = tab, onSelect = { tab = it })
            }
        }
    }
}

/** The top tabs. As on any TV tab row, focusing a tab opens it. */
@Composable
private fun TvTabs(
    selected: TvTab,
    onSelect: (TvTab) -> Unit,
) {
    TabRow(
        selectedTabIndex = selected.ordinal,
        separator = { Spacer(modifier = Modifier.width(4.dp)) },
        indicator = { positions, hasFocus ->
            positions.getOrNull(selected.ordinal)?.let {
                TabRowDefaults.PillIndicator(
                    currentTabPosition = it,
                    doesTabRowHaveFocus = hasFocus,
                    // Crimson while the tabs have focus, light as drawn otherwise.
                    activeColor = MaterialTheme.colorScheme.primary,
                    inactiveColor = MaterialTheme.colorScheme.inverseSurface,
                )
            }
        },
        modifier =
            Modifier
                .clip(CircleShape)
                .background(PhoneMaterialTheme.colorScheme.surfaceContainerLow)
                .padding(4.dp),
    ) {
        TvTab.entries.forEach { entry ->
            val isSelected = entry == selected
            Tab(
                selected = isSelected,
                onFocus = { onSelect(entry) },
                onClick = { onSelect(entry) },
                colors =
                    TabDefaults.pillIndicatorTabColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                        focusedContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        painter = painterResource(if (isSelected) entry.selectedIcon else entry.icon),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(text = entry.label, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
