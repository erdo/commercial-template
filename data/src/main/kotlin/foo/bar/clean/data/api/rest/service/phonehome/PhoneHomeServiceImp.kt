package foo.bar.clean.data.api.rest.service.phonehome

import co.early.fore.kt.core.type.Either
import co.early.fore.kt.net.ktor.CallWrapperKtor
import foo.bar.clean.data.DataError
import foo.bar.clean.data.toDomain
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.services.api.PhoneHomeResult
import foo.bar.clean.domain.services.api.PhoneHomeService

class PhoneHomeServiceImp(
    private val api: PhoneHomeApi,
    private val wrapper: CallWrapperKtor<DataError>,
) : PhoneHomeService {

    override suspend fun phoneHome(): Either<DomainError, PhoneHomeResult> {
        return toDomain(wrapper.processCallAwait {
            api.phoneHome()
        }) {
            it.toDomain()
        }
    }
}
