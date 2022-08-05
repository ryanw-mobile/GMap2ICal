/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import io.kotest.core.spec.style.FreeSpec
import uk.ryanwong.gmap2ics.data.repository.MockPlaceDetailsRepository
import us.dustinj.timezonemap.TimeZoneMap

internal class ExportChildVisitUseCaseImplTest : FreeSpec() {

    lateinit var exportChildVisitUseCase: ExportChildVisitUseCaseImpl
    lateinit var mockPlaceDetailsRepository: MockPlaceDetailsRepository

    private fun setupUseCase() {
        mockPlaceDetailsRepository = MockPlaceDetailsRepository()

        exportChildVisitUseCase = ExportChildVisitUseCaseImpl(
            placeDetailsRepository = mockPlaceDetailsRepository,
            timeZoneMap = TimeZoneMap.forEverywhere() // TODO: MockK required
        )
    }

    init {


    }
}