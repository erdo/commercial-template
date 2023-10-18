package foo.bar.clean.data.api.rest.service.phonehome

import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.type.Either
import co.early.fore.kt.net.ktor.CallWrapperKtor
import foo.bar.clean.data.DataError
import foo.bar.clean.data.toDomain
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.SLOMO
import foo.bar.clean.domain.services.api.PhoneHomeResult
import foo.bar.clean.domain.services.api.PhoneHomeService
import kotlinx.coroutines.delay

class PhoneHomeServiceImp(
    private val api: PhoneHomeApi,
    private val wrapper: CallWrapperKtor<DataError>,
) : PhoneHomeService {

    override suspend fun phoneHome(): Either<DomainError, PhoneHomeResult> {

        if (SLOMO) {
            delay(500)
        }

        Fore.i("phoneHome()")

        return toDomain(wrapper.processCallAwait {
            val res = api.phoneHome()
            Fore.e("result:$res")
            res
        }) {
            it.toDomain()
        }
    }
}
