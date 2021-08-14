package foo.bar.clean.di.domain

import co.early.persista.PerSista
import org.koin.dsl.module

/**
 * Domain Persistence
 */
val domainPersistence = module {

    single {
        PerSista(
            dataDirectory = get(),
            logger = get(),
        )
    }
}
