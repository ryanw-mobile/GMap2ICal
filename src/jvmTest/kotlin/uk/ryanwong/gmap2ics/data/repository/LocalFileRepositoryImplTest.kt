/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import uk.ryanwong.gmap2ics.data.source.local.MockLocalDataSource

@OptIn(ExperimentalCoroutinesApi::class)
internal class LocalFileRepositoryImplTest : FreeSpec() {

    private lateinit var localFileRepository: LocalFileRepositoryImpl
    private lateinit var localDataSource: MockLocalDataSource

    private fun setupRepository() {
        localDataSource = MockLocalDataSource()

        localFileRepository = LocalFileRepositoryImpl(
            localDataSource = localDataSource,
            dispatcher = StandardTestDispatcher()
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

        }
    }
}