/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import uk.ryanwong.gmap2ics.app.models.Place
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.PlaceVisit
import uk.ryanwong.gmap2ics.utils.timezonemap.TimeZoneMapWrapper

class ExportPlaceVisitUseCaseImpl(
    private val placeDetailsRepository: PlaceDetailsRepository,
    private val timeZoneMap: TimeZoneMapWrapper
) : ExportPlaceVisitUseCase {
    override suspend operator fun invoke(
        placeVisit: PlaceVisit,
        enablePlacesApiLookup: Boolean,
        ignoredVisitedPlaceIds: List<String>
    ): VEvent? {
        if (ignoredVisitedPlaceIds.contains(placeVisit.location.placeId)) {
            return null
        }

        val place: Place? =
            if (enablePlacesApiLookup && placeVisit.location.placeId != null) {
                placeDetailsRepository.getPlaceDetails(
                    placeId = placeVisit.location.placeId,
                    placeTimeZoneId = placeVisit.getEventTimeZone(timeZoneMap)?.zoneId
                ).getOrNull()
            } else null

        val timelineItem = placeVisit.asTimelineItem(timeZoneMap = timeZoneMap, place = place)

        return VEvent.from(timelineItem = timelineItem)
    }
}