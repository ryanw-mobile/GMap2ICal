/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.usecases.impl

import io.github.aakira.napier.Napier
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.activity.ActivitySegment
import uk.ryanwong.gmap2ics.app.usecases.VEventFromActivitySegmentUseCase
import uk.ryanwong.gmap2ics.app.utils.timezonemap.shouldShowMiles
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepository

class VEventFromActivitySegmentUseCaseImpl(
    private val placeDetailsRepository: PlaceDetailsRepository
) : VEventFromActivitySegmentUseCase {

    override suspend operator fun invoke(
        activitySegment: ActivitySegment,
        enablePlacesApiLookup: Boolean
    ): VEvent {

        // Extra information required by timelineItem
        val eventTimeZone = activitySegment.eventTimeZone
        val firstPlaceDetails = activitySegment.waypointPath?.roadSegmentPlaceIds?.firstOrNull()?.let { placeId ->
            placeDetailsRepository.getPlaceDetails(
                placeId = placeId,
                placeTimeZoneId = eventTimeZone?.zoneId,
                enablePlacesApiLookup = enablePlacesApiLookup
            ).let { result ->
                result.exceptionOrNull()?.let {
                    Napier.e(tag = "firstPlaceDetails", message = it.localizedMessage)
                }
                result.getOrNull()
            }
        }

        val lastPlaceDetails = activitySegment.waypointPath?.roadSegmentPlaceIds?.lastOrNull()?.let { placeId ->
            placeDetailsRepository.getPlaceDetails(
                placeId = placeId,
                placeTimeZoneId = eventTimeZone?.zoneId,
                enablePlacesApiLookup = enablePlacesApiLookup
            ).let { result ->
                result.exceptionOrNull()?.let {
                    Napier.e(tag = "lastPlaceDetails", message = it.localizedMessage)
                }
                result.getOrNull()
            }
        }

        val startPlaceDetails = activitySegment.startLocation.placeId?.let { placeId ->
            placeDetailsRepository.getPlaceDetails(
                placeId = placeId,
                placeTimeZoneId = eventTimeZone?.zoneId,
                enablePlacesApiLookup = enablePlacesApiLookup
            ).let { result ->
                result.exceptionOrNull()?.let {
                    Napier.e(tag = "startPlaceDetails", message = it.localizedMessage)
                }
                result.getOrNull()
            }
        }
        val endPlaceDetails = activitySegment.endLocation.placeId?.let { placeId ->
            placeDetailsRepository.getPlaceDetails(
                placeId = placeId,
                placeTimeZoneId = eventTimeZone?.zoneId,
                enablePlacesApiLookup = enablePlacesApiLookup
            ).let { result ->
                result.exceptionOrNull()?.let {
                    Napier.e(tag = "endPlaceDetails", message = it.localizedMessage)
                }
                result.getOrNull()
            }
        }

        return VEvent.from(
            activitySegment = activitySegment,
            shouldShowMiles = eventTimeZone?.shouldShowMiles() ?: false,
            firstPlaceDetails = firstPlaceDetails,
            lastPlaceDetails = lastPlaceDetails,
            startPlaceDetails = startPlaceDetails,
            endPlaceDetails = endPlaceDetails
        )
    }
}