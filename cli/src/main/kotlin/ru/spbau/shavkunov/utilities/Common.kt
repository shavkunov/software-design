package ru.spbau.shavkunov.utilities

import org.apache.commons.io.FileUtils
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Path

fun getFileContent(path: Path): String {
    return try {
        FileUtils.readFileToString(path.toFile(), Charset.defaultCharset())
    } catch (_: IOException) {
        "Cannot open open file $path"
    }
}