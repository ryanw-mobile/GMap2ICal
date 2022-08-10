/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.ryanwong.gmap2ics.app.models.Place
import uk.ryanwong.gmap2ics.configs.Config
import uk.ryanwong.gmap2ics.data.source.googleapi.GoogleApiDataSource
import uk.ryanwong.gmap2ics.data.source.googleapi.retrofit.RetrofitGoogleApiDataSource

class PlaceDetailsRepositoryImpl(
    private val configFile: Config,
    private val networkDataSource: GoogleApiDataSource = RetrofitGoogleApiDataSource(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : PlaceDetailsRepository {

    private val placesCache = mutableMapOf<String, Place>()

    override suspend fun getPlaceDetails(placeId: String, placeTimeZoneId: String?): Result<Place> {
        if (placesCache.contains(key = placeId)) {
            return Result.success(placesCache.getValue(placeId))
        }

        return withContext(dispatcher) {
            // Do API lookup and cache the results
            // If user does not supply an API Key means we always return null
            configFile.placesApiKey?.let { apiKey ->
                val language: String? = configFile.apiLanguageOverride.getOrDefault(
                    key = placeTimeZoneId,
                    defaultValue = configFile.apiLanguageOverride.get(key = "default")
                )

                val placeResult =
                    networkDataSource.getPlaceDetails(placeId = placeId, apiKey = apiKey, language = language)
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