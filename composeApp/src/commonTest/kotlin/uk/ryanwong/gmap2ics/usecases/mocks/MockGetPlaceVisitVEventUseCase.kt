/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases.mocks

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import uk.ryanwong.gmap2ics.domain.models.UILogEntry
import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.models.timeline.placevisit.PlaceVisit
import uk.ryanwong.gmap2ics.domain.usecases.GetPlaceVisitVEventUseCase

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
        verboseConsoleLog: Boolean,
    ): List<VEvent> {
        return mockUseCaseResponse ?: throw Exception("mock response unavailable")
    }

    suspend fun emitIgnoredEvent(uiLogEntry: UILogEntry) {
        _ignoredEvents.emit(uiLogEntry)
    }

    suspend fun emitExportedEvent(uiLogEntry: UILogEntry) {
        _exportedEvents.emit(uiLogEntry)
    }
}
