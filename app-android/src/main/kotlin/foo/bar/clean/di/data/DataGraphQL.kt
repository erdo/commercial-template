package foo.bar.clean.di.data

import co.early.fore.kt.net.InterceptorLogging
import co.early.fore.kt.net.apollo3.CallWrapperApollo3
import foo.bar.clean.data.api.graphql.ApolloBuilder
import foo.bar.clean.data.api.graphql.ErrorHandlerGql
import foo.bar.clean.data.api.graphql.InterceptorCommonGql
import foo.bar.clean.data.api.graphql.service.auth.AuthApi
import foo.bar.clean.data.api.graphql.service.launch.LaunchApi
import foo.bar.clean.domain.features.auth.AuthModel
import org.koin.dsl.module

/**
 * GraphQL Data Sources
 */
val dataGraphQL = module {

    /**
     * Apollo
     */

    single {
        ApolloBuilder.create(
            InterceptorCommonGql(
                metaModel = get(),
                authModel = { get() as AuthModel },
            ),
            InterceptorLogging()
        )//logging interceptor should be the last one
    }

    single {
        CallWrapperApollo3(
            errorHandler = ErrorHandlerGql(get())
        )
    }

    /**
     * Data Sources
     */

    single {
        AuthApi(apolloClient = get())
    }

    single {
        LaunchApi(apolloClient = get())
    }
}
