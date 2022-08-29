/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.usecases.mocks

import uk.ryanwong.gmap2ics.app.usecases.GetOutputFilenameUseCase

class MockGetOutputFilenameUseCase : GetOutputFilenameUseCase {
    var mockUseCaseResponse: String? = null
    override fun invoke(
        originalFilename: String,
        jsonPath: String,
        iCalPath: String,
        exportPlaceVisit: Boolean,
        exportActivitySegment: Boolean
    ): String {
        return mockUseCaseResponse ?: throw Exception("mock response unavailable")
    }
}
