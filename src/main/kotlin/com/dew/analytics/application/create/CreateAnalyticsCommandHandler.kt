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
class CreateAnalyticsCommandHandler(private val repository: AnalyticsRepository) :
    RequestHandler<CreateAnalyticsCommand, Flux<Boolean>> {

    override fun handle(request: CreateAnalyticsCommand): Flux<Boolean> {
        val now = LocalDate.now()

        return Flux.just(
            createOrUpdateAnalytics(now, AnalyticsFrequency.DAILY, request),
            createOrUpdateAnalytics(now, AnalyticsFrequency.WEEKLY, request),
            createOrUpdateAnalytics(now, AnalyticsFrequency.MONTHLY, request),
            createOrUpdateAnalytics(now, AnalyticsFrequency.YEARLY, request)
        ).flatMap { it.map { true } }
    }

    private fun createOrUpdateAnalytics(
        now: LocalDate, frequency: AnalyticsFrequency, request: CreateAnalyticsCommand
    ): Mono<Boolean> {

        return repository.find(frequency, now, request.userId).switchIfEmpty(
            Mono.just(Analytics(now, frequency, 1, request.total, 1, request.products, request.userId))
        ).flatMap { analytics ->
            if (analytics.id != null) {
                return@flatMap Mono.from(
                    Flux.just(
                        repository.increaseCustomers(analytics.id),
                        repository.increaseInvoices(analytics.id),
                        repository.increaseProducts(analytics.id, request.products),
                        repository.increaseSales(analytics.id, request.total)
                    ).flatMap { it.map { true } }
                )
            }

            repository.save(analytics).map { true }
        }
    }
}