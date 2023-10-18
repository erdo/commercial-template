package foo.bar.clean.data.api.graphql.service.launch

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import foo.bar.clean.data.BookTripMutation
import foo.bar.clean.data.CancelTripMutation
import foo.bar.clean.data.LaunchDetailsQuery
import foo.bar.clean.data.LaunchListQuery
import foo.bar.clean.data.api.offlineApolloClient
import foo.bar.clean.data.api.offlineRestClient

/**
 * Raw graphql service, network DTOs are provided for us by Apollo
 */
class LaunchApi(
    private val apolloClient: ApolloClient,
) {
    suspend fun getLaunchList(): ApolloResponse<LaunchListQuery.Data> {
        val client = offlineApolloClient("demostubs/gql/spacelaunches.json", apolloClient)
        return client.query(LaunchListQuery()).execute()
    }

    suspend fun refreshLaunchDetail(id: String): ApolloResponse<LaunchDetailsQuery.Data> {
        val client = offlineApolloClient("demostubs/gql/spacelaunchdetail.json", apolloClient)
        return client.query(LaunchDetailsQuery(id)).execute()
    }

    suspend fun bookTrip(id: String): ApolloResponse<BookTripMutation.Data> {
        return apolloClient.mutation(BookTripMutation(id)).execute()
    }

    suspend fun cancelTrip(id: String): ApolloResponse<CancelTripMutation.Data> {
        return apolloClient.mutation(CancelTripMutation(id)).execute()
    }
}
