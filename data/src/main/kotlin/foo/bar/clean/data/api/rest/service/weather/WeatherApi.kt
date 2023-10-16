package foo.bar.clean.data.api.rest.service.weather

import foo.bar.clean.data.api.Endpoints
import foo.bar.clean.data.api.rest.service.endpoints.EndpointKey.WeatherPollenCount
import foo.bar.clean.data.api.rest.service.endpoints.EndpointKey.WeatherTemperature
import foo.bar.clean.data.api.rest.service.endpoints.EndpointKey.WeatherWindSpeed
import foo.bar.clean.domain.services.api.PollenLevel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

/**
 * Raw http service, most of the stuff related to ktor is in here. These stubs
 * are hosted at https://www.mocky.io/
 *
 * pollen success example response:
 * https://run.mocky.io/v3/2c43bf76-1a3b-4601-953f-ab4b6a68c6e0
 *
 */
class WeatherApi(
    private val httpClient: HttpClient,
    private val baseUrl: String = "https://run.mocky.io/v3/",
    private val endpoints: Endpoints,
) {

    val smallDelay = 1

    suspend fun fetchPollenCount(): List<PollenPojo> {
        // WeatherPollenCount "https://run.mocky.io/v3/2132a415-e07f-47f3-a3e2-6e2a2df147c6"
        return httpClient.get("${endpoints.url(WeatherPollenCount)}/?mocky-delay=${smallDelay}s")
            .body()
    }

    suspend fun fetchTemperature(): List<TemperaturePojo> {
        // WeatherTemperature "https://run.mocky.io/v3/3dc71499-35ee-4377-8f05-ea00376ef2b9"
        return httpClient.get("${endpoints.url(WeatherTemperature)}/?mocky-delay=${smallDelay}s")
            .body()
    }

    suspend fun fetchWindSpeed(): List<WindSpeedPojo> {
        // WeatherWindSpeed "https://run.mocky.io/v3/60e00580-b683-4193-9cb3-856743f7863a"
        return httpClient.get("${endpoints.url(WeatherWindSpeed)}/?mocky-delay=${smallDelay}s")
            .body()
    }
}


/**
 * network DTOs / POJOs
 */

@Serializable
data class PollenPojo(
    var level: Level = Level.UNKNOWN,
)

@Serializable
enum class Level(val domainPollenLevel: PollenLevel) {
    HIGH(PollenLevel.HIGH),
    MEDIUM(PollenLevel.MEDIUM),
    LOW(PollenLevel.LOW),
    UNKNOWN(PollenLevel.UNKNOWN),
}

@Serializable
data class TemperaturePojo(
    val maxTempC: Int = 35,
    val minTempC: Int = 10,
)

@Serializable
data class WindSpeedPojo(
    val windSpeedKmpH: Int = 35,
)
