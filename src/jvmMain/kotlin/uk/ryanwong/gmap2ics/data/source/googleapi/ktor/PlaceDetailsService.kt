/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.ktor

import uk.ryanwong.gmap2ics.data.source.googleapi.models.places.PlaceDetails

interface PlaceDetailsService {
    suspend fun getPlaceDetails(
        placeId: String,
        key: String,
        language: String? = null
    ): PlaceDetails?
}