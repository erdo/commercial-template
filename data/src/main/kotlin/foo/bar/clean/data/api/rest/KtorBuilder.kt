package foo.bar.clean.data.api.rest

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
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
object KtorBuilder {

    /**
     *
     * @param interceptors list of interceptors NB if you add a logging interceptor, it has to be
     * the last one in the list
     * @return ktor HttpClient object suitable for instantiating service interfaces
     */
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
                    }
                )
            }
            install(HttpRequestRetry){
                maxRetries = 2
                exponentialDelay(
                    maxDelayMs = 3000,
                    randomizationMs = 2000
                )
            }
        }
    }
}
