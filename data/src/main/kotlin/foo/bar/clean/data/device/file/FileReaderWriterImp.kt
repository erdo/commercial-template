package foo.bar.clean.data.device.file

import co.early.fore.kt.core.coroutine.awaitCustom
import co.early.fore.kt.core.coroutine.launchMain
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.logging.Logger
import foo.bar.clean.domain.services.device.FileReaderWriter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.io.File
import java.io.FileNotFoundException
import java.util.concurrent.Executors

/**
 * Reads and writes to the file system, for use on android:
 * inject dataDirectory = Environment.getDataDirectory()
 */
class FileReaderWriterImp(
    private val dataDirectory: File,
    private val subFolder: String,
    private val writeReadDispatcher: CoroutineDispatcher = Executors.newSingleThreadExecutor()
        .asCoroutineDispatcher(),
    private val logger: Logger = Fore.getLogger(),
) : FileReaderWriter {

    init {
        getFolder().mkdirs()
    }

    override fun write(data: String, fileName: String, done: (Boolean) -> Unit) {
        launchMain {
            done(write(data, fileName))
        }
    }

    override fun read(fileName: String, complete: (String) -> Unit) {
        launchMain {
            complete(read(fileName))
        }
    }

    override fun clear(fileName: String, done: (Boolean) -> Unit) {
        launchMain {
            done(clear(fileName))
        }
    }

    override fun wipeEverything(done: (Boolean) -> Unit) {
        launchMain {
            done(wipeEverything())
        }
    }

    override suspend fun write(data: String, fileName: String): Boolean {
        return awaitCustom(writeReadDispatcher) {
            try {
                getFile(fileName).writeText(data, Charsets.UTF_8)
                true
            } catch (e: Exception) {
                logger.e("write failed", e)
                false
            }
        }
    }

    override suspend fun read(fileName: String): String {
        return awaitCustom(writeReadDispatcher) {
            try {
                getFile(fileName).readText(Charsets.UTF_8)
            } catch (e: Exception) {
                when (e) {
                    is FileNotFoundException -> logger.e("file not found", e)
                    else -> logger.e("read failed", e)
                }
                ""
            }
        }
    }

    override suspend fun clear(fileName: String): Boolean {
        return awaitCustom(writeReadDispatcher) {
            try {
                getFile(fileName).delete()
                true
            } catch (e: Exception) {
                logger.e("clear failed", e)
                false
            }
        }
    }

    override suspend fun wipeEverything(): Boolean {
        logger.d("wipeEverything()")
        return awaitCustom(writeReadDispatcher) {
            try {
                getFolder().deleteRecursively()
                getFolder().mkdirs()
                true
            } catch (e: Exception) {
                logger.e("wipeEverything failed", e)
                false
            }
        }
    }

    private fun getFile(fileName: String): File {
        return File(dataDirectory, getFilePath(fileName))
    }

    private fun getFolder(): File {
        return File(dataDirectory, getFolderPath())
    }

    private fun getFilePath(fileName: String): String {
        return "${getFolderPath()}${File.separator}$fileName"
    }

    private fun getFolderPath(): String {
        return "${File.separator}$subFolder"
    }
}
