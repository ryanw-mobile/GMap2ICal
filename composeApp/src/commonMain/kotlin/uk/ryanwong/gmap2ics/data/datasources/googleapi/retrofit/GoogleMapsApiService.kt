/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.googleapi.retrofit

import org.jetbrains.annotations.Nullable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uk.ryanwong.gmap2ics.data.models.places.PlaceDetails

interface GoogleMapsApiService {
    @GET("/maps/api/place/details/json")
    suspend fun getMapsApiPlaceDetails(
        @Query("place_id") placeId: String,
        @Query("key") key: String,
        @Nullable
        @Query("language")
        language: String? = null,
    ): Response<PlaceDetails>
}
