package foo.bar.clean.data.api.rest.service.config

import co.early.fore.kt.core.type.Either
import co.early.fore.kt.net.ktor.CallWrapperKtor
import foo.bar.clean.data.DataError
import foo.bar.clean.data.toDomain
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.services.api.Config
import foo.bar.clean.domain.services.api.ConfigService

class ConfigServiceImp(
    private val api: ConfigApi,
    private val wrapper: CallWrapperKtor<DataError>,
) : ConfigService {

    override suspend fun fetchConfig(): Either<DomainError, Config> {
        return toDomain(wrapper.processCallAwait {
            api.fetchConfig()
        }) {
            it.toDomain()
        }
    }
}
