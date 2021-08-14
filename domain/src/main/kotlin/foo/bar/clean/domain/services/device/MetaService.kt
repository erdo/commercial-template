package foo.bar.clean.domain.services.device

import kotlinx.serialization.Serializable

interface MetaService {
    suspend fun getMetaData(): Meta
}

@Serializable
data class Meta(
    val appName: String,
    val version: String,
    val packageName: String,
)
