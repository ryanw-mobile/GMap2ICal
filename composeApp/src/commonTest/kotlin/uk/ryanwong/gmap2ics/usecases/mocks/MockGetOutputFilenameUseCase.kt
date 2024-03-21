/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases.mocks

import uk.ryanwong.gmap2ics.domain.usecases.GetOutputFilenameUseCase

class MockGetOutputFilenameUseCase : GetOutputFilenameUseCase {
    var mockUseCaseResponse: String? = null
    override fun invoke(
        originalFilename: String,
        jsonPath: String,
        iCalPath: String,
        exportPlaceVisit: Boolean,
        exportActivitySegment: Boolean,
    ): String {
        return mockUseCaseResponse ?: throw Exception("mock response unavailable")
    }
}
