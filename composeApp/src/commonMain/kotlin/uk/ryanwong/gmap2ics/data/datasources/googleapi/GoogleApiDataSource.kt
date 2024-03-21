/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.googleapi

import uk.ryanwong.gmap2ics.domain.models.timeline.PlaceDetails

/**
 * The idea for setting up this data source, is to encapsulate the implementation details of the network library.
 * The return value should never be a library-specific object, so we are allow to switch between Retrofit and Ktor easily.
 */
interface GoogleApiDataSource {
    suspend fun getMapsApiPlaceDetails(
        placeId: String,
        apiKey: String,
        language: String?,
    ): Result<PlaceDetails>
}

class GetPlaceDetailsAPIErrorException(apiErrorMessage: String) : Exception() {
    override val message = "⛔️ Error getting API results: $apiErrorMessage"
}
