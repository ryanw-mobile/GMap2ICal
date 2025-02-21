/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models

import java.time.zone.ZoneRulesException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class RawTimestampTest {

    @Test
    fun `toLocalizedTimestamp should return correct localized Time Stamp for good timestamp and timezoneId`() {
        val timestamp = "2011-11-11T11:22:22.222Z"
        val timezoneId = "Asia/Tokyo"

        val formattedTimeStamp = RawTimestamp(timestamp = timestamp, timezoneId = timezoneId).toLocalizedTimestamp()

        assertEquals("20111111T202222", formattedTimeStamp)
    }

    @Test
    fun `toLocalizedTimestamp should return exception if timezoneId is invalid`() {
        val timestamp = "2011-11-11T11:22:22.222Z"
        val timezoneId = "some-incorrect-timezoneid"

        assertFailsWith<ZoneRulesException>(message = "Unknown time-zone ID: some-incorrect-timezoneid") {
            RawTimestamp(
                timestamp = timestamp,
                timezoneId = timezoneId,
            ).toLocalizedTimestamp()
        }
    }

    @Test
    fun `toLocalizedTimestamp should return exception if timestamp is invalid`() {
        val timestamp = "some-invalid-timestamp"
        val timezoneId = "Asia/Tokyo"

        assertFailsWith<java.time.format.DateTimeParseException>(message = "Text 'some-invalid-timestamp' could not be parsed at index 0") {
            RawTimestamp(
                timestamp = timestamp,
                timezoneId = timezoneId,
            ).toLocalizedTimestamp()
        }
    }

    @Test
    fun `toUITimestamp should return correct UI Time Stamp for good timestamp and timezoneId`() {
        val timestamp = "2011-12-13T11:22:22.222Z"
        val timezoneId = "Asia/Tokyo"

        val formattedTimeStamp =
            RawTimestamp(timestamp = timestamp, timezoneId = timezoneId).toUITimestamp()

        assertEquals("13/12/2011 20:22:22", formattedTimeStamp)
    }

    @Test
    fun `toUITimestamp should return exception if timezoneId is invalid`() {
        val timestamp = "2011-11-11T11:22:22.222Z"
        val timezoneId = "some-incorrect-timezoneid"

        assertFailsWith<ZoneRulesException>(message = "Unknown time-zone ID: some-incorrect-timezoneid") {
            RawTimestamp(
                timestamp = timestamp,
                timezoneId = timezoneId,
            ).toUITimestamp()
        }
    }

    @Test
    fun `toUITimestamp should return exception if timestamp is invalid`() {
        val timestamp = "some-invalid-timestamp"
        val timezoneId = "Asia/Tokyo"

        assertFailsWith<java.time.format.DateTimeParseException>(message = "Text 'some-invalid-timestamp' could not be parsed at index 0") {
            RawTimestamp(
                timestamp = timestamp,
                timezoneId = timezoneId,
            ).toUITimestamp()
        }
    }
}
