/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.app.models.Place

class MockPlaceDetailsRepository : PlaceDetailsRepository {
    var getPlaceResponse: Result<Place>? = null
    override suspend fun getPlaceDetails(placeId: String, placeTimeZoneId: String?): Result<Place> {
        return getPlaceResponse ?: Result.failure(Exception("mock response unavailable"))
    }
}