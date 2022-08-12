package com.dew.analytics.application.create

import com.dew.common.domain.bus.Mediator
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Requires(notEnv = [Environment.TEST])
@KafkaListener
class OnGenerateInvoice(private val mediator: Mediator) {

    @Topic("generate-invoice")
    fun onGenerateInvoice(invoice: GeneratedInvoice): Flux<Boolean> =
        mediator.send(CreateAnalyticsCommand(invoice.products, invoice.total, invoice.userId))
}