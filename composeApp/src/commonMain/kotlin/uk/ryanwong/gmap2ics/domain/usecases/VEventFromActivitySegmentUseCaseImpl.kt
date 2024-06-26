/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.usecases

import io.github.aakira.napier.Napier
import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.domain.models.timeline.activity.ActivitySegment
import uk.ryanwong.gmap2ics.domain.repositories.PlaceDetailsRepository
import uk.ryanwong.gmap2ics.domain.usecases.interfaces.VEventFromActivitySegmentUseCase
import uk.ryanwong.gmap2ics.domain.utils.timezonemap.shouldShowMiles

class VEventFromActivitySegmentUseCaseImpl(
    private val placeDetailsRepository: PlaceDetailsRepository,
) : VEventFromActivitySegmentUseCase {

    override suspend operator fun invoke(
        activitySegment: ActivitySegment,
        enablePlacesApiLookup: Boolean,
    ): VEvent {
        val eventTimeZone = activitySegment.eventTimeZone

        // Note: Known that if roadSegmentPlaceIds has only one placeId,
        // then firstPlaceDetails will be the same as lastPlaceDetails,
        // and it still makes sense
        val firstPlaceDetails = getPlaceDetails(
            placeId = activitySegment.waypointPath?.roadSegmentPlaceIds?.firstOrNull(),
            placeTimeZoneId = eventTimeZone?.zoneId,
            enablePlacesApiLookup = enablePlacesApiLookup,
            napierTag = "firstPlaceDetails",
        )

        val lastPlaceDetails = getPlaceDetails(
            placeId = activitySegment.waypointPath?.roadSegmentPlaceIds?.lastOrNull(),
            placeTimeZoneId = eventTimeZone?.zoneId,
            enablePlacesApiLookup = enablePlacesApiLookup,
            napierTag = "lastPlaceDetails",
        )

        val startPlaceDetails = getPlaceDetails(
            placeId = activitySegment.startLocation.placeId,
            placeTimeZoneId = eventTimeZone?.zoneId,
            enablePlacesApiLookup = enablePlacesApiLookup,
            napierTag = "startPlaceDetails",
        )

        val endPlaceDetails = getPlaceDetails(
            placeId = activitySegment.endLocation.placeId,
            placeTimeZoneId = eventTimeZone?.zoneId,
            enablePlacesApiLookup = enablePlacesApiLookup,
            napierTag = "endPlaceDetails",
        )

        return VEvent.from(
            activitySegment = activitySegment,
            shouldShowMiles = eventTimeZone?.shouldShowMiles() ?: false,
            firstPlaceDetails = firstPlaceDetails,
            lastPlaceDetails = lastPlaceDetails,
            startPlaceDetails = startPlaceDetails,
            endPlaceDetails = endPlaceDetails,
        )
    }

    private suspend fun getPlaceDetails(
        placeId: String?,
        placeTimeZoneId: String?,
        enablePlacesApiLookup: Boolean,
        napierTag: String,
    ): PlaceDetails? {
        if (placeId == null) {
            return null
        }

        val result = placeDetailsRepository.getPlaceDetails(
            placeId = placeId,
            placeTimeZoneId = placeTimeZoneId,
            enablePlacesApiLookup = enablePlacesApiLookup,
        )

        result.exceptionOrNull()?.let {
            Napier.e(tag = napierTag, message = it.localizedMessage)
        }

        return result.getOrNull()
    }
}
