/*
 * Copyright (c) 2023-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models

import kotlin.test.Test
import kotlin.test.assertEquals

internal class ActivityTypeTest {

    @Test
    fun `returns correct enum when input is a known activity type`() {
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
            val actualActivityTypeEnum = ActivityType.parse(activityType = knownActivityType.key)
            assertEquals(knownActivityType.value, actualActivityTypeEnum)
        }
    }

    @Test
    fun `returns UNKNOWN_ACTIVITY_TYPE when input is an unknown activity type`() {
        val unknownActivityString = "some-unknown-activity-type"
        val actualActivityTypeEnum = ActivityType.parse(activityType = unknownActivityString)
        assertEquals(ActivityType.UNKNOWN_ACTIVITY_TYPE, actualActivityTypeEnum)
    }

    @Test
    fun `returns UNKNOWN_ACTIVITY_TYPE when input is null`() {
        val unknownActivityString = null
        val actualActivityTypeEnum = ActivityType.parse(activityType = unknownActivityString)
        assertEquals(ActivityType.UNKNOWN_ACTIVITY_TYPE, actualActivityTypeEnum)
    }
}
