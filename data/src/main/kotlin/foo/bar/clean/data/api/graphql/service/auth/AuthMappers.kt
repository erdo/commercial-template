package foo.bar.clean.data.api.graphql.service.auth

import co.early.fore.kt.net.apollo3.CallWrapperApollo3
import foo.bar.clean.data.DataError
import foo.bar.clean.data.LoginMutation
import foo.bar.clean.domain.services.api.Auth

/**
 * For mapping Apollo pojos / DTOs to domain classes
 */

fun CallWrapperApollo3.SuccessResult<LoginMutation.Data, DataError>.toDomain(): Auth {
    return Auth(
        token = data.login?.token
    )
}
