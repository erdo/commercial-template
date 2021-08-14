package foo.bar.clean.data.api.rest

import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.logging.Logger
import co.early.fore.kt.net.ktor.ErrorHandler
import co.early.fore.net.MessageProvider
import foo.bar.clean.data.DataError
import foo.bar.clean.data.DataError.*
import foo.bar.clean.data.api.EndpointException
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.UnknownServiceException
import java.nio.charset.CoderMalfunctionError
import kotlin.reflect.KClass

/**
 * You can probably use this class almost as is for your own app, but you might want to
 * customise the behaviour for specific HTTP codes etc.
 *
 * Retry logic is handled in CustomKtorBuilder
 */
class ErrorHandlerRest(private val logWrapper: Logger? = null) : ErrorHandler<DataError> {

    override suspend fun <CE : MessageProvider<DataError>> handleError(
        t: Throwable,
        customErrorKlazz: KClass<CE>?
    ): DataError {

        Fore.getLogger(logWrapper).d("handling error in global error handler", t)

        val errorMessage = when (t) {

            is EndpointException -> EndpointMissing(t.key)

            is ResponseException -> {

                //initial error type
                var msg = when (t) {
                    is ClientRequestException -> { Server } // in 400..499
                    is RedirectResponseException -> { Server } //in 300..399
                    is ServerResponseException -> { Server } //in 500..599
                    else -> { Network } //something else
                }

                val response = t.response

                Fore.getLogger(logWrapper).e("handleError() HTTP:" + response.status)

                //get more specific with the error type
                msg = when (response.status.value) {
                    401 -> SessionTimedOut
                    400, 405 -> Client
                    429 -> RateLimited
                    404 -> Server //if this happens in prod, it's usually a server config issue
                    else -> null
                } ?: msg

                //let's get even more specifics about the error
                customErrorKlazz?.let { klazz ->
                    msg = parseCustomError(msg, response, klazz)
                }

                msg
            }
            is NoTransformationFoundException -> Server // content type is probably wrong, check response from server in app logs
            is SerializationException -> Server //parsing issue, maybe response is not json, or does not match expected type, or is empty
            is UnknownServiceException -> SecurityUnknown //most likely https related, check for usesCleartextTraffic if required
            is IOException -> Network //airplane mode is on, no network coverage etc
            else -> Network
        }

        Fore.getLogger(logWrapper).d("replyWithFailure() returning:$errorMessage")
        return errorMessage
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun <CE : MessageProvider<DataError>> parseCustomError(
        provisionalErrorMessage: DataError,
        errorResponse: HttpResponse,
        customErrorKlazz: KClass<CE>
    ): DataError {

        var customError: DataError = provisionalErrorMessage

        try {

            val bodyContent = errorResponse.bodyAsText(Charsets.UTF_8)
            Fore.getLogger(logWrapper).d("parseCustomError() attempting to parse this content:\n $bodyContent")
            val errorClass = Json.decodeFromString(serializer(customErrorKlazz.java), bodyContent) as CE
            customError = errorClass.message

        } catch (t: Throwable) {

            Fore.getLogger(logWrapper).e("parseCustomError() unexpected issue $t")

            when (t) {
                is IllegalStateException, is CoderMalfunctionError -> {Fore.getLogger(logWrapper).e("01")} //problem reading body text
                is SerializationException -> {Fore.getLogger(logWrapper).e("02")} //parsing error, @Serializable missing, wrong error class specified etc
                is UnsupportedEncodingException -> {Fore.getLogger(logWrapper).e("03")}
                is NullPointerException -> {Fore.getLogger(logWrapper).e("04")}
                else -> {Fore.getLogger(logWrapper).e("05")}
            }
        }

        Fore.getLogger(logWrapper).d("parseCustomError() returning:$customError")
        return customError
    }
}
