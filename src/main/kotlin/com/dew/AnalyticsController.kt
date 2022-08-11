package com.dew

import com.dew.analytics.application.AnalyticsResponse
import com.dew.analytics.application.find.FindAnalyticsQuery
import com.dew.analytics.domain.AnalyticsFrequency
import com.dew.common.domain.bus.Mediator
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import reactor.core.publisher.Mono
import java.time.LocalDate

@Controller("/analytics")
@Secured(SecurityRule.IS_AUTHENTICATED)
open class AnalyticsController(private val mediator: Mediator) {

    @Get("/now")
    fun find(): Mono<HttpResponse<AnalyticsResponse>> {
        return mediator.send(FindAnalyticsQuery(LocalDate.now(), AnalyticsFrequency.DAILY))
            .map { if (it != null) HttpResponse.ok(it) else HttpResponse.ok(DEFAULT_RESPONSE) }
    }

    companion object {
        val DEFAULT_RESPONSE = AnalyticsResponse(
            LocalDate.now(),
            AnalyticsFrequency.DAILY,
            0,
            0.0,
            0,
            0,
            "",
            ""
        )
    }
}