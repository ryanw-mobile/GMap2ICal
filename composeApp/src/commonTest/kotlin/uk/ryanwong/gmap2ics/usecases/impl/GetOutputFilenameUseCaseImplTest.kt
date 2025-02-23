/*
 * Copyright (c) 2022-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases.impl

import uk.ryanwong.gmap2ics.domain.usecases.GetOutputFilenameUseCaseImpl
import uk.ryanwong.gmap2ics.domain.usecases.interfaces.GetOutputFilenameUseCase
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GetOutputFilenameUseCaseImplTest {

    private lateinit var getOutputFilenameUseCase: GetOutputFilenameUseCase

    @BeforeTest
    fun setup() {
        getOutputFilenameUseCase = GetOutputFilenameUseCaseImpl()
    }

    @Test
    fun `returns correct filename when exporting both place visit and activity segment`() {
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

        assertEquals("/some-path/ical/some-file-1_all.ics", outputFilename)
    }

    @Test
    fun `returns correct filename when only exporting place visit`() {
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

        assertEquals("/some-path/ical/some-file-1_places.ics", outputFilename)
    }

    @Test
    fun `returns correct filename when only exporting activity segment`() {
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

        assertEquals("/some-path/ical/some-file-1_activities.ics", outputFilename)
    }

    @Test
    fun `returns default filename when not exporting place visit and activity segment`() {
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

        assertEquals("/some-path/ical/some-file-1_activities.ics", outputFilename)
    }
}
