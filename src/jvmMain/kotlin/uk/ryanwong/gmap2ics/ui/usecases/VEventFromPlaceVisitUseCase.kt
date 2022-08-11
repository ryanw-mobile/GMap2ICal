/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceVisit

interface VEventFromPlaceVisitUseCase {
    suspend operator fun invoke(
        placeVisit: PlaceVisit,
        enablePlacesApiLookup: Boolean
    ): VEvent
}