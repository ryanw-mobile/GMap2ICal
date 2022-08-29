/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.local.impl

import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import uk.ryanwong.gmap2ics.data.source.local.LocalDataSource
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class LocalDataSourceImplTest : FreeSpec() {

    private lateinit var scope: TestScope
    private lateinit var localDataSource: LocalDataSource
    private lateinit var tempDir: File
    private lateinit var tempFileList: List<File>

    /**
     * Simulate a virtual environment having some files
     */
    override suspend fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        tempDir = tempdir(prefix = "some-temp-dir", suffix = "some-temp-dir")
        tempFileList = listOf(
            File(tempDir.absolutePath + "/some-temp-file-1.json").apply { createNewFile() },
            File(tempDir.absolutePath + "/some-temp-file-2.txt").apply { createNewFile() },
            File(tempDir.absolutePath + "/some-temp-file-3.json").apply { createNewFile() },
            File(tempDir.absolutePath + "/some-temp-file-4.ics").apply { createNewFile() },
        )
    }

    private fun setupLocalDataSource() {
        val dispatcher = StandardTestDispatcher()
        scope = TestScope(dispatcher)
        localDataSource = LocalDataSourceImpl(dispatcher = dispatcher)
    }

    init {
        "getFileList" - {
            "Should return correct file list if the file path exists" {
                setupLocalDataSource()
                scope.runTest {
                    // 🔴 Given
                    val absolutePath = tempDir.absolutePath
                    val extension = "json"
                    val jsonFileList = tempFileList.mapNotNull { file ->
                        if (file.absolutePath.endsWith(".json")) {
                            file.absolutePath
                        } else {
                            null
                        }
                    }

                    // 🟡 When
                    val fileListResult = localDataSource.getFileList(
                        absolutePath = absolutePath,
                        extension = extension,
                    )

                    // 🟢 Then
                    fileListResult.isSuccess shouldBe true
                    fileListResult.getOrNull() shouldContainExactlyInAnyOrder jsonFileList
                }
            }

            "Should return empty list if no file matching the extension" {
                setupLocalDataSource()
                scope.runTest {
                    // 🔴 Given
                    val absolutePath = tempDir.absolutePath
                    val extension = "some-strange-extension"

                    // 🟡 When
                    val fileListResult = localDataSource.getFileList(
                        absolutePath = absolutePath,
                        extension = extension,
                    )

                    // 🟢 Then
                    fileListResult.isSuccess shouldBe true
                    fileListResult.getOrNull() shouldBe emptyList()
                }
            }

            "Should return failure if the file path is invalid" {
                setupLocalDataSource()
                scope.runTest {
                    // 🔴 Given
                    val absolutePath = tempDir.absolutePath + "/some-invalid-file-path"
                    val extension = ".json"

                    // 🟡 When
                    val fileListResult = localDataSource.getFileList(
                        absolutePath = absolutePath,
                        extension = extension,
                    )

                    // 🟢 Then
                    fileListResult.isFailure shouldBe true
                    fileListResult.exceptionOrNull() should beInstanceOf<java.nio.file.NoSuchFileException>()
                }
            }
        }

        "readStringFromFile" {
        }

        "fileWriter" {
        }
    }
}