/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.googleapi

import uk.ryanwong.gmap2ics.data.models.places.GeometryDto
import uk.ryanwong.gmap2ics.data.models.places.LocationDto
import uk.ryanwong.gmap2ics.data.models.places.PlaceDetailsDto
import uk.ryanwong.gmap2ics.data.models.places.ResultDto

internal object KtorGoogleApiDataSourceTestData {

    const val PLACE_DETAILS_GREG_AVE_JSON = """{
   "html_attributions" : [],
   "result" : {
      "address_components" : [
         {
            "long_name" : "8",
            "short_name" : "8",
            "types" : [ "street_number" ]
         },
         {
            "long_name" : "Greg Avenue",
            "short_name" : "Greg Ave",
            "types" : [ "route" ]
         },
         {
            "long_name" : "Bollington",
            "short_name" : "Bollington",
            "types" : [ "locality", "political" ]
         },
         {
            "long_name" : "Macclesfield",
            "short_name" : "Macclesfield",
            "types" : [ "postal_town" ]
         },
         {
            "long_name" : "Cheshire East",
            "short_name" : "Cheshire East",
            "types" : [ "administrative_area_level_2", "political" ]
         },
         {
            "long_name" : "England",
            "short_name" : "England",
            "types" : [ "administrative_area_level_1", "political" ]
         },
         {
            "long_name" : "United Kingdom",
            "short_name" : "GB",
            "types" : [ "country", "political" ]
         },
         {
            "long_name" : "SK10 5HR",
            "short_name" : "SK10 5HR",
            "types" : [ "postal_code" ]
         }
      ],
      "adr_address" : "\u003cspan class=\"street-address\"\u003e8 Greg Avenue\u003c/span\u003e, \u003cspan class=\"extended-address\"\u003eBollington\u003c/span\u003e, \u003cspan class=\"locality\"\u003eMacclesfield\u003c/span\u003e \u003cspan class=\"postal-code\"\u003eSK10 5HR\u003c/span\u003e, \u003cspan class=\"country-name\"\u003eUK\u003c/span\u003e",
      "formatted_address" : "8 Greg Ave, Bollington, Macclesfield SK10 5HR, UK",
      "geometry" : {
         "location" : {
            "lat" : 53.2945761,
            "lng" : -2.114387
         },
         "viewport" : {
            "northeast" : {
               "lat" : 53.2959197302915,
               "lng" : -2.112909119708498
            },
            "southwest" : {
               "lat" : 53.2932217697085,
               "lng" : -2.115607080291502
            }
         }
      },
      "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/geocode-71.png",
      "icon_background_color" : "#7B9EB0",
      "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/generic_pinlet",
      "name" : "8 Greg Ave",
      "place_id" : "ChIJ43NG7NxLekgR7NFDJzb-WXw",
      "reference" : "ChIJ43NG7NxLekgR7NFDJzb-WXw",
      "types" : [ "premise" ],
      "url" : "https://maps.google.com/?q=8+Greg+Ave,+Bollington,+Macclesfield+SK10+5HR,+UK&ftid=0x487a4bdcec4673e3:0x7c59fe362743d1ec",
      "utc_offset" : 60,
      "vicinity" : "Bollington"
   },
   "status" : "OK"
}"""

    val PLACE_DETAILS_GREG_AVE_DTO = PlaceDetailsDto(
        ResultDto(
            formattedAddress = "8 Greg Ave, Bollington, Macclesfield SK10 5HR, UK",
            formattedPhoneNumber = null,
            geometry = GeometryDto(location = LocationDto(lat = 53.2945761, lng = -2.114387)),
            icon = "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/geocode-71.png",
            name = "8 Greg Ave",
            placeId = "ChIJ43NG7NxLekgR7NFDJzb-WXw",
            reference = "ChIJ43NG7NxLekgR7NFDJzb-WXw",
            types = listOf("premise"),
            url = "https://maps.google.com/?q=8+Greg+Ave,+Bollington,+Macclesfield+SK10+5HR,+UK&ftid=0x487a4bdcec4673e3:0x7c59fe362743d1ec",
            userRatingsTotal = null,
            utcOffset = 60,
            vicinity = "Bollington",
            website = null,
        ),
    )
}
