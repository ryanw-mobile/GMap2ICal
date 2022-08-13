/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.ktor

import io.ktor.client.HttpClient
import uk.ryanwong.gmap2ics.data.models.places.PlaceDetails
import kotlin.coroutines.cancellation.CancellationException

class PlaceDetailsServiceImpl(private val client: HttpClient): PlaceDetailsService {
    override suspend fun getPlaceDetails(placeId: String, key: String, language: String?): PlaceDetails? {
        return try {
           // client.get { url(HttpRoutes.PLACE_DETAILS) }.
            null
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
}