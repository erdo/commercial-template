package foo.bar.clean.di.data

import foo.bar.clean.App
import foo.bar.clean.data.db.AndroidDriverFactory
import foo.bar.clean.data.db.DriverFactory
import foo.bar.clean.data.db.service.todo.TodoItemDb
import org.koin.dsl.module

/**
 * Db Data Sources
 */
val dataDb = module {

    /**
     * Driver
     */

    single<DriverFactory> {
        AndroidDriverFactory(
            application = get() as App,
        )
    }


    /**
     * DB
     */

    single {
        TodoItemDb(
            df = get(),
        )
    }
}
