package foo.bar.clean.domain.services.api

import co.early.fore.kt.core.type.Either
import foo.bar.clean.domain.DomainError
import kotlinx.serialization.Serializable

interface FeatureFlagsService {
    suspend fun fetchFlags(): Either<DomainError, Map<FeatureFlag, Boolean>>
}

@Serializable
sealed class FeatureFlag {
    @Serializable
    object Fruit : FeatureFlag()

    @Serializable
    object Counter : FeatureFlag()
}
