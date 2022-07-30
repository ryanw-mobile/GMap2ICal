/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.retrofit

import retrofit2.Response
import uk.ryanwong.gmap2ics.data.source.googleapi.GoogleApiDataSource
import uk.ryanwong.gmap2ics.data.source.googleapi.models.places.PlaceDetails

class RetrofitGoogleApiDataSource : GoogleApiDataSource {
    private val placeDetailsService = PlacesDetailApi.retrofitService

    override suspend fun getPlaceDetails(placeId: String, apiKey: String, language: String?): Response<PlaceDetails> {
        return placeDetailsService.getPlaceDetails(placeId = placeId, key = apiKey, language = language)
    }
}