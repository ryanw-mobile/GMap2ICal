/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.usecases

import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.ChildVisit

interface VEventFromChildVisitUseCase {
    suspend operator fun invoke(
        childVisit: ChildVisit,
        enablePlacesApiLookup: Boolean
    ): VEvent?
}
