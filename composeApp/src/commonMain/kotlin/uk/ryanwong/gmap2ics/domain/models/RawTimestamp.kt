/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class RawTimestamp(
    val timestamp: String,
    val timezoneId: String,
) {
    fun toLocalizedTimestamp(): String = DateTimeFormatter
        .ofPattern("yyyyMMdd'T'HHmmss")
        .withZone(ZoneId.of(timezoneId))
        .format(Instant.parse(timestamp))

    fun toUITimestamp(): String = DateTimeFormatter
        .ofPattern("dd/MM/yyyy HH:mm:ss")
        .withZone(ZoneId.of(timezoneId))
        .format(Instant.parse(timestamp))
}
