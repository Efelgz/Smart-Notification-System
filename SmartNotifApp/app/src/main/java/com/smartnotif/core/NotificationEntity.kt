package com.smartnotif.core

import androidx.room.Entity
import androidx.room.PrimaryKey

// ── Notification sources ──────────────────────────────────────────────────
enum class NotificationSource(val displayName: String, val emoji: String) {
    SMS      ("SMS",       "💬"),
    EMAIL    ("Email",     "✉️"),
    WHATSAPP ("WhatsApp",  "💚"),
    INSTAGRAM("Instagram", "📷"),
    OTHER    ("Other",     "🔔");

    companion object {
        fun fromPackage(pkg: String): NotificationSource = when {
            pkg.contains("whatsapp",   ignoreCase = true) -> WHATSAPP
            pkg.contains("instagram",  ignoreCase = true) -> INSTAGRAM
            pkg.contains("gmail")      || 
            pkg.contains("email")      || 
            pkg.contains("outlook")    || 
            pkg.contains("mail")                          -> EMAIL
            pkg.contains("mms")        || 
            pkg.contains("messaging")  ||
            pkg.contains("sms")                           -> SMS
            else -> OTHER
        }
    }
}

// ── State machine states ──────────────────────────────────────────────────
enum class NotificationState { RECEIVED, ANALYZING, SILENCED, ALERTED }

// ── Room entity — persisted to DB ─────────────────────────────────────────
@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id             : Long   = 0,
    val sender         : String,
    val content        : String,
    val source         : String,              // NotificationSource.name
    val packageName    : String = "",
    val timestampMs    : Long   = System.currentTimeMillis(),
    val importanceScore: Float  = 0f,
    val isImportant    : Boolean = false,
    val state          : String  = NotificationState.RECEIVED.name,
    val reason         : String  = ""         // explains why alerted/silenced
) {
    val sourceEnum: NotificationSource
        get() = NotificationSource.entries.firstOrNull { it.name == source }
            ?: NotificationSource.OTHER
}
