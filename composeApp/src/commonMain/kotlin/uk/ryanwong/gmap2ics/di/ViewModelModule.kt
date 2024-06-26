/*
 * Copyright (c) 2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.di

import org.koin.dsl.module
import uk.ryanwong.gmap2ics.app.configs.ProvideConfig
import uk.ryanwong.gmap2ics.ui.viewmodels.MainScreenViewModel

val viewModelModule = module {
    single {
        MainScreenViewModel(
            configFile = ProvideConfig.getConfig(),
            timelineRepository = get(),
            localFileRepository = get(),
            getActivitySegmentVEventUseCase = get(),
            getOutputFilenameUseCase = get(),
            getPlaceVisitVEventUseCase = get(),
        )
    }
}
