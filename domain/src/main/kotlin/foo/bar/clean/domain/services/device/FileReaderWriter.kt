package foo.bar.clean.domain.services.device

/**
 * Reads and writes to the file system
 */
interface FileReaderWriter {
    fun write(data: String, fileName: String, done: (Boolean) -> Unit)
    fun read(fileName: String, complete: (String) -> Unit)
    fun clear(fileName: String, done: (Boolean) -> Unit)
    fun wipeEverything(done: (Boolean) -> Unit)
    suspend fun write(data: String, fileName: String): Boolean
    suspend fun read(fileName: String): String
    suspend fun clear(fileName: String): Boolean
    suspend fun wipeEverything(): Boolean
}
