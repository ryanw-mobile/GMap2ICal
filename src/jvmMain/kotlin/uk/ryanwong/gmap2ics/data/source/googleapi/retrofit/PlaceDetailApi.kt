/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.retrofit

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl("https://maps.googleapis.com")
    .build()

object PlacesDetailApi {
    val retrofitService: PlaceDetailService by lazy {
        retrofit.create(PlaceDetailService::class.java)
    }
}