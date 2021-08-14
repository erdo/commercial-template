package foo.bar.clean.data.api.rest.service.fruit

import foo.bar.clean.domain.services.api.Fruit

/**
 * For mapping our data pojos / DTOs to domain classes
 */

fun List<FruitPojo>.toDomain(): List<Fruit> {
    return this.map {
        it.toDomain()
    }
}

fun FruitPojo.toDomain(): Fruit {
    return Fruit.FruitSome(
        name = this.name,
        isCitrus = this.isCitrus,
        tastyPercentScore = this.tastyPercentScore,
    )
}
