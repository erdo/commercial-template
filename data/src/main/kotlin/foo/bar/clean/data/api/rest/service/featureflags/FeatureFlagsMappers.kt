package foo.bar.clean.data.api.rest.service.featureflags

import foo.bar.clean.domain.services.api.FeatureFlag

/**
 * For mapping our data pojos / DTOs to domain classes
 */

fun Map<String, Boolean>.toDomain(): Map<FeatureFlag, Boolean> {
    return this.map {
        it.toDomain()
    }.filterNotNull().toMap()
}

fun Map.Entry<String, Boolean>.toDomain(): Pair<FeatureFlag, Boolean>? {
    val newKey = when (this.key) {
        "fruit" -> FeatureFlag.Fruit
        "counter" -> FeatureFlag.Counter
        else -> null
    }

    return newKey?.let {
        it to this.value
    }
}
