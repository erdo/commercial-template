package foo.bar.clean.data.api.graphql.service.launch

import co.early.fore.kt.core.type.Either
import co.early.fore.kt.net.apollo3.CallWrapperApollo3
import foo.bar.clean.data.DataError
import foo.bar.clean.data.toDomain
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.SLOMO
import foo.bar.clean.domain.features.auth.AuthModel
import foo.bar.clean.domain.features.observeUntil
import foo.bar.clean.domain.services.api.BookingResult
import foo.bar.clean.domain.services.api.Launch
import foo.bar.clean.domain.services.api.LaunchService
import kotlinx.coroutines.delay

class LaunchServiceImp(
    private val api: LaunchApi,
    private val wrapper: CallWrapperApollo3<DataError>,
    private val authModel: () -> AuthModel,
) : LaunchService {

    override suspend fun refreshLaunchList(): Either<DomainError, List<Launch>> {

        if (SLOMO) {
            delay(2000)
        }

        return toDomain(wrapper.processCallAwait {
            api.getLaunchList()
        }) {
            it.data.toDomain()
        }
    }

    override suspend fun refreshLaunchDetail(id: String): Either<DomainError, Launch.ALaunch> {

        if (SLOMO) {
            delay(1000)
        }

        return toDomain(wrapper.processCallAwait {
            api.refreshLaunchDetail(id)
        }) {
            it.data.toDomain()
        }
    }

    override suspend fun bookTrip(id: String): Either<DomainError, BookingResult> {

        var dataResult = wrapper.processCallAwait {
            api.bookTrip(id)
        }

        if (dataResult is Either.Fail && dataResult.value == DataError.SessionTimedOut) {
            // try again after we log in
            authModel().signIn()
            authModel().observeUntil { !authModel().state.loading }
            dataResult = wrapper.processCallAwait {
                api.bookTrip(id)
            }
        }

        return toDomain(dataResult) {
            it.data.toDomain()
        }
    }

    override suspend fun cancelTrip(id: String): Either<DomainError, BookingResult> {

        var dataResult = wrapper.processCallAwait {
            api.cancelTrip(id)
        }

        if (dataResult is Either.Fail && dataResult.value == DataError.SessionTimedOut) {
            // try again after we log in
            authModel().signIn()
            authModel().observeUntil { !authModel().state.loading }
            dataResult = wrapper.processCallAwait {
                api.cancelTrip(id)
            }
        }

        return toDomain(dataResult) {
            it.data.toDomain()
        }
    }
}
