package foo.bar.clean.data.api.rest

import co.early.fore.kt.core.delegate.Fore
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor

/**
 * Most of this will all be specific to your application, when customising for your own case
 * bare in mind that you should be able to use this class in your tests to mock the server
 * by passing different interceptors in:
 *
 * see @[co.early.fore.net.testhelpers.InterceptorStubbedService]
 *
 */
private const val TIME_OUT = 10_000L

object KtorBuilder {

    /**
     *
     * @param interceptors list of interceptors NB if you add a logging interceptor, it has to be
     * the last one in the list
     * @return ktor HttpClient object suitable for instantiating service interfaces
     */
    @OptIn(ExperimentalSerializationApi::class)
    fun create(vararg interceptors: Interceptor): HttpClient {

        val okHttpConfig = OkHttp.create {
            for (interceptor in interceptors) {
                addInterceptor(interceptor)
            }
        }

        return HttpClient(okHttpConfig) {

            expectSuccess = true
            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                        explicitNulls = false
                    }
                )
            }
            install(HttpRequestRetry) {
                maxRetries = 2
                exponentialDelay(
                    maxDelayMs = 3000,
                    randomizationMs = 2000
                )
            }
            install(HttpTimeout) {
                requestTimeoutMillis = TIME_OUT
                connectTimeoutMillis = TIME_OUT
                socketTimeoutMillis = TIME_OUT
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Fore.i("Logger Ktor => $message")
                    }
                }
                level = LogLevel.INFO
            }
            /**
             * Set common headers and/or url if required
             *
             * install(DefaultRequest) {
             * header(HttpHeaders.ContentType, ContentType.Application.Json)
             * }
             *
             * Default headers can also be added using the OkHttp interceptor classes:
             * Namely {@link foo.bar.clean.data.api.rest.InterceptorCommonRest} and {@link foo.bar.clean.data.api.graphql.InterceptorCommonGql}
             *
             */
        }
    }
}
