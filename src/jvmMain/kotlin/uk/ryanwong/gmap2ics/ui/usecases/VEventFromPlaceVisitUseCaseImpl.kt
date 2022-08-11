/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import uk.ryanwong.gmap2ics.app.models.PlaceDetails
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.PlaceVisit
import uk.ryanwong.gmap2ics.utils.timezonemap.TimeZoneMapWrapper

class VEventFromPlaceVisitUseCaseImpl(
    private val placeDetailsRepository: PlaceDetailsRepository,
    private val timeZoneMap: TimeZoneMapWrapper
) : VEventFromPlaceVisitUseCase {
    override suspend operator fun invoke(
        placeVisit: PlaceVisit,
        enablePlacesApiLookup: Boolean
    ): VEvent {
        val placeDetails: PlaceDetails? =
            if (enablePlacesApiLookup && placeVisit.location.placeId != null) {
                placeDetailsRepository.getPlaceDetails(
                    placeId = placeVisit.location.placeId,
                    placeTimeZoneId = placeVisit.getEventTimeZone(timeZoneMap)?.zoneId
                ).getOrNull()
            } else null

        val timelineItem = placeVisit.asTimelineItem(timeZoneMap = timeZoneMap, placeDetails = placeDetails)
        return VEvent.from(timelineItem = timelineItem)
    }
}