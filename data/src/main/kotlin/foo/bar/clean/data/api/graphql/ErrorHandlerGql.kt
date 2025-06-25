package foo.bar.clean.data.api.graphql

import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.logging.Logger
import co.early.fore.kt.net.apollo3.ErrorHandler
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Error
import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException
import com.apollographql.apollo.exception.JsonEncodingException
import foo.bar.clean.data.DataError

/**
 * You can probably use this class almost as it is for your own app, but you might want to
 * customise the behaviour for specific HTTP codes, GraphQL error formats etc, hence it's not
 * in the fore library
 */
class ErrorHandlerGql(private val logger: Logger? = null) : ErrorHandler<DataError> {

    override fun handleError(
        t: Throwable?,
        errorResponse: ApolloResponse<*>?,
    ): DataError {

        Fore.getLogger(logger).e("handleError() t:$t errorResponse:$errorResponse")

        val message = parseSpecificError(errorResponse?.errors?.first()) ?: parseGeneralErrors(t)

        Fore.getLogger(logger).e("handleError() returning:$message")

        return message
    }

    override fun handlePartialErrors(errors: List<Error?>?): List<DataError> {
        return errors?.mapNotNull {
            parseSpecificError(it)
        } ?: emptyList()
    }

    private fun parseGeneralErrors(t: Throwable?): DataError {
        return t?.let {
            when (it) {
                is java.lang.IllegalStateException -> DataError.AlreadyExecuted
                is JsonEncodingException -> DataError.Server
                is ApolloNetworkException -> DataError.Network
                is java.net.UnknownServiceException -> DataError.SecurityUnknown
                is java.net.SocketTimeoutException -> DataError.Network
                is ApolloHttpException -> {
                    Fore.getLogger(logger)
                        .e("handleError() HTTP:" + it.statusCode + " " + it.message)
                    when (it.statusCode) {
                        401 -> DataError.SessionTimedOut
                        400, 405 -> DataError.Client
                        429 -> DataError.RateLimited
                        //realise 404 is officially a "client" error, but in my experience if it happens in prod it is usually the fault of the server ;)
                        404, 500, 503 -> DataError.Server
                        else -> DataError.Misc
                    }
                }

                else -> DataError.Misc
            }
        } ?: DataError.Misc
    }

    private fun parseSpecificError(error: Error?): DataError? {
        // amazingly GraphQL never had an error code in its standard error
        // block so it usually (but not always) gets put under the extensions
        // block like this: https://spec.graphql.org/draft/#example-8b658
        return error?.extensions?.let { extensions ->
            (extensions as? Map<*, *>)?.get("code")?.let { code ->
                when (code) {
                    "SERVER_BUSY" -> DataError.Busy
                    else -> DataError.Misc
                }
            }
        }
    }
}
