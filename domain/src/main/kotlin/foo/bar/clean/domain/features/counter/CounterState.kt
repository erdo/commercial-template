package foo.bar.clean.domain.features.counter

import foo.bar.clean.domain.features.State
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Copyright © 2015-2023 early.co. All rights reserved.
 */

@Serializable
data class CounterState(
    val amount: Int,
    val max: Int,
    val min: Int,
    @Transient
    val loading: Boolean = false,
): State {
    fun canIncrease(): Boolean = !loading && amount < max
    fun canDecrease(): Boolean = !loading && amount > min
}
