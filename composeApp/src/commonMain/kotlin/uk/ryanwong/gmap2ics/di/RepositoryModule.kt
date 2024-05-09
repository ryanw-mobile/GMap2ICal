/*
 * Copyright (c) 2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.di

import kotlinx.serialization.json.Json
import org.koin.dsl.module
import uk.ryanwong.gmap2ics.app.configs.ProvideConfig
import uk.ryanwong.gmap2ics.data.repositories.LocalFileRepositoryImpl
import uk.ryanwong.gmap2ics.data.repositories.PlaceDetailsRepositoryImpl
import uk.ryanwong.gmap2ics.data.repositories.TimelineRepositoryImpl
import uk.ryanwong.gmap2ics.domain.repositories.LocalFileRepository
import uk.ryanwong.gmap2ics.domain.repositories.PlaceDetailsRepository
import uk.ryanwong.gmap2ics.domain.repositories.TimelineRepository

val repositoryModule = module {
    single<PlaceDetailsRepository> {
        PlaceDetailsRepositoryImpl(
            networkDataSource = get(),
            placesApiKey = ProvideConfig.getConfig().placesApiKey,
            apiLanguageOverride = ProvideConfig.getConfig().apiLanguageOverride,
        )
    }

    single<LocalFileRepository> {
        LocalFileRepositoryImpl()
    }

    single<TimelineRepository> {
        TimelineRepositoryImpl(
            timeZoneMap = get(),
            kotlinJson = Json { ignoreUnknownKeys = true },
        )
    }
}
