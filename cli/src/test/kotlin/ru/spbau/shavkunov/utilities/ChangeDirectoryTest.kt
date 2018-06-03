package ru.spbau.shavkunov.utilities

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.spbau.shavkunov.WorkingDirectory


class ChangeDirectoryTest {
    @Rule
    @JvmField
    var folder = TemporaryFolder()

    @Test
    fun folderTest() {
        val subfolder = folder.newFolder()

        val rootPath = folder.root.toPath()
        val workingDirectory = WorkingDirectory(rootPath)
        val result = ChangeDirectory.execute(workingDirectory, listOf(subfolder.name), "")

        assertThat(workingDirectory.getPath(), `is`(equalTo(rootPath.resolve(subfolder.name))))
        assertThat(result.output, `is`(equalTo("")))
        assertThat(result.isExit, `is`(equalTo(false)))
    }

    @Test
    fun previousFolderTest() {
        val subfolder = folder.newFolder()

        val previousFolder = folder.root.toPath()
        val rootPath = subfolder.toPath()

        val workingDirectory = WorkingDirectory(rootPath)
        val result = ChangeDirectory.execute(workingDirectory, listOf(".."), "")

        assertThat(workingDirectory.getPath(), `is`(equalTo(previousFolder)))
        assertThat(result.output, `is`(equalTo("")))
        assertThat(result.isExit, `is`(equalTo(false)))
    }

    @Test
    fun nonExistingFolderTest() {
        val rootPath = folder.root.toPath()

        val workingDirectory = WorkingDirectory(rootPath)
        val result = ChangeDirectory.execute(workingDirectory, listOf("folder"), "")

        assertThat(workingDirectory.getPath(), `is`(equalTo(rootPath)))
        assertThat(result.output, `is`(equalTo(ChangeDirectory.noSuchFolderMessage)))
        assertThat(result.isExit, `is`(equalTo(false)))
    }

    @Test
    fun fileTest() {
        val file = folder.newFile()
        val rootPath = folder.root.toPath()

        val workingDirectory = WorkingDirectory(rootPath)
        val result = ChangeDirectory.execute(workingDirectory, listOf(file.name), "")

        assertThat(workingDirectory.getPath(), `is`(equalTo(rootPath)))
        assertThat(result.output, `is`(equalTo(ChangeDirectory.notAFolderMessage)))
        assertThat(result.isExit, `is`(equalTo(false)))
    }

    @Test
    fun numOfArgumentsTest() {
        val rootPath = folder.root.toPath()

        val workingDirectory = WorkingDirectory(rootPath)
        val result = ChangeDirectory.execute(workingDirectory, listOf("a", "b"), "")

        assertThat(workingDirectory.getPath(), `is`(equalTo(rootPath)))
        assertThat(result.output, `is`(equalTo(ChangeDirectory.invalidNumOfArgsMessage)))
        assertThat(result.isExit, `is`(equalTo(false)))
    }
}