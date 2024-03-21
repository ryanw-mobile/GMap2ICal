/*
 * Copyright (c) 2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.di

import org.koin.dsl.module
import uk.ryanwong.gmap2ics.app.configs.ProvideConfig
import uk.ryanwong.gmap2ics.ui.viewmodels.MainScreenViewModel
import java.util.Locale
import java.util.ResourceBundle

val viewModelModule = module {
    single {
        MainScreenViewModel(
            configFile = ProvideConfig.getConfig(),
            resourceBundle = ResourceBundle.getBundle("resources", Locale.ENGLISH),
            timelineRepository = get(),
            localFileRepository = get(),
            getActivitySegmentVEventUseCase = get(),
            getOutputFilenameUseCase = get(),
            getPlaceVisitVEventUseCase = get(),
        )
    }
}
