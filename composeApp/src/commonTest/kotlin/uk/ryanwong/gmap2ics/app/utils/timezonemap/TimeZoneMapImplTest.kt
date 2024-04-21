/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.app.utils.timezonemap

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.domain.utils.timezonemap.TimeZoneMapImpl
import us.dustinj.timezonemap.TimeZoneMap

internal class TimeZoneMapImplTest : FreeSpec() {

    /***
     * Note:
     * It is said that we should not test 3rd party codes.
     * However, this is MY project, and this library is a critical part of it.
     * So I say, we SHOULD test it to ensure it always work as expected.
     */
    lateinit var timeZoneMapImpl: TimeZoneMapImpl

    init {
        beforeTest {
            timeZoneMapImpl = TimeZoneMapImpl(timeZoneMap = TimeZoneMap.forEverywhere())
        }

        "getOverlappingTimeZone" - {
            "should return correct zoneId for Japan coordinates" {
                val degreesLatitude = 26.217072
                val degreesLongitude = 127.719477

                val timeZone = timeZoneMapImpl.getOverlappingTimeZone(
                    degreesLatitude = degreesLatitude,
                    degreesLongitude = degreesLongitude,
                )

                timeZone!!.zoneId shouldBe "Asia/Tokyo"
            }

            "should return correct zoneId for Korea coordinates" {
                val degreesLatitude = 37.55108
                val degreesLongitude = 126.988148

                val timeZone = timeZoneMapImpl.getOverlappingTimeZone(
                    degreesLatitude = degreesLatitude,
                    degreesLongitude = degreesLongitude,
                )

                timeZone!!.zoneId shouldBe "Asia/Seoul"
            }

            "should return correct zoneId for UK coordinates" {
                val degreesLatitude = 52.090277
                val degreesLongitude = 1.448719

                val timeZone = timeZoneMapImpl.getOverlappingTimeZone(
                    degreesLatitude = degreesLatitude,
                    degreesLongitude = degreesLongitude,
                )

                timeZone!!.zoneId shouldBe "Europe/London"
            }
        }
    }
}
