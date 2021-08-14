package foo.bar.clean.domain.features.fruit

import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.services.api.Fruit
import foo.bar.clean.domain.services.api.Fruit.FruitNone
import foo.bar.clean.domain.features.State
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.Random

/**
 * Copyright © 2015-2023 early.co. All rights reserved.
 */
@Serializable
data class FruitState(
    val fruit: Fruit = FruitNone,
    val error: DomainError = DomainError.NoError,
    @Transient
    val loading: Boolean = false,
) : State

// our sample API stubs return static lists, so we just take one of the items at random
// to make things unpredictable, delete this for a real app
fun <T> List<T>.takeRnd(default: T): T {
    return if (isNotEmpty()) {
        this[Random().nextInt(size)]
    } else default
}