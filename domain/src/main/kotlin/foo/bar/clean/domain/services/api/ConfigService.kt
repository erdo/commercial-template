package foo.bar.clean.domain.services.api

import co.early.fore.kt.core.type.Either
import foo.bar.clean.domain.DomainError
import kotlinx.serialization.Serializable

interface ConfigService {
    suspend fun fetchConfig(): Either<DomainError, Config>
}

@Serializable
data class Config(
    @Serializable
    val counter: Counter = Counter(),
)

@Serializable
data class Counter(
    val min: Int = 0,
    val max: Int = 5,
)