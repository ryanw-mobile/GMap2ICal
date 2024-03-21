/*
 * Copyright (c) 2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.di

import io.ktor.client.engine.cio.CIO
import org.koin.core.qualifier.named
import org.koin.dsl.module
import uk.ryanwong.gmap2ics.data.datasources.googleapi.GoogleApiDataSource
import uk.ryanwong.gmap2ics.data.datasources.googleapi.ktor.GoogleMapsApiClient
import uk.ryanwong.gmap2ics.data.datasources.googleapi.ktor.KtorGoogleApiDataSource
import uk.ryanwong.gmap2ics.data.datasources.googleapi.ktor.impl.GoogleMapsApiClientImpl
import uk.ryanwong.gmap2ics.data.datasources.googleapi.retrofit.RetrofitGoogleApiDataSource

val networkModule = module {
    single { CIO.create() }
    single<GoogleMapsApiClient> { GoogleMapsApiClientImpl(engine = get()) }
    single<GoogleApiDataSource>(named("ktor")) { KtorGoogleApiDataSource(googleMapsApiClient = get()) }
    single<GoogleApiDataSource>(named("retrofit")) { RetrofitGoogleApiDataSource() }
}
