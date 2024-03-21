/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases.mocks

import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.models.timeline.placevisit.PlaceVisit
import uk.ryanwong.gmap2ics.domain.usecases.VEventFromPlaceVisitUseCase

class MockVEventFromPlaceVisitUseCase : VEventFromPlaceVisitUseCase {
    var mockUseCaseResponse: VEvent? = null
    override suspend fun invoke(
        placeVisit: PlaceVisit,
        enablePlacesApiLookup: Boolean,
    ): VEvent {
        return mockUseCaseResponse ?: throw Exception("mock response unavailable")
    }
}
