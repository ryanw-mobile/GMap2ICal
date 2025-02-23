/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.app.utils.timezonemap.fakes

import com.esri.core.geometry.Polygon
import uk.ryanwong.gmap2ics.domain.utils.timezonemap.TimeZoneMapWrapper
import us.dustinj.timezonemap.TimeZone

internal class FakeTimeZoneMap : TimeZoneMapWrapper {

    var zoneId: String? = null
    override fun getOverlappingTimeZone(degreesLatitude: Double, degreesLongitude: Double): TimeZone {
        return TimeZone(
            zoneId = zoneId ?: "Asia/Tokyo", // Needs real zone as it affects time calculation
            region = Polygon(),
        )
    }
}
