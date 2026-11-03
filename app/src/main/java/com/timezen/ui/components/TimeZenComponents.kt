package com.timezen.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.timezen.domain.model.UsageStatus
import com.timezen.ui.theme.TimeZenCyan
import com.timezen.ui.theme.TimeZenGreen
import com.timezen.ui.theme.TimeZenWarning
import com.timezen.ui.theme.toColor
import com.timezen.ui.theme.toLabel

/** Rounded card used for every dashboard section. Pass `Modifier.weight(1f)` to share a Row. */
@Composable
fun TimeZenCard(
    modifier: Modifier = Modifier.fillMaxWidth(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(20.dp), content = content)
    }
}

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier,
    )
}

/**
 * Orange banner shown whenever numbers on screen are NOT real device usage.
 * Rule of this project: sample values must always be clearly labeled.
 */
@Composable
fun SampleDataBanner(message: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(TimeZenWarning.copy(alpha = 0.16f))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = Icons.Rounded.Info, contentDescription = null, tint = TimeZenWarning)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

/** Small pill showing a usage status (green / orange / red). */
@Composable
fun StatusChip(status: UsageStatus, modifier: Modifier = Modifier) {
    val color = status.toColor()
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.16f))
            .padding(horizontal = 14.dp, vertical = 6.dp),
    ) {
        Text(
            text = status.toLabel(),
            style = MaterialTheme.typography.labelLarge,
            color = color,
        )
    }
}

/** Small pill with any text, in the primary color (used for "Coming in Phase N"). */
@Composable
fun StatusChipText(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.16f))
            .padding(horizontal = 14.dp, vertical = 6.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

/** Value on top, small label underneath. */
@Composable
fun LabeledValue(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** A small rounded tile with a label and a big value (used for device-activity numbers). */
@Composable
fun StatTile(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(14.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

private val appAccentColors = listOf(
    TimeZenCyan,
    TimeZenGreen,
    Color(0xFF7C9CFF),
    TimeZenWarning,
    Color(0xFFB583FF),
)

/** Picks the same accent color for the same app name every time. */
fun accentColorFor(name: String): Color {
    val index = (name.hashCode() and Int.MAX_VALUE) % appAccentColors.size
    return appAccentColors[index]
}

/** One line in the "Most used apps" list: avatar, name, time, percentage and a progress bar. */
@Composable
fun AppUsageRow(
    name: String,
    durationText: String,
    percentOfTotal: Int,
    modifier: Modifier = Modifier,
) {
    val accent = accentColorFor(name)
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Placeholder avatar: the first letter. Real app icons arrive in Phase 2.
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = name.firstOrNull()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.titleMedium,
                    color = accent,
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = durationText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = "$percentOfTotal%",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = accent,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { (percentOfTotal / 100f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50)),
            color = accent,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

/** Big tappable tile with an icon and a label (shortcuts to other features). */
@Composable
fun FeatureTile(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
    }
}
