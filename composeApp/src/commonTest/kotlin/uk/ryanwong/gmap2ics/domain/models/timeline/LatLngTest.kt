/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class LatLngTest : FreeSpec() {
    init {
        "getFormattedLatLng" - {
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
