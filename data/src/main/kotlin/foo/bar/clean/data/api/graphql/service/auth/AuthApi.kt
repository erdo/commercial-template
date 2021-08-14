package foo.bar.clean.data.api.graphql.service.auth

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import foo.bar.clean.data.LoginMutation

/**
 * Raw graphql service, network DTOs are provided for us by Apollo
 */
class AuthApi(
    private val apolloClient: ApolloClient,
) {
    suspend fun login(email: String): ApolloResponse<LoginMutation.Data> {
        return apolloClient.mutation(LoginMutation(Optional.Present(email))).execute()
    }
}
