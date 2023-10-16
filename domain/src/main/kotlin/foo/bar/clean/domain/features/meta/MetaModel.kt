package foo.bar.clean.domain.features.meta

import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.coroutine.awaitMain
import co.early.fore.kt.core.coroutine.launchIO
import co.early.fore.kt.core.observer.ObservableImp
import foo.bar.clean.domain.features.ReadableStateCanLoad
import foo.bar.clean.domain.services.device.MetaService

/**
 * Maintains the meta data for the app for easy access (see MetaState) the data is loaded during
 * initialisation by the PreInitModel
 *
 * Copyright © 2015-2023 early.co. All rights reserved.
 */
class MetaModel(
    private val metaService: MetaService,
) : ReadableStateCanLoad<MetaState>, Observable by ObservableImp() {

    override var state = MetaState()
        private set

    override fun load() {

        if (state.loading) {
            return
        }

        state = state.copy(loading = true)
        notifyObservers()

        launchIO {
            val metaData = metaService.getMetaData()
            awaitMain {
                state = state.copy(
                    meta = metaData,
                    loading = false,
                )
                notifyObservers()
            }
        }
    }
}
