/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics

import androidx.compose.ui.window.application
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.GlobalContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import uk.ryanwong.gmap2ics.di.appModule
import uk.ryanwong.gmap2ics.di.networkModule
import uk.ryanwong.gmap2ics.di.repositoryModule
import uk.ryanwong.gmap2ics.di.useCaseModule
import uk.ryanwong.gmap2ics.di.viewModelModule
import uk.ryanwong.gmap2ics.ui.screens.mainScreen
import uk.ryanwong.gmap2ics.ui.theme.gregorygreen.GregoryGreenTheme
import uk.ryanwong.gmap2ics.ui.viewmodels.MainScreenViewModel

fun main() = application {
    Napier.base(DebugAntilog())

    startKoin {
        printLogger(Level.ERROR) // Use ERROR level to minimize log noise
        modules(appModule, networkModule, repositoryModule, useCaseModule, viewModelModule)
    }

    val koin = GlobalContext.get()
    val viewModel = getViewModel(
        key = "main-screen",
        factory = viewModelFactory {
            koin.get<MainScreenViewModel>()
        },
    )

    GregoryGreenTheme {
        mainScreen(
            onCloseRequest = { exitApplication() },
            mainScreenViewModel = viewModel,
        )
    }
}
