/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository.mocks

import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepository

class MockPlaceDetailsRepository : PlaceDetailsRepository {
    var getPlaceDetailsResponse: Result<PlaceDetails>? = null
    override suspend fun getPlaceDetails(
        placeId: String,
        placeTimeZoneId: String?,
        enablePlacesApiLookup: Boolean
    ): Result<PlaceDetails> {
        return getPlaceDetailsResponse ?: Result.failure(Exception("mock response unavailable"))
    }
}
