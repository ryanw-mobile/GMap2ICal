/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.repositories.fakes

import uk.ryanwong.gmap2ics.domain.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.domain.repositories.PlaceDetailsRepository

class FakePlaceDetailsRepository : PlaceDetailsRepository {
    var getPlaceDetailsResponse: Result<PlaceDetails>? = null
    override suspend fun getPlaceDetails(
        placeId: String,
        placeTimeZoneId: String?,
        enablePlacesApiLookup: Boolean,
    ): Result<PlaceDetails> {
        return getPlaceDetailsResponse ?: Result.failure(Exception("response not defined"))
    }
}
