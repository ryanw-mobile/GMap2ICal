/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.usecases

import kotlinx.coroutines.flow.SharedFlow
import uk.ryanwong.gmap2ics.domain.models.UILogEntry
import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.models.timeline.placevisit.PlaceVisit

interface GetPlaceVisitVEventUseCase {

    val ignoredEvents: SharedFlow<UILogEntry>
    val exportedEvents: SharedFlow<UILogEntry>

    suspend operator fun invoke(
        placeVisit: PlaceVisit,
        ignoredVisitedPlaceIds: List<String>,
        enablePlacesApiLookup: Boolean,
        verboseConsoleLog: Boolean,
    ): List<VEvent>
}
