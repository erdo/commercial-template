package foo.bar.clean.data.api.rest.service.config

import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.type.Either
import co.early.fore.kt.net.ktor.CallWrapperKtor
import foo.bar.clean.data.DataError
import foo.bar.clean.data.toDomain
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.SLOMO
import foo.bar.clean.domain.services.api.Config
import foo.bar.clean.domain.services.api.ConfigService
import kotlinx.coroutines.delay

class ConfigServiceImp(
    private val api: ConfigApi,
    private val wrapper: CallWrapperKtor<DataError>,
) : ConfigService {

    override suspend fun fetchConfig(): Either<DomainError, Config> {

        if (SLOMO) {
            delay(500)
        }

        Fore.i("fetchConfig()")

        return toDomain(wrapper.processCallAwait {
            try {
                api.fetchConfig()
            } catch (t: Throwable) {
                Fore.e("WTF t")
            }
            ConfigPojo(counter = CounterPojo(0, 9))
        }) {
            it.toDomain()
        }
    }
}
