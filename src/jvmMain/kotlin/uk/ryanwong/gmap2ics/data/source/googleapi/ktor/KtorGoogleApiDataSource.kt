/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.ktor

import uk.ryanwong.gmap2ics.app.models.Place
import uk.ryanwong.gmap2ics.data.source.googleapi.GoogleApiDataSource

class KtorGoogleApiDataSource() : GoogleApiDataSource {
    override suspend fun getPlaceDetails(placeId: String, apiKey: String, language: String?): Result<Place> {
        TODO("Not yet implemented")
    }
}