package foo.bar.clean.data.api.graphql.service.auth

import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.type.Either
import co.early.fore.kt.net.apollo3.CallWrapperApollo3
import foo.bar.clean.data.DataError
import foo.bar.clean.data.toDomain
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.SLOMO
import foo.bar.clean.domain.services.api.Auth
import foo.bar.clean.domain.services.api.AuthService
import kotlinx.coroutines.delay

class AuthServiceImp(
    private val api: AuthApi,
    private val wrapper: CallWrapperApollo3<DataError>,
) : AuthService {

    override suspend fun signin(email: String): Either<DomainError, Auth> {

        if (SLOMO) {
            delay(1000)
        }

        Fore.i("signin()")

        return toDomain(wrapper.processCallAwait {
            api.login(email)
        }) {
            it.toDomain()
        }
    }
}
