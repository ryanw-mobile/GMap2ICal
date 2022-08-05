/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import uk.ryanwong.gmap2ics.app.models.Place
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ChildVisit
import us.dustinj.timezonemap.TimeZoneMap

class ExportChildVisitUseCaseImpl(
    private val placeDetailsRepository: PlaceDetailsRepository,
    private val timeZoneMap: TimeZoneMap = TimeZoneMap.forEverywhere()
) : ExportChildVisitUseCase {

    override suspend operator fun invoke(
        childVisit: ChildVisit,
        ignoredVisitedPlaceIds: List<String>,
        enablePlacesApiLookup: Boolean
    ): VEvent? {
        // If we have child-visits, we export them as individual events
        // ChildVisit might have unconfirmed location which does not have a duration
        if (ignoredVisitedPlaceIds.contains(childVisit.location.placeId)) {
            return null
        }

        val childPlace: Place? =
            if (enablePlacesApiLookup && childVisit.location.placeId != null)
                placeDetailsRepository.getPlaceDetails(
                    placeId = childVisit.location.placeId,
                    placeTimeZoneId = childVisit.getEventTimeZone(timeZoneMap)?.zoneId
                ).getOrNull()
            else null

        return childVisit.asTimelineItem(timeZoneMap = timeZoneMap, place = childPlace)
            ?.let { timelineItem ->
                VEvent.from(timelineItem = timelineItem)
            }
    }
}