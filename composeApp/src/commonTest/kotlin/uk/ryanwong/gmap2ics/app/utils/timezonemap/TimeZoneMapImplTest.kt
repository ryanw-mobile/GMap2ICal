/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.app.utils.timezonemap

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import us.dustinj.timezonemap.TimeZoneMap

internal class TimeZoneMapImplTest : FreeSpec() {

    /***
     * Note:
     * It is said that we should not test 3rd party codes.
     * However, this is MY project, and this library is a critical part of it.
     * So I say, we SHOULD test it to ensure it always work as expected.
     */
    lateinit var timeZoneMapImpl: TimeZoneMapImpl

    private fun setupTimeZoneMapImpl() {
        timeZoneMapImpl = TimeZoneMapImpl(timeZoneMap = TimeZoneMap.forEverywhere())
    }

    init {
        "getOverlappingTimeZone" - {
            "should return correct zoneId for Japan coordinates" {
                // 🔴 Given
                setupTimeZoneMapImpl()
                val degreesLatitude = 26.217072
                val degreesLongitude = 127.719477

                // 🟡 When
                val timeZone = timeZoneMapImpl.getOverlappingTimeZone(
                    degreesLatitude = degreesLatitude,
                    degreesLongitude = degreesLongitude,
                )

                // 🟢 Then
                timeZone!!.zoneId shouldBe "Asia/Tokyo"
            }

            "should return correct zoneId for Korea coordinates" {
                // 🔴 Given
                setupTimeZoneMapImpl()
                val degreesLatitude = 37.55108
                val degreesLongitude = 126.988148

                // 🟡 When
                val timeZone = timeZoneMapImpl.getOverlappingTimeZone(
                    degreesLatitude = degreesLatitude,
                    degreesLongitude = degreesLongitude,
                )

                // 🟢 Then
                timeZone!!.zoneId shouldBe "Asia/Seoul"
            }

            "should return correct zoneId for UK coordinates" {
                // 🔴 Given
                setupTimeZoneMapImpl()
                val degreesLatitude = 52.090277
                val degreesLongitude = 1.448719

                // 🟡 When
                val timeZone = timeZoneMapImpl.getOverlappingTimeZone(
                    degreesLatitude = degreesLatitude,
                    degreesLongitude = degreesLongitude,
                )

                // 🟢 Then
                timeZone!!.zoneId shouldBe "Europe/London"
            }
        }
    }
}
