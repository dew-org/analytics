package com.dew.analytics.application

import com.dew.analytics.domain.AnalyticsFrequency
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.ReflectiveAccess
import java.time.LocalDate

@Introspected
@ReflectiveAccess
data class AnalyticsResponse(
    val date: LocalDate,
    val frequency: AnalyticsFrequency,
    val invoices: Int,
    val sales: Double,
    val customers: Int,
    val products: Int,
    val userId: String,
    val id: String
)
