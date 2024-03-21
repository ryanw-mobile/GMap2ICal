/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases.mocks

import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.models.timeline.placevisit.ChildVisit
import uk.ryanwong.gmap2ics.domain.usecases.VEventFromChildVisitUseCase

class MockVEventFromChildVisitUseCase : VEventFromChildVisitUseCase {
    var mockUseCaseResponse: VEvent? = null
    override suspend fun invoke(
        childVisit: ChildVisit,
        enablePlacesApiLookup: Boolean,
    ): VEvent? {
        return mockUseCaseResponse
    }
}
