/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.googleapi.retrofit

import uk.ryanwong.gmap2ics.data.datasources.googleapi.GetPlaceDetailsAPIErrorException
import uk.ryanwong.gmap2ics.data.datasources.googleapi.GoogleApiDataSource
import uk.ryanwong.gmap2ics.data.except
import uk.ryanwong.gmap2ics.data.repositories.PlaceDetailsNotFoundException
import uk.ryanwong.gmap2ics.domain.models.timeline.PlaceDetails
import kotlin.coroutines.cancellation.CancellationException

class RetrofitGoogleApiDataSource(
    private val retrofitService: GoogleMapsApiService = GoogleMapsApiClient.retrofitService,
) : GoogleApiDataSource {

    override suspend fun getMapsApiPlaceDetails(
        placeId: String,
        apiKey: String,
        language: String?,
    ): Result<PlaceDetails> {
        return Result.runCatching {
            val placeDetailsResponse =
                retrofitService.getMapsApiPlaceDetails(placeId = placeId, key = apiKey, language = language)

            if (!placeDetailsResponse.isSuccessful) {
                throw GetPlaceDetailsAPIErrorException(apiErrorMessage = placeDetailsResponse.message())
            }

            placeDetailsResponse.body()?.result?.let { result ->
                PlaceDetails.from(placeDetailsResult = result)
            } ?: throw PlaceDetailsNotFoundException(placeId = placeId)
        }.except<CancellationException, _>()
    }
}
