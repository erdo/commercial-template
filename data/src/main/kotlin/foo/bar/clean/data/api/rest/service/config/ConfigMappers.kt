package foo.bar.clean.data.api.rest.service.config

import foo.bar.clean.domain.services.api.Config
import foo.bar.clean.domain.services.api.Counter

/**
 * For mapping our data pojos / DTOs to domain classes
 */

fun ConfigPojo.toDomain(): Config {
    return Config(
        counter = Counter(
            min = this.counter.min,
            max = this.counter.max,
        )
    )
}
