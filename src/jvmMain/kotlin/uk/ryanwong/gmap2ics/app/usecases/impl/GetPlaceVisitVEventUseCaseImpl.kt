/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.usecases.impl

import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import uk.ryanwong.gmap2ics.app.models.UILogEntry
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.PlaceVisit
import uk.ryanwong.gmap2ics.app.usecases.GetPlaceVisitVEventUseCase
import uk.ryanwong.gmap2ics.app.usecases.VEventFromChildVisitUseCase
import uk.ryanwong.gmap2ics.app.usecases.VEventFromPlaceVisitUseCase

class GetPlaceVisitVEventUseCaseImpl(
    private val vEventFromChildVisitUseCase: VEventFromChildVisitUseCase,
    private val vEventFromPlaceVisitUseCase: VEventFromPlaceVisitUseCase,
) : GetPlaceVisitVEventUseCase {

    private val _ignoredEvents = MutableSharedFlow<UILogEntry>()
    override val ignoredEvents: SharedFlow<UILogEntry> = _ignoredEvents

    private val _exportedEvents = MutableSharedFlow<UILogEntry>()
    override val exportedEvents: SharedFlow<UILogEntry> = _exportedEvents

    override suspend operator fun invoke(
        placeVisit: PlaceVisit,
        ignoredVisitedPlaceIds: List<String>,
        enablePlacesApiLookup: Boolean,
        verboseConsoleLog: Boolean,
    ): List<VEvent> {
        val eventList: MutableList<VEvent> = mutableListOf()

        // If parent visit is to be ignored, child has no meaning to stay
        if (ignoredVisitedPlaceIds.contains(placeVisit.location.placeId)) {
            _ignoredEvents.emit(
                UILogEntry(
                    emoji = "🚫",
                    message = "${placeVisit.durationStartTimestamp.toUITimestamp()}: Place ID ${placeVisit.location.placeId}"
                )
            )
        } else {
            vEventFromPlaceVisitUseCase(
                placeVisit = placeVisit,
                enablePlacesApiLookup = enablePlacesApiLookup
            ).let { vEvent ->
                eventList.add(vEvent)
                _exportedEvents.emit(
                    UILogEntry(
                        emoji = "\uD83D\uDDD3",
                        message = "${vEvent.dtStart.toUITimestamp()}: ${vEvent.summary}"
                    )
                )
                printVerboseConsoleLog(verboseLogs = verboseConsoleLog, message = vEvent.toString())
            }

            // If we have child-visits, we export them as individual events
            // ChildVisit might have unconfirmed location which does not have a duration
            placeVisit.childVisits.forEach { childVisit ->
                if (ignoredVisitedPlaceIds.contains(childVisit.location.placeId)) {
                    _ignoredEvents.emit(
                        UILogEntry(
                            emoji = "🚫",
                            message = "${childVisit.durationStartTimestamp.toUITimestamp()}: Place ID ${childVisit.location.placeId}"
                        )
                    )
                } else {
                    vEventFromChildVisitUseCase(
                        childVisit = childVisit,
                        enablePlacesApiLookup = enablePlacesApiLookup
                    )?.let { vEvent ->
                        eventList.add(vEvent)
                        _exportedEvents.emit(
                            UILogEntry(
                                emoji = "\uD83D\uDDD3",
                                message = "${vEvent.dtStart.toUITimestamp()}: ${vEvent.summary}"
                            )
                        )
                        printVerboseConsoleLog(verboseLogs = verboseConsoleLog, message = vEvent.toString())
                    }
                }
            }
        }
        return eventList
    }

    private fun printVerboseConsoleLog(verboseLogs: Boolean, message: String) {
        if (verboseLogs) {
            Napier.v(tag = "GetPlaceVisitVEventUseCaseImpl", message = message)
        }
    }
}
