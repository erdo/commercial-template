package foo.bar.clean.data.api.rest.service.weather

import co.early.fore.kt.core.type.Either
import co.early.fore.kt.net.ktor.CallWrapperKtor
import foo.bar.clean.data.DataError
import foo.bar.clean.data.toDomain
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.features.fruit.takeRnd
import foo.bar.clean.domain.services.api.PollenCount
import foo.bar.clean.domain.services.api.Temperature
import foo.bar.clean.domain.services.api.WeatherService
import foo.bar.clean.domain.services.api.WindSpeed

class WeatherServiceImp(
    private val api: WeatherApi,
    private val wrapper: CallWrapperKtor<DataError>,
) : WeatherService {

    override suspend fun getPollenCount(): Either<DomainError, PollenCount> {
        return toDomain(wrapper.processCallAwait {
            api.fetchPollenCount()
        }) { it.takeRnd(PollenPojo()).toDomain() }
    }

    override suspend fun getTemperature(): Either<DomainError, Temperature> {
        return toDomain(wrapper.processCallAwait {
            api.fetchTemperature()
        }) { it.takeRnd(TemperaturePojo()).toDomain() }
    }

    override suspend fun getWindSpeed(): Either<DomainError, WindSpeed> {
        return toDomain(wrapper.processCallAwait {
            api.fetchWindSpeed()
        }) { it.takeRnd(WindSpeedPojo()).toDomain() }
    }
}
