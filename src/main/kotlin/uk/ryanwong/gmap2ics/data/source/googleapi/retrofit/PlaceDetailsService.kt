package uk.ryanwong.gmap2ics.data.source.googleapi.retrofit

import org.jetbrains.annotations.Nullable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceDetailsService {
    @GET("/maps/api/place/details/json")
    suspend fun getPlaceDetails(
        @Query("place_id") placeId: String,
        @Query("key") key: String,
        @Nullable @Query("language") language: String? = null
    ): Response<uk.ryanwong.gmap2ics.data.source.googleapi.models.places.PlaceDetails>
}
