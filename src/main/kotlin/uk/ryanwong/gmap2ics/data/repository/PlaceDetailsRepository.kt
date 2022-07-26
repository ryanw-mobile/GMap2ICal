package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.domain.models.PlaceDetails

interface PlaceDetailsRepository {
    suspend fun getPlaceDetails(placeId: String, placeTimeZoneId: String?): PlaceDetails?
}