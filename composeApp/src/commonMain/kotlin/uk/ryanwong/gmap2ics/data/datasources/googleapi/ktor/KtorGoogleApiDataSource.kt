/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.googleapi.ktor

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.ryanwong.gmap2ics.data.datasources.googleapi.GetPlaceDetailsAPIErrorException
import uk.ryanwong.gmap2ics.data.datasources.googleapi.GoogleApiDataSource
import uk.ryanwong.gmap2ics.data.except
import uk.ryanwong.gmap2ics.data.repositories.PlaceDetailsNotFoundException
import uk.ryanwong.gmap2ics.domain.models.timeline.PlaceDetails
import kotlin.coroutines.cancellation.CancellationException

class KtorGoogleApiDataSource(
    private val googleMapsApiClient: GoogleMapsApiClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : GoogleApiDataSource {
    override suspend fun getMapsApiPlaceDetails(
        placeId: String,
        apiKey: String,
        language: String?,
    ): Result<PlaceDetails> {
        return withContext(dispatcher) {
            Result.runCatching {
                val result = try {
                    val response =
                        googleMapsApiClient.getPlaceDetails(placeId = placeId, apiKey = apiKey, language = language)

                    response?.result?.let { result ->
                        PlaceDetails.from(placeDetailsResult = result)
                    }
                } catch (cancellationException: CancellationException) {
                    throw cancellationException
                } catch (ex: Exception) {
                    Napier.e(message = "getPlaceDetails", throwable = ex)
                    throw GetPlaceDetailsAPIErrorException(apiErrorMessage = ex.localizedMessage)
                }

                result ?: throw PlaceDetailsNotFoundException(placeId = placeId)
            }.except<CancellationException, _>()
        }
    }
}
