package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.app.models.PlaceDetails

class MockPlaceDetailsRepository : PlaceDetailsRepository {

    var getPlaceDetailsResponse: PlaceDetails? = null
    override suspend fun getPlaceDetails(placeId: String, placeTimeZoneId: String?): PlaceDetails? {
        return getPlaceDetailsResponse
    }
}