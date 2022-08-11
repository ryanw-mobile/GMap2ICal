/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.data.source.googleapi.GoogleApiDataSource
import uk.ryanwong.gmap2ics.data.source.googleapi.retrofit.RetrofitGoogleApiDataSource

class PlaceDetailsRepositoryImpl(
    private val networkDataSource: GoogleApiDataSource = RetrofitGoogleApiDataSource(),
    private val placesApiKey: String?,
    private val apiLanguageOverride: Map<String, String>, // TODO: Move to function level when configurable through UI
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : PlaceDetailsRepository {

    private val placesCache = mutableMapOf<String, PlaceDetails>()

    override suspend fun getPlaceDetails(placeId: String, placeTimeZoneId: String?): Result<PlaceDetails> {
        if (placesCache.contains(key = placeId)) {
            return Result.success(placesCache.getValue(placeId))
        }

        return withContext(dispatcher) {
            // Do API lookup and cache the results
            // If user does not supply an API Key means we always return null
            placesApiKey?.let { apiKey ->
                val language: String? = apiLanguageOverride.getOrDefault(
                    key = placeTimeZoneId,
                    defaultValue = apiLanguageOverride.get(key = "default")
                )

                val placeResult =
                    networkDataSource.getMapsApiPlaceDetails(placeId = placeId, apiKey = apiKey, language = language)
                placeResult.getOrNull()?.let { place ->
                    placesCache[placeId] = place
                }
                placeResult
            } ?: Result.failure(PlaceDetailsNotFoundException(placeId = placeId))
        }
    }
}

class GetPlaceDetailsAPIErrorException(apiErrorMessage: String) : Exception() {
    override val message = "⛔️ Error getting API results: $apiErrorMessage"
}

class PlaceDetailsNotFoundException(placeId: String) : Exception() {
    override val message = "⛔️ placeId $placeId not found"
}