package foo.bar.clean.data.api.rest.service.tickets

import foo.bar.clean.data.api.Endpoints
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

    private val smallDelayS = 0

    suspend fun createUser(): UserPojo {
        // TicketCreateUser "https://run.mocky.io/v3/4a219284-49fb-4fc9-9c2e-8fe7d8ec3611"
        return httpClient.get("${endpoints.url(TicketCreateUser)}/?mocky-delay=${smallDelayS}s")
            .body()
    }

    suspend fun createUserTicket(userId: Int): TicketPojo {
        // TicketCreateTicket "https://run.mocky.io/v3/aa7e9f0d-45ef-45c3-9512-3d2aa1d3bdfd"
        return httpClient.get("${endpoints.url(TicketCreateTicket)}/?mocky-delay=${smallDelayS}s")
            .body()
    }

    suspend fun fetchEstimatedWaitingTime(ticketRef: String): TimePojo {
        // TicketEstWaitingTime "https://run.mocky.io/v3/d348c606-9056-46d6-af7c-329a22486144"
        return httpClient.get("${endpoints.url(TicketEstWaitingTime)}/?mocky-delay=${smallDelayS}s")
            .body()
    }

    suspend fun cancelTicket(ticketRef: String): TicketResultPojo {
        // TicketCancelTicket "https://run.mocky.io/v3/3612c710-1dd9-42cc-bef8-899ede915392"
        return httpClient.get("${endpoints.url(TicketCancelTicket)}/?mocky-delay=${smallDelayS}s")
            .body()
    }

    suspend fun confirmTicket(ticketRef: String): TicketResultPojo {
        // TicketConfirmTicket "https://run.mocky.io/v3/697c0574-4a9f-46ec-9ea1-3b526c91f90f"
        return httpClient.get("${endpoints.url(TicketConfirmTicket)}/?mocky-delay=${smallDelayS}s")
            .body()
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
