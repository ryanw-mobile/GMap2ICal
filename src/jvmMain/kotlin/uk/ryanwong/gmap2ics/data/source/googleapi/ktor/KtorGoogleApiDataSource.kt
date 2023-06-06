/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.ktor

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.data.except
import uk.ryanwong.gmap2ics.data.repository.impl.PlaceDetailsNotFoundException
import uk.ryanwong.gmap2ics.data.source.googleapi.GetPlaceDetailsAPIErrorException
import uk.ryanwong.gmap2ics.data.source.googleapi.GoogleApiDataSource
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
