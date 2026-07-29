package com.raen.optidroid.presentation.screen.settings.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun AutoOptimizationCard(
    enabled: Boolean,
    unlockDelayMinutes: Int,
    periodicScheduleHours: Int,
    minUnlockIntervalHours: Int = 0,
    optimizationProgress: com.raen.optidroid.domain.model.common.OptimizationProgress = com.raen.optidroid.domain.model.common.OptimizationProgress(),
    onEnabledChanged: (Boolean) -> Unit,
    onUnlockDelayChanged: (Int) -> Unit,
    onPeriodicScheduleChanged: (Int) -> Unit,
    onMinUnlockIntervalChanged: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Enable Auto-Optimization",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Automatically optimize apps periodically and on device open",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = enabled,
                    onCheckedChange = onEnabledChanged
                )
            }

            AnimatedVisibility(visible = optimizationProgress.isRunning) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Auto-Optimization In Progress...",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${(optimizationProgress.progress * 100f).toInt().coerceIn(0, 100)}%",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    androidx.compose.material3.LinearProgressIndicator(
                        progress = { optimizationProgress.progress.coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                    )

                    if (optimizationProgress.currentAppPackage.isNotBlank()) {
                        Text(
                            text = "Current App: ${optimizationProgress.currentAppPackage}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (optimizationProgress.totalCount > 0) {
                        Text(
                            text = "Processed: ${optimizationProgress.processedCount} of ${optimizationProgress.totalCount} apps (${optimizationProgress.skippedCount} skipped)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            AnimatedVisibility(visible = enabled) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Post-Unlock Delay Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Device Open / Unlock Delay",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = if (unlockDelayMinutes == 0) "Immediately" else "$unlockDelayMinutes min",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = "Delay after opening/unlocking device before auto-optimizing (0 to 60 mins)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Slider(
                            value = unlockDelayMinutes.toFloat(),
                            onValueChange = { onUnlockDelayChanged(it.roundToInt()) },
                            valueRange = 0f..60f,
                            steps = 59
                        )
                    }

                    // Minimum Unlock Interval Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Minimum Hours Between Unlock Triggers",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = if (minUnlockIntervalHours == 0) "Every unlock" else "$minUnlockIntervalHours hrs",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = "Only trigger unlock auto-optimization if device was unlocked after at least X hours (0 to 24 hrs)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Slider(
                            value = minUnlockIntervalHours.toFloat(),
                            onValueChange = { onMinUnlockIntervalChanged(it.roundToInt()) },
                            valueRange = 0f..24f,
                            steps = 23
                        )
                    }

                    // Periodic Schedule Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Periodic Schedule Interval",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = when {
                                    periodicScheduleHours < 24 -> "$periodicScheduleHours hours"
                                    else -> "${periodicScheduleHours / 24} days"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = "Background periodic optimization interval (1 hour to 7 days)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Slider(
                            value = periodicScheduleHours.toFloat(),
                            onValueChange = { onPeriodicScheduleChanged(it.roundToInt()) },
                            valueRange = 1f..168f,
                            steps = 166
                        )
                    }
                }
            }
        }
    }
}
