package uk.ryanwong.gmap2ics.domain.models

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class LatLngTest : FreeSpec() {
    init {
        "LatLng" - {
            "should return rounded LatLng in a correct format" {
                // given
                val latitude = 12.12345678
                val longitude = 123.12345678

                // when
                val latlng = LatLng(latitude = latitude, longitude = longitude)

                // then
                latlng.getFormattedLatLng() shouldBe "12.123457,123.123457"
            }
        }
    }
}