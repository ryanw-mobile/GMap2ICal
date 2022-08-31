/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.usecases.mocks

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import uk.ryanwong.gmap2ics.app.models.UILogEntry
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.PlaceVisit
import uk.ryanwong.gmap2ics.app.usecases.GetPlaceVisitVEventUseCase

class MockGetPlaceVisitVEventUseCase : GetPlaceVisitVEventUseCase {

    private val _ignoredEvents = MutableSharedFlow<UILogEntry>()
    override val ignoredEvents: SharedFlow<UILogEntry> = _ignoredEvents

    private val _exportedEvents = MutableSharedFlow<UILogEntry>()
    override val exportedEvents: SharedFlow<UILogEntry> = _exportedEvents

    var mockUseCaseResponse: List<VEvent>? = null
    override suspend fun invoke(
        placeVisit: PlaceVisit,
        ignoredVisitedPlaceIds: List<String>,
        enablePlacesApiLookup: Boolean,
        verboseConsoleLog: Boolean
    ): List<VEvent> {
        return mockUseCaseResponse ?: throw Exception("mock response unavailable")
    }
}
