package foo.bar.clean.data.api.rest.service.tickets

import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.type.Either
import co.early.fore.kt.net.ktor.CallWrapperKtor
import foo.bar.clean.data.DataError
import foo.bar.clean.data.toDomain
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.SLOMO
import foo.bar.clean.domain.services.api.Ticket
import foo.bar.clean.domain.services.api.TicketResult
import foo.bar.clean.domain.services.api.TicketService
import foo.bar.clean.domain.services.api.Time
import foo.bar.clean.domain.services.api.User
import kotlinx.coroutines.delay
import java.util.Random

class TicketServiceImp(
    private val api: TicketApi,
    private val wrapper: CallWrapperKtor<DataError>,
) : TicketService {

    override suspend fun createUser(): Either<DomainError, User> {

        if (SLOMO){
            delay(500)
        }

        Fore.i("createUser()")

        return toDomain(wrapper.processCallAwait {
            api.createUser()
        }) { it.toDomain() }
    }

    override suspend fun createTicket(user: User): Either<DomainError, Ticket> {

        if (SLOMO){
            delay(500)
        }

        Fore.i("createTicket()")

        return toDomain(wrapper.processCallAwait {
            api.createUserTicket(user.userId)
        }) { it.toDomain() }
    }

    override suspend fun getEstimatedWaitingTime(ticket: Ticket): Either<DomainError, Time> {

        if (SLOMO){
            delay(500)
        }

        Fore.i("getEstimatedWaitingTime()")

        return toDomain(wrapper.processCallAwait {
            api.fetchEstimatedWaitingTime(ticket.ticketRef).randomize()
        }) { it.toDomain() }
    }

    override suspend fun cancelTicket(ticket: Ticket): Either<DomainError, TicketResult> {

        if (SLOMO){
            delay(500)
        }

        Fore.i("cancelTicket()")

        return toDomain(wrapper.processCallAwait {
            api.cancelTicket(ticket.ticketRef)
        }) { it.toDomain() }
    }

    override suspend fun confirmTicket(ticket: Ticket): Either<DomainError, TicketResult> {

        if (SLOMO){
            delay(500)
        }

        Fore.i("confirmTicket()")

        return toDomain(wrapper.processCallAwait {
            api.confirmTicket(ticket.ticketRef)
        }) { it.toDomain() }
    }
}

// for demo purposes we take our server response and add a few random minutes (our domain will
// cancel the ticket if the wait time is longer than 10 minutes)
private fun TimePojo.randomize(): TimePojo {
    return this.copy(minutesWait = this.minutesWait + Random().nextInt(9))
}
