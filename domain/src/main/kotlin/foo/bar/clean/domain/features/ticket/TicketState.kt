package foo.bar.clean.domain.features.ticket

import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.features.ticket.Status.Waiting
import foo.bar.clean.domain.features.ticket.Step.*
import foo.bar.clean.domain.services.api.Fruit
import foo.bar.clean.domain.services.api.Fruit.FruitNone
import foo.bar.clean.domain.features.State
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Copyright © 2015-2023 early.co. All rights reserved.
 */
@Serializable
data class TicketState(
    val freeFruit: Fruit = FruitNone,
    val waitTimeMin: Int? = null,
    val maxAcceptableWaitTimeMin: Int = 10,
    val error: DomainError = DomainError.NoError,
    val progress: List<Pair<Step, Status>> = listOf(
        Initialising to Waiting,
        CreatingUser to Waiting,
        CreatingTicket to Waiting,
        FetchingWaitingTime to Waiting,
        CancellingTicket to Waiting,
        ConfirmingTicket to Waiting,
        ClaimingFreeFruit to Waiting,
    ),
    @Transient
    val loading: Boolean = false,
): State {
    fun hasData(): Boolean {
        return this != TicketState()
    }
    fun waitIsTooLong(): Boolean {
        return waitTimeMin?.let { it > maxAcceptableWaitTimeMin} ?: false
    }
}

@Serializable
sealed class Step {
    data object Initialising: Step()
    data object CreatingUser: Step()
    data object CreatingTicket: Step()
    data object FetchingWaitingTime: Step()
    data object CancellingTicket: Step()
    data object ConfirmingTicket: Step()
    data object ClaimingFreeFruit: Step()
    data object Complete: Step()
}

@Serializable
sealed class Status {
    data object Waiting: Status()
    data object InProgress: Status()
    data object Done: Status()
    data object Failed: Status()
}
