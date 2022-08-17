/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.ktor

import uk.ryanwong.gmap2ics.data.models.places.AddressComponent
import uk.ryanwong.gmap2ics.data.models.places.Geometry
import uk.ryanwong.gmap2ics.data.models.places.Location
import uk.ryanwong.gmap2ics.data.models.places.Result

object KtorGoogleApiDataSourceTestData {
    /***
     * Data source unit tests I would prefer using real data samples.
     */
    val mockPlaceDetailsDataModel = uk.ryanwong.gmap2ics.data.models.places.PlaceDetails(
        result = Result(
            addressComponents =
            listOf(
                AddressComponent(longName = "8", shortName = "8", types = listOf("street_number")),
                AddressComponent(
                    longName = "Greg Avenue",
                    shortName = "Greg Ave",
                    types = listOf("route")
                ),
                AddressComponent(
                    longName = "Bollington",
                    shortName = "Bollington",
                    types = listOf("locality", "political")
                ),
                AddressComponent(
                    longName = "Macclesfield",
                    shortName = "Macclesfield",
                    types = listOf("postal_town")
                ),
                AddressComponent(
                    longName = "Cheshire East",
                    shortName = "Cheshire East",
                    types = listOf("administrative_area_level_2", "political")
                ),
                AddressComponent(
                    longName = "England",
                    shortName = "England",
                    types = listOf("administrative_area_level_1", "political")
                ),
                AddressComponent(
                    longName = "United Kingdom",
                    shortName = "GB",
                    types = listOf("country", "political")
                ),
                AddressComponent(
                    longName = "SK10 5HR",
                    shortName = "SK10 5HR",
                    types = listOf("postal_code")
                )
            ),
            adrAddress = """<span class="street-address">8 Greg Avenue</span>, <span class="extended-address">Bollington</span>, <span class="locality">Macclesfield</span> <span class="postal-code">SK10 5HR</span>, <span class="country-name">UK</span>""",
            formattedAddress = "8 Greg Ave, Bollington, Macclesfield SK10 5HR, UK",
            formattedPhoneNumber = null,
            geometry = Geometry(location = Location(lat = 53.2945761, lng = -2.114387)),
            icon = "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/geocode-71.png",
            iconBackgroundColor = "#7B9EB0",
            iconMaskBaseUri = "https://maps.gstatic.com/mapfiles/place_api/icons/v2/generic_pinlet",
            name = "8 Greg Ave",
            placeId = "ChIJ43NG7NxLekgR7NFDJzb-WXw",
            plusCode = null,
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