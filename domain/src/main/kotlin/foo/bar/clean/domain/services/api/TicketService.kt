package foo.bar.clean.domain.services.api

import co.early.fore.kt.core.type.Either
import foo.bar.clean.domain.DomainError
import kotlinx.serialization.Serializable

interface TicketService {
    suspend fun createUser(): Either<DomainError, User>
    suspend fun createTicket(user: User): Either<DomainError, Ticket>
    suspend fun getEstimatedWaitingTime(ticket: Ticket): Either<DomainError, Time>
    suspend fun cancelTicket(ticket: Ticket): Either<DomainError, TicketResult>
    suspend fun confirmTicket(ticket: Ticket): Either<DomainError, TicketResult>
}

@Serializable
data class User(val userId: Int)

@Serializable
data class Ticket(val ticketRef: String)

@Serializable
data class TicketResult(val ticketRef: String, val completed: Boolean)

@Serializable
data class Time(val minutesWait: Int)
