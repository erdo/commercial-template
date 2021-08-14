package foo.bar.clean.data.api.rest.service.endpoints

import foo.bar.clean.domain.SLOMO
import foo.bar.clean.domain.utils.MapStringStringDeserializer
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

/**
 * Raw http service, most of the stuff related to ktor is in here. These stubs
 * are hosted at https://www.mocky.io/
 *
 * This provides the endpoints for all the other rest services so needs to be called after
 * the PhoneHome call, but before any other services
 */
class EndpointsApi(
    private val httpClient: HttpClient,
) {

    private val delay = if (SLOMO) { "/?mocky-delay=500ms" } else ""

    // endpoints "https://run.mocky.io/v3/527b8f12-138e-431e-b7de-6698e8c6b32f",
    suspend fun fetchEndpoints(url: String): EndpointsPojo {
        return httpClient.get(url + delay).body()
    }
}

/**
 * network DTOs / POJOs
 */

@Serializable
data class EndpointsPojo(
    @Serializable(with = MapStringStringDeserializer::class)
    val endpoints: Map<String, String>,
)

