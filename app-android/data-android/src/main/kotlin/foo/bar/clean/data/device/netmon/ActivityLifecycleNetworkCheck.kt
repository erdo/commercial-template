package foo.bar.clean.data.device.netmon

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

class ActivityLifecycleNetworkCheck(
    private val networkMonitor: AndroidNetworkMonitor,
) : ActivityLifecycleCallbacks {

    override fun onActivityResumed(activity: Activity) {
        networkMonitor.checkIfDown()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}
