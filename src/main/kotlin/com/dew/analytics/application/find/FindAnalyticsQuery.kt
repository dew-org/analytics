package com.dew.analytics.application.find

import com.dew.analytics.application.AnalyticsResponse
import com.dew.analytics.domain.AnalyticsFrequency
import com.dew.common.domain.bus.Request
import reactor.core.publisher.Mono
import java.time.LocalDate

data class FindAnalyticsQuery(val date: LocalDate, val frequency: AnalyticsFrequency, val userId: String) :
    Request<Mono<AnalyticsResponse>>
