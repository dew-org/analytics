package com.dew.analytics.application.create

import com.dew.common.domain.bus.Mediator
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.gcp.pubsub.annotation.PubSubListener
import io.micronaut.gcp.pubsub.annotation.Subscription
import reactor.core.publisher.Mono

@Requires(notEnv = [Environment.TEST])
@PubSubListener
class OnGenerateInvoice(private val mediator: Mediator) {

    @Subscription("generate-invoice")
    fun onGenerateInvoice(invoice: GeneratedInvoice) {
        Mono.from(mediator.send(CreateAnalyticsCommand(invoice.products, invoice.total, invoice.userId)))
            .block()
    }
}