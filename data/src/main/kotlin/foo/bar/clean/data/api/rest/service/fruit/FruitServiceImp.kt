package foo.bar.clean.data.api.rest.service.fruit

import co.early.fore.kt.core.type.Either
import co.early.fore.kt.net.ktor.CallWrapperKtor
import foo.bar.clean.data.DataError
import foo.bar.clean.data.toDomain
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.features.fruit.takeRnd
import foo.bar.clean.domain.services.api.Fruit
import foo.bar.clean.domain.services.api.FruitService

class FruitServiceImp(
    private val api: FruitApi,
    private val wrapper: CallWrapperKtor<DataError>,
) : FruitService {

    override suspend fun getFruits(): Either<DomainError, List<Fruit>> {
        return toDomain(wrapper.processCallAwait(FruitCustomError::class) {
            api.fetchFruitsOk()
        }) {
            it.toDomain()
        }
    }

    override suspend fun getFruitsForceFailure(): Either<DomainError, List<Fruit>> {
        return toDomain(wrapper.processCallAwait(FruitCustomError::class) {
            api.fetchFruitsNotAuthorised()
        }) {
            it.toDomain()
        }
    }

    override suspend fun claimFreeFruit(ticketRef: String): Either<DomainError, Fruit> {
        return toDomain(wrapper.processCallAwait {
            api.claimFreeFruit(ticketRef)
        }) {
            it.takeRnd(FruitPojo("Apple", false, 100)).toDomain()
        }
    }
}
