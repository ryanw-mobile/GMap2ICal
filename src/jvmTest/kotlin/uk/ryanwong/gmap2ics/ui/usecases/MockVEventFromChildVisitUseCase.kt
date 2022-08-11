/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ChildVisit

class MockVEventFromChildVisitUseCase : VEventFromChildVisitUseCase {
    var mockUseCaseResponse: VEvent? = null
    override suspend fun invoke(
        childVisit: ChildVisit,
        enablePlacesApiLookup: Boolean
    ): VEvent? {
        return mockUseCaseResponse
    }
}