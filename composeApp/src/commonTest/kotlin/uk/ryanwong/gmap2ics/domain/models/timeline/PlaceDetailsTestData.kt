/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline

import uk.ryanwong.gmap2ics.data.models.places.GeometryDto
import uk.ryanwong.gmap2ics.data.models.places.LocationDto
import uk.ryanwong.gmap2ics.data.models.places.ResultDto

internal object PlaceDetailsTestData {
    /***
     * Note: Seeing real data would help understand what kind of data we are getting from Google.
     */

    val placeDetailsResultDto = ResultDto(
        formattedAddress = "Wakefield WF4 4LG, UK",
        formattedPhoneNumber = "01924 832631",
        geometryDto = GeometryDto(
            locationDto = LocationDto(
                lat = 53.6152405,
                lng = -1.5639315,
            ),
        ),
        icon = "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/generic_business-71.png",
        name = "Yorkshire Sculpture Park",
        placeId = "ChIJOX4_ortjeUgR2_LUcFpvLg4",
        reference = "ChIJOX4_ortjeUgR2_LUcFpvLg4",
        types = listOf("art_gallery", "tourist_attraction", "park", "museum", "point_of_interest", "establishment"),
        url = "https://maps.google.com/?cid=1021876599690425051",
        userRatingsTotal = 8319,
        utcOffset = 60,
        vicinity = "Wakefield",
        website = "http://www.ysp.org.uk/",
    )
}
