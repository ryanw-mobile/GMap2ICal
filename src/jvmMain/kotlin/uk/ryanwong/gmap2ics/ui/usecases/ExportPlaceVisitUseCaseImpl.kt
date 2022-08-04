/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import uk.ryanwong.gmap2ics.app.models.PlaceDetails
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.PlaceVisit
import us.dustinj.timezonemap.TimeZoneMap

class ExportPlaceVisitUseCaseImpl(
    private val placeDetailsRepository: PlaceDetailsRepository,
    private val timeZoneMap: TimeZoneMap = TimeZoneMap.forEverywhere()
) : ExportPlaceVisitUseCase {
    override suspend operator fun invoke(
        placeVisit: PlaceVisit,
        enablePlacesApiLookup: Boolean,
        ignoredVisitedPlaceIds: List<String>
    ): VEvent? {
        val placeDetails: PlaceDetails? =
            if (enablePlacesApiLookup && placeVisit.location.placeId != null) placeDetailsRepository.getPlaceDetails(
                placeId = placeVisit.location.placeId,
                placeTimeZoneId = placeVisit.getEventTimeZone(timeZoneMap)?.zoneId
            ).getOrNull()
            else null

        val gMapTimelineObject = placeVisit.asTimelineItem(timeZoneMap = timeZoneMap, placeDetails = placeDetails)

        return if (!ignoredVisitedPlaceIds.contains(gMapTimelineObject.placeId)) {
            VEvent.from(timelineItem = gMapTimelineObject)
        } else null
    }
}