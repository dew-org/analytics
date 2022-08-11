package com.dew.analytics.domain

import org.bson.types.ObjectId
import reactor.core.publisher.Mono
import java.time.LocalDate

interface AnalyticsRepository {

    fun save(analytics: Analytics): Mono<Boolean>

    fun find(frequency: AnalyticsFrequency, date: LocalDate): Mono<Analytics>

    fun increaseInvoices(id: ObjectId): Mono<Boolean>

    fun increaseSales(id: ObjectId, amount: Double): Mono<Boolean>

    fun increaseCustomers(id: ObjectId): Mono<Boolean>

    fun increaseProducts(id: ObjectId, amount: Int): Mono<Boolean>
}