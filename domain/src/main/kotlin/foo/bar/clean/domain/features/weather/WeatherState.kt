package foo.bar.clean.domain.features.weather

import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.services.api.PollenCount
import foo.bar.clean.domain.services.api.Temperature
import foo.bar.clean.domain.services.api.WindSpeed
import foo.bar.clean.domain.features.State
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class WeatherState(
    val weatherReport: WeatherReport = WeatherReport(),
    val error: DomainError? = null,
    @Transient
    val loading: Boolean = false,
) : State

@Serializable
data class WeatherReport(
    val pollenCount: PollenCount = PollenCount(),
    val temperature: Temperature = Temperature(),
    val windSpeed: WindSpeed = WindSpeed(),
)