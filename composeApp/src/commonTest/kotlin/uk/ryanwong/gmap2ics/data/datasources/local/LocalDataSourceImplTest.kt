/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.local

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.types.beInstanceOf
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import uk.ryanwong.gmap2ics.data.datasources.local.interfaces.LocalDataSource

internal class LocalDataSourceImplTest {

    private lateinit var testScope: TestScope
    private lateinit var localDataSource: LocalDataSource
    private lateinit var tempDir: File
    private lateinit var tempFileList: List<File>

    @JvmField
    @Rule
    var tempFolder: TemporaryFolder = TemporaryFolder()

    @BeforeTest
    fun setUp() {
        // Simulate a virtual environment having some files
        tempDir = tempFolder.newFolder("some-temp-dir")
        tempFileList = listOf(
            File(tempDir.absolutePath + "/some-temp-file-1.json").apply { createNewFile() },
            File(tempDir.absolutePath + "/some-temp-file-2.txt").apply { createNewFile() },
            File(tempDir.absolutePath + "/some-temp-file-3.json").apply { createNewFile() },
            File(tempDir.absolutePath + "/some-temp-file-4.ics").apply { createNewFile() },
        )

        FileWriter(tempDir.absolutePath + "/some-temp-file-5", false).use { fileWriter ->
            fileWriter.write("some-file-contents")
        }

        val dispatcher = StandardTestDispatcher()
        testScope = TestScope(dispatcher)
        localDataSource = LocalDataSourceImpl(dispatcher = dispatcher)
    }

    @Test
    fun `getFileList should return correct file list if the file path exists`() = testScope.runTest {
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

    @Test
    fun `getFileList should return empty list if no files matching the extension`() = testScope.runTest {
        val absolutePath = tempDir.absolutePath
        val extension = "some-strange-extension"

        val fileListResult = localDataSource.getFileList(
            absolutePath = absolutePath,
            extension = extension,
        )

        fileListResult.isSuccess shouldBe true
        fileListResult.getOrNull() shouldBe emptyList()
    }

    @Test
    fun `getFileList should return failure if the file path is invalid`() = testScope.runTest {
        val absolutePath = tempDir.absolutePath + "/some-invalid-file-path"
        val extension = ".json"

        val fileListResult = localDataSource.getFileList(
            absolutePath = absolutePath,
            extension = extension,
        )

        fileListResult.isFailure shouldBe true
        fileListResult.exceptionOrNull() should beInstanceOf<java.nio.file.NoSuchFileException>()
    }

    @Test
    fun `readStringFromFile should return correct file contents if the file exists`() = testScope.runTest {
        val absolutePath = tempDir.absolutePath + "/some-temp-file-5"
        val fileContents = localDataSource.readStringFromFile(filePath = absolutePath)
        fileContents shouldBe "some-file-contents"
    }

    @Test
    fun `readStringFromFile should throw FileNotFoundException if the file does not exist`() = testScope.runTest {
        val absolutePath = tempDir.absolutePath + "/some-invalid-file-path"
        val exception = shouldThrow<FileNotFoundException> { localDataSource.readStringFromFile(filePath = absolutePath) }
        exception.message shouldEndWith "(No such file or directory)"
    }

    @Test
    fun `fileWriter should correctly write the contents to the specified filepath`() = testScope.runTest {
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

    @Test
    fun `fileWriter should return Failure if the given file path is not writable`() = testScope.runTest {
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
