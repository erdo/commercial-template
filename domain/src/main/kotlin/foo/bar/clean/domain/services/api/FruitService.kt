package foo.bar.clean.domain.services.api

import co.early.fore.kt.core.type.Either
import foo.bar.clean.domain.DomainError
import kotlinx.serialization.Serializable

interface FruitService {
    suspend fun getFruits(): Either<DomainError, List<Fruit>>
    suspend fun getFruitsForceFailure(): Either<DomainError, List<Fruit>>
    suspend fun claimFreeFruit(ticketRef: String): Either<DomainError, Fruit>
}

@Serializable
sealed class Fruit {
    data class FruitSome(
        val name: String,
        val isCitrus: Boolean = false,
        val tastyPercentScore: Int = 50,
    ) : Fruit()
    object FruitNone: Fruit()

    /**
     * Just so we can visualise the state easier on the UI,
     * feel free to remove this function
     */
    override fun toString(): String {
        return this.javaClass.simpleName
    }
}