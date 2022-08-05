/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.retrofit

import uk.ryanwong.gmap2ics.app.models.Place
import uk.ryanwong.gmap2ics.data.except
import uk.ryanwong.gmap2ics.data.repository.GetPlaceDetailsAPIErrorException
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsNotFoundException
import uk.ryanwong.gmap2ics.data.source.googleapi.GoogleApiDataSource
import kotlin.coroutines.cancellation.CancellationException

class RetrofitGoogleApiDataSource : GoogleApiDataSource {
    private val placeDetailsService = PlacesDetailApi.retrofitService

    override suspend fun getPlaceDetails(placeId: String, apiKey: String, language: String?): Result<Place> {
        return Result.runCatching {
            val placeDetailsResponse =
                placeDetailsService.getPlaceDetails(placeId = placeId, key = apiKey, language = language)

            if (!placeDetailsResponse.isSuccessful) {
                throw GetPlaceDetailsAPIErrorException(apiErrorMessage = placeDetailsResponse.message())
            }

            placeDetailsResponse.body()?.result?.let { result ->
                Place.from(placeDetailsResult = result)
            } ?: throw PlaceDetailsNotFoundException(placeId = placeId)
        }.except<CancellationException, _>()
    }
}