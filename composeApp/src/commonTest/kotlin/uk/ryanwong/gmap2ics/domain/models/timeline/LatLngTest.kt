/*
 * Copyright (c) 2022-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline

import kotlin.test.Test
import kotlin.test.assertEquals

internal class LatLngTest {
    @Test
    fun `returns formatted latlng when given latlng`() {
        val latitude = 12.1234567
        val longitude = 123.1234567
        val latlng = LatLng(latitude = latitude, longitude = longitude)

        assertEquals("12.123457,123.123457", latlng.getFormattedLatLng())
    }
}
