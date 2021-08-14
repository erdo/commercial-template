package foo.bar.clean.domain.features.init

import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.features.State

/**
 * Copyright © 2015-2023 early.co. All rights reserved.
 */
data class InitState(
    val step: Step = Step.Uninitialised,
) : State

sealed class Step {
    object Uninitialised : Step()
    data class Loading(val progress: Float) : Step()
    data class Ready(val nagBeforeStart: Boolean) : Step()
    data class Error(val domainError: DomainError) : Step()
}
