package foo.bar.clean.data.api.rest.service.featureflags

import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.type.Either
import co.early.fore.kt.net.ktor.CallWrapperKtor
import foo.bar.clean.data.DataError
import foo.bar.clean.data.toDomain
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.SLOMO
import foo.bar.clean.domain.services.api.FeatureFlag
import foo.bar.clean.domain.services.api.FeatureFlagsService
import kotlinx.coroutines.delay

class FeatureFlagsServiceImp(
    private val api: FeatureFlagsApi,
    private val wrapper: CallWrapperKtor<DataError>,
) : FeatureFlagsService {

    override suspend fun fetchFlags(): Either<DomainError, Map<FeatureFlag, Boolean>> {

        if (SLOMO) {
            delay(500)
        }

        Fore.i("fetchFlags()")

        return toDomain(wrapper.processCallAwait {
            api.fetchFlags().flags
        }) { it.toDomain() }
    }
}
