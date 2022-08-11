package com.dew.analytics.application.create

import com.dew.analytics.domain.Analytics
import com.dew.analytics.domain.AnalyticsFrequency
import com.dew.analytics.domain.AnalyticsRepository
import com.dew.common.domain.bus.RequestHandler
import jakarta.inject.Singleton
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Singleton
class CreateAnalyticsCommandHandler(private val analyticsRepository: AnalyticsRepository) :
    RequestHandler<CreateAnalyticsCommand, Mono<Boolean>> {

    override fun handle(request: CreateAnalyticsCommand): Mono<Boolean> {
        val now = LocalDate.now()

        val analytics = Flux.concat(
            createOrUpdateAnalytics(now, AnalyticsFrequency.DAILY, request),
            createOrUpdateAnalytics(now, AnalyticsFrequency.WEEKLY, request),
            createOrUpdateAnalytics(now, AnalyticsFrequency.MONTHLY, request),
            createOrUpdateAnalytics(now, AnalyticsFrequency.YEARLY, request)
        )

        return Mono.from(analytics).map { true }
    }

    private fun createOrUpdateAnalytics(
        now: LocalDate,
        frequency: AnalyticsFrequency,
        request: CreateAnalyticsCommand
    ): Mono<Boolean> {
        val dailyAnalytics = analyticsRepository.find(frequency, now).map { analytics: Analytics? ->
            if (analytics != null) {
                return@map Flux.concat(
                    analyticsRepository.increaseCustomers(analytics.id!!),
                    analyticsRepository.increaseInvoices(analytics.id),
                    analyticsRepository.increaseProducts(analytics.id, request.products),
                    analyticsRepository.increaseSales(analytics.id, request.total)
                )
            } else {
                val newAnalytics =
                    Analytics(now, frequency, 1, request.total, 1, request.products, request.userId)
                return@map Flux.from(analyticsRepository.save(newAnalytics))
            }
        }
        return Mono.from(dailyAnalytics).map { true }
    }
}