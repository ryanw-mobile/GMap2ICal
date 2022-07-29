package uk.ryanwong.gmap2ics.data.source.network

import retrofit2.Response
import uk.ryanwong.gmap2ics.data.models.places.PlaceDetails

interface NetworkDataSource {
    suspend fun getPlaceDetails(placeId: String, key: String, language: String? = null): Response<PlaceDetails>
}