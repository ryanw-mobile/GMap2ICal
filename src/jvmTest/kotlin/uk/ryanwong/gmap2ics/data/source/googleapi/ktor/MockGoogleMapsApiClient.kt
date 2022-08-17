/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.ktor

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