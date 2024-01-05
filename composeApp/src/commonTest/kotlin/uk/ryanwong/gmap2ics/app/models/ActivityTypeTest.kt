/*
 * Copyright (c) 2023-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.app.models

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class ActivityTypeTest : FreeSpec() {

    init {
        "parse()" - {
            "should convert correctly from known ActivityType strings to the Enums" {
                // 🔴 Given
                val knownActivityTypes = mapOf(
                    "BOATING" to ActivityType.BOATING,
                    "CYCLING" to ActivityType.CYCLING,
                    "FLYING" to ActivityType.FLYING,
                    "HIKING" to ActivityType.HIKING,
                    "IN_BUS" to ActivityType.IN_BUS,
                    "IN_CABLECAR" to ActivityType.IN_CABLECAR,
                    "IN_FERRY" to ActivityType.IN_FERRY,
                    "IN_PASSENGER_VEHICLE" to ActivityType.IN_PASSENGER_VEHICLE,
                    "IN_SUBWAY" to ActivityType.IN_SUBWAY,
                    "IN_TAXI" to ActivityType.IN_TAXI,
                    "IN_TRAIN" to ActivityType.IN_TRAIN,
                    "IN_TRAM" to ActivityType.IN_TRAM,
                    "IN_VEHICLE" to ActivityType.IN_VEHICLE,
                    "MOTORCYCLING" to ActivityType.MOTORCYCLING,
                    "RUNNING" to ActivityType.RUNNING,
                    "SAILING" to ActivityType.SAILING,
                    "SKIING" to ActivityType.SKIING,
                    "STILL" to ActivityType.STILL,
                    "WALKING" to ActivityType.WALKING,
                )

                knownActivityTypes.forEach { knownActivityType ->
                    // 🟡 When
                    val activityTypeEnum = ActivityType.parse(activityType = knownActivityType.key)

                    // 🟢 Then
                    activityTypeEnum shouldBe knownActivityType.value
                }
            }

            "should return ActivityType.UNKNOWN_ACTIVITY_TYPE for unknown strings" {
                // 🔴 Given
                val unknownActivityString = "some-unknown-activity-type"

                // 🟡 When
                val activityTypeEnum = ActivityType.parse(activityType = unknownActivityString)

                // 🟢 Then
                activityTypeEnum shouldBe ActivityType.UNKNOWN_ACTIVITY_TYPE
            }

            "should return ActivityType.UNKNOWN_ACTIVITY_TYPE for null input" {
                // 🔴 Given
                val unknownActivityString = null

                // 🟡 When
                val activityTypeEnum = ActivityType.parse(activityType = unknownActivityString)

                // 🟢 Then
                activityTypeEnum shouldBe ActivityType.UNKNOWN_ACTIVITY_TYPE
            }
        }
    }
}
