package foo.bar.clean.data.api

import co.early.fore.kt.net.InterceptorLogging
import co.early.fore.kt.net.testhelpers.InterceptorStubOkHttp3
import co.early.fore.kt.net.testhelpers.Stub
import com.apollographql.apollo.ApolloClient
import foo.bar.clean.data.api.graphql.ApolloBuilder
import foo.bar.clean.data.api.rest.KtorBuilder
import foo.bar.clean.domain.OFFLINE_APOLLO
import foo.bar.clean.domain.OFFLINE_MOCKY
import io.ktor.client.HttpClient

/**
 * Just for demo purposes
 * (when mocky.io is down)
 */
fun offlineRestClient(file: String, realClient: HttpClient): HttpClient {
    return if (OFFLINE_MOCKY) {
        KtorBuilder.create(
            InterceptorLogging(),
            InterceptorStubOkHttp3(
                Stub<Unit>(
                    httpCode = 200,
                    bodyContentResourceFileName = file,
                    headers = listOf(Stub.Header("Content-Type", "application/json"))
                )
            )
        )
    } else {
        realClient
    }
}

/**
 * Just for demo purposes
 * (when apollo-fullstack-tutorial.herokuapp.com is down)
 */
fun offlineApolloClient(file: String, realClient: ApolloClient): ApolloClient {
    return if (OFFLINE_APOLLO) {
        ApolloBuilder.create(
            InterceptorLogging(),
            InterceptorStubOkHttp3(
                Stub<Unit>(
                    httpCode = 200,
                    bodyContentResourceFileName = file,
                    headers = listOf(Stub.Header("Content-Type", "application/json"))
                )
            )
        )
    } else {
        realClient
    }
}
