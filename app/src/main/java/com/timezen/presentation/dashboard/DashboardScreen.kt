package com.timezen.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.timezen.core.util.formatDuration
import com.timezen.domain.model.HABIT_LEVEL_DISCLAIMER
import com.timezen.navigation.Screen
import com.timezen.ui.components.AppUsageRow
import com.timezen.ui.components.CircularUsageIndicator
import com.timezen.ui.components.FeatureTile
import com.timezen.ui.components.LabeledValue
import com.timezen.ui.components.SampleDataBanner
import com.timezen.ui.components.SectionTitle
import com.timezen.ui.components.StatTile
import com.timezen.ui.components.StatusChip
import com.timezen.ui.components.TimeZenCard
import com.timezen.ui.components.WeeklyBarChart
import com.timezen.ui.theme.TimeZenGreen
import com.timezen.ui.theme.TimeZenTheme
import com.timezen.ui.theme.TimeZenWarning
import com.timezen.ui.theme.toColor
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * The stateful screen: it gets the ViewModel and observes its state.
 * It passes plain data down to [DashboardContent], which only draws.
 * Splitting a screen like this ("state hoisting") makes previews and tests easy.
 */
@Composable
fun DashboardScreen(
    onOpenFeature: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DashboardContent(state = state, onOpenFeature = onOpenFeature, modifier = modifier)
}

/** The stateless screen: draws whatever [state] it is given. */
@Composable
fun DashboardContent(
    state: DashboardUiState,
    onOpenFeature: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // LazyColumn is a scrolling list that only draws the items currently on screen.
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { DashboardHeader() }
        if (state.isSampleData) {
            item {
                SampleDataBanner(
                    message = "Sample data. These numbers are placeholders, not your real " +
                        "usage. Real tracking starts in Phase 2.",
                )
            }
        }
        item { OverviewCard(state) }
        item { DeviceActivityCard(state) }
        item { TopAppsCard(state) }
        item { HighlightsRow(state) }
        item { WeeklyCard(state) }
        item { ScoreSection(state) }
        item { ExploreSection(onOpenFeature) }
    }
}

@Composable
private fun DashboardHeader() {
    // `remember` keeps the computed text between redraws instead of recalculating it.
    val today = remember {
        LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
    }
    Column {
        Text(
            text = "Today",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = today,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun OverviewCard(state: DashboardUiState) {
    TimeZenCard {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Screen time",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(16.dp))
            CircularUsageIndicator(
                progress = state.usageFraction,
                color = state.status.toColor(),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = formatDuration(state.usedMinutes),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = "${state.usagePercent}% of target",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            StatusChip(status = state.status)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                LabeledValue(label = "Daily target", value = formatDuration(state.targetMinutes))
                LabeledValue(label = "Remaining", value = formatDuration(state.remainingMinutes))
            }
        }
    }
}

@Composable
private fun DeviceActivityCard(state: DashboardUiState) {
    TimeZenCard {
        SectionTitle("Device activity")
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            StatTile("Unlocks", state.unlocks.toString(), Modifier.weight(1f))
            StatTile("Notifications", state.notifications.toString(), Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            StatTile("Avg. session", formatDuration(state.averageSessionMinutes), Modifier.weight(1f))
            StatTile("App launches", state.appLaunches.toString(), Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "First activity ${state.firstActivity}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "Last activity ${state.lastActivity}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun TopAppsCard(state: DashboardUiState) {
    TimeZenCard {
        SectionTitle("Most used apps")
        Spacer(modifier = Modifier.height(16.dp))
        state.topApps.forEachIndexed { index, app ->
            if (index > 0) Spacer(modifier = Modifier.height(16.dp))
            AppUsageRow(
                name = app.name,
                durationText = formatDuration(app.minutes),
                percentOfTotal = state.shareOfTotalPercent(app),
            )
        }
    }
}

@Composable
private fun HighlightsRow(state: DashboardUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        val mostUsed = state.mostUsedApp
        val mostOpened = state.mostOpenedApp
        HighlightCard(
            title = "Most used",
            primary = mostUsed?.name ?: "No data",
            secondary = mostUsed?.let { formatDuration(it.minutes) } ?: "",
            modifier = Modifier.weight(1f),
        )
        HighlightCard(
            title = "Most opened",
            primary = mostOpened?.name ?: "No data",
            secondary = mostOpened?.let { "${it.opens} opens" } ?: "",
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun HighlightCard(
    title: String,
    primary: String,
    secondary: String,
    modifier: Modifier = Modifier,
) {
    TimeZenCard(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = primary,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = secondary,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun WeeklyCard(state: DashboardUiState) {
    val difference = state.differenceFromYesterdayMinutes
    val comparisonText = when {
        difference < 0 -> "${formatDuration(-difference)} less than yesterday"
        difference > 0 -> "${formatDuration(difference)} more than yesterday"
        else -> "Same as yesterday"
    }
    val comparisonColor = if (difference > 0) TimeZenWarning else TimeZenGreen

    TimeZenCard {
        SectionTitle("This week")
        Text(
            text = "Daily average ${formatDuration(state.weeklyAverageMinutes)}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = comparisonText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = comparisonColor,
        )
        Spacer(modifier = Modifier.height(16.dp))
        WeeklyBarChart(
            values = state.weeklyMinutes,
            labels = state.weekDayLabels,
            highlightIndex = state.todayIndex,
        )
    }
}

@Composable
private fun ScoreSection(state: DashboardUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            TimeZenCard(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Productivity score",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${state.productivityScore}/100",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { state.productivityScore / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(50)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
            TimeZenCard(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Habit level",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = state.habitLevel.displayName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }
        Text(
            text = HABIT_LEVEL_DISCLAIMER,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ExploreSection(onOpenFeature: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle("Explore")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            FeatureTile(
                icon = Icons.Rounded.VideoLibrary,
                label = "Reels & Shorts",
                onClick = { onOpenFeature(Screen.Blocker.route) },
                modifier = Modifier.weight(1f),
            )
            FeatureTile(
                icon = Icons.Rounded.Schedule,
                label = "App limits",
                onClick = { onOpenFeature(Screen.Limits.route) },
                modifier = Modifier.weight(1f),
            )
            FeatureTile(
                icon = Icons.Rounded.EmojiEvents,
                label = "Challenges",
                onClick = { onOpenFeature(Screen.Challenges.route) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

// Android Studio shows @Preview functions in the "Split"/"Design" view without running the app.
@Preview(showBackground = true, backgroundColor = 0xFF101719, heightDp = 1800)
@Composable
private fun DashboardPreview() {
    TimeZenTheme {
        DashboardContent(state = DashboardSampleData.create(), onOpenFeature = {})
    }
}
