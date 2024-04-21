/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.usecases

import uk.ryanwong.gmap2ics.domain.usecases.interfaces.GetOutputFilenameUseCase

class GetOutputFilenameUseCaseImpl : GetOutputFilenameUseCase {
    override fun invoke(
        originalFilename: String,
        jsonPath: String,
        iCalPath: String,
        exportPlaceVisit: Boolean,
        exportActivitySegment: Boolean,
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
