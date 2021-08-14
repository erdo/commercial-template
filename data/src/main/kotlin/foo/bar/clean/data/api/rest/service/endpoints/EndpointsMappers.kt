package foo.bar.clean.data.api.rest.service.endpoints

import kotlinx.serialization.Serializable

/**
 * For mapping our data pojos / DTOs
 *
 * In this case the results don't need to go to the domain layer,
 * as the endpoints are an exclusive concern of the data layer
 *
 * So this mapper is a little different to the rest, it's not a good template to copy
 * for a regular mapper, see any of the others
 */

fun Map<String, String>.toData(): Map<EndpointKey, EndpointUrl> {
    return this.map {
        it.toData()
    }.filterNotNull().toMap()
}

fun Map.Entry<String, String>.toData(): Pair<EndpointKey, String>? {
    val newKey = when (this.key) {
        "config_fetchconfig" -> EndpointKey.Config
        "featureflags_fetchflags" -> EndpointKey.FeatureFlags
        "fruit_fetchfruitsok" -> EndpointKey.FruitFetchOk
        "fruit_fetchfruitsnotauthorised" -> EndpointKey.FruitFetchNotOk
        "ticket_createuser" -> EndpointKey.TicketCreateUser
        "ticket_createuserticket" -> EndpointKey.TicketCreateTicket
        "ticket_fetchestimatedwaitingtime" -> EndpointKey.TicketEstWaitingTime
        "ticket_cancelticket" -> EndpointKey.TicketCancelTicket
        "ticket_confirmticket" -> EndpointKey.TicketConfirmTicket
        "weather_fetchpollencount" -> EndpointKey.WeatherPollenCount
        "weather_fetchtemperature" -> EndpointKey.WeatherTemperature
        "weather_fetchwindspeed" -> EndpointKey.WeatherWindSpeed
        else -> null
    }

    return newKey?.let {
        it to this.value
    }
}

@Serializable
sealed class EndpointKey {
    object Config : EndpointKey()
    object FeatureFlags : EndpointKey()
    object FruitFetchOk : EndpointKey()
    object FruitFetchNotOk : EndpointKey()
    object TicketCreateUser : EndpointKey()
    object TicketCreateTicket : EndpointKey()
    object TicketEstWaitingTime : EndpointKey()
    object TicketCancelTicket : EndpointKey()
    object TicketConfirmTicket : EndpointKey()
    object WeatherPollenCount : EndpointKey()
    object WeatherTemperature : EndpointKey()
    object WeatherWindSpeed : EndpointKey()
}

typealias EndpointUrl = String