/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases.impl

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.domain.usecases.GetOutputFilenameUseCaseImpl
import uk.ryanwong.gmap2ics.domain.usecases.interfaces.GetOutputFilenameUseCase

class GetOutputFilenameUseCaseImplTest : FreeSpec() {

    private lateinit var getOutputFilenameUseCase: GetOutputFilenameUseCase

    init {
        beforeTest {
            getOutputFilenameUseCase = GetOutputFilenameUseCaseImpl()
        }

        "Should set correct filename when exportPlaceVisit and exportActivitySegment are true" {
            val originalFilename = "/some-path/json/some-file-1.json"
            val exportPlaceVisit = true
            val exportActivitySegment = true
            val jsonPath = "/some-path/json/"
            val iCalPath = "/some-path/ical/"

            val outputFilename = getOutputFilenameUseCase(
                originalFilename = originalFilename,
                jsonPath = jsonPath,
                iCalPath = iCalPath,
                exportPlaceVisit = exportPlaceVisit,
                exportActivitySegment = exportActivitySegment,
            )

            outputFilename shouldBe "/some-path/ical/some-file-1_all.ics"
        }

        "Should set correct filename when only exportPlaceVisit is true" {
            val originalFilename = "/some-path/json/some-file-1.json"
            val exportPlaceVisit = true
            val exportActivitySegment = false
            val jsonPath = "/some-path/json/"
            val iCalPath = "/some-path/ical/"

            val outputFilename = getOutputFilenameUseCase(
                originalFilename = originalFilename,
                jsonPath = jsonPath,
                iCalPath = iCalPath,
                exportPlaceVisit = exportPlaceVisit,
                exportActivitySegment = exportActivitySegment,
            )

            outputFilename shouldBe "/some-path/ical/some-file-1_places.ics"
        }

        "Should set correct filename when only exportActivitySegment is true" {
            val originalFilename = "/some-path/json/some-file-1.json"
            val exportPlaceVisit = false
            val exportActivitySegment = true
            val jsonPath = "/some-path/json/"
            val iCalPath = "/some-path/ical/"

            val outputFilename = getOutputFilenameUseCase(
                originalFilename = originalFilename,
                jsonPath = jsonPath,
                iCalPath = iCalPath,
                exportPlaceVisit = exportPlaceVisit,
                exportActivitySegment = exportActivitySegment,
            )

            outputFilename shouldBe "/some-path/ical/some-file-1_activities.ics"
        }

        "Should set default filename when exportPlaceVisit and exportActivitySegment are false" {
            val originalFilename = "/some-path/json/some-file-1.json"
            val exportPlaceVisit = false
            val exportActivitySegment = false
            val jsonPath = "/some-path/json/"
            val iCalPath = "/some-path/ical/"

            val outputFilename = getOutputFilenameUseCase(
                originalFilename = originalFilename,
                jsonPath = jsonPath,
                iCalPath = iCalPath,
                exportPlaceVisit = exportPlaceVisit,
                exportActivitySegment = exportActivitySegment,
            )

            outputFilename shouldBe "/some-path/ical/some-file-1_activities.ics"
        }
    }
}
