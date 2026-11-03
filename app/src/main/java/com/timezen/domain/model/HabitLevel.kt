package com.timezen.domain.model

/** Shown wherever a habit level appears. This is NOT a medical or clinical measure. */
const val HABIT_LEVEL_DISCLAIMER =
    "Your habit level is a digital habit awareness score based on your own goals. " +
        "It is not a medical diagnosis."

/**
 * Digital habit levels, from healthiest to least healthy.
 * The rules that choose a level are written in Phase 10; Phase 1 only defines the names.
 */
enum class HabitLevel(val displayName: String) {
    CHAMPION("Champion"),
    BALANCED("Balanced"),
    MODERATE("Moderate"),
    HEAVY("Heavy"),
    EXCESSIVE("Excessive"),
    ADDICTED("Addicted"),
}
