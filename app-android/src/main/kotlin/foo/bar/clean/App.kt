package foo.bar.clean

import androidx.multidex.MultiDexApplication
import co.early.fore.kt.core.delegate.DebugDelegateDefault
import co.early.fore.kt.core.delegate.Fore
import foo.bar.clean.BuildConfig
import foo.bar.clean.di.data.dataDb
import foo.bar.clean.di.data.dataDevice
import foo.bar.clean.di.data.dataGraphQL
import foo.bar.clean.di.data.dataMisc
import foo.bar.clean.di.data.dataRest
import foo.bar.clean.di.domain.domainModels
import foo.bar.clean.di.domain.domainPersistence
import foo.bar.clean.di.domain.domainServices
import foo.bar.clean.di.domain.domainActionHandlers
import foo.bar.clean.domain.features.init.InitModel
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Copyright © 2019-2023 early.co. All rights reserved.
 */
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        inst = this

        if (BuildConfig.DEBUG) {
            Fore.setDelegate(DebugDelegateDefault(tagPrefix = "clean_"))
        }

        Fore.e("APP")

        startKoin {
            if (BuildConfig.DEBUG) {
                // Use Koin Android Logger
                androidLogger()
             }
            // declare Android context
            androidContext(this@App)
            // declare modules to use
            modules(
                listOf(
                    // data
                    dataDevice,
                    dataGraphQL,
                    dataMisc,
                    dataRest,
                    dataDb,
                    // domain
                    domainModels,
                    domainPersistence,
                    domainServices,
                    // ui
                    domainActionHandlers,
                )
            )
            allowOverride(BuildConfig.DEBUG)
        }

        init()
    }

    companion object {
        lateinit var inst: App private set

        fun init() {
            // run any initialisation code here
            val initModel: InitModel = inst.get()
            initModel.initialise()
        }
    }
}
