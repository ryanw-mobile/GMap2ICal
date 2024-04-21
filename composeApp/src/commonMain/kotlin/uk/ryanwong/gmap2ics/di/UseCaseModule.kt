/*
 * Copyright (c) 2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.di

import org.koin.dsl.module
import uk.ryanwong.gmap2ics.domain.usecases.GetActivitySegmentVEventUseCaseImpl
import uk.ryanwong.gmap2ics.domain.usecases.GetOutputFilenameUseCaseImpl
import uk.ryanwong.gmap2ics.domain.usecases.GetPlaceVisitVEventUseCaseImpl
import uk.ryanwong.gmap2ics.domain.usecases.VEventFromActivitySegmentUseCaseImpl
import uk.ryanwong.gmap2ics.domain.usecases.VEventFromChildVisitUseCaseImpl
import uk.ryanwong.gmap2ics.domain.usecases.VEventFromPlaceVisitUseCaseImpl
import uk.ryanwong.gmap2ics.domain.usecases.interfaces.GetActivitySegmentVEventUseCase
import uk.ryanwong.gmap2ics.domain.usecases.interfaces.GetOutputFilenameUseCase
import uk.ryanwong.gmap2ics.domain.usecases.interfaces.GetPlaceVisitVEventUseCase
import uk.ryanwong.gmap2ics.domain.usecases.interfaces.VEventFromActivitySegmentUseCase

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
