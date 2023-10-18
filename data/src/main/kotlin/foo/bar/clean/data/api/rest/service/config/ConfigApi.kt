package foo.bar.clean.data.api.rest.service.config

import foo.bar.clean.data.api.Endpoints
import foo.bar.clean.data.api.offlineRestClient
import foo.bar.clean.data.api.rest.service.endpoints.EndpointKey.Config
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

/**
 * Raw http service, most of the stuff related to ktor is in here. These stubs
 * are hosted at https://www.mocky.io/
 */
class ConfigApi(
    private val httpClient: HttpClient,
    private val endpoints: Endpoints,
) {

    private val client = offlineRestClient("demostubs/rest/config.json", httpClient)

    // Config: "https://run.mocky.io/v3/6d6f12f2-7ac5-4957-a7f2-8bb319587b5b"
    suspend fun fetchConfig(): ConfigPojo {
        return client.get(endpoints.url(Config)).body()
    }
}

/**
 * network DTOs / POJOs
 */
@Serializable
data class ConfigPojo(
    val counter: CounterPojo,
)

@Serializable
data class CounterPojo(
    val min: Int,
    val max: Int,
)