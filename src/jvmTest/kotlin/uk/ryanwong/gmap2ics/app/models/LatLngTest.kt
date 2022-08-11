/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.models.timeline.LatLng

internal class LatLngTest : FreeSpec() {
    init {
        "LatLng" - {
            "should return rounded LatLng in a correct format" {
                // 🔴 Given
                val latitude = 12.1234567
                val longitude = 123.1234567

                // 🟡 When
                val latlng = LatLng(latitude = latitude, longitude = longitude)

                // 🟢 Then
                latlng.getFormattedLatLng() shouldBe "12.123457,123.123457"
            }
        }
    }
}