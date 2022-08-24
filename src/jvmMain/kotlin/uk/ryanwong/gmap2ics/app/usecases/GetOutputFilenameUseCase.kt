/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.usecases

interface GetOutputFilenameUseCase {
    operator fun invoke(
        originalFilename: String,
        jsonPath: String,
        iCalPath: String,
        exportPlaceVisit: Boolean,
        exportActivitySegment: Boolean
    ): String
}