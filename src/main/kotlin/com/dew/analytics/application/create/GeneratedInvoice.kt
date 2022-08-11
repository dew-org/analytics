package com.dew.analytics.application.create

import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.ReflectiveAccess

@Introspected
@ReflectiveAccess
data class GeneratedInvoice(
    val products: Int,
    val total: Double,
    val userId: String,
)
