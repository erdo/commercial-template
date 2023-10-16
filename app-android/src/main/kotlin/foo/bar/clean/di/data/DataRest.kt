package foo.bar.clean.di.data

import co.early.fore.kt.net.InterceptorLogging
import co.early.fore.kt.net.ktor.CallWrapperKtor
import foo.bar.clean.data.api.rest.ErrorHandlerRest
import foo.bar.clean.data.api.rest.InterceptorCommonRest
import foo.bar.clean.data.api.rest.KtorBuilder
import foo.bar.clean.data.api.rest.service.config.ConfigApi
import foo.bar.clean.data.api.rest.service.endpoints.EndpointsApi
import foo.bar.clean.data.api.rest.service.featureflags.FeatureFlagsApi
import foo.bar.clean.data.api.rest.service.fruit.FruitApi
import foo.bar.clean.data.api.rest.service.phonehome.PhoneHomeApi
import foo.bar.clean.data.api.rest.service.tickets.TicketApi
import foo.bar.clean.data.api.rest.service.weather.WeatherApi
import org.koin.dsl.module

/**
 * Rest Data Sources
 */
val dataRest = module {

    /**
     * Ktor
     */

    single {
        KtorBuilder.create(
            InterceptorCommonRest(),
            InterceptorLogging()
        )//logging interceptor should be the last one
    }

    single {
        CallWrapperKtor(
            errorHandler = ErrorHandlerRest(get())
        )
    }

    /**
     * Data Sources
     */

    single {
        PhoneHomeApi(httpClient = get())
    }

    single {
        EndpointsApi(httpClient = get())
    }

    single {
        ConfigApi(httpClient = get(), endpoints = get())
    }

    single {
        FeatureFlagsApi(httpClient = get(), endpoints = get())
    }

    single {
        FruitApi(httpClient = get(), endpoints = get())
    }

    single {
        TicketApi(httpClient = get(), endpoints = get())
    }

    single {
        WeatherApi(httpClient = get(), endpoints = get())
    }
}
