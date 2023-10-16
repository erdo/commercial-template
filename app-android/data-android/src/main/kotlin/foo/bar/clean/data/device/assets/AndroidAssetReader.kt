package foo.bar.clean.data.device.assets

import android.content.res.AssetManager
import co.early.fore.kt.core.coroutine.awaitCustom
import co.early.fore.kt.core.coroutine.launchCustom
import foo.bar.clean.domain.services.device.AssetReader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.concurrent.Executors

/**
 * Reads local asset files from android
 */
class AndroidAssetReader(
    private val assetManager: AssetManager,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val readDispatcher: CoroutineDispatcher = Executors.newSingleThreadExecutor()
        .asCoroutineDispatcher(),
) : AssetReader {

    override fun asInputStream(fileName: String): InputStream {
        return assetManager.open(fileName)
    }

    override fun asInputStreamReader(fileName: String, charSet: Charset): InputStreamReader {
        return InputStreamReader(asInputStream(fileName), charSet)
    }

    override fun asBufferedReader(fileName: String, charSet: Charset): BufferedReader {
        return asInputStreamReader(fileName, charSet).buffered()
    }

    override fun asText(fileName: String, charSet: Charset, complete: (String) -> Unit) {
        launchCustom(mainDispatcher) {
            complete(asText(fileName, charSet))
        }
    }

    override suspend fun asText(fileName: String, charSet: Charset): String {
        return awaitCustom(readDispatcher) {
            asInputStreamReader(fileName, charSet).use {
                it.readText()
            }
        }
    }
}
