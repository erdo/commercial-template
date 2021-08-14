package foo.bar.clean.data.api.rest.service.featureflags

import co.early.fore.kt.core.type.Either
import co.early.fore.kt.net.ktor.CallWrapperKtor
import foo.bar.clean.data.DataError
import foo.bar.clean.data.toDomain
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.services.api.FeatureFlag
import foo.bar.clean.domain.services.api.FeatureFlagsService

class FeatureFlagsServiceImp(
    private val api: FeatureFlagsApi,
    private val wrapper: CallWrapperKtor<DataError>,
) : FeatureFlagsService {

    override suspend fun fetchFlags(): Either<DomainError, Map<FeatureFlag, Boolean>> {
        return toDomain(wrapper.processCallAwait {
            api.fetchFlags().flags
        }) { it.toDomain() }
    }
}
