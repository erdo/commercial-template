package foo.bar.clean.data.api.rest.service.tickets

import foo.bar.clean.domain.services.api.Ticket
import foo.bar.clean.domain.services.api.TicketResult
import foo.bar.clean.domain.services.api.Time
import foo.bar.clean.domain.services.api.User

/**
 * For mapping our data pojos / DTOs to domain classes
 */

fun UserPojo.toDomain(): User {
    return User(userId = this.userId)
}

fun TicketPojo.toDomain(): Ticket {
    return Ticket(ticketRef = this.ticketRef)
}

fun TicketResultPojo.toDomain(): TicketResult {
    return TicketResult(
        ticketRef = this.ticketRef,
        completed = this.completed
    )
}

fun TimePojo.toDomain(): Time {
    return Time(minutesWait = this.minutesWait)
}
