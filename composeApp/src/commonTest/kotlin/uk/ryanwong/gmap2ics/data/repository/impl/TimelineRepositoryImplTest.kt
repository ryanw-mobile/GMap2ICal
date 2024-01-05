/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.repository.impl

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.serialization.json.Json
import uk.ryanwong.gmap2ics.app.utils.timezonemap.mocks.MockTimeZoneMap
import uk.ryanwong.gmap2ics.data.repository.impl.TimelineRepositoryImplTestData.mockJsonString
import uk.ryanwong.gmap2ics.data.repository.impl.TimelineRepositoryImplTestData.mockTimeLineFromJsonString
import uk.ryanwong.gmap2ics.data.source.local.mocks.MockLocalDataSource

@OptIn(ExperimentalCoroutinesApi::class)
internal class TimelineRepositoryImplTest : FreeSpec() {

    private lateinit var timelineRepository: TimelineRepositoryImpl
    private lateinit var localDataSource: MockLocalDataSource
    private lateinit var mockTimeZoneMap: MockTimeZoneMap
    private val kotlinJson = Json { ignoreUnknownKeys = true }

    private fun setupRepository() {
        localDataSource = MockLocalDataSource()
        mockTimeZoneMap = MockTimeZoneMap()

        timelineRepository = TimelineRepositoryImpl(
            localDataSource = localDataSource,
            timeZoneMap = mockTimeZoneMap,
            dispatcher = UnconfinedTestDispatcher(),
            kotlinJson = kotlinJson,
        )
    }

    init {
        "getTimeLine" - {
            "Should return correct TimeLine object if the localDataSource returns valid JSON String" {
                // 🔴 Given
                setupRepository()
                localDataSource.getJsonStringResponse = mockJsonString
                mockTimeZoneMap.mockZoneId = "Asia/Tokyo"

                // 🟡 When
                val timeLine = timelineRepository.getTimeLine(filePath = "/some-absolute-path/")

                // 🟢 Then
                timeLine.isSuccess shouldBe true
                timeLine.getOrNull() shouldBe mockTimeLineFromJsonString
            }

            "Should return Result.Failure if the localDataSource returns invalid JSON String" {
                // 🔴 Given
                // Alternatively can Mock json.decodeFromString and throw an exception
                setupRepository()
                localDataSource.getJsonStringResponse = "some-invalid-json-string"
                mockTimeZoneMap.mockZoneId = "Asia/Tokyo"

                // 🟡 When
                val timeLine = timelineRepository.getTimeLine(filePath = "/some-absolute-path/")

                // 🟢 Then - kotlinx.serialization.json.internal.JsonDecodingException is internal. Assert message only.
                timeLine.isFailure shouldBe true
                timeLine.exceptionOrNull()!!.message shouldBe "Unexpected JSON token at offset 0: Expected start of the object '{', but had 's' instead at path: \$\nJSON input: some-invalid-json-string"
            }
        }
    }
}
