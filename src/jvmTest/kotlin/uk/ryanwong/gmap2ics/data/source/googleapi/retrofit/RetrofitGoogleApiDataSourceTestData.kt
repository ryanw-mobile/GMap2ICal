/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.retrofit

import okhttp3.ResponseBody
import retrofit2.Response
import uk.ryanwong.gmap2ics.data.models.places.Geometry
import uk.ryanwong.gmap2ics.data.models.places.Location
import uk.ryanwong.gmap2ics.data.models.places.PlaceDetails
import uk.ryanwong.gmap2ics.data.models.places.Result

object RetrofitGoogleApiDataSourceTestData {

    val retrofitApiResponseSuccess: Response<PlaceDetails> = Response.success(
        200,
        PlaceDetails(
            Result(
                formattedAddress = "some-formatted-address",
                formattedPhoneNumber = null,
                geometry = Geometry(location = Location(lat = 50.44444444444444, lng = -2.111111111111111)),
                icon = "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/geocode-71.png",
                name = "some-name",
                placeId = "some-place-id",
                reference = "some-reference",
                types = listOf("route"),
                url = "https://maps.google.com/?q=some+address,+Manchester+M21,+UK&ftid=0x1111111111111111:0x1111111111111111",
                userRatingsTotal = null,
                utcOffset = 60,
                vicinity = null,
                website = null,
            ),
        ),
    )

    val retrofitApiResponseFailure: Response<PlaceDetails> =
        Response.error(500, ResponseBody.create(null, ByteArray(0)))
}
