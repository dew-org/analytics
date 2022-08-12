package com.dew.analytics.infrastructure.persistence.mongo

import com.dew.analytics.domain.Analytics
import com.dew.analytics.domain.AnalyticsFrequency
import com.dew.analytics.domain.AnalyticsRepository
import com.dew.common.infrastructure.persistence.mongo.MongoDbConfiguration
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Field
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import jakarta.inject.Singleton
import org.bson.Document
import org.bson.types.ObjectId
import reactor.core.publisher.Mono
import java.time.LocalDate

@Singleton
class MongoDbAnalyticsRepository(
    private val mongoDbConfiguration: MongoDbConfiguration,
    private val mongoClient: MongoClient

) : AnalyticsRepository {

    override fun save(analytics: Analytics): Mono<ObjectId> = Mono.from(
        collection.insertOne(analytics)
    ).mapNotNull { success -> success.insertedId?.asObjectId()?.value }

    override fun find(frequency: AnalyticsFrequency, date: LocalDate, userId: String): Mono<Analytics> = Mono.from(
        collection.aggregate(
            listOf(
                Aggregates.addFields(
                    Field("month", Document("\$month", "\$date")),
                    Field("year", Document("\$year", "\$date"))
                ),
                Aggregates.match(
                    Filters.and(
                        Filters.eq("frequency", frequency.name),
                        Filters.eq("userId", userId),
                        when (frequency) {
                            AnalyticsFrequency.DAILY -> Filters.eq("date", date)

                            AnalyticsFrequency.MONTHLY -> Filters.and(
                                Filters.eq("month", date.monthValue), Filters.eq("year", date.year)
                            )

                            AnalyticsFrequency.YEARLY -> Filters.eq("year", date.year)
                            else -> Filters.empty()
                        }
                    )
                )
            )
        ).first()
    )

    private fun increment(id: ObjectId, field: String, amount: Number = 1): Mono<Boolean> = Mono.from(
        collection.updateOne(
            Filters.eq("_id", id),
            Updates.inc(field, amount)
        )
    ).map { true }.onErrorReturn(false)

    override fun increaseInvoices(id: ObjectId): Mono<Boolean> = increment(id, "invoices")

    override fun increaseSales(id: ObjectId, amount: Double): Mono<Boolean> = increment(id, "sales", amount)

    override fun increaseCustomers(id: ObjectId): Mono<Boolean> = increment(id, "customers")

    override fun increaseProducts(id: ObjectId, amount: Int): Mono<Boolean> = increment(id, "products", amount)

    private val collection: MongoCollection<Analytics>
        get() = mongoClient.getDatabase(mongoDbConfiguration.name)
            .getCollection(mongoDbConfiguration.collection, Analytics::class.java)
}