package com.smartnotif.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartnotif.core.NotificationEntity
import com.smartnotif.core.NotificationSource
import java.text.SimpleDateFormat
import java.util.*

// ── Source colors ─────────────────────────────────────────────────────────
fun sourceColor(source: NotificationSource): Color = when (source) {
    NotificationSource.SMS       -> Color(0xFF7F77DD)
    NotificationSource.EMAIL     -> Color(0xFF1D9E75)
    NotificationSource.WHATSAPP  -> Color(0xFF639922)
    NotificationSource.INSTAGRAM -> Color(0xFFD85A30)
    NotificationSource.OTHER     -> Color(0xFF888780)
}

fun sourceColorLight(source: NotificationSource): Color = when (source) {
    NotificationSource.SMS       -> Color(0xFFEEEDFE)
    NotificationSource.EMAIL     -> Color(0xFFE1F5EE)
    NotificationSource.WHATSAPP  -> Color(0xFFEAF3DE)
    NotificationSource.INSTAGRAM -> Color(0xFFFAECE7)
    NotificationSource.OTHER     -> Color(0xFFF1EFE8)
}

// ── Notification card ─────────────────────────────────────────────────────
@Composable
fun NotificationCard(
    entity    : NotificationEntity,
    onDelete  : (() -> Unit)? = null,
    modifier  : Modifier = Modifier
) {
    val src   = entity.sourceEnum
    val color = sourceColor(src)
    val bg    = sourceColorLight(src)
    val pct   = (entity.importanceScore * 100).toInt()

    val animPct by animateFloatAsState(
        targetValue = entity.importanceScore,
        label       = "certainty"
    )

    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Source emoji circle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(bg),
                contentAlignment = Alignment.Center
            ) {
                Text(src.emoji, fontSize = 18.sp)
            }

            Spacer(Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Top row: sender + source badge + state badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text       = entity.sender,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 14.sp,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis,
                        modifier   = Modifier.weight(1f, fill = false)
                    )
                    Spacer(Modifier.width(6.dp))
                    SourceBadge(src)
                    Spacer(Modifier.width(4.dp))
                    StateBadge(entity.isImportant)
                }

                Spacer(Modifier.height(2.dp))
                Text(
                    text     = entity.content,
                    fontSize = 13.sp,
                    color    = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(6.dp))

                // Certainty bar
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LinearProgressIndicator(
                        progress   = { animPct },
                        modifier   = Modifier.weight(1f).height(4.dp).clip(RoundedCornerShape(2.dp)),
                        color      = if (entity.isImportant) color else Color(0xFF888780),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text     = "$pct%",
                        fontSize = 11.sp,
                        color    = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Reason row
                if (entity.reason.isNotBlank()) {
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text     = entity.reason,
                        fontSize = 10.sp,
                        color    = MaterialTheme.colorScheme.outline,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Timestamp
                Spacer(Modifier.height(2.dp))
                Text(
                    text     = formatTime(entity.timestampMs),
                    fontSize = 10.sp,
                    color    = MaterialTheme.colorScheme.outline
                )
            }

            if (onDelete != null) {
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun SourceBadge(source: NotificationSource) {
    val color = sourceColor(source)
    val bg    = sourceColorLight(source)
    Surface(
        shape  = RoundedCornerShape(8.dp),
        color  = bg
    ) {
        Text(
            text     = source.displayName,
            fontSize = 10.sp,
            color    = color,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun StateBadge(important: Boolean) {
    val color = if (important) Color(0xFF534AB7) else Color(0xFF5F5E5A)
    val bg    = if (important) Color(0xFFEEEDFE) else Color(0xFFF1EFE8)
    val label = if (important) "alerted" else "silenced"
    Surface(shape = RoundedCornerShape(8.dp), color = bg) {
        Text(
            text     = label,
            fontSize = 10.sp,
            color    = color,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
        )
    }
}

private val fmt = SimpleDateFormat("HH:mm · dd MMM", Locale.getDefault())
fun formatTime(ms: Long): String = fmt.format(Date(ms))
