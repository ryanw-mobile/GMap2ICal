/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.local

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.types.beInstanceOf
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import uk.ryanwong.gmap2ics.data.datasources.local.interfaces.LocalDataSource

class LocalDataSourceImplTest : FreeSpec() {

    private lateinit var scope: TestScope
    private lateinit var localDataSource: LocalDataSource
    private lateinit var tempDir: File
    private lateinit var tempFileList: List<File>

    init {
        beforeTest {
            // Simulate a virtual environment having some files
            tempDir = tempdir(prefix = "some-temp-dir", suffix = "some-temp-dir")
            tempFileList = listOf(
                File(tempDir.absolutePath + "/some-temp-file-1.json").apply { createNewFile() },
                File(tempDir.absolutePath + "/some-temp-file-2.txt").apply { createNewFile() },
                File(tempDir.absolutePath + "/some-temp-file-3.json").apply { createNewFile() },
                File(tempDir.absolutePath + "/some-temp-file-4.ics").apply { createNewFile() },
            )

            FileWriter(tempDir.absolutePath + "/some-temp-file-5", false).use { fileWriter ->
                fileWriter.write("some-file-contents")
            }

            // Set up local Data Source
            val dispatcher = StandardTestDispatcher()
            scope = TestScope(dispatcher)
            localDataSource = LocalDataSourceImpl(dispatcher = dispatcher)
        }

        "getFileList" - {
            "Should return correct file list if the file path exists" {
                scope.runTest {
                    val absolutePath = tempDir.absolutePath
                    val extension = "json"
                    val jsonFileList = tempFileList.mapNotNull { file ->
                        if (file.absolutePath.endsWith(".json")) {
                            file.absolutePath
                        } else {
                            null
                        }
                    }

                    val fileListResult = localDataSource.getFileList(
                        absolutePath = absolutePath,
                        extension = extension,
                    )

                    fileListResult.isSuccess shouldBe true
                    fileListResult.getOrNull() shouldContainExactlyInAnyOrder jsonFileList
                }
            }

            "Should return empty list if no files matching the extension" {
                scope.runTest {
                    val absolutePath = tempDir.absolutePath
                    val extension = "some-strange-extension"

                    val fileListResult = localDataSource.getFileList(
                        absolutePath = absolutePath,
                        extension = extension,
                    )

                    fileListResult.isSuccess shouldBe true
                    fileListResult.getOrNull() shouldBe emptyList()
                }
            }

            "Should return failure if the file path is invalid" {
                scope.runTest {
                    val absolutePath = tempDir.absolutePath + "/some-invalid-file-path"
                    val extension = ".json"

                    val fileListResult = localDataSource.getFileList(
                        absolutePath = absolutePath,
                        extension = extension,
                    )

                    fileListResult.isFailure shouldBe true
                    fileListResult.exceptionOrNull() should beInstanceOf<java.nio.file.NoSuchFileException>()
                }
            }
        }

        "readStringFromFile" - {
            "Should return correct file contents if the file exists" {
                scope.runTest {
                    val absolutePath = tempDir.absolutePath + "/some-temp-file-5"
                    val fileContents = localDataSource.readStringFromFile(filePath = absolutePath)
                    fileContents shouldBe "some-file-contents"
                }
            }

            "Should throw FileNotFoundException if the file does not exist" {
                scope.runTest {
                    val absolutePath = tempDir.absolutePath + "/some-invalid-file-path"
                    val exception = shouldThrow<FileNotFoundException> { localDataSource.readStringFromFile(filePath = absolutePath) }
                    exception.message shouldEndWith "(No such file or directory)"
                }
            }
        }

        "fileWriter" - {
            "Should correctly write the contents to the specified filepath" {
                scope.runTest {
                    val absolutePath = tempDir.absolutePath + "/some-file-writer-path"
                    val contents = "some-contents\\nsome-more-contents"

                    val result = localDataSource.fileWriter(
                        filePath = absolutePath,
                        contents = contents,
                    )

                    result.isSuccess shouldBe true
                    val fileContents = localDataSource.readStringFromFile(absolutePath)
                    fileContents shouldBe contents
                }
            }

            "Should return Failure if the given file path is not writable" {
                scope.runTest {
                    val absolutePath = tempDir.absolutePath

                    val result = localDataSource.fileWriter(
                        filePath = absolutePath,
                        contents = "some-contents",
                    )

                    result.isFailure shouldBe true
                    result.exceptionOrNull() should beInstanceOf<FileNotFoundException>()
                    result.exceptionOrNull()!!.message shouldEndWith "(Is a directory)"
                }
            }
        }
    }
}
