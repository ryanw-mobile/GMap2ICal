/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases.fakes

import uk.ryanwong.gmap2ics.domain.usecases.interfaces.GetOutputFilenameUseCase

class FakeGetOutputFilenameUseCase : GetOutputFilenameUseCase {
    var useCaseResponse: String? = null
    override fun invoke(
        originalFilename: String,
        jsonPath: String,
        iCalPath: String,
        exportPlaceVisit: Boolean,
        exportActivitySegment: Boolean,
    ): String {
        return useCaseResponse ?: throw Exception("response not defined")
    }
}
