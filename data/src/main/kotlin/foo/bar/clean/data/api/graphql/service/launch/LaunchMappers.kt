package foo.bar.clean.data.api.graphql.service.launch

import foo.bar.clean.data.BookTripMutation
import foo.bar.clean.data.CancelTripMutation
import foo.bar.clean.data.LaunchDetailsQuery
import foo.bar.clean.data.LaunchListQuery
import foo.bar.clean.domain.services.api.BookingResult
import foo.bar.clean.domain.services.api.Launch

/**
 * For mapping Apollo pojos / DTOs to domain classes
 */

fun LaunchListQuery.Data.toDomain(): List<Launch> {
    return this.launches.launches.mapNotNull {
        it?.toDomain()
    }
}

fun LaunchListQuery.Launch.toDomain(): Launch {
    return Launch.ALaunch(
        id = this.id,
        site = this.site ?: "",
        isBooked = this.isBooked,
        patchThumbImgUrl = this.mission?.missionPatch ?: "",
    )
}

fun LaunchDetailsQuery.Data.toDomain(): Launch.ALaunch? {
    return this.launch?.let {
        Launch.ALaunch(
            id = it.id,
            site = it.site ?: "",
            isBooked = it.isBooked,
            patchImgUrl = it.mission?.missionPatch ?: "",
            patchThumbImgUrl = it.mission?.thumb ?: "",
        )
    }
}

fun BookTripMutation.Data.toDomain(): BookingResult {
    return this.bookTrips.let {
        BookingResult(
            message = it.message ?: "",
            success = it.success
        )
    }
}

fun CancelTripMutation.Data.toDomain(): BookingResult {
    return this.cancelTrip.let {
        BookingResult(
            message = it.message ?: "",
            success = it.success
        )
    }
}
