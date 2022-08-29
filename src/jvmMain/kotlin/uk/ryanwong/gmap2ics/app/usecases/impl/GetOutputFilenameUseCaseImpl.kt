/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.usecases.impl

import uk.ryanwong.gmap2ics.app.usecases.GetOutputFilenameUseCase

class GetOutputFilenameUseCaseImpl : GetOutputFilenameUseCase {
    override fun invoke(
        originalFilename: String,
        jsonPath: String,
        iCalPath: String,
        exportPlaceVisit: Boolean,
        exportActivitySegment: Boolean
    ): String {
        val outputFilenameSuffix = when {
            exportPlaceVisit && exportActivitySegment -> "_all"
            exportPlaceVisit -> "_places"
            else -> "_activities"
        }

        return originalFilename.replace(oldValue = jsonPath, newValue = iCalPath)
            // casually reuse the filename
            .replace(oldValue = ".json", newValue = "$outputFilenameSuffix.ics")
    }
}
