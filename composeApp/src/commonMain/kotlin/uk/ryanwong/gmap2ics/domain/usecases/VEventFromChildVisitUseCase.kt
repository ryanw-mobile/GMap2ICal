/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.usecases

import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.models.timeline.placevisit.ChildVisit

interface VEventFromChildVisitUseCase {
    suspend operator fun invoke(
        childVisit: ChildVisit,
        enablePlacesApiLookup: Boolean,
    ): VEvent?
}
