package uk.ryanwong.gmap2ics.domain.models

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class LatLngTest : FreeSpec() {
    init {
        "LatLng" - {
            "should return rounded LatLng in a correct format" {
                // 🔴 Given
                val latitude = 12.12345678
                val longitude = 123.12345678

                // 🟡 When
                val latlng = LatLng(latitude = latitude, longitude = longitude)

                // 🟢 Then
                latlng.getFormattedLatLng() shouldBe "12.123457,123.123457"
            }
        }
    }
}