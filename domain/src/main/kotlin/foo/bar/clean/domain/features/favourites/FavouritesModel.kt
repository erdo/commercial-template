package foo.bar.clean.domain.features.favourites

import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.coroutine.launchIO
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.observer.ObservableImp
import co.early.persista.PerSista
import foo.bar.clean.domain.features.ReadableState

/**
 * This is a basic observable model that exposes its state and persists that state across rotation
 * or process death with PerSista (PerSista stores data classes as json on the file system). For a
 * basic model which doesn't persist its state see NetworkModel or TicketModel.
 *
 * The purpose of this model is to maintain a list of favourites which the user can add items to or
 * remove items from
 *
 * Open the Structure pane on the left to get an overview of the public API for this model
 *
 * Copyright © 2015-2023 early.co. All rights reserved.
 */
class FavouritesModel(
    private val perSista: PerSista,
) : ReadableState<FavouritesState>, Observable by ObservableImp() {

    override var state = FavouritesState(loading = true)
        private set

    init {
        launchIO {
            perSista.read(state) {
                state = it.copy(
                    loading = false,
                )
                notifyObservers()
            }
        }
    }

    fun toggleFavourite(feature: Feature) {

        if (state.loading) {
            return
        }

        val newList = if (state.isFavourite(feature)) {
            removeFavourite(feature)
        } else {
            addFavourite(feature)
        }

        updateState(
            FavouritesState(favourites = newList)
        )
    }

    fun clearAllFavourites() {

        if (state.loading) {
            return
        }

        updateState(FavouritesState())
    }

    private fun removeFavourite(feature: Feature): List<Feature> {
        Fore.i("remove favourite $feature")
        return state.favourites.filter {
            it != feature
        }
    }

    private fun addFavourite(feature: Feature): List<Feature> {
        Fore.i("adding favourite $feature")
        val newFavourites = state.favourites.toMutableList()
        newFavourites.add(feature)
        return newFavourites
    }

    private fun updateState(newState: FavouritesState) {
        state = newState
        perSista.write(state) {
            notifyObservers()
        }
    }
}
