/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.utils.timezonemap.mocks

import com.esri.core.geometry.Polygon
import uk.ryanwong.gmap2ics.app.utils.timezonemap.TimeZoneMapWrapper
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
