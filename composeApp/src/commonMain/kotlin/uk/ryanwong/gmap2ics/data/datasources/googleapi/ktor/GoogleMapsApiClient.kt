/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.googleapi.ktor

import uk.ryanwong.gmap2ics.data.models.places.PlaceDetails

interface GoogleMapsApiClient {
    suspend fun getPlaceDetails(
        placeId: String,
        apiKey: String,
        language: String?,
    ): PlaceDetails?
}
