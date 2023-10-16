package foo.bar.clean.domain.services.api

import co.early.fore.kt.core.type.Either
import foo.bar.clean.domain.DomainError
import kotlinx.serialization.Serializable

interface LaunchService {
    suspend fun refreshLaunchList(): Either<DomainError, List<Launch>>
    suspend fun refreshLaunchDetail(id: String): Either<DomainError, Launch.ALaunch>
    suspend fun bookTrip(id: String): Either<DomainError, BookingResult>
    suspend fun cancelTrip(id: String): Either<DomainError, BookingResult>
}

@Serializable
sealed class Launch {
    @Serializable
    data class ALaunch(
        val id: String,
        val site: String = "",
        val isBooked: Boolean = false,
        val patchThumbImgUrl: String = "",
        val patchImgUrl: String = "",
    ) : Launch()

    @Serializable
    data object NoLaunch : Launch()
}

data class BookingResult(
    val message: String,
    val success: Boolean,
)
