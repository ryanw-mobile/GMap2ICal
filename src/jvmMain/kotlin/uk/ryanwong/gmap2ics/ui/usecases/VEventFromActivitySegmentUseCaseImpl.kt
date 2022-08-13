/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.activity.ActivitySegment
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepository
import uk.ryanwong.gmap2ics.utils.timezonemap.shouldShowMiles

class VEventFromActivitySegmentUseCaseImpl(
    private val placeDetailsRepository: PlaceDetailsRepository
) : VEventFromActivitySegmentUseCase {

    override suspend operator fun invoke(
        activitySegment: ActivitySegment,
        enablePlacesApiLookup: Boolean
    ): VEvent {

        // Extra information required by timelineItem
        val eventTimeZone = activitySegment.eventTimeZone
        val firstPlaceDetails = activitySegment.waypointPath?.roadSegment?.first()?.placeId?.let { placeId ->
            placeDetailsRepository.getPlaceDetails(
                placeId = placeId,
                placeTimeZoneId = eventTimeZone?.zoneId,
                enablePlacesApiLookup = enablePlacesApiLookup
            ).getOrNull()
        }

        val lastPlaceDetails = activitySegment.waypointPath?.roadSegment?.last()?.placeId?.let { placeId ->
            placeDetailsRepository.getPlaceDetails(
                placeId = placeId,
                placeTimeZoneId = eventTimeZone?.zoneId,
                enablePlacesApiLookup = enablePlacesApiLookup
            ).getOrNull()
        }

        val startPlaceDetails = activitySegment.startLocation.placeId?.let { placeId ->
            placeDetailsRepository.getPlaceDetails(
                placeId = placeId,
                placeTimeZoneId = eventTimeZone?.zoneId,
                enablePlacesApiLookup = enablePlacesApiLookup
            ).getOrNull()
        }
        val endPlaceDetails = activitySegment.endLocation.placeId?.let { placeId ->
            placeDetailsRepository.getPlaceDetails(
                placeId = placeId,
                placeTimeZoneId = eventTimeZone?.zoneId,
                enablePlacesApiLookup = enablePlacesApiLookup
            ).getOrNull()
        }

        return VEvent.from(
            activitySegment = activitySegment,
            shouldShowMiles = eventTimeZone?.shouldShowMiles() ?: false,
            firstPlaceDetails = firstPlaceDetails,
            lastPlaceDetails = lastPlaceDetails,
            startPlaceDetails = startPlaceDetails,
            endPlaceDetails = endPlaceDetails,
            eventTimeZone = eventTimeZone
        )
    }
}