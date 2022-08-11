/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import uk.ryanwong.gmap2ics.app.models.Place
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ChildVisit
import uk.ryanwong.gmap2ics.utils.timezonemap.TimeZoneMapWrapper

class ExportChildVisitUseCaseImpl(
    private val placeDetailsRepository: PlaceDetailsRepository,
    private val timeZoneMap: TimeZoneMapWrapper
) : ExportChildVisitUseCase {

    override suspend operator fun invoke(
        childVisit: ChildVisit,
        enablePlacesApiLookup: Boolean
    ): VEvent? {
        // If we have child-visits, we export them as individual events
        // ChildVisit might have unconfirmed location which does not have a duration
        val childPlace: Place? =
            if (enablePlacesApiLookup && childVisit.location.placeId != null) {
                placeDetailsRepository.getPlaceDetails(
                    placeId = childVisit.location.placeId,
                    placeTimeZoneId = childVisit.getEventTimeZone(timeZoneMap)?.zoneId
                ).getOrNull()
            } else null

        return childVisit.asTimelineItem(timeZoneMap = timeZoneMap, place = childPlace)
            ?.let { timelineItem ->
                VEvent.from(timelineItem = timelineItem)
            }
    }
}