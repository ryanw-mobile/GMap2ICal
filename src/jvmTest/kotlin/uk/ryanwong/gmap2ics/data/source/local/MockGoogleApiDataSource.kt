/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.local

import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.data.source.googleapi.GoogleApiDataSource

class MockGoogleApiDataSource() : GoogleApiDataSource {
    var getMapsApiPlaceDetailsResponse: Result<PlaceDetails>? = null
    var getMapsApiPlaceDetailsLanguageRequested: String? = null

    override suspend fun getMapsApiPlaceDetails(
        placeId: String,
        apiKey: String,
        language: String?
    ): Result<PlaceDetails> {
        getMapsApiPlaceDetailsLanguageRequested = language
        return getMapsApiPlaceDetailsResponse ?: Result.failure(Exception("mock response unavailable"))
    }
}