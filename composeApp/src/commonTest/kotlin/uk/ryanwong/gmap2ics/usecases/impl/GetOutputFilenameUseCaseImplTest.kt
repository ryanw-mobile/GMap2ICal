/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases.impl

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.domain.usecases.GetOutputFilenameUseCase
import uk.ryanwong.gmap2ics.usecases.GetOutputFilenameUseCaseImpl

class GetOutputFilenameUseCaseImplTest : FreeSpec() {

    private lateinit var getOutputFilenameUseCase: GetOutputFilenameUseCase

    private fun setupUseCase() {
        getOutputFilenameUseCase = GetOutputFilenameUseCaseImpl()
    }

    init {
        "Should set correct filename when exportPlaceVisit and exportActivitySegment are true" {
            // 🔴 Given
            setupUseCase()
            val originalFilename = "/some-path/json/some-file-1.json"
            val exportPlaceVisit = true
            val exportActivitySegment = true
            val jsonPath = "/some-path/json/"
            val iCalPath = "/some-path/ical/"

            // 🟡 When
            val outputFilename = getOutputFilenameUseCase(
                originalFilename = originalFilename,
                jsonPath = jsonPath,
                iCalPath = iCalPath,
                exportPlaceVisit = exportPlaceVisit,
                exportActivitySegment = exportActivitySegment,
            )

            // 🟢 Then
            outputFilename shouldBe "/some-path/ical/some-file-1_all.ics"
        }

        "Should set correct filename when only exportPlaceVisit is true" {
            // 🔴 Given
            setupUseCase()
            val originalFilename = "/some-path/json/some-file-1.json"
            val exportPlaceVisit = true
            val exportActivitySegment = false
            val jsonPath = "/some-path/json/"
            val iCalPath = "/some-path/ical/"

            // 🟡 When
            val outputFilename = getOutputFilenameUseCase(
                originalFilename = originalFilename,
                jsonPath = jsonPath,
                iCalPath = iCalPath,
                exportPlaceVisit = exportPlaceVisit,
                exportActivitySegment = exportActivitySegment,
            )

            // 🟢 Then
            outputFilename shouldBe "/some-path/ical/some-file-1_places.ics"
        }

        "Should set correct filename when only exportActivitySegment is true" {
            // 🔴 Given
            setupUseCase()
            val originalFilename = "/some-path/json/some-file-1.json"
            val exportPlaceVisit = false
            val exportActivitySegment = true
            val jsonPath = "/some-path/json/"
            val iCalPath = "/some-path/ical/"

            // 🟡 When
            val outputFilename = getOutputFilenameUseCase(
                originalFilename = originalFilename,
                jsonPath = jsonPath,
                iCalPath = iCalPath,
                exportPlaceVisit = exportPlaceVisit,
                exportActivitySegment = exportActivitySegment,
            )

            // 🟢 Then
            outputFilename shouldBe "/some-path/ical/some-file-1_activities.ics"
        }

        "Should set default filename when exportPlaceVisit and exportActivitySegment are false" {
            // 🔴 Given
            setupUseCase()
            val originalFilename = "/some-path/json/some-file-1.json"
            val exportPlaceVisit = false
            val exportActivitySegment = false
            val jsonPath = "/some-path/json/"
            val iCalPath = "/some-path/ical/"

            // 🟡 When
            val outputFilename = getOutputFilenameUseCase(
                originalFilename = originalFilename,
                jsonPath = jsonPath,
                iCalPath = iCalPath,
                exportPlaceVisit = exportPlaceVisit,
                exportActivitySegment = exportActivitySegment,
            )

            // 🟢 Then
            outputFilename shouldBe "/some-path/ical/some-file-1_activities.ics"
        }
    }
}
