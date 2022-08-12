/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.ChildVisit
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepository

class VEventFromChildVisitUseCaseImpl(
    private val placeDetailsRepository: PlaceDetailsRepository
) : VEventFromChildVisitUseCase {

    override suspend operator fun invoke(
        childVisit: ChildVisit,
        enablePlacesApiLookup: Boolean
    ): VEvent {
        // If we have child-visits, we export them as individual events
        // ChildVisit might have unconfirmed location which does not have a duration, so return value can be null
        val childPlaceDetails: PlaceDetails? =
            if (enablePlacesApiLookup && childVisit.location.placeId != null) {
                placeDetailsRepository.getPlaceDetails(
                    placeId = childVisit.location.placeId,
                    placeTimeZoneId = childVisit.eventTimeZone?.zoneId
                ).getOrNull()
            } else null

        return VEvent.from(childVisit = childVisit, placeDetails = childPlaceDetails)
    }
}