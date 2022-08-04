/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.ryanwong.gmap2ics.app.models.PlaceDetails
import uk.ryanwong.gmap2ics.configs.Config
import uk.ryanwong.gmap2ics.data.except
import uk.ryanwong.gmap2ics.data.source.googleapi.GoogleApiDataSource
import uk.ryanwong.gmap2ics.data.source.googleapi.retrofit.RetrofitGoogleApiDataSource
import kotlin.coroutines.cancellation.CancellationException

class PlaceDetailsRepositoryImpl(
    private val configFile: Config,
    private val networkDataSource: GoogleApiDataSource = RetrofitGoogleApiDataSource(),
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
            configFile.placesApiKey?.let { apiKey ->
                val language: String? = configFile.apiLanguageOverride.getOrDefault(
                    key = placeTimeZoneId,
                    defaultValue = configFile.apiLanguageOverride.get(key = "default")
                )

                Result.runCatching {
                    val placeDetailsResponse =
                        networkDataSource.getPlaceDetails(placeId = placeId, key = apiKey, language = language)

                    if (!placeDetailsResponse.isSuccessful) {
                        throw GetPlaceDetailsAPIErrorException(apiErrorMessage = placeDetailsResponse.message())
                    }

                    placeDetailsResponse.body()?.result?.let { result ->
                        PlaceDetails.from(placeDetailsResult = result)
                            .also { placeDetailsDomainObject ->
                                placesCache[placeId] = placeDetailsDomainObject
                            }
                    } ?: throw PlaceDetailsNotFoundException(placeId = placeId)
                }.except<CancellationException, _>()
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