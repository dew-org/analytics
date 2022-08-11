package com.dew.analytics.domain

import io.micronaut.core.annotation.Introspected

@Introspected
enum class AnalyticsFrequency(val value: String) {
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    YEARLY("yearly")
}