/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.local.mocks

import uk.ryanwong.gmap2ics.data.datasources.googleapi.GoogleApiDataSource
import uk.ryanwong.gmap2ics.domain.models.timeline.PlaceDetails

class MockGoogleApiDataSource : GoogleApiDataSource {
    var getMapsApiPlaceDetailsResponse: Result<PlaceDetails>? = null
    var getMapsApiPlaceDetailsLanguageRequested: String? = null

    override suspend fun getMapsApiPlaceDetails(
        placeId: String,
        apiKey: String,
        language: String?,
    ): Result<PlaceDetails> {
        getMapsApiPlaceDetailsLanguageRequested = language
        return getMapsApiPlaceDetailsResponse ?: Result.failure(Exception("mock response unavailable"))
    }
}
