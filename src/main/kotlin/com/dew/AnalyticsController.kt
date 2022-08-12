package com.dew

import com.dew.analytics.application.AnalyticsResponse
import com.dew.analytics.application.create.CreateAnalyticsCommand
import com.dew.analytics.application.find.FindAnalyticsQuery
import com.dew.analytics.domain.Analytics
import com.dew.analytics.domain.AnalyticsFrequency
import com.dew.analytics.domain.AnalyticsRepository
import com.dew.common.domain.bus.Mediator
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import org.bson.types.ObjectId
import reactor.core.publisher.Mono
import java.time.LocalDate

@Controller("/analytics")
@Secured(SecurityRule.IS_AUTHENTICATED)
open class AnalyticsController(private val mediator: Mediator, private val analyticsRepository: AnalyticsRepository) {

    @Get("/now")
    fun find(): Mono<MutableHttpResponse<AnalyticsResponse>> {
        return mediator.send(FindAnalyticsQuery(LocalDate.now(), AnalyticsFrequency.DAILY))
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