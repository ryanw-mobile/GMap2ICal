/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.local.fakes

import uk.ryanwong.gmap2ics.data.datasources.googleapi.interfaces.GoogleApiDataSource
import uk.ryanwong.gmap2ics.data.models.places.PlaceDetailsDto

class FakeGoogleApiDataSource : GoogleApiDataSource {
    var fakeException: Throwable? = null
    var getMapsApiPlaceDetailsResponse: PlaceDetailsDto? = null
    var getMapsApiPlaceDetailsLanguageRequested: String? = null

    override suspend fun getMapsApiPlaceDetails(
        placeId: String,
        apiKey: String,
        language: String?,
    ): PlaceDetailsDto {
        getMapsApiPlaceDetailsLanguageRequested = language
        return fakeException?.let { throw it } ?: getMapsApiPlaceDetailsResponse ?: throw Exception("response not defined")
    }
}
