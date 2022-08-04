/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ChildVisit

class MockExportChildVisitUseCase : ExportChildVisitUseCase {
    override suspend fun invoke(
        childVisit: ChildVisit,
        ignoredVisitedPlaceIds: List<String>,
        enablePlacesApiLookup: Boolean
    ): VEvent? {
        TODO("Not yet implemented")
    }
}