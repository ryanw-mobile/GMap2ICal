/*
 * Copyright (c) 2022-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.local

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import uk.ryanwong.gmap2ics.data.datasources.local.interfaces.LocalDataSource
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

internal class LocalDataSourceImplTest {

    private lateinit var testScope: TestScope
    private lateinit var localDataSource: LocalDataSource
    private lateinit var tempDir: File
    private lateinit var tempFileList: List<File>

    @JvmField
    @Rule
    var tempFolder: TemporaryFolder = TemporaryFolder()

    @BeforeTest
    fun setup() {
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
    fun `returns correct file list when file path exists`() = testScope.runTest {
        val absolutePath = tempDir.absolutePath
        val extension = "json"
        val expectedFileList = tempFileList.mapNotNull { file ->
            if (file.absolutePath.endsWith(".json")) {
                file.absolutePath
            } else {
                null
            }
        }

        val actualFileList = localDataSource.getFileList(
            absolutePath = absolutePath,
            extension = extension,
        )

        assertTrue(actualFileList.isSuccess)
        assertEquals(expectedFileList.sorted(), actualFileList.getOrNull()?.sorted())
    }

    @Test
    fun `returns empty list when no files matching the extension`() = testScope.runTest {
        val absolutePath = tempDir.absolutePath
        val extension = "some-strange-extension"

        val actualFileList = localDataSource.getFileList(
            absolutePath = absolutePath,
            extension = extension,
        )

        assertTrue(actualFileList.isSuccess)
        assertEquals(emptyList(), actualFileList.getOrNull())
    }

    @Test
    fun `returns failure when file path is invalid`() = testScope.runTest {
        val absolutePath = tempDir.absolutePath + "/some-invalid-file-path"
        val extension = ".json"

        val actualFileList = localDataSource.getFileList(
            absolutePath = absolutePath,
            extension = extension,
        )

        assertTrue(actualFileList.isFailure)
        assertIs<java.nio.file.NoSuchFileException>(actualFileList.exceptionOrNull())
    }

    @Test
    fun `returns correct file contents when file exists`() = testScope.runTest {
        val absolutePath = tempDir.absolutePath + "/some-temp-file-5"
        val actualFileContents = localDataSource.readStringFromFile(filePath = absolutePath)
        assertEquals("some-file-contents", actualFileContents)
    }

    @Test
    fun `readStringFromFile should throw FileNotFoundException if the file does not exist`() = testScope.runTest {
        val absolutePath = tempDir.absolutePath + "/some-invalid-file"

        val exception = assertFailsWith<FileNotFoundException> {
            localDataSource.readStringFromFile(filePath = absolutePath)
        }
        assertTrue(exception.message!!.endsWith("(No such file or directory)"))
    }

    @Test
    fun `writes contents correctly to file path`() = testScope.runTest {
        val absolutePath = tempDir.absolutePath + "/some-file-writer-path"
        val expectedContents = "some-contents\\nsome-more-contents"

        val result = localDataSource.fileWriter(
            filePath = absolutePath,
            contents = expectedContents,
        )
        val actualFileContents = localDataSource.readStringFromFile(absolutePath)

        assertTrue(result.isSuccess)
        assertEquals(expectedContents, actualFileContents)
    }

    @Test
    fun `returns failure when given file path is not writable`() = testScope.runTest {
        val absolutePath = tempDir.absolutePath

        val result = localDataSource.fileWriter(
            filePath = absolutePath,
            contents = "some-contents",
        )

        assertTrue(result.isFailure)
        assertIs<FileNotFoundException>(result.exceptionOrNull())
        assertTrue(result.exceptionOrNull()!!.message!!.endsWith("(Is a directory)"))
    }
}
