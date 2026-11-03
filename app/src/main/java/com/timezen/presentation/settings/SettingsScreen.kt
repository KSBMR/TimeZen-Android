package com.timezen.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.timezen.domain.model.ThemeMode
import com.timezen.ui.components.SectionTitle
import com.timezen.ui.components.TimeZenCard

@Composable
fun SettingsScreen(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        item {
            TimeZenCard {
                SectionTitle("Appearance")
                Text(
                    text = "This choice is kept only until the app is closed. " +
                        "Saving it permanently is added in Phase 11.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(8.dp))
                ThemeMode.entries.forEach { mode ->
                    // `selectable` makes the whole row tappable and tells screen readers
                    // it is a radio button.
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = mode == themeMode,
                                onClick = { onThemeModeChange(mode) },
                                role = Role.RadioButton,
                            )
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected = mode == themeMode, onClick = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = mode.displayName,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }

        item {
            TimeZenCard {
                SectionTitle("Privacy and permissions")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "TimeZen currently asks for no permissions and collects no data. " +
                        "When real tracking is added, each sensitive permission " +
                        "(Usage Access, Accessibility, Display over other apps, Notifications) " +
                        "will be explained on a screen before Android asks, and you will be " +
                        "able to revoke it at any time in Android Settings.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        item {
            TimeZenCard {
                SectionTitle("About")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "TimeZen 0.1.0 (Phase 1 of 12)",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "Track Your Time. Reclaim Your Life.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
