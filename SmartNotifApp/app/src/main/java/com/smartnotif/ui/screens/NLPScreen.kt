package com.smartnotif.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartnotif.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NLPScreen(vm: MainViewModel) {
    var inputText   by remember { mutableStateOf("") }
    var isShortcode by remember { mutableStateOf(false) }
    var isGroup     by remember { mutableStateOf(false) }

    val breakdown = remember(inputText, isShortcode, isGroup) {
        if (inputText.isBlank()) null
        else vm.analyzeText(inputText, isShortcode, isGroup)
    }

    val animScore by animateFloatAsState(
        targetValue = breakdown?.score ?: 0f,
        label       = "nlp_score"
    )

    val prefs by vm.prefsFlow.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title  = { Text("NLP analyser", fontSize = 17.sp, fontWeight = FontWeight.SemiBold) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Input ─────────────────────────────────────────────────
            OutlinedTextField(
                value         = inputText,
                onValueChange = { inputText = it },
                label         = { Text("Type any message to analyse…") },
                modifier      = Modifier.fillMaxWidth().heightIn(min = 80.dp),
                maxLines      = 4
            )
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isShortcode, onCheckedChange = { isShortcode = it })
                    Text("Short-code sender", fontSize = 13.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isGroup, onCheckedChange = { isGroup = it })
                    Text("Group message", fontSize = 13.sp)
                }
            }

            // ── Score display ─────────────────────────────────────────
            if (breakdown != null) {
                val pct   = (breakdown.score * 100).toInt()
                val above = breakdown.score > prefs.threshold
                val color = if (above) MaterialTheme.colorScheme.primary else Color(0xFF888780)

                Card(
                    shape  = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text       = "$pct%",
                                fontSize   = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color      = color
                            )
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    text     = if (above) "Would ALERT" else "Would SILENCE",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color    = color
                                )
                                Text(
                                    text     = "Threshold: ${(prefs.threshold * 100).toInt()}%",
                                    fontSize = 11.sp,
                                    color    = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress   = { animScore },
                            modifier   = Modifier.fillMaxWidth().height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color      = color,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )

                        // Signal breakdown
                        Spacer(Modifier.height(14.dp))
                        Text("Signal breakdown", fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp, color = MaterialTheme.colorScheme.outline)
                        Spacer(Modifier.height(8.dp))

                        SignalRow("Marketing signals",
                            "${breakdown.marketingHits}",
                            if (breakdown.marketingHits >= 2) "Penalty applied" else "",
                            if (breakdown.marketingHits >= 2) Color(0xFFD85A30) else MaterialTheme.colorScheme.onSurface)

                        SignalRow("Personal signals",
                            "${breakdown.personalHits}",
                            if (breakdown.personalHits > 0) "+${(minOf(breakdown.personalHits * 0.15f, 0.45f) * 100).toInt()}% boost" else "",
                            if (breakdown.personalHits > 0) Color(0xFF1D9E75) else MaterialTheme.colorScheme.onSurface)

                        SignalRow("Urgency clusters",
                            "${breakdown.clusterNames.size}",
                            breakdown.clusterNames.joinToString(", "),
                            if (breakdown.clusterNames.isNotEmpty()) Color(0xFF7F77DD) else MaterialTheme.colorScheme.onSurface)

                        if (breakdown.modifiers.isNotEmpty()) {
                            breakdown.modifiers.forEach { mod ->
                                SignalRow("Modifier", "–", mod, Color(0xFFBA7517))
                            }
                        }
                    }
                }
            }

            // ── Example messages ──────────────────────────────────────
            Text("Try these examples", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)

            val examples = listOf(
                Triple("Mom is at the hospital — emergency room. Please come now!", false, false),
                Triple("Exclusive health insurance promo! Free checkup discount offer!", true,  false),
                Triple("The final exam is on May 10th. This is urgent.", false, false),
                Triple("WIN a FREE iPhone! Click here to claim your prize!", true,  false),
                Triple("Hey anyone has the homework answers?", false, true),
                Triple("Call me when you can, no rush at all.", false, false),
            )
            examples.forEach { (text, sc, grp) ->
                OutlinedButton(
                    onClick  = { inputText = text; isShortcode = sc; isGroup = grp },
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text     = (if (sc) "📱 " else "") + text,
                        fontSize = 12.sp,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

@Composable
private fun SignalRow(label: String, value: String, note: String, valueColor: Color) {
    Row(
        modifier          = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(130.dp))
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = valueColor,
            modifier = Modifier.width(24.dp))
        Text(note, fontSize = 11.sp, color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.weight(1f))
    }
}
