package foo.bar.clean.data.api.rest.service.featureflags

import foo.bar.clean.data.api.Endpoints
import foo.bar.clean.data.api.offlineRestClient
import foo.bar.clean.data.api.rest.service.endpoints.EndpointKey.FeatureFlags
import foo.bar.clean.domain.utils.MapStringBooleanDeserializer
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

/**
 * Raw http service, most of the stuff related to ktor is in here. These stubs
 * are hosted at https://www.mocky.io/
 */
class FeatureFlagsApi(
    private val httpClient: HttpClient,
    private val endpoints: Endpoints,
) {

    private val client = offlineRestClient("demostubs/rest/featureflags.json", httpClient)

    // FeatureFlags: "https://run.mocky.io/v3/9a969b95-84b9-4d72-9b61-0c7135421f5d"
    suspend fun fetchFlags(): FeatureFlagsPojo {
        return client.get(endpoints.url(FeatureFlags)).body()
    }
}


@Serializable
data class FeatureFlagsPojo(
    @Serializable(with = MapStringBooleanDeserializer::class)
    val flags: Map<String, Boolean>,
)
