package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.app.models.PlaceDetails

interface PlaceDetailsRepository {
    suspend fun getPlaceDetails(placeId: String, placeTimeZoneId: String?): Result<PlaceDetails>
}