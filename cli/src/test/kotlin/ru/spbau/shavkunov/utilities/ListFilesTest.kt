package ru.spbau.shavkunov.utilities

import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.spbau.shavkunov.WorkingDirectory

class ListFilesTest {
    @Rule
    @JvmField
    var folder = TemporaryFolder()

    @Test
    fun noArgsTest() {
        val file1 = folder.newFile()
        val file2 = folder.newFile()
        val subfolder = folder.newFolder()

        val workingDirectory = WorkingDirectory(folder.root.toPath())

        val result = ListFiles.execute(workingDirectory, listOf(), "")

        assertThat(result.output.split(System.lineSeparator()).dropLast(1),
            containsInAnyOrder(
                file1.name, file2.name, subfolder.name
            )
        )

        assertThat(result.isExit, `is`(equalTo(false)))
    }

    @Test
    fun folderTest() {
        val subfolder = folder.newFolder()
        val folder1 = folder.newFolder(subfolder.name, "file1")
        val folder2 = folder.newFolder(subfolder.name, "file2")

        val workingDirectory = WorkingDirectory(folder.root.toPath())
        val result = ListFiles.execute(workingDirectory, listOf(subfolder.name), "")

        assertThat(result.output.split(System.lineSeparator()).dropLast(1),
            containsInAnyOrder(
                folder1.name, folder2.name
            )
        )

        assertThat(result.isExit, `is`(equalTo(false)))
    }

    @Test
    fun fileTest() {
        val file = folder.newFile()

        val workingDirectory = WorkingDirectory(folder.root.toPath())
        val result = ListFiles.execute(workingDirectory, listOf(file.name), "")

        assertThat(
            result.output.split(System.lineSeparator()).dropLast(1),
            containsInAnyOrder(file.name)
        )

        assertThat(result.isExit, `is`(equalTo(false)))
    }

    @Test
    fun numOfArgumentsTest() {
        val rootPath = folder.root.toPath()

        val workingDirectory = WorkingDirectory(rootPath)
        val result = ListFiles.execute(workingDirectory, listOf("a", "b"), "")

        assertThat(result.output, `is`(equalTo(ListFiles.invalidNumOfArgsMessage)))
        assertThat(result.isExit, `is`(equalTo(false)))
    }

    @Test
    fun noSuchElementTest() {
        val rootPath = folder.root.toPath()

        val workingDirectory = WorkingDirectory(rootPath)
        val result = ListFiles.execute(workingDirectory, listOf("a"), "")

        assertThat(result.output, `is`(equalTo(ListFiles.noSuchElementMessage)))
        assertThat(result.isExit, `is`(equalTo(false)))
    }
}