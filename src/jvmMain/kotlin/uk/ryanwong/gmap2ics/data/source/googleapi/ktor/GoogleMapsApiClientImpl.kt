/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import uk.ryanwong.gmap2ics.data.models.places.PlaceDetails

class GoogleMapsApiClientImpl(engine: HttpClientEngine) : GoogleMapsApiClient {
    private val httpClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    override suspend fun getPlaceDetails(
        placeId: String, apiKey: String, language: String?
    ): PlaceDetails? {
        return httpClient.get(HttpRoutes.PLACE_DETAILS) {
            parameter("place_id", placeId)
            parameter("key", apiKey)
            parameter("language", language)
        }.body()
    }
}