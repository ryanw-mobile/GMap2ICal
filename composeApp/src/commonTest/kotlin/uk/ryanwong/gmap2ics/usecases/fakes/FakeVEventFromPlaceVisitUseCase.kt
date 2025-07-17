/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases.fakes

import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.models.timeline.placevisit.PlaceVisit
import uk.ryanwong.gmap2ics.domain.usecases.interfaces.VEventFromPlaceVisitUseCase

class FakeVEventFromPlaceVisitUseCase : VEventFromPlaceVisitUseCase {
    var useCaseResponse: VEvent? = null
    override suspend fun invoke(
        placeVisit: PlaceVisit,
        enablePlacesApiLookup: Boolean,
    ): VEvent = useCaseResponse ?: throw Exception("response not defined")
}
