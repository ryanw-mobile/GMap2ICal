/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.utils.timezonemap

import com.esri.core.geometry.Polygon
import us.dustinj.timezonemap.TimeZone

class MockTimeZoneMap : TimeZoneMapWrapper {

    var mockZoneId: String? = null
    override fun getOverlappingTimeZone(degreesLatitude: Double, degreesLongitude: Double): TimeZone {
        return TimeZone(
            zoneId = mockZoneId ?: "Asia/Tokyo", // Needs real zone as it affects time calculation
            region = Polygon()
        )
    }
}