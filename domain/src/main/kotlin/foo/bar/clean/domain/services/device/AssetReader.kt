package foo.bar.clean.domain.services.device

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

interface AssetReader {
    fun asInputStream(fileName: String): InputStream
    fun asInputStreamReader(fileName: String, charSet: Charset = Charsets.UTF_8): InputStreamReader
    fun asBufferedReader(fileName: String, charSet: Charset = Charsets.UTF_8): BufferedReader
    fun asText(fileName: String, charSet: Charset = Charsets.UTF_8, complete: (String) -> Unit)
    suspend fun asText(fileName: String, charSet: Charset = Charsets.UTF_8): String
}
