/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.usecases.mocks

import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.PlaceVisit
import uk.ryanwong.gmap2ics.app.usecases.VEventFromPlaceVisitUseCase

class MockVEventFromPlaceVisitUseCase : VEventFromPlaceVisitUseCase {
    var mockUseCaseResponse: VEvent? = null
    override suspend fun invoke(
        placeVisit: PlaceVisit,
        enablePlacesApiLookup: Boolean
    ): VEvent {
        return mockUseCaseResponse ?: throw Exception("mock response unavailable")
    }
}