package uk.ryanwong.gmap2ics.domain.models

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class LatLngTest {

    @Test
    fun `should return rounded LatLng in a correct format`() {
        // given
        val latitude = 12.12345678
        val longitude = 123.12345678

        // when
        val latlng = LatLng(latitude = latitude, longitude = longitude)

        // then
        latlng.getFormattedLatLng() shouldBe "12.123457,123.123457"
    }
}