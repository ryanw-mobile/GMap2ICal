/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.googleapi

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import uk.ryanwong.gmap2ics.data.datasources.googleapi.interfaces.GoogleApiDataSource
import uk.ryanwong.gmap2ics.data.models.places.PlaceDetailsDto
import uk.ryanwong.gmap2ics.data.repositories.PlaceDetailsNotFoundException

class KtorGoogleApiDataSource(
    engine: HttpClientEngine,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : GoogleApiDataSource {
    private val baseUrl = "https://maps.googleapis.com"
    private val placeDetailsUrl = "$baseUrl/maps/api/place/details/json"

    private val httpClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                },
            )
        }
    }

    override suspend fun getMapsApiPlaceDetails(
        placeId: String,
        apiKey: String,
        language: String?,
    ): PlaceDetailsDto = withContext(dispatcher) {
        val response: PlaceDetailsDto? = httpClient.get(placeDetailsUrl) {
            parameter("place_id", placeId)
            parameter("key", apiKey)
            parameter("language", language)
        }.body()

        response?.result?.let {
            response
        } ?: throw PlaceDetailsNotFoundException(placeId = placeId)
    }
}
