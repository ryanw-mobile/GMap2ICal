/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.ryanwong.gmap2ics.data.datasources.googleapi.interfaces.GoogleApiDataSource
import uk.ryanwong.gmap2ics.data.except
import uk.ryanwong.gmap2ics.data.repositories.mapper.toPlaceDetails
import uk.ryanwong.gmap2ics.domain.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.domain.repositories.PlaceDetailsRepository
import kotlin.coroutines.cancellation.CancellationException

class PlaceDetailsRepositoryImpl(
    private val networkDataSource: GoogleApiDataSource,
    private val placesApiKey: String?,
    private val apiLanguageOverride: Map<String, String>, // TODO: Move to function level when configurable through UI
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : PlaceDetailsRepository {

    // TODO: Can put this in local database so we lookup once and for all.
    private val placesCache = mutableMapOf<String, PlaceDetails>()

    override suspend fun getPlaceDetails(
        placeId: String,
        placeTimeZoneId: String?,
        enablePlacesApiLookup: Boolean,
    ): Result<PlaceDetails> = when {
        placesCache.contains(key = placeId) -> {
            Result.success(placesCache.getValue(placeId))
        }

        !enablePlacesApiLookup -> {
            Result.failure(PlaceDetailsNotFoundException(placeId = placeId))
        }

        placesApiKey == null -> {
            Result.failure(PlaceDetailsNotFoundException(placeId = placeId))
        }

        else -> {
            withContext(dispatcher) {
                // Do API lookup and cache the results
                // If user does not supply an API Key means we always return null
                runCatching {
                    val language: String? = apiLanguageOverride.getOrDefault(
                        key = placeTimeZoneId,
                        defaultValue = apiLanguageOverride.get(key = "default"),
                    )

                    val placeDetailsDto = networkDataSource.getMapsApiPlaceDetails(
                        placeId = placeId,
                        apiKey = placesApiKey,
                        language = language,
                    )

                    val placeDetails = placeDetailsDto.result?.toPlaceDetails()

                    placeDetails?.let {
                        placesCache[placeId] = it
                        it
                    } ?: throw PlaceDetailsNotFoundException(placeId = placeId)
                }.except<CancellationException, _>()
            }
        }
    }
}

class PlaceDetailsNotFoundException(placeId: String) : Exception() {
    override val message = "⛔️ placeId $placeId not found"
}
