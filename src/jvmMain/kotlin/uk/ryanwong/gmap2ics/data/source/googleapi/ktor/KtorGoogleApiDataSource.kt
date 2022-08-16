/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.ktor

import io.github.aakira.napier.Napier
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.data.except
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsNotFoundException
import uk.ryanwong.gmap2ics.data.source.googleapi.GetPlaceDetailsAPIErrorException
import uk.ryanwong.gmap2ics.data.source.googleapi.GoogleApiDataSource
import kotlin.coroutines.cancellation.CancellationException

class KtorGoogleApiDataSource(
    private val googleMapsApiClient: GoogleMapsApiClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GoogleApiDataSource {
    override suspend fun getMapsApiPlaceDetails(
        placeId: String,
        apiKey: String,
        language: String?
    ): Result<PlaceDetails> {
        return withContext(dispatcher) {
            Result.runCatching {
                try {
                    val response =
                        googleMapsApiClient.getPlaceDetails(placeId = placeId, apiKey = apiKey, language = language)

                    response?.result?.let { result ->
                        PlaceDetails.from(placeDetailsResult = result)
                    } ?: throw PlaceDetailsNotFoundException(placeId = placeId)

                } catch (redirectResponseException: RedirectResponseException) {
                    Napier.e(message = "getPlaceDetails", throwable = redirectResponseException)
                    throw GetPlaceDetailsAPIErrorException(apiErrorMessage = redirectResponseException.localizedMessage)

                } catch (clientRequestException: ClientRequestException) {
                    Napier.e(message = "getPlaceDetails", throwable = clientRequestException)
                    throw GetPlaceDetailsAPIErrorException(apiErrorMessage = clientRequestException.localizedMessage)

                } catch (serverResponseException: ServerResponseException) {
                    Napier.e(message = "getPlaceDetails", throwable = serverResponseException)
                    throw GetPlaceDetailsAPIErrorException(apiErrorMessage = serverResponseException.localizedMessage)

                } catch (cancellationException: CancellationException) {
                    throw cancellationException

                } catch (ex: Exception) {
                    Napier.e(message = "getPlaceDetails", throwable = ex)
                    throw GetPlaceDetailsAPIErrorException(apiErrorMessage = ex.localizedMessage)
                }

            }.except<CancellationException, _>()
        }
    }
}