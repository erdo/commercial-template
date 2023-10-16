package foo.bar.clean.domain.features.network

import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.observer.ObservableImp
import foo.bar.clean.domain.features.ReadableState
import foo.bar.clean.domain.services.device.DeviceNetworkChange
import foo.bar.clean.domain.services.device.NetworkMonitorService

/**
 * This is a basic observable model that publishes a network available state, the state
 * is derived from device information, so we don't store anything persistently, see CounterModel
 * for a basic model that persists its state
 *
 * The purpose of this model is to maintain a network state so that this information can be
 * displayed on a UI.
 *
 * NB: don't use the state to decide whether you should attempt a network
 * call or not: ALWAYS attempt the call anyway and handle the failure if necessary
 *
 * Open the Structure pane on the left to get an overview of the public API for this model
 *
 * Copyright © 2015-2023 early.co. All rights reserved.
 */
class NetworkModel(
    networkMonitorService: NetworkMonitorService,
) : ReadableState<NetworkState>, Observable by ObservableImp() {

    override var state: NetworkState = NetworkState()
        private set

    init {
        Fore.i("init()")
        networkMonitorService.setCallBack(object : DeviceNetworkChange {
            override fun onAvailable() {
                Fore.i("onAvailable()")
                state = state.copy(
                    availability = Availability.Available,
                )
                notifyObservers()
            }

            override fun onLost() {
                Fore.i("onLost()")
                state = state.copy(
                    availability = Availability.Unavailable,
                )
                notifyObservers()
            }
        })
    }
}
