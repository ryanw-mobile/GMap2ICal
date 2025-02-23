/*
 * Copyright (c) 2022-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.repositories

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import uk.ryanwong.gmap2ics.data.datasources.local.fakes.FakeGoogleApiDataSource
import uk.ryanwong.gmap2ics.data.models.places.GeometryDto
import uk.ryanwong.gmap2ics.data.models.places.LocationDto
import uk.ryanwong.gmap2ics.data.models.places.PlaceDetailsDto
import uk.ryanwong.gmap2ics.data.models.places.ResultDto
import uk.ryanwong.gmap2ics.domain.models.timeline.LatLng
import uk.ryanwong.gmap2ics.domain.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.domain.repositories.PlaceDetailsRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class PlaceDetailsRepositoryImplTest {

    private lateinit var placeDetailsRepository: PlaceDetailsRepository
    private lateinit var fakeGoogleApiDataSource: FakeGoogleApiDataSource

    private fun setupRepository(apiLanguageOverride: Map<String, String> = mapOf()) {
        fakeGoogleApiDataSource = FakeGoogleApiDataSource()
        placeDetailsRepository = PlaceDetailsRepositoryImpl(
            networkDataSource = fakeGoogleApiDataSource,
            placesApiKey = "some-api-key",
            apiLanguageOverride = apiLanguageOverride,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    // 🗂️ getPlaceDetails
    // ➡️ enablePlacesApiLookup is false
    @Test
    fun `returns PlaceDetailsNotFoundException when enablePlacesApiLookup is false and placeId is not cached`() = runTest {
        setupRepository()
        val placeId = "some-place-id"
        val enablePlacesApiLookup = false

        val placeDetails = placeDetailsRepository.getPlaceDetails(
            placeId = placeId,
            placeTimeZoneId = "Asia/Tokyo",
            enablePlacesApiLookup = enablePlacesApiLookup,
        )

        assertTrue(placeDetails.isFailure)
        assertIs<PlaceDetailsNotFoundException>(placeDetails.exceptionOrNull())
    }

    @Test
    fun `returns cached PlaceDetails when enablePlacesApiLookup is false and placeId is cached`() = runTest {
        setupRepository()
        val placeId = "some-place-id"
        val enablePlacesApiLookup = false

        // cache the PlaceDetails first
        val apiResponse = PlaceDetailsDto(
            ResultDto(
                placeId = "some-place-id",
                name = "some-name",
                formattedAddress = "some-formatted-address",
                geometry = GeometryDto(LocationDto(lat = 53.6152405, lng = -1.5639315)),
                types = listOf(
                    "some-unknown-type",
                ),
                url = "https://maps.google.com/?cid=1021876599690425051",
            ),
        )
        val expectedPlaceDetails = PlaceDetails(
            placeId = "some-place-id",
            name = "some-name",
            formattedAddress = "some-formatted-address",
            geo = LatLng(latitude = 53.6152405, longitude = -1.5639315),
            types = listOf("some-unknown-type"),
            url = "https://maps.google.com/?cid=1021876599690425051",
        )
        fakeGoogleApiDataSource.getMapsApiPlaceDetailsResponse = apiResponse
        placeDetailsRepository.getPlaceDetails(
            placeId = placeId,
            placeTimeZoneId = "Asia/Tokyo",
            enablePlacesApiLookup = true,
        )
        fakeGoogleApiDataSource.getMapsApiPlaceDetailsResponse = null

        val placeDetails = placeDetailsRepository.getPlaceDetails(
            placeId = placeId,
            placeTimeZoneId = "Asia/Tokyo",
            enablePlacesApiLookup = enablePlacesApiLookup,
        )

        assertTrue(placeDetails.isSuccess)
        assertEquals(expectedPlaceDetails, placeDetails.getOrNull())
    }

    // ➡️ enablePlacesApiLookup is true
    @Test
    fun `returns correct PlaceDetails when enablePlacesApiLookup is true and data source returns data`() = runTest {
        setupRepository()
        val apiResponse = PlaceDetailsDto(
            ResultDto(
                placeId = "some-place-id",
                name = "some-name",
                formattedAddress = "some-formatted-address",
                geometry = GeometryDto(LocationDto(lat = 53.6152405, lng = -1.5639315)),
                types = listOf(
                    "some-unknown-type",
                ),
                url = "https://maps.google.com/?cid=1021876599690425051",
            ),
        )
        val expectedPlaceDetails = PlaceDetails(
            placeId = "some-place-id",
            name = "some-name",
            formattedAddress = "some-formatted-address",
            geo = LatLng(latitude = 53.6152405, longitude = -1.5639315),
            types = listOf("some-unknown-type"),
            url = "https://maps.google.com/?cid=1021876599690425051",
        )
        fakeGoogleApiDataSource.getMapsApiPlaceDetailsResponse = apiResponse

        val placeDetails = placeDetailsRepository.getPlaceDetails(
            placeId = "some-place-id",
            placeTimeZoneId = "Asia/Tokyo",
            enablePlacesApiLookup = true,
        )

        assertTrue(placeDetails.isSuccess)
        assertEquals(expectedPlaceDetails, placeDetails.getOrNull())
    }

    @Test
    fun `returns PlaceDetailsNotFoundException when enablePlacesApiLookup is true and data source returns not found and placeId is not cached`() = runTest {
        setupRepository()
        val placeId = "some-place-id"
        val enablePlacesApiLookup = true
        fakeGoogleApiDataSource.fakeException = PlaceDetailsNotFoundException(placeId = placeId)

        val placeDetails = placeDetailsRepository.getPlaceDetails(
            placeId = placeId,
            placeTimeZoneId = "Asia/Tokyo",
            enablePlacesApiLookup = enablePlacesApiLookup,
        )

        assertTrue(placeDetails.isFailure)
        assertIs<PlaceDetailsNotFoundException>(placeDetails.exceptionOrNull())
        assertEquals("⛔️ placeId some-place-id not found", placeDetails.exceptionOrNull()!!.message)
    }

    @Test
    fun `queries data source with overridden language code when apiLanguageOverride has timezone-specific language`() = runTest {
        setupRepository(
            apiLanguageOverride = mapOf(
                Pair("Asia/Tokyo", "ja"),
                Pair("default", "some-language"),
            ),
        )
        val placeId = "some-place-id"
        val placeTimeZoneId = "Asia/Tokyo"
        val enablePlacesApiLookup = true

        placeDetailsRepository.getPlaceDetails(
            placeId = placeId,
            placeTimeZoneId = placeTimeZoneId,
            enablePlacesApiLookup = enablePlacesApiLookup,
        )

        assertEquals("ja", fakeGoogleApiDataSource.getMapsApiPlaceDetailsLanguageRequested)
    }

    @Test
    fun `queries data source with default language code when apiLanguageOverride has default language`() = runTest {
        setupRepository(
            apiLanguageOverride = mapOf(
                Pair("Asia/Tokyo", "ja"),
                Pair("default", "some-language"),
            ),
        )
        val placeId = "some-place-id"
        val placeTimeZoneId = "some-place-timezone-id"
        val enablePlacesApiLookup = true

        placeDetailsRepository.getPlaceDetails(
            placeId = placeId,
            placeTimeZoneId = placeTimeZoneId,
            enablePlacesApiLookup = enablePlacesApiLookup,
        )

        assertEquals("some-language", fakeGoogleApiDataSource.getMapsApiPlaceDetailsLanguageRequested)
    }

    @Test
    fun `queries data source without language code when apiLanguageOverride is empty`() = runTest {
        setupRepository(
            apiLanguageOverride = mapOf(),
        )
        val placeId = "some-place-id"
        val placeTimeZoneId = "some-place-timezone-id"
        val enablePlacesApiLookup = true

        placeDetailsRepository.getPlaceDetails(
            placeId = placeId,
            placeTimeZoneId = placeTimeZoneId,
            enablePlacesApiLookup = enablePlacesApiLookup,
        )

        assertEquals(null, fakeGoogleApiDataSource.getMapsApiPlaceDetailsLanguageRequested)
    }
}
