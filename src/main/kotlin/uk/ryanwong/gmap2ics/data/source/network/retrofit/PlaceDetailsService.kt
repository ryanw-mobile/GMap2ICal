package uk.ryanwong.gmap2ics.data.source.network.retrofit

import org.jetbrains.annotations.Nullable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uk.ryanwong.gmap2ics.data.models.places.PlaceDetails

interface PlaceDetailsService {
    @GET("/maps/api/place/details/json")
    suspend fun getPlaceDetails(
        @Query("place_id") placeId: String,
        @Query("key") key: String,
        @Nullable @Query("language") language: String? = null
    ): Response<PlaceDetails>
}
