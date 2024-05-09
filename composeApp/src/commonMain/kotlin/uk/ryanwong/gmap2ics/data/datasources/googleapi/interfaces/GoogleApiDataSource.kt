/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.googleapi.interfaces

import uk.ryanwong.gmap2ics.data.models.places.PlaceDetailsDto

interface GoogleApiDataSource {
    suspend fun getMapsApiPlaceDetails(
        placeId: String,
        apiKey: String,
        language: String?,
    ): Result<PlaceDetailsDto>
}
