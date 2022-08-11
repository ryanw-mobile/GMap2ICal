/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.ChildVisit
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepository
import uk.ryanwong.gmap2ics.utils.timezonemap.TimeZoneMapWrapper

class VEventFromChildVisitUseCaseImpl(
    private val placeDetailsRepository: PlaceDetailsRepository,
    private val timeZoneMap: TimeZoneMapWrapper
) : VEventFromChildVisitUseCase {

    override suspend operator fun invoke(
        childVisit: ChildVisit,
        enablePlacesApiLookup: Boolean
    ): VEvent? {
        // If we have child-visits, we export them as individual events
        // ChildVisit might have unconfirmed location which does not have a duration, so return value can be null
        val childPlaceDetails: PlaceDetails? =
            if (enablePlacesApiLookup && childVisit.location.placeId != null) {
                placeDetailsRepository.getPlaceDetails(
                    placeId = childVisit.location.placeId,
                    placeTimeZoneId = childVisit.eventTimeZone?.zoneId
                ).getOrNull()
            } else null

        return childVisit.asTimelineItem(timeZoneMap = timeZoneMap, placeDetails = childPlaceDetails)
            ?.let { timelineItem ->
                VEvent.from(timelineItem = timelineItem)
            }
    }
}