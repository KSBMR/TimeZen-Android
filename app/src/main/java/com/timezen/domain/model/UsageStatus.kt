package com.timezen.domain.model

/**
 * How close a person is to a usage limit (or daily target).
 *
 *  - WITHIN_TARGET:     below 80% of the limit   (green)
 *  - APPROACHING_LIMIT: 80% to 99% of the limit  (orange)
 *  - LIMIT_REACHED:     100% or more             (red)
 *
 * An `enum class` is a fixed list of allowed values. Kotlin's `when` can check
 * that every value is handled, which prevents forgotten cases.
 */
enum class UsageStatus {
    WITHIN_TARGET,
    APPROACHING_LIMIT,
    LIMIT_REACHED;

    companion object {
        const val WARNING_THRESHOLD_PERCENT = 80

        /** A limit of 0 or less means "no limit", so it is never exceeded. */
        fun from(usedMinutes: Int, limitMinutes: Int): UsageStatus {
            if (limitMinutes <= 0) return WITHIN_TARGET
            val percent = usedMinutes * 100 / limitMinutes // whole-number division rounds down
            return when {
                percent >= 100 -> LIMIT_REACHED
                percent >= WARNING_THRESHOLD_PERCENT -> APPROACHING_LIMIT
                else -> WITHIN_TARGET
            }
        }
    }
}
