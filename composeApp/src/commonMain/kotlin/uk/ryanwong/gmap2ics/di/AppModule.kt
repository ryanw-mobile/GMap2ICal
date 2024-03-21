/*
 * Copyright (c) 2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.di

import org.koin.dsl.module
import uk.ryanwong.gmap2ics.app.utils.timezonemap.TimeZoneMapImpl
import uk.ryanwong.gmap2ics.app.utils.timezonemap.TimeZoneMapWrapper
import us.dustinj.timezonemap.TimeZoneMap

val appModule = module {
    single<TimeZoneMapWrapper> {
        TimeZoneMapImpl(timeZoneMap = TimeZoneMap.forEverywhere())
    }
}
