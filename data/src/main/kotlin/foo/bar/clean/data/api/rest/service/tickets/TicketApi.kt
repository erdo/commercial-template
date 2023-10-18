package foo.bar.clean.data.api.rest.service.tickets

import foo.bar.clean.data.api.Endpoints
import foo.bar.clean.data.api.offlineRestClient
import foo.bar.clean.data.api.rest.service.endpoints.EndpointKey.TicketCancelTicket
import foo.bar.clean.data.api.rest.service.endpoints.EndpointKey.TicketConfirmTicket
import foo.bar.clean.data.api.rest.service.endpoints.EndpointKey.TicketCreateTicket
import foo.bar.clean.data.api.rest.service.endpoints.EndpointKey.TicketCreateUser
import foo.bar.clean.data.api.rest.service.endpoints.EndpointKey.TicketEstWaitingTime
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

/**
 * Raw http service, most of the stuff related to ktor is in here. These stubs
 * are hosted at https://www.mocky.io/
 */
class TicketApi(
    private val httpClient: HttpClient,
    private val endpoints: Endpoints,
) {

    suspend fun createUser(): UserPojo {
        val client = offlineRestClient("demostubs/rest/ticket_createuser.json", httpClient)
        // TicketCreateUser "https://run.mocky.io/v3/4a219284-49fb-4fc9-9c2e-8fe7d8ec3611"
        return client.get(endpoints.url(TicketCreateUser)).body()
    }

    suspend fun createUserTicket(userId: Int): TicketPojo {
        val client = offlineRestClient("demostubs/rest/ticket_createticket.json", httpClient)
        // TicketCreateTicket "https://run.mocky.io/v3/aa7e9f0d-45ef-45c3-9512-3d2aa1d3bdfd"
        return client.get(endpoints.url(TicketCreateTicket)).body()
    }

    suspend fun fetchEstimatedWaitingTime(ticketRef: String): TimePojo {
        val client = offlineRestClient("demostubs/rest/ticket_estimatewaitingtime.json", httpClient)
        // TicketEstWaitingTime "https://run.mocky.io/v3/d348c606-9056-46d6-af7c-329a22486144"
        return client.get(endpoints.url(TicketEstWaitingTime)).body()
    }

    suspend fun cancelTicket(ticketRef: String): TicketResultPojo {
        val client = offlineRestClient("demostubs/rest/ticket_cancelticket.json", httpClient)
        // TicketCancelTicket "https://run.mocky.io/v3/3612c710-1dd9-42cc-bef8-899ede915392"
        return client.get(endpoints.url(TicketCancelTicket)).body()
    }

    suspend fun confirmTicket(ticketRef: String): TicketResultPojo {
        val client = offlineRestClient("demostubs/rest/ticket_confirmticket.json", httpClient)
        // TicketConfirmTicket "https://run.mocky.io/v3/697c0574-4a9f-46ec-9ea1-3b526c91f90f"
        return client.get(endpoints.url(TicketConfirmTicket)).body()
    }
}


/**
 * network DTOs / POJOs
 */

@Serializable
data class UserPojo(val userId: Int)

@Serializable
data class TicketPojo(val ticketRef: String)

@Serializable
data class TicketResultPojo(val ticketRef: String, val completed: Boolean)

@Serializable
data class TimePojo(val minutesWait: Int)
