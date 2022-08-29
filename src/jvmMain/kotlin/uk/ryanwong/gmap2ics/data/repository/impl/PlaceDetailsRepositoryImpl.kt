/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.source.googleapi.GoogleApiDataSource

class PlaceDetailsRepositoryImpl(
    private val networkDataSource: GoogleApiDataSource,
    private val placesApiKey: String?,
    private val apiLanguageOverride: Map<String, String>, // TODO: Move to function level when configurable through UI
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : PlaceDetailsRepository {

    // TODO: Can put this in local database so we lookup once and for all.
    private val placesCache = mutableMapOf<String, PlaceDetails>()

    override suspend fun getPlaceDetails(
        placeId: String,
        placeTimeZoneId: String?,
        enablePlacesApiLookup: Boolean
    ): Result<PlaceDetails> {
        return when {
            placesCache.contains(key = placeId) -> {
                Result.success(placesCache.getValue(placeId))
            }

            !enablePlacesApiLookup -> {
                Result.failure(PlaceDetailsNotFoundException(placeId = placeId))
            }

            else -> {
                withContext(dispatcher) {
                    // Do API lookup and cache the results
                    // If user does not supply an API Key means we always return null
                    placesApiKey?.let { apiKey ->
                        val language: String? = apiLanguageOverride.getOrDefault(
                            key = placeTimeZoneId,
                            defaultValue = apiLanguageOverride.get(key = "default")
                        )

                        val placeResult =
                            networkDataSource.getMapsApiPlaceDetails(
                                placeId = placeId,
                                apiKey = apiKey,
                                language = language
                            )
                        placeResult.getOrNull()?.let { place ->
                            placesCache[placeId] = place
                        }
                        placeResult
                    } ?: Result.failure(PlaceDetailsNotFoundException(placeId = placeId))
                }
            }
        }
    }
}

class PlaceDetailsNotFoundException(placeId: String) : Exception() {
    override val message = "⛔️ placeId $placeId not found"
}
