package com.smartnotif.nlp

/**
 * AIEngine — NLP importance scorer.
 *
 * Implements the Class Diagram:
 *   AIEngine
 *   +float importanceScore
 *   +analyzeText(String text): float
 *   +updateModel(Feedback data)
 */
class AIEngine {

    var importanceScore: Float = 0f
        private set

    // ── Signal banks ──────────────────────────────────────────────────────

    private val marketingSignals = listOf(
        "discount","offer","sale","% off","free","win","prize",
        "click here","subscribe","unsubscribe","promo","coupon",
        "deal","limited time","exclusive","congratulations",
        "winner","selected","reward","hospital network","health insurance promo"
    )

    private val personalSignals = listOf(
        "mom","dad","mother","father","brother","sister","family",
        "grandma","grandpa","wife","husband","son","daughter",
        "friend","prof","@",".edu","please come","come now","call me"
    )

    private val urgentClusters = listOf(
        listOf("hospital","emergency","ambulance","accident","injury","surgery","critical","icu","urgent care"),
        listOf("exam","final","midterm","deadline","submission","assignment","grade","professor","lecture"),
        listOf("urgent","asap","immediately","help me","please call","important","critical","need you","family"),
        listOf("payment","overdue","bill","invoice","password reset","security alert","unauthorized")
    )

    data class ScoreBreakdown(
        val score          : Float,
        val marketingHits  : Int,
        val personalHits   : Int,
        val clusterNames   : List<String>,
        val modifiers      : List<String>
    )

    private val clusterNames = listOf("Medical/safety","Academic","Personal urgency","Financial/security")

    // ── Main scoring pipeline ─────────────────────────────────────────────

    fun analyzeText(
        text       : String,
        isShortcode: Boolean = false,
        isGroup    : Boolean = false
    ): ScoreBreakdown {
        val t = text.lowercase()
        val modifiers = mutableListOf<String>()

        // Step 1 — Marketing penalty
        val marketingHits = marketingSignals.count { it in t }
        if (marketingHits >= 2) {
            val penalised = maxOf(0.05f, 0.30f - marketingHits * 0.06f)
            importanceScore = penalised
            return ScoreBreakdown(penalised, marketingHits, 0, emptyList(),
                listOf("Marketing penalty applied"))
        }

        // Step 2 — Personal boost
        val personalHits = personalSignals.count { it in t }
        var score = minOf(personalHits * 0.15f, 0.45f)

        // Step 3 — Urgency clusters
        val hitClusters = urgentClusters.mapIndexedNotNull { i, cluster ->
            if (cluster.any { it in t }) i else null
        }
        score += minOf(hitClusters.size * 0.20f, 0.60f)

        // Step 4 — Metadata modifiers
        if (isShortcode) { score *= 0.4f; modifiers += "Short-code penalty (×0.4)" }
        if (isGroup)     { score *= 0.7f; modifiers += "Group message penalty (×0.7)" }

        importanceScore = score.coerceIn(0f, 1f)
        return ScoreBreakdown(
            score         = importanceScore,
            marketingHits = marketingHits,
            personalHits  = personalHits,
            clusterNames  = hitClusters.map { clusterNames[it] },
            modifiers     = modifiers
        )
    }
}
