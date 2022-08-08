/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.startWith
import java.time.format.DateTimeParseException

class VEventTest : FreeSpec() {
    init {

        "getLocalizedTimeStamp" - {
            "Should return correct localized Time Stamp for good timestamp and timezoneId" {
                // 🔴 Given
                val timestamp = "2011-11-11T11:22:22.222Z"
                val timezoneId = "Asia/Tokyo"

                // 🟡 When
                val formattedTimeStamp = getLocalizedTimeStamp(timestamp = timestamp, timezoneId = timezoneId)

                // 🟢 Then
                formattedTimeStamp shouldBe "20111111T202222"
            }
            "Should return exception if timezoneId is invalid" {
                // 🔴 Given
                val timestamp = "2011-11-11T11:22:22.222Z"
                val timezoneId = "some-incorrect-timezoneid"

                // 🟡 When
                val exception = shouldThrow<java.time.zone.ZoneRulesException> {
                    getLocalizedTimeStamp(
                        timestamp = timestamp,
                        timezoneId = timezoneId
                    )
                }

                // 🟢 Then
                exception.message shouldBe startWith("Unknown time-zone ID: some-incorrect-timezoneid")
            }
            "Should return exception if timestamp is invalid" {
                // 🔴 Given
                val timestamp = "some-invalid-timestamp"
                val timezoneId = "Asia/Tokyo"

                // 🟡 When
                val exception = shouldThrow<DateTimeParseException> {
                    getLocalizedTimeStamp(
                        timestamp = timestamp,
                        timezoneId = timezoneId
                    )
                }

                // 🟢 Then
                exception.message shouldBe startWith("Text 'some-invalid-timestamp' could not be parsed at index 0")
            }
        }
    }
}