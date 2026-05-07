package com.smartnotif.core

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        val KEY_FOCUS_MODE    = booleanPreferencesKey("focus_mode")
        val KEY_SMS_ON        = booleanPreferencesKey("sms_on")
        val KEY_EMAIL_ON      = booleanPreferencesKey("email_on")
        val KEY_WHATSAPP_ON   = booleanPreferencesKey("whatsapp_on")
        val KEY_INSTAGRAM_ON  = booleanPreferencesKey("instagram_on")
        val KEY_OTHER_ON      = booleanPreferencesKey("other_on")
        val KEY_THRESHOLD     = floatPreferencesKey("threshold")
        val KEY_KEYWORDS      = stringPreferencesKey("keywords")  // comma-separated

        val DEFAULT_KEYWORDS  = listOf(
            "urgent","exam","final","deadline","family",
            "hospital","emergency","important","asap","critical"
        )
    }

    data class Prefs(
        val focusMode   : Boolean        = false,
        val smsOn       : Boolean        = true,
        val emailOn     : Boolean        = true,
        val whatsAppOn  : Boolean        = true,
        val instagramOn : Boolean        = false,
        val otherOn     : Boolean        = true,
        val threshold   : Float          = 0.80f,
        val keywords    : List<String>   = DEFAULT_KEYWORDS
    ) {
        fun isChannelEnabled(source: NotificationSource) = when (source) {
            NotificationSource.SMS       -> smsOn
            NotificationSource.EMAIL     -> emailOn
            NotificationSource.WHATSAPP  -> whatsAppOn
            NotificationSource.INSTAGRAM -> instagramOn
            NotificationSource.OTHER     -> otherOn
        }
    }

    val prefsFlow: Flow<Prefs> = context.dataStore.data.map { p ->
        Prefs(
            focusMode   = p[KEY_FOCUS_MODE]   ?: false,
            smsOn       = p[KEY_SMS_ON]        ?: true,
            emailOn     = p[KEY_EMAIL_ON]      ?: true,
            whatsAppOn  = p[KEY_WHATSAPP_ON]   ?: true,
            instagramOn = p[KEY_INSTAGRAM_ON]  ?: false,
            otherOn     = p[KEY_OTHER_ON]      ?: true,
            threshold   = p[KEY_THRESHOLD]     ?: 0.80f,
            keywords    = (p[KEY_KEYWORDS] ?: DEFAULT_KEYWORDS.joinToString(","))
                            .split(",").map { it.trim() }.filter { it.isNotEmpty() }
        )
    }

    suspend fun setFocusMode(on: Boolean)  = context.dataStore.edit { it[KEY_FOCUS_MODE]   = on }
    suspend fun setChannel(source: NotificationSource, on: Boolean) {
        context.dataStore.edit { p ->
            when (source) {
                NotificationSource.SMS       -> p[KEY_SMS_ON]       = on
                NotificationSource.EMAIL     -> p[KEY_EMAIL_ON]     = on
                NotificationSource.WHATSAPP  -> p[KEY_WHATSAPP_ON]  = on
                NotificationSource.INSTAGRAM -> p[KEY_INSTAGRAM_ON] = on
                NotificationSource.OTHER     -> p[KEY_OTHER_ON]     = on
            }
        }
    }
    suspend fun setThreshold(v: Float)       = context.dataStore.edit { it[KEY_THRESHOLD] = v }
    suspend fun setKeywords(kws: List<String>) = context.dataStore.edit { it[KEY_KEYWORDS] = kws.joinToString(",") }
}
