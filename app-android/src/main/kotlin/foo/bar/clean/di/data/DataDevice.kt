package foo.bar.clean.di.data

import android.content.Context
import android.net.ConnectivityManager
import android.os.Environment
import co.early.fore.core.time.SystemTimeWrapper
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.net.InterceptorLogging
import foo.bar.clean.App
import foo.bar.clean.data.device.assets.AndroidAssetReader
import foo.bar.clean.data.device.file.FileReaderWriterImp
import foo.bar.clean.data.device.netmon.AndroidNetworkMonitor
import foo.bar.clean.domain.services.device.AssetReader
import foo.bar.clean.domain.services.device.FileReaderWriter
import foo.bar.clean.domain.services.device.NetworkMonitorService
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Device Data Sources
 */
val dataDevice = module {

    single { androidContext() as App }

    single {
        (get() as App).filesDir
    }

    single {
        Fore.getLogger()
    }

    single {
        SystemTimeWrapper()
    }

    single<AssetReader> {
        AndroidAssetReader(
            assetManager = (get() as App).assets,
            mainDispatcher = Dispatchers.IO,
            readDispatcher = Dispatchers.Main,
        )
    }

    single<FileReaderWriter> {
        FileReaderWriterImp(
            dataDirectory = Environment.getDataDirectory(),
            subFolder = "external",
            logger = get(),
        )
    }

    single<NetworkMonitorService> {
        AndroidNetworkMonitor(
            connectivityManager = (get() as App).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager,
            okHttpClient = OkHttpClient().newBuilder().addInterceptor(InterceptorLogging()).build(),
            application = get(),
            logger = get(),
        )
    }
}
