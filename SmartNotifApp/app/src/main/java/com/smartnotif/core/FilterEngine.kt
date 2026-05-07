package com.smartnotif.core

import com.smartnotif.nlp.AIEngine

// ── Strategy interface ────────────────────────────────────────────────────
interface FilterStrategy {
    val modeName: String
    /** Returns importance score 0..1 and sets entity fields. */
    fun filter(
        sender     : String,
        content    : String,
        source     : NotificationSource,
        keywords   : List<String>,
        threshold  : Float,
        isGroup    : Boolean = false
    ): FilterResult
}

data class FilterResult(
    val score    : Float,
    val important: Boolean,
    val state    : NotificationState,
    val reason   : String
)

// ── NormalStrategy — pass-through ─────────────────────────────────────────
class NormalStrategy : FilterStrategy {
    override val modeName = "Normal Mode"
    override fun filter(sender: String, content: String, source: NotificationSource,
                        keywords: List<String>, threshold: Float, isGroup: Boolean) =
        FilterResult(1f, true, NotificationState.ALERTED, "Normal mode — all pass")
}

// ── FocusStrategy — keyword gate + NLP gate ───────────────────────────────
class FocusStrategy(private val aiEngine: AIEngine = AIEngine()) : FilterStrategy {
    override val modeName = "Focus Mode"

    override fun filter(
        sender    : String,
        content   : String,
        source    : NotificationSource,
        keywords  : List<String>,
        threshold : Float,
        isGroup   : Boolean
    ): FilterResult {
        val scanText = "$sender $content".lowercase()

        // Stage 1 — Keyword gate
        val kwHit = keywords.firstOrNull { it in scanText }
        if (kwHit != null) {
            return FilterResult(1f, true, NotificationState.ALERTED, "Keyword: \"$kwHit\"")
        }

        // Stage 2 — NLP gate
        val isShortcode = sender.replace("+","").replace(" ","").all { it.isDigit() }
                       && sender.replace("+","").length <= 6
        val breakdown = aiEngine.analyzeText(content, isShortcode = isShortcode, isGroup = isGroup)
        val important = breakdown.score > threshold
        val state     = if (important) NotificationState.ALERTED else NotificationState.SILENCED
        val reason    = "NLP score: ${(breakdown.score * 100).toInt()}%" +
                        if (breakdown.clusterNames.isNotEmpty())
                            " — ${breakdown.clusterNames.joinToString()}" else ""
        return FilterResult(breakdown.score, important, state, reason)
    }
}

// ── FilterEngine — coordinates strategy ──────────────────────────────────
class FilterEngine {
    private var strategy: FilterStrategy = NormalStrategy()
    val currentMode: String get() = strategy.modeName

    fun setNormalMode() { strategy = NormalStrategy() }
    fun setFocusMode()  { strategy = FocusStrategy()  }

    fun process(
        sender    : String,
        content   : String,
        source    : NotificationSource,
        channelOn : Boolean,
        keywords  : List<String>,
        threshold : Float,
        isGroup   : Boolean = false
    ): FilterResult {
        if (!channelOn) return FilterResult(0f, false, NotificationState.SILENCED, "Channel disabled")
        return strategy.filter(sender, content, source, keywords, threshold, isGroup)
    }
}
