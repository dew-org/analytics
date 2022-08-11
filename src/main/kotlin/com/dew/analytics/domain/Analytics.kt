package com.dew.analytics.domain

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.ReflectiveAccess
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import java.time.LocalDate
import javax.validation.constraints.NotBlank

@Introspected
@ReflectiveAccess
data class Analytics @Creator @BsonCreator constructor(
    @field:BsonProperty("date")
    @param:BsonProperty("date")
    val date: LocalDate,

    @field:BsonProperty("frequency")
    @param:BsonProperty("frequency")
    val frequency: AnalyticsFrequency,

    @field:BsonProperty("invoices")
    @param:BsonProperty("invoices")
    val invoices: Int,

    @field:BsonProperty("sales")
    @param:BsonProperty("sales")
    val sales: Double,

    @field:BsonProperty("customers")
    @param:BsonProperty("customers")
    val customers: Int,

    @field:BsonProperty("products")
    @param:BsonProperty("products")
    val products: Int,

    @field:BsonProperty("userId")
    @param:BsonProperty("userId")
    @field:NotBlank
    val userId: String,

    @field:BsonProperty("_id")
    @param:BsonProperty("_id")
    val id: ObjectId? = null,
)
