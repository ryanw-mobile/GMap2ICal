/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.startWith
import java.time.zone.ZoneRulesException

class RawTimestampTest : FreeSpec() {

    init {
        "toLocalizedTimestamp" - {
            "Should return correct localized Time Stamp for good timestamp and timezoneId" {
                val timestamp = "2011-11-11T11:22:22.222Z"
                val timezoneId = "Asia/Tokyo"

                val formattedTimeStamp =
                    RawTimestamp(timestamp = timestamp, timezoneId = timezoneId).toLocalizedTimestamp()

                formattedTimeStamp shouldBe "20111111T202222"
            }

            "Should return exception if timezoneId is invalid" {
                val timestamp = "2011-11-11T11:22:22.222Z"
                val timezoneId = "some-incorrect-timezoneid"

                val exception = shouldThrow<ZoneRulesException> {
                    RawTimestamp(
                        timestamp = timestamp,
                        timezoneId = timezoneId,
                    ).toLocalizedTimestamp()
                }

                exception.message shouldBe startWith(prefix = "Unknown time-zone ID: some-incorrect-timezoneid")
            }

            "Should return exception if timestamp is invalid" {
                val timestamp = "some-invalid-timestamp"
                val timezoneId = "Asia/Tokyo"

                val exception = shouldThrow<java.time.format.DateTimeParseException> {
                    RawTimestamp(
                        timestamp = timestamp,
                        timezoneId = timezoneId,
                    ).toLocalizedTimestamp()
                }

                exception.message shouldBe startWith(prefix = "Text 'some-invalid-timestamp' could not be parsed at index 0")
            }
        }

        "toUITimestamp" - {
            "Should return correct UI Time Stamp for good timestamp and timezoneId" {
                val timestamp = "2011-12-13T11:22:22.222Z"
                val timezoneId = "Asia/Tokyo"

                val formattedTimeStamp =
                    RawTimestamp(timestamp = timestamp, timezoneId = timezoneId).toUITimestamp()

                formattedTimeStamp shouldBe "13/12/2011 20:22:22"
            }

            "Should return exception if timezoneId is invalid" {
                val timestamp = "2011-11-11T11:22:22.222Z"
                val timezoneId = "some-incorrect-timezoneid"

                val exception = shouldThrow<ZoneRulesException> {
                    RawTimestamp(
                        timestamp = timestamp,
                        timezoneId = timezoneId,
                    ).toUITimestamp()
                }

                exception.message shouldBe startWith(prefix = "Unknown time-zone ID: some-incorrect-timezoneid")
            }

            "Should return exception if timestamp is invalid" {
                val timestamp = "some-invalid-timestamp"
                val timezoneId = "Asia/Tokyo"

                val exception = shouldThrow<java.time.format.DateTimeParseException> {
                    RawTimestamp(
                        timestamp = timestamp,
                        timezoneId = timezoneId,
                    ).toUITimestamp()
                }

                exception.message shouldBe startWith(prefix = "Text 'some-invalid-timestamp' could not be parsed at index 0")
            }
        }
    }
}
