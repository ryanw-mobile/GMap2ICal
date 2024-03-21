/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.googleapi.ktor.mocks

import uk.ryanwong.gmap2ics.data.datasources.googleapi.ktor.GoogleMapsApiClient
import uk.ryanwong.gmap2ics.data.models.places.PlaceDetails

class MockGoogleMapsApiClient : GoogleMapsApiClient {
    var getPlaceDetailsException: Throwable? = null
    var getPlaceDetailsResponse: PlaceDetails? = null
    override suspend fun getPlaceDetails(placeId: String, apiKey: String, language: String?): PlaceDetails? {
        return getPlaceDetailsException?.let {
            throw it
        } ?: getPlaceDetailsResponse
    }
}
