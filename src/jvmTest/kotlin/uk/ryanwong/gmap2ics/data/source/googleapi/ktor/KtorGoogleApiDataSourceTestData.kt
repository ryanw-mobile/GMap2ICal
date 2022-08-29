/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.ktor

import uk.ryanwong.gmap2ics.data.models.places.Geometry
import uk.ryanwong.gmap2ics.data.models.places.Location
import uk.ryanwong.gmap2ics.data.models.places.Result

object KtorGoogleApiDataSourceTestData {
    /***
     * Data source unit tests I would prefer using real data samples.
     */
    val mockPlaceDetailsDataModel = uk.ryanwong.gmap2ics.data.models.places.PlaceDetails(
        result = Result(
            formattedAddress = "8 Greg Ave, Bollington, Macclesfield SK10 5HR, UK",
            formattedPhoneNumber = null,
            geometry = Geometry(location = Location(lat = 53.2945761, lng = -2.114387)),
            icon = "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/geocode-71.png",
            name = "8 Greg Ave",
            placeId = "ChIJ43NG7NxLekgR7NFDJzb-WXw",
            reference = "ChIJ43NG7NxLekgR7NFDJzb-WXw",
            types = listOf("premise"),
            url = "https://maps.google.com/?q=8+Greg+Ave,+Bollington,+Macclesfield+SK10+5HR,+UK&ftid=0x487a4bdcec4673e3:0x7c59fe362743d1ec",
            userRatingsTotal = null,
            utcOffset = 60,
            vicinity = "Bollington",
            website = null
        )
    )
}
