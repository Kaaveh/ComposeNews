@file:Suppress("MagicNumber", "LongMethod")

package ir.composenews.marketdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ir.composenews.designsystem.component.QuadLineChart
import ir.composenews.designsystem.component.shimmerEffect

@Composable
fun MarketDetailLoadingView() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .shimmerEffect(),
                    )
                    Column(
                        modifier = Modifier.weight(1F),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .clip(RoundedCornerShape(4.dp))
                                .height(20.dp)
                                .shimmerEffect(),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.2f)
                                .clip(RoundedCornerShape(4.dp))
                                .height(12.dp)
                                .shimmerEffect(),
                        )
                    }
                }
            }

            QuadLineChart(data = listOf())
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .clip(RoundedCornerShape(4.dp))
                        .height(24.dp)
                        .shimmerEffect(),
                )
            }
            HorizontalDivider(color = Color.Gray)
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(top = 8.dp),
            ) {
                repeat(4) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .clip(RoundedCornerShape(4.dp))
                                .height(20.dp)
                                .shimmerEffect(),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                                .clip(RoundedCornerShape(4.dp))
                                .height(16.dp)
                                .shimmerEffect(),
                        )
                    }
                }
            }
        }
    }
}
