package uk.ryanwong.gmap2ics.data.source.network.retrofit

import retrofit2.Response
import uk.ryanwong.gmap2ics.data.models.places.PlaceDetails
import uk.ryanwong.gmap2ics.data.source.network.NetworkDataSource

class RetrofitNetworkDataSource : NetworkDataSource {
    private val placeDetailsService = PlacesDetailApi.retrofitService

    override suspend fun getPlaceDetails(placeId: String, apiKey: String, language: String?): Response<PlaceDetails> {
        return placeDetailsService.getPlaceDetails(placeId = placeId, key = apiKey, language = language)
    }
}