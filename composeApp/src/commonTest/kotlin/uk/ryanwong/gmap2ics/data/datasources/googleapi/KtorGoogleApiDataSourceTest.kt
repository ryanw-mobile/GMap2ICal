/*
 * Copyright (c) 2022-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.googleapi

import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import uk.ryanwong.gmap2ics.data.datasources.googleapi.KtorGoogleApiDataSourceTestData.PLACE_DETAILS_GREG_AVE_DTO
import uk.ryanwong.gmap2ics.data.datasources.googleapi.KtorGoogleApiDataSourceTestData.PLACE_DETAILS_GREG_AVE_JSON
import uk.ryanwong.gmap2ics.data.repositories.PlaceDetailsNotFoundException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class KtorGoogleApiDataSourceTest {
    private lateinit var ktorGoogleApiDataSource: KtorGoogleApiDataSource

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setupDataSource(status: HttpStatusCode, contentType: String, payload: String) {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel(payload),
                status = status,
                headers = headersOf(HttpHeaders.ContentType, contentType),
            )
        }
        ktorGoogleApiDataSource = KtorGoogleApiDataSource(
            engine = mockEngine,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `returns PlaceDetails when API request is successful`() = runTest {
        setupDataSource(
            status = HttpStatusCode.OK,
            contentType = "application/json",
            payload = PLACE_DETAILS_GREG_AVE_JSON,
        )
        val expectedPlaceDetails = PLACE_DETAILS_GREG_AVE_DTO

        val placeDetails = ktorGoogleApiDataSource.getMapsApiPlaceDetails(
            placeId = "some-place-id",
            apiKey = "some-api-key",
            language = "some-language",
        )

        assertEquals(expected = expectedPlaceDetails, actual = placeDetails)
    }

    @Test
    fun `throws PlaceDetailsNotFoundException when API request returns error message`() = runTest {
        setupDataSource(
            status = HttpStatusCode.OK,
            contentType = "application/json",
            payload = """{
                                   "error_message" : "Missing the placeid or reference parameter.",
                                   "html_attributions" : [],
                                   "status" : "INVALID_REQUEST"
                                }""",
        )

        assertFailsWith<PlaceDetailsNotFoundException>(message = "⛔\uFE0F placeId some-place-id not found") {
            ktorGoogleApiDataSource.getMapsApiPlaceDetails(
                placeId = "some-place-id",
                apiKey = "some-api-key",
                language = "some-language",
            )
        }
    }

    @Test
    fun `throws NoTransformationFoundException when API request throws an exception`() = runTest {
        setupDataSource(
            status = HttpStatusCode.InternalServerError,
            contentType = "text/plain",
            payload = "Internal Server Error",
        )

        assertFailsWith<NoTransformationFoundException> {
            ktorGoogleApiDataSource.getMapsApiPlaceDetails(
                placeId = "some-place-id",
                apiKey = "some-api-key",
                language = "some-language",
            )
        }
    }
}
