/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails

interface PlaceDetailsRepository {
    suspend fun getPlaceDetails(
        placeId: String,
        placeTimeZoneId: String?,
        enablePlacesApiLookup: Boolean,
    ): Result<PlaceDetails>
}
