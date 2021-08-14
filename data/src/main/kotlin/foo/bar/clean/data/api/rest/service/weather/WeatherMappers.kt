package foo.bar.clean.data.api.rest.service.weather

import foo.bar.clean.domain.services.api.PollenCount
import foo.bar.clean.domain.services.api.Temperature
import foo.bar.clean.domain.services.api.WindSpeed

/**
 * For mapping our data pojos / DTOs to domain classes
 */

fun PollenPojo.toDomain(): PollenCount {
    return PollenCount(this.level.domainPollenLevel)
}

fun TemperaturePojo.toDomain(): Temperature {
    return Temperature(
        maxTempC = this.maxTempC,
        minTempC = this.minTempC,
    )
}

fun WindSpeedPojo.toDomain(): WindSpeed {
    return WindSpeed(this.windSpeedKmpH)
}
