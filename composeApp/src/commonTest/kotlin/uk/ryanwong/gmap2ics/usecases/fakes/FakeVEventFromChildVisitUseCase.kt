/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases.fakes

import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.models.timeline.placevisit.ChildVisit
import uk.ryanwong.gmap2ics.domain.usecases.VEventFromChildVisitUseCase

class FakeVEventFromChildVisitUseCase : VEventFromChildVisitUseCase {
    var useCaseResponse: VEvent? = null
    override suspend fun invoke(
        childVisit: ChildVisit,
        enablePlacesApiLookup: Boolean,
    ): VEvent? {
        return useCaseResponse
    }
}
