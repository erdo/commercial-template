package foo.bar.clean.data.db

import android.app.Application
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

class AndroidDriverFactory(
    private val application: Application,
    private val inMemory: Boolean = false
): DriverFactory {
    // createDriver(schema = MyDatabase.Schema, name = "myDatabase.db")
    override fun createDriver(schema: SqlSchema<QueryResult.Value<Unit>>, name: String): SqlDriver {
        return AndroidSqliteDriver(schema, application, if (inMemory) null else name)
    }
}
