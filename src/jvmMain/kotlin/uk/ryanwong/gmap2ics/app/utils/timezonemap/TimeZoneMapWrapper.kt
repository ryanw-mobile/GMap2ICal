/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.utils.timezonemap

import us.dustinj.timezonemap.TimeZone

interface TimeZoneMapWrapper {
    fun getOverlappingTimeZone(degreesLatitude: Double, degreesLongitude: Double): TimeZone?
}