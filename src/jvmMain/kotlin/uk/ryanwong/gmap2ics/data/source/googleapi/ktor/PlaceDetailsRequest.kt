/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.ktor

import kotlinx.serialization.Serializable

@Serializable
data class PlaceDetailsRequest(
    val place_id: String,
    val key: String,
    val language: String? = null
)
//interface PlaceDetailsService {
//    @GET("/maps/api/place/details/json")
//    suspend fun getPlaceDetails(
//        @Query("place_id") placeId: String,
//        @Query("key") key: String,
//        @Nullable @Query("language") language: String? = null
//    ): Response<PlaceDetails>
//}
