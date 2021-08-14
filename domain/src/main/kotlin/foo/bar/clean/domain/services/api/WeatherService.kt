package foo.bar.clean.domain.services.api

import co.early.fore.kt.core.type.Either
import foo.bar.clean.domain.DomainError
import kotlinx.serialization.Serializable

interface WeatherService {
    suspend fun getPollenCount(): Either<DomainError, PollenCount>
    suspend fun getTemperature(): Either<DomainError, Temperature>
    suspend fun getWindSpeed(): Either<DomainError, WindSpeed>
}

@Serializable
data class PollenCount(
    val pollenLevel: PollenLevel = PollenLevel.UNKNOWN,
)

@Serializable
data class Temperature(
    val maxTempC: Int? = null,
    val minTempC: Int? = null,
)

@Serializable
data class WindSpeed(
    val windSpeedKmpH: Int = 0,
)

@Serializable
enum class PollenLevel {
    HIGH,
    MEDIUM,
    LOW,
    UNKNOWN,
}
