package foo.bar.clean.data.api.rest.service.phonehome

import foo.bar.clean.data.api.offlineRestClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

/**
 * Raw http service, most of the stuff related to ktor is in here. These stubs
 * are hosted at https://www.mocky.io/
 *
 * This is the first service to be called - it gets the main endpoint to call for all the
 * other services, and specifies the min supported version
 *
 * Would prefer an HTTP call NOT HTTPS, so that older clients can still connect to this URL
 * and be told to upgrade in the case that a certificate has been invalidated
 * (but mocky.io doesn't support HTTP),
 */
class PhoneHomeApi(
    private val httpClient: HttpClient,
    private val url: String = "https://run.mocky.io/v3/707ec06d-3008-4e73-a383-7a2177f3ef90",
) {

    private val client = offlineRestClient("demostubs/rest/phonehome.json", httpClient)

    suspend fun phoneHome(): PhoneHomeResultPojo {
        return client.get(url).body()
    }
}

/**
 * network DTOs / POJOs
 */
@Serializable
data class PhoneHomeResultPojo(
    val endpoints: String,
    val minSupportedVersion: String,
    val minNoNagVersion: String,
)
