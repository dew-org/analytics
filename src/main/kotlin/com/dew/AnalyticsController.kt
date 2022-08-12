package com.dew

import com.dew.analytics.application.AnalyticsResponse
import com.dew.analytics.application.find.FindAnalyticsQuery
import com.dew.analytics.domain.AnalyticsFrequency
import com.dew.common.domain.bus.Mediator
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import reactor.core.publisher.Mono
import java.time.LocalDate

@Controller("/analytics")
@Secured(SecurityRule.IS_AUTHENTICATED)
open class AnalyticsController(private val mediator: Mediator) {

    @Get("/now")
    fun find(@QueryValue("userId") userId: String): Mono<MutableHttpResponse<AnalyticsResponse>> {
        return mediator.send(FindAnalyticsQuery(LocalDate.now(), AnalyticsFrequency.DAILY, userId))
            .switchIfEmpty(Mono.just(DEFAULT_RESPONSE))
            .map { HttpResponse.ok(it) }
    }

    @Get("/{date}/{frequency}")
    fun find(
        date: String,
        frequency: String,
        @QueryValue("userId") userId: String
    ): Mono<MutableHttpResponse<AnalyticsResponse>> {
        return mediator.send(FindAnalyticsQuery(LocalDate.parse(date), AnalyticsFrequency.valueOf(frequency), userId))
            .switchIfEmpty(Mono.just(DEFAULT_RESPONSE))
            .map { HttpResponse.ok(it) }
    }

    companion object {
        val DEFAULT_RESPONSE = AnalyticsResponse(
            LocalDate.now(),
            AnalyticsFrequency.DAILY,
            0,
            0.0,
            0,
            0,
            "NA",
        )
    }
}