package foo.bar.clean.data.device.meta

import android.app.Application
import foo.bar.clean.domain.services.device.Meta
import foo.bar.clean.domain.services.device.MetaService

class MetaServiceImp(
    private val application: Application,
    private val appName: String,
) : MetaService {

    private val metaData by lazy {
        val packageName = application.packageName
        val pInfo = application.packageManager.getPackageInfo(packageName, 0)
        val versionName = if (pInfo == null) "MISSING_VERSION_NAME" else pInfo.versionName
        Meta(
            appName = appName,
            version = versionName,
            packageName = packageName,
        )
    }

    override suspend fun getMetaData(): Meta = metaData
}
