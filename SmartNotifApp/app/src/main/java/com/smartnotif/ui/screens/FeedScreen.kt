package com.smartnotif.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartnotif.ui.MainViewModel
import com.smartnotif.ui.components.NotificationCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(vm: MainViewModel) {
    val notifications by vm.notificationsFlow.collectAsState()
    val prefs         by vm.prefsFlow.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Notification feed", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                        Text(
                            text     = if (prefs.focusMode) "Focus mode active" else "Normal mode",
                            fontSize = 12.sp,
                            color    = MaterialTheme.colorScheme.outline
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            // ── Feed list ─────────────────────────────────────────────
            if (notifications.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔔", fontSize = 48.sp)
                        Spacer(Modifier.height(12.dp))
                        Text("No intercepted notifications", fontWeight = FontWeight.Medium)
                        Text(
                            "Incoming notifications from WhatsApp, SMS, etc. will appear here after filtering.",
                            fontSize = 12.sp,
                            color    = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(horizontal = 32.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier            = Modifier.fillMaxSize(),
                    contentPadding      = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notifications, key = { it.id }) { n ->
                        NotificationCard(
                            entity   = n,
                            onDelete = { vm.deleteNotification(n.id) }
                        )
                    }
                }
            }
        }
    }
}
