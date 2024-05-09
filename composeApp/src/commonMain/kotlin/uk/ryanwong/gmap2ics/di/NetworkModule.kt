/*
 * Copyright (c) 2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.di

import io.ktor.client.engine.cio.CIO
import org.koin.dsl.module
import uk.ryanwong.gmap2ics.data.datasources.googleapi.KtorGoogleApiDataSource
import uk.ryanwong.gmap2ics.data.datasources.googleapi.interfaces.GoogleApiDataSource

val networkModule = module {
    single { CIO.create() }
    single<GoogleApiDataSource> { KtorGoogleApiDataSource(engine = get()) }
}
