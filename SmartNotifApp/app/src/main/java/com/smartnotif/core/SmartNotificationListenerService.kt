package com.smartnotif.core

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import com.smartnotif.R
import kotlinx.coroutines.*

/**
 * SmartNotificationListenerService
 * ─────────────────────────────────
 * Hooks into the Android OS notification stream (Observer Pattern).
 * Every time an app posts a notification, onNotificationPosted() fires.
 *
 * Requires the user to grant "Notification Access" in system settings.
 * The app prompts them to do this on first launch.
 */
class SmartNotificationListenerService : NotificationListenerService() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val filterEngine = FilterEngine()
    private lateinit var db     : NotificationDatabase
    private lateinit var prefs  : UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        db    = NotificationDatabase.getInstance(applicationContext)
        prefs = UserPreferencesRepository(applicationContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    // ── Called by Android for every incoming notification ──────────────
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val pkg     = sbn.packageName
        if (pkg == packageName) return // 💡 IMPORTANT: Don't intercept our own alerts!

        val extras  = sbn.notification.extras
        val title   = extras.getString("android.title") ?: ""
        val text    = extras.getCharSequence("android.text")?.toString() ?: ""
        val isGroup = extras.getBoolean("android.isGroupConversation")
        val source  = NotificationSource.fromPackage(pkg)
        val content = listOf(title, text).filter { it.isNotBlank() }.joinToString(" — ")

        if (content.isBlank()) return

        serviceScope.launch {
            // Collect current preferences (one-shot)
            prefs.prefsFlow.collect { p ->
                // Set engine mode before processing
                if (p.focusMode) filterEngine.setFocusMode() else filterEngine.setNormalMode()

                val result = filterEngine.process(
                    sender    = title.ifBlank { pkg },
                    content   = content,
                    source    = source,
                    channelOn = p.isChannelEnabled(source),
                    keywords  = p.keywords,
                    threshold = p.threshold,
                    isGroup   = isGroup
                )

                // Persist to Room
                val entity = NotificationEntity(
                    sender          = title.ifBlank { pkg },
                    content         = content,
                    source          = source.name,
                    packageName     = pkg,
                    importanceScore = result.score,
                    isImportant     = result.important,
                    state           = result.state.name,
                    reason          = result.reason
                )
                db.dao().insert(entity)

                // ── Active Filtering Logic ────────────────────────────────
                // If Focus Mode is ON, we intercept and "filter" the OS notifications.
                if (p.focusMode) {
                    Log.d("SmartNotif", "Focus Mode Active. Result Important: ${result.important} for $pkg")
                    // Cancel the original notification so it doesn't distract the user
                    cancelNotification(sbn.key)

                    // If the AI determined it's important, we re-surface it as a Smart Alert
                    if (result.important) {
                        Log.d("SmartNotif", "Triggering Smart Alert for ${title.ifBlank { pkg }}")
                        triggerAlert(title.ifBlank { pkg }, content)
                    }
                }

                return@collect  // one-shot collect
            }
        }
    }

    // ── Post an audible heads-up notification ─────────────────────────
    private fun triggerAlert(title: String, content: String) {
        val channelId = "smart_notif_alerts_v2"
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        if (nm.getNotificationChannel(channelId) == null) {
            val channel = NotificationChannel(
                channelId, 
                "Important Smart Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Urgent notifications filtered by AI"
                enableLights(true)
                enableVibration(true)
                setBypassDnd(true) // 💡 Allows it to break through app-level focus
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
            }
            nm.createNotificationChannel(channel)
        }

        val notif = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("📌 $title")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // For older OS
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setDefaults(NotificationCompat.DEFAULT_ALL) // Sound, Vibrate, Lights
            .setFullScreenIntent(null, true) // Increases chance of heads-up
            .setAutoCancel(true)
            .build()

        // Use a unique positive ID
        val id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
        Log.d("SmartNotif", "Posting notification with ID: $id")
        nm.notify(id, notif)
    }
}
