package foo.bar.clean.di.data

import foo.bar.clean.data.api.Endpoints
import org.koin.dsl.module

/**
 * Misc Data Sources
 */
val dataMisc = module {

    /**
     * Endpoints to be used for all services
     */

    single {
        Endpoints()
    }
}
