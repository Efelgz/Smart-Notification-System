package com.smartnotif.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartnotif.core.NotificationSource
import com.smartnotif.ui.MainViewModel
import com.smartnotif.ui.components.NotificationCard
import com.smartnotif.ui.components.sourceColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(vm: MainViewModel) {
    val stats  by vm.dashboardStats.collectAsState()
    val notifs by vm.notificationsFlow.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title  = { Text("History & dashboard", fontSize = 17.sp, fontWeight = FontWeight.SemiBold) },
                actions = {
                    IconButton(onClick = { vm.clearHistory() }) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = "Clear all",
                            tint = MaterialTheme.colorScheme.outline)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier       = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ── Stat cards ────────────────────────────────────────────
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatCard("Total",     stats.total.toString(),    Modifier.weight(1f))
                    StatCard("Important", stats.important.toString(),Modifier.weight(1f))
                    StatCard("Silenced",  stats.silenced.toString(), Modifier.weight(1f))
                    StatCard("Avg score", "${(stats.avgScore * 100).toInt()}%", Modifier.weight(1f))
                }
            }

            // ── Source breakdown ──────────────────────────────────────
            item {
                Card(shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(1.dp)) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("By source", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Spacer(Modifier.height(10.dp))
                        val maxCount = (stats.bySource.values.maxOrNull() ?: 1).coerceAtLeast(1)
                        NotificationSource.entries.forEach { src ->
                            val count = stats.bySource[src] ?: 0
                            val anim by animateFloatAsState(
                                targetValue = if (maxCount > 0) count.toFloat() / maxCount else 0f,
                                label       = "bar_${src.name}"
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier          = Modifier.padding(vertical = 4.dp)
                            ) {
                                Text("${src.emoji} ${src.displayName}",
                                    fontSize = 12.sp, modifier = Modifier.width(88.dp))
                                Box(
                                    modifier = Modifier
                                        .weight(1f).height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(anim)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(sourceColor(src))
                                    )
                                }
                                Spacer(Modifier.width(8.dp))
                                Text("$count", fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.width(20.dp))
                            }
                        }
                    }
                }
            }

            // ── All notifications ─────────────────────────────────────
            item {
                Text("All notifications", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
            if (notifs.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center) {
                        Text("No notifications yet.", color = MaterialTheme.colorScheme.outline)
                    }
                }
            } else {
                items(notifs, key = { it.id }) { n ->
                    NotificationCard(entity = n, onDelete = { vm.deleteNotification(n.id) })
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier   = modifier,
        shape      = RoundedCornerShape(10.dp),
        tonalElevation = 2.dp,
        color      = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(2.dp))
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
