package com.smartnotif.ui.screens

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartnotif.core.NotificationSource
import com.smartnotif.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(vm: MainViewModel) {
    val prefs   by vm.prefsFlow.collectAsState()
    val context = LocalContext.current
    var newKw   by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title  = { Text("Settings", fontSize = 17.sp, fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ── Mode ──────────────────────────────────────────────────
            SectionCard("Mode") {
                Row(
                    modifier          = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(if (prefs.focusMode) "Focus mode" else "Normal mode",
                            fontWeight = FontWeight.SemiBold)
                        Text(
                            if (prefs.focusMode)
                                "Only important notifications alert"
                            else
                                "All notifications alert normally",
                            fontSize = 12.sp,
                            color    = MaterialTheme.colorScheme.outline
                        )
                    }
                    Switch(
                        checked         = prefs.focusMode,
                        onCheckedChange = { vm.toggleFocusMode(it) }
                    )
                }
            }

            // ── Channels ──────────────────────────────────────────────
            SectionCard("Channel preferences") {
                NotificationSource.entries.forEach { src ->
                    val on = prefs.isChannelEnabled(src)
                    Row(
                        modifier          = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${src.emoji}  ${src.displayName}", fontSize = 14.sp)
                        Switch(
                            checked         = on,
                            onCheckedChange = { vm.toggleChannel(src, it) }
                        )
                    }
                }
            }

            // ── Threshold ─────────────────────────────────────────────
            SectionCard("Importance threshold") {
                Text(
                    "Notifications must score above ${(prefs.threshold * 100).toInt()}% to alert in Focus mode.",
                    fontSize = 12.sp,
                    color    = MaterialTheme.colorScheme.outline
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Slider(
                        value         = prefs.threshold,
                        onValueChange = { vm.setThreshold(it) },
                        valueRange    = 0.40f..0.99f,
                        steps         = 58,
                        modifier      = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "${(prefs.threshold * 100).toInt()}%",
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 14.sp,
                        modifier   = Modifier.width(40.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Low (40%)" to 0.40f, "Medium (80%)" to 0.80f, "High (92%)" to 0.92f).forEach { (label, v) ->
                        OutlinedButton(
                            onClick  = { vm.setThreshold(v) },
                            shape    = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).padding(horizontal = 2.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                        ) {
                            Text(label, fontSize = 10.sp)
                        }
                    }
                }
            }

            // ── Keywords ──────────────────────────────────────────────
            SectionCard("Keywords  (instant-pass in Focus mode)") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement   = Arrangement.spacedBy(6.dp),
                    modifier              = Modifier.fillMaxWidth()
                ) {
                    prefs.keywords.forEach { kw ->
                        InputChip(
                            selected  = false,
                            onClick   = {},
                            label     = { Text(kw, fontSize = 12.sp) },
                            trailingIcon = {
                                IconButton(
                                    onClick  = { vm.removeKeyword(kw) },
                                    modifier = Modifier.size(16.dp)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Remove",
                                        modifier = Modifier.size(12.dp))
                                }
                            }
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value         = newKw,
                        onValueChange = { newKw = it },
                        label         = { Text("Add keyword", fontSize = 12.sp) },
                        singleLine    = true,
                        modifier      = Modifier.weight(1f),
                        textStyle     = LocalTextStyle.current.copy(fontSize = 13.sp)
                    )
                    FilledTonalButton(onClick = {
                        if (newKw.isNotBlank()) { vm.addKeyword(newKw); newKw = "" }
                    }) {
                        Text("Add")
                    }
                }
            }

            // ── Notification access ───────────────────────────────────
            SectionCard("Notification access") {
                Text(
                    "Grant access so the app can intercept real notifications from WhatsApp, SMS, and other apps.",
                    fontSize = 12.sp,
                    color    = MaterialTheme.colorScheme.outline
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        context.startActivity(
                            Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Open notification access settings")
                }
            }
        }
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}
