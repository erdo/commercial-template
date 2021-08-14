package foo.bar.clean.domain.services.device

interface DeviceNetworkChange {
    fun onAvailable()
    fun onLost()
}

interface NetworkMonitorService {
    fun setCallBack(networkChangeCallback: DeviceNetworkChange?)
}