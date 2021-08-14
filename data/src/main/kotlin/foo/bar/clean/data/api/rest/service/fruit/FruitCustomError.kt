package foo.bar.clean.data.api.rest.service.fruit

import co.early.fore.net.MessageProvider
import foo.bar.clean.data.DataError
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *
 * <Code>
 *
 * The server returns custom error codes in this form:
 *
 * {
 * "errorCode":"FRUIT_USER_LOCKED"
 * }
 *
 * </Code>
 *
 * example http://www.mocky.io/v2/59ef2a6c2e00002a1a1c5dea
 *
 */
@Serializable
class FruitCustomError(private val errorCode: ErrorCode?) : MessageProvider<DataError> {

    @Serializable
    enum class ErrorCode constructor(val errorMessage: DataError) {

        @SerialName("FRUIT_USER_LOGIN_CREDENTIALS_INCORRECT")
        LOGIN_CREDENTIALS_INCORRECT(DataError.FruitUserLoginCredentialsIncorrect),

        @SerialName("FRUIT_USER_LOCKED")
        USER_LOCKED(DataError.FruitUserLocked),

        @SerialName("FRUIT_USER_NOT_ENABLED")
        USER_NOT_ENABLED(DataError.FruitUserNotEnabled);
    }

    override val message: DataError = errorCode?.errorMessage ?: DataError.Misc
}
