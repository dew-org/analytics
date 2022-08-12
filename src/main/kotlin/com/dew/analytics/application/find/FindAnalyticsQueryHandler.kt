package com.dew.analytics.application.find

import com.dew.analytics.application.AnalyticsResponse
import com.dew.analytics.domain.AnalyticsRepository
import com.dew.common.domain.bus.RequestHandler
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.inject.Singleton
import reactor.core.publisher.Mono

@Singleton
class FindAnalyticsQueryHandler(
    private val analyticsRepository: AnalyticsRepository,
    private val objectMapper: ObjectMapper
) :
    RequestHandler<FindAnalyticsQuery, Mono<AnalyticsResponse>> {

    override fun handle(request: FindAnalyticsQuery): Mono<AnalyticsResponse> {
        return analyticsRepository.find(request.frequency, request.date)
            .map { objectMapper.convertValue(it, AnalyticsResponse::class.java) }
    }
}