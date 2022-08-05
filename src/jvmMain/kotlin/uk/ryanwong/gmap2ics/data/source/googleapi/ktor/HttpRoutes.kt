/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.ktor

object HttpRoutes {
    private const val BASE_URL = "https://maps.googleapis.com"
    const val PLACE_DETAILS = "$BASE_URL/maps/api/place/details/json"
}