/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.repositories.impl

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.serialization.json.Json
import uk.ryanwong.gmap2ics.app.utils.timezonemap.fakes.FakeTimeZoneMap
import uk.ryanwong.gmap2ics.data.datasources.local.fakes.FakeLocalDataSource
import uk.ryanwong.gmap2ics.data.repositories.TimelineRepositoryImpl
import uk.ryanwong.gmap2ics.data.repositories.impl.TimelineRepositoryImplTestData.JSON_STRING
import uk.ryanwong.gmap2ics.data.repositories.impl.TimelineRepositoryImplTestData.timeLineFromJsonString

@OptIn(ExperimentalCoroutinesApi::class)
internal class TimelineRepositoryImplTest : FreeSpec() {

    private lateinit var timelineRepository: TimelineRepositoryImpl
    private lateinit var localDataSource: FakeLocalDataSource
    private lateinit var fakeTimeZoneMap: FakeTimeZoneMap
    private val kotlinJson = Json { ignoreUnknownKeys = true }

    init {
        beforeTest {
            localDataSource = FakeLocalDataSource()
            fakeTimeZoneMap = FakeTimeZoneMap()

            timelineRepository = TimelineRepositoryImpl(
                localDataSource = localDataSource,
                timeZoneMap = fakeTimeZoneMap,
                dispatcher = UnconfinedTestDispatcher(),
                kotlinJson = kotlinJson,
            )
        }

        "getTimeLine" - {
            "Should return correct TimeLine object if the localDataSource returns valid JSON String" {
                localDataSource.getJsonStringResponse = JSON_STRING
                fakeTimeZoneMap.zoneId = "Asia/Tokyo"

                val timeLine = timelineRepository.getTimeLine(filePath = "/some-absolute-path/")

                timeLine.isSuccess shouldBe true
                timeLine.getOrNull() shouldBe timeLineFromJsonString
            }

            "Should return Result.Failure if the localDataSource returns invalid JSON String" {
                // Alternatively can Mock json.decodeFromString and throw an exception
                localDataSource.getJsonStringResponse = "some-invalid-json-string"
                fakeTimeZoneMap.zoneId = "Asia/Tokyo"

                val timeLine = timelineRepository.getTimeLine(filePath = "/some-absolute-path/")

                // kotlinx.serialization.json.internal.JsonDecodingException is internal. Assert message only.
                timeLine.isFailure shouldBe true
                timeLine.exceptionOrNull()!!.message shouldBe "Unexpected JSON token at offset 0: Expected start of the object '{', but had 's' instead at path: \$\nJSON input: some-invalid-json-string"
            }
        }
    }
}
