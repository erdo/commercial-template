package foo.bar.clean.domain.features.settings

import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.coroutine.launchIO
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.observer.ObservableImp
import co.early.persista.PerSista
import foo.bar.clean.domain.features.ReadableStateCanLoad

/**
 * This is a basic observable model that exposes its state and persists that state across rotation
 * or process death with PerSista (PerSista stores data classes as json on the file system). For a
 * basic model which doesn't persist its state see NetworkModel or TicketModel.
 *
 * The purpose of this model is to the users preferences
 *
 * Open the Structure pane on the left to get an overview of the public API for this model
 *
 * Copyright © 2015-2023 early.co. All rights reserved.
 */
class SettingsModel(
    private val perSista: PerSista,
) : ReadableStateCanLoad<SettingsState>, Observable by ObservableImp() {

    override var state = SettingsState()
        private set

    override fun load() {

        Fore.i("load()")

        if (state.loading) {
            return
        }

        state = state.copy(loading = true)
        notifyObservers()

        launchIO {
            perSista.read(state) {
                state = it.copy(
                    loading = false,
                )
                notifyObservers()
            }
        }
    }

    fun setDarkMode(darkMode: DarkMode) {
        updateState(
            state.copy(darkMode = darkMode)
        )
    }

    private fun updateState(newState: SettingsState) {
        state = newState
        perSista.write(state) {
            notifyObservers()
        }
    }
}
