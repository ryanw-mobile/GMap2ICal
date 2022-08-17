/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline

import uk.ryanwong.gmap2ics.data.models.places.AddressComponent
import uk.ryanwong.gmap2ics.data.models.places.Geometry
import uk.ryanwong.gmap2ics.data.models.places.Location
import uk.ryanwong.gmap2ics.data.models.places.Result

object PlaceDetailsTestData {
    /***
     * Note: Seeing real data would help understand what kind of data we are getting from Google.
     */

    val mockPlaceDetailsResult = Result(
        addressComponents = listOf(
            AddressComponent(longName = "Wakefield", shortName = "Wakefield", types = listOf("postal_town")),
            AddressComponent(
                longName = "West Yorkshire",
                shortName = "West Yorkshire",
                types = listOf("administrative_area_level_2", "political")
            ),
            AddressComponent(
                longName = "England",
                shortName = "England",
                types = listOf("administrative_area_level_1", "political")
            ),
            AddressComponent(longName = "United Kingdom", shortName = "GB", types = listOf("country", "political")),
            AddressComponent(longName = "WF4 4LG", shortName = "WF4 4LG", types = listOf("postal_code"))
        ),
        adrAddress = "<span class=\"locality\">Wakefield</span> <span class=\"postal-code\">WF4 4LG</span>, <span class=\"country-name\">UK</span>",
        formattedAddress = "Wakefield WF4 4LG, UK",
        formattedPhoneNumber = "01924 832631",
        geometry = Geometry(
            location = Location(
                lat = 53.6152405,
                lng = -1.5639315
            )
        ),
        icon = "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/generic_business-71.png",
        iconBackgroundColor = "#7B9EB0",
        iconMaskBaseUri = "https://maps.gstatic.com/mapfiles/place_api/icons/v2/generic_pinlet",
        name = "Yorkshire Sculpture Park",
        placeId = "ChIJOX4_ortjeUgR2_LUcFpvLg4",
        reference = "ChIJOX4_ortjeUgR2_LUcFpvLg4",
        types = listOf("art_gallery", "tourist_attraction", "park", "museum", "point_of_interest", "establishment"),
        url = "https://maps.google.com/?cid=1021876599690425051",
        userRatingsTotal = 8319,
        utcOffset = 60,
        vicinity = "Wakefield",
        website = "http://www.ysp.org.uk/"
    )
}