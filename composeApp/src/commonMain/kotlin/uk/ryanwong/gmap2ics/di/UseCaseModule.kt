/*
 * Copyright (c) 2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.di

import org.koin.dsl.module
import uk.ryanwong.gmap2ics.domain.usecases.GetActivitySegmentVEventUseCase
import uk.ryanwong.gmap2ics.domain.usecases.GetOutputFilenameUseCase
import uk.ryanwong.gmap2ics.domain.usecases.GetPlaceVisitVEventUseCase
import uk.ryanwong.gmap2ics.domain.usecases.VEventFromActivitySegmentUseCase
import uk.ryanwong.gmap2ics.usecases.GetActivitySegmentVEventUseCaseImpl
import uk.ryanwong.gmap2ics.usecases.GetOutputFilenameUseCaseImpl
import uk.ryanwong.gmap2ics.usecases.GetPlaceVisitVEventUseCaseImpl
import uk.ryanwong.gmap2ics.usecases.VEventFromActivitySegmentUseCaseImpl
import uk.ryanwong.gmap2ics.usecases.VEventFromChildVisitUseCaseImpl
import uk.ryanwong.gmap2ics.usecases.VEventFromPlaceVisitUseCaseImpl

val useCaseModule = module {
    factory<GetPlaceVisitVEventUseCase> {
        GetPlaceVisitVEventUseCaseImpl(
            vEventFromChildVisitUseCase = VEventFromChildVisitUseCaseImpl(placeDetailsRepository = get()),
            vEventFromPlaceVisitUseCase = VEventFromPlaceVisitUseCaseImpl(placeDetailsRepository = get()),
        )
    }

    factory<GetOutputFilenameUseCase> {
        GetOutputFilenameUseCaseImpl()
    }

    factory<VEventFromActivitySegmentUseCase> {
        VEventFromActivitySegmentUseCaseImpl(placeDetailsRepository = get())
    }

    factory<GetActivitySegmentVEventUseCase> {
        GetActivitySegmentVEventUseCaseImpl(vEventFromActivitySegmentUseCase = get())
    }
}
