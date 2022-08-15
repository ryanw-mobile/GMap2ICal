/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import io.github.aakira.napier.Napier
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.PlaceVisit
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepository

class VEventFromPlaceVisitUseCaseImpl(
    private val placeDetailsRepository: PlaceDetailsRepository
) : VEventFromPlaceVisitUseCase {

    override suspend operator fun invoke(
        placeVisit: PlaceVisit,
        enablePlacesApiLookup: Boolean
    ): VEvent {
        val placeDetails = placeVisit.location.placeId?.let { placeId ->
            placeDetailsRepository.getPlaceDetails(
                placeId = placeId,
                placeTimeZoneId = placeVisit.eventTimeZone?.zoneId,
                enablePlacesApiLookup = enablePlacesApiLookup
            ).let { result ->
                result.exceptionOrNull()?.let {
                    Napier.e(tag = "VEventFromPlaceVisitUseCase", message = it.localizedMessage)
                }
                result.getOrNull()
            }
        }

        return VEvent.from(placeVisit = placeVisit, placeDetails = placeDetails)
    }
}