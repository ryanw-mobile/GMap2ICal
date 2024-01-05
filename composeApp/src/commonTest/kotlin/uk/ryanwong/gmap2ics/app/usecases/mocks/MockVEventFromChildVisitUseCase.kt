/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.app.usecases.mocks

import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.ChildVisit
import uk.ryanwong.gmap2ics.app.usecases.VEventFromChildVisitUseCase

class MockVEventFromChildVisitUseCase : VEventFromChildVisitUseCase {
    var mockUseCaseResponse: VEvent? = null
    override suspend fun invoke(
        childVisit: ChildVisit,
        enablePlacesApiLookup: Boolean,
    ): VEvent? {
        return mockUseCaseResponse
    }
}
