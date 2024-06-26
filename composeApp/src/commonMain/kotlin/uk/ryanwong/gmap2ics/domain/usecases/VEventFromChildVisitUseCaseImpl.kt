/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.usecases

import io.github.aakira.napier.Napier
import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.models.timeline.placevisit.ChildVisit
import uk.ryanwong.gmap2ics.domain.repositories.PlaceDetailsRepository
import uk.ryanwong.gmap2ics.domain.usecases.interfaces.VEventFromChildVisitUseCase

class VEventFromChildVisitUseCaseImpl(
    private val placeDetailsRepository: PlaceDetailsRepository,
) : VEventFromChildVisitUseCase {

    override suspend operator fun invoke(
        childVisit: ChildVisit,
        enablePlacesApiLookup: Boolean,
    ): VEvent {
        // If we have child visits, we export them as individual events
        val childPlaceDetails = childVisit.location.placeId?.let { placeId ->
            placeDetailsRepository.getPlaceDetails(
                placeId = placeId,
                placeTimeZoneId = childVisit.eventTimeZone?.zoneId,
                enablePlacesApiLookup = enablePlacesApiLookup,
            ).let { result ->
                result.exceptionOrNull()?.let {
                    Napier.e(tag = "childPlaceDetails", message = it.localizedMessage)
                }
                result.getOrNull()
            }
        }

        return VEvent.from(childVisit = childVisit, placeDetails = childPlaceDetails)
    }
}
