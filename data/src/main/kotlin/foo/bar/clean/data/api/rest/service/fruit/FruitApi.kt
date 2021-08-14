package foo.bar.clean.data.api.rest.service.fruit

import foo.bar.clean.data.api.Endpoints
import foo.bar.clean.data.api.rest.service.endpoints.EndpointKey.FruitFetchNotOk
import foo.bar.clean.data.api.rest.service.endpoints.EndpointKey.FruitFetchOk
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

/**
 * Raw http calls, most of the stuff related to ktor is in here. These stubs
 * are hosted at https://www.mocky.io/
 *
 * Success example response:
 * http://www.mocky.io/v2/59efa0132e0000ef331c5f9b
 *[
 * {
 *  "name":"orange",
 *  "isCitrus":true,
 *  "tastyPercentScore":43
 * },
 * {
 *  "name":"papaya",
 *  "isCitrus":false,
 *  "tastyPercentScore":98
 * }
 * ...
 *]
 *
 *
 * Fail example response:
 * http://www.mocky.io/v2/59ef2a6c2e00002a1a1c5dea
 * {
 *  "errorCode":"FRUIT_USER_LOCKED"
 * }
 *
 */
class FruitApi(
    private val httpClient: HttpClient,
    private val endpoints: Endpoints,
) {

    private val mediumDelay = 3
    private val smallDelay = 0

    suspend fun fetchFruitsOk(): List<FruitPojo> {
        // FruitFetchOk "https://run.mocky.io/v3/04943c15-1797-470f-a161-cf4234d765a5"
        return httpClient.get("${endpoints.url(FruitFetchOk)}/?mocky-delay=${smallDelay}s").body()
    }

    suspend fun fetchFruitsNotAuthorised(): List<FruitPojo> {
        // FruitFetchNotOk "https://run.mocky.io/v3/64107782-92b2-4a29-bb60-d41390e70134"
        return httpClient.get("${endpoints.url(FruitFetchNotOk)}/?mocky-delay=${mediumDelay}s")
            .body()
    }

    suspend fun claimFreeFruit(ticketRef: String): List<FruitPojo> {
        return fetchFruitsOk()
    }
}

/**
 * network DTOs / POJOs
 */
@Serializable
data class FruitPojo(
    val name: String,
    val isCitrus: Boolean,
    val tastyPercentScore: Int
)
