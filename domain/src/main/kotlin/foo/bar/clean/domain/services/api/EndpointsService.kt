package foo.bar.clean.domain.services.api

import co.early.fore.kt.core.type.Either
import foo.bar.clean.domain.DomainError

interface EndpointsService {
    suspend fun fetchEndpoints(endpointsUrl: String): Either<DomainError, Unit>
}
