/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.ktor

import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.data.source.googleapi.GoogleApiDataSource

class KtorGoogleApiDataSource() : GoogleApiDataSource {
    override suspend fun getMapsApiPlaceDetails(placeId: String, apiKey: String, language: String?): Result<PlaceDetails> {
        TODO("Not yet implemented")
    }
}