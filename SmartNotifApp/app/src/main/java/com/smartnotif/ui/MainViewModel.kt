package com.smartnotif.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smartnotif.core.*
import com.smartnotif.nlp.AIEngine
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val db    = NotificationDatabase.getInstance(app)
    private val prefs = UserPreferencesRepository(app)
    private val filterEngine = FilterEngine()
    private val aiEngine     = AIEngine()

    // ── Live streams ──────────────────────────────────────────────────────
    val prefsFlow       : StateFlow<UserPreferencesRepository.Prefs>
    val notificationsFlow: StateFlow<List<NotificationEntity>>

    init {
        prefsFlow = prefs.prefsFlow
            .stateIn(viewModelScope, SharingStarted.Eagerly,
                UserPreferencesRepository.Prefs())

        notificationsFlow = db.dao().observeAll()
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    }

    // ── Dashboard stats ───────────────────────────────────────────────────
    val dashboardStats: StateFlow<DashboardStats> = notificationsFlow.map { list ->
        val total   = list.size
        val imp     = list.count { it.isImportant }
        val avg     = if (total > 0) list.sumOf { it.importanceScore.toDouble() } / total else 0.0
        val bySrc   = NotificationSource.entries.associateWith { src ->
            list.count { it.source == src.name }
        }
        DashboardStats(total, imp, total - imp, avg.toFloat(), bySrc)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, DashboardStats())

    // ── User actions ──────────────────────────────────────────────────────

    fun toggleFocusMode(on: Boolean) = viewModelScope.launch {
        prefs.setFocusMode(on)
    }

    fun toggleChannel(source: NotificationSource, on: Boolean) = viewModelScope.launch {
        prefs.setChannel(source, on)
    }

    fun setThreshold(value: Float) = viewModelScope.launch {
        prefs.setThreshold(value)
    }

    fun addKeyword(kw: String) = viewModelScope.launch {
        val current = prefsFlow.value.keywords.toMutableList()
        if (kw.isNotBlank() && kw.lowercase() !in current) {
            current.add(kw.lowercase().trim())
            prefs.setKeywords(current)
        }
    }

    fun removeKeyword(kw: String) = viewModelScope.launch {
        val current = prefsFlow.value.keywords.toMutableList()
        current.remove(kw)
        prefs.setKeywords(current)
    }

    fun clearHistory() = viewModelScope.launch {
        db.dao().clearAll()
    }

    fun deleteNotification(id: Long) = viewModelScope.launch {
        db.dao().deleteById(id)
    }

    // ── NLP live analysis ──────────────────────────────────────────────────
    fun analyzeText(text: String, isShortcode: Boolean, isGroup: Boolean) =
        aiEngine.analyzeText(text, isShortcode, isGroup)
}

data class DashboardStats(
    val total      : Int   = 0,
    val important  : Int   = 0,
    val silenced   : Int   = 0,
    val avgScore   : Float = 0f,
    val bySource   : Map<NotificationSource, Int> = emptyMap()
)
