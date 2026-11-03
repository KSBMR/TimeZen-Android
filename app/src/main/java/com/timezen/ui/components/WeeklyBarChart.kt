package com.timezen.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A simple bar chart: one bar per value, the tallest bar fills `chartHeight`.
 * Built from plain boxes (no chart library yet). A chart library is added in Phase 9.
 *
 * @param highlightIndex which bar is "today" (drawn in the primary color)
 */
@Composable
fun WeeklyBarChart(
    values: List<Int>,
    labels: List<String>,
    highlightIndex: Int,
    modifier: Modifier = Modifier,
    chartHeight: Dp = 120.dp,
) {
    val maxValue = (values.maxOrNull() ?: 1).coerceAtLeast(1)
    val highlightColor = MaterialTheme.colorScheme.primary
    val normalColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Bar chart of screen time for each day this week" },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        values.forEachIndexed { index, value ->
            // Never let a bar disappear completely: minimum 4% height.
            val fraction = (value.toFloat() / maxValue).coerceIn(0.04f, 1f)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .width(26.dp)
                        .height(chartHeight * fraction)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(if (index == highlightIndex) highlightColor else normalColor),
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = labels.getOrElse(index) { "" },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
