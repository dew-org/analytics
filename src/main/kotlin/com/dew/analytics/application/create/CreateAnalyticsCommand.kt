package com.dew.analytics.application.create

import com.dew.common.domain.bus.Request
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

data class CreateAnalyticsCommand(
    val products: Int,
    val total: Double,
    val userId: String,
) : Request<Flux<Boolean>>