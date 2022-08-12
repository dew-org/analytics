package com.dew

import com.dew.analytics.application.AnalyticsResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

@Client("/analytics")
interface AnalyticsClient {

    @Get("/now")
    HttpResponse<AnalyticsResponse> find()
}
