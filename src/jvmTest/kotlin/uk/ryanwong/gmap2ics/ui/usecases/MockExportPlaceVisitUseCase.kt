/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.PlaceVisit

class MockExportPlaceVisitUseCase : ExportPlaceVisitUseCase {
    override suspend fun invoke(
        placeVisit: PlaceVisit,
        enablePlacesApiLookup: Boolean,
        ignoredVisitedPlaceIds: List<String>
    ): VEvent? {
        TODO("Not yet implemented")
    }
}