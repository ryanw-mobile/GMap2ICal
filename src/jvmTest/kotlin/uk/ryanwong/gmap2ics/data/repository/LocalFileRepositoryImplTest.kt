/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.LatLng
import uk.ryanwong.gmap2ics.data.repository.impl.LocalFileRepositoryImpl
import uk.ryanwong.gmap2ics.data.source.local.mocks.MockLocalDataSource
import java.io.IOException

internal class LocalFileRepositoryImplTest : FreeSpec() {

    private val someDegreesLatitude = 26.3383300
    private val someDegreesLongitude = 127.8000000

    private lateinit var localFileRepository: LocalFileRepositoryImpl
    private lateinit var localDataSource: MockLocalDataSource

    private fun setupRepository() {
        localDataSource = MockLocalDataSource()

        localFileRepository = LocalFileRepositoryImpl(
            localDataSource = localDataSource
        )
    }

    init {
        "getFileList" - {
            "Should return a list of filenames if datasource request success" {
                // 🔴 Given
                setupRepository()
                val response = Result.success(
                    listOf(
                        "/some-absolute-path/some-file-1",
                        "/some-absolute-path/some-file-2",
                        "/some-absolute-path/some-file-3"
                    )
                )
                localDataSource.getFileListResponse = response

                // 🟡 When
                val fileList = localFileRepository.getFileList(
                    absolutePath = "/some-absolute-path/",
                    extension = "some-extension"
                )

                // 🟢 Then
                fileList shouldBe response
            }

            "Should return failure if datasource returns error" {
                // 🔴 Given
                setupRepository()
                val response: Result<List<String>> = Result.failure(Exception("some-data-source-exception"))
                localDataSource.getFileListResponse = response

                // 🟡 When
                val fileList = localFileRepository.getFileList(
                    absolutePath = "/some-absolute-path/",
                    extension = "some-extension"
                )

                // 🟢 Then
                fileList shouldBe response
            }
        }

        "exportICal" - {
            "should export iCal with correct filename and contents" {
                // 🔴 Given
                setupRepository()
                localDataSource.fileWriterResponse = Result.success(Unit)
                val vEventList = listOf(
                    VEvent(
                        uid = "2011-11-11T11:22:22.222Z",
                        placeId = "place-id-to-be-kept",
                        dtStamp = "2011-11-11T11:22:22.222Z",
                        organizer = null,
                        dtStart = RawTimestamp(timestamp = "2011-11-11T20:11:11.111Z", timezoneId = "Asia/Tokyo"),
                        dtEnd = RawTimestamp(timestamp = "2011-11-11T20:22:22.222Z", timezoneId = "Asia/Tokyo"),
                        summary = "\uD83C\uDFE7 some-place-name",
                        location = "some-formatted-address",
                        geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                        description = "Place ID:\\nplace-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://some.url/",
                        url = "https://some.url/",
                        lastModified = "2011-11-11T11:22:22.222Z"
                    ),
                    VEvent(
                        uid = "2011-11-11T11:22:22.222Z",
                        placeId = "some-place-id",
                        dtStamp = "2011-11-11T11:22:22.222Z",
                        organizer = null,
                        dtStart = RawTimestamp(timestamp = "2011-11-11T20:11:11.111Z", timezoneId = "Asia/Tokyo"),
                        dtEnd = RawTimestamp(timestamp = "2011-11-11T20:22:22.222Z", timezoneId = "Asia/Tokyo"),
                        summary = "📍 null",
                        location = "",
                        geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                        description = "Place ID:\\nsome-place-id\\n\\nGoogle Maps URL:\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id",
                        url = "https://www.google.com/maps/place/?q=place_id:some-place-id",
                        lastModified = "2011-11-11T11:22:22.222Z"
                    )
                )

                // 🟡 When
                val exportICalResponse = localFileRepository.exportICal(
                    filename = "some-file-name",
                    vEvents = vEventList
                )

                // 🟢 Then
                exportICalResponse.isSuccess shouldBe true
                localDataSource.fileWriterFileName shouldBe "some-file-name"
                localDataSource.fileWriterContents shouldBe "BEGIN:VCALENDAR\n" +
                        "VERSION:2.0\n" +
                        "BEGIN:VEVENT\n" +
                        "TRANSP:OPAQUE\n" +
                        "DTSTART;TZID=Asia/Tokyo:20111112T051111\n" +
                        "DTEND;TZID=Asia/Tokyo:20111112T052222\n" +
                        "X-APPLE-STRUCTURED-LOCATION;VALUE=URI;X-APPLE-RADIUS=147;\n" +
                        "X-TITLE=\"some-formatted-address\":geo:26.33833,127.8\n" +
                        "UID:2011-11-11T11:22:22.222Z\n" +
                        "DTSTAMP:2011-11-11T11:22:22.222Z\n" +
                        "LOCATION:some-formatted-address\n" +
                        "SUMMARY:\uD83C\uDFE7 some-place-name\n" +
                        "DESCRIPTION:Place ID:\\nplace-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://some.url/\n" +
                        "URL;VALUE=URI:https://some.url/\n" +
                        "STATUS:CONFIRMED\n" +
                        "SEQUENCE:1\n" +
                        "LAST-MODIFIED:2011-11-11T11:22:22.222Z\n" +
                        "CREATED:2011-11-11T11:22:22.222Z\n" +
                        "X-APPLE-TRAVEL-ADVISORY-BEHAVIOR:AUTOMATIC\n" +
                        "END:VEVENT\n" +
                        "BEGIN:VEVENT\n" +
                        "TRANSP:OPAQUE\n" +
                        "DTSTART;TZID=Asia/Tokyo:20111112T051111\n" +
                        "DTEND;TZID=Asia/Tokyo:20111112T052222\n" +
                        "X-APPLE-STRUCTURED-LOCATION;VALUE=URI;X-APPLE-RADIUS=147;\n" +
                        "X-TITLE=\"26.33833 127.8\":geo:26.33833,127.8\n" +
                        "UID:2011-11-11T11:22:22.222Z\n" +
                        "DTSTAMP:2011-11-11T11:22:22.222Z\n" +
                        "LOCATION:\n" +
                        "SUMMARY:\uD83D\uDCCD null\n" +
                        "DESCRIPTION:Place ID:\\nsome-place-id\\n\\nGoogle Maps URL:\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\n" +
                        "URL;VALUE=URI:https://www.google.com/maps/place/?q=place_id:some-place-id\n" +
                        "STATUS:CONFIRMED\n" +
                        "SEQUENCE:1\n" +
                        "LAST-MODIFIED:2011-11-11T11:22:22.222Z\n" +
                        "CREATED:2011-11-11T11:22:22.222Z\n" +
                        "X-APPLE-TRAVEL-ADVISORY-BEHAVIOR:AUTOMATIC\n" +
                        "END:VEVENT\n" +
                        "END:VCALENDAR\n"
            }

            "should return Result.failure if data source return error" {
                // 🔴 Given
                setupRepository()
                localDataSource.fileWriterResponse = Result.failure(exception = IOException())
                val vEventList = listOf(
                    VEvent(
                        uid = "2011-11-11T11:22:22.222Z",
                        placeId = "place-id-to-be-kept",
                        dtStamp = "2011-11-11T11:22:22.222Z",
                        organizer = null,
                        dtStart = RawTimestamp(timestamp = "2011-11-11T20:11:11.111Z", timezoneId = "Asia/Tokyo"),
                        dtEnd = RawTimestamp(timestamp = "2011-11-11T20:22:22.222Z", timezoneId = "Asia/Tokyo"),
                        summary = "\uD83C\uDFE7 some-place-name",
                        location = "some-formatted-address",
                        geo = LatLng(latitude = someDegreesLatitude, longitude = someDegreesLongitude),
                        description = "Place ID:\\nplace-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://some.url/",
                        url = "https://some.url/",
                        lastModified = "2011-11-11T11:22:22.222Z"
                    )
                )

                // 🟡 When
                val exportICalResponse = localFileRepository.exportICal(
                    filename = "some-file-name",
                    vEvents = vEventList
                )

                // 🟢 Then
                exportICalResponse.isFailure shouldBe true
            }
        }
    }
}