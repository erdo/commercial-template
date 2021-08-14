package foo.bar.clean.data.api.rest.service.endpoints

import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.type.Either
import co.early.fore.kt.core.type.Either.Companion.success
import co.early.fore.kt.net.ktor.CallWrapperKtor
import foo.bar.clean.data.DataError
import foo.bar.clean.data.api.Endpoints
import foo.bar.clean.data.toDomain
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.services.api.EndpointsService

class EndpointsServiceImp(
    private val api: EndpointsApi,
    private val wrapper: CallWrapperKtor<DataError>,
    private val endpoints: Endpoints,
) : EndpointsService {

    override suspend fun fetchEndpoints(endpointsUrl: String): Either<DomainError, Unit> {

        val result = toDomain(wrapper.processCallAwait {
            api.fetchEndpoints(url = endpointsUrl).endpoints
        }) { it.toData() }

        Fore.e("fetchEndpoints result: $result")

        return when (result) {
            is Either.Fail -> result
            is Either.Success -> {
                endpoints.setEndpoints(newEndpoints = result.value)
                success(Unit)
            }
        }
    }
}
