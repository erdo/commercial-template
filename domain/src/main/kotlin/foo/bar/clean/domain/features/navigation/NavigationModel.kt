package foo.bar.clean.domain.features.navigation

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
 * The purpose of this model is to maintain a backstack of locations, popping and pushing
 * locations as the user navigates the app. The top of the back stack (the last element in the
 * list) is the current location.
 *
 * Open the Structure pane on the left to get an overview of the public API for this model, also
 * see the UnitTests for a full understanding of the behaviour
 *
 *

 * Example Locations:
 *
 * sealed class Location {
 *
 *     data object NewYork : Location()
 *
 *     data object Tokyo : Location()
 *
 *     data class Sydney(val withSunCreamFactor: Int? = null) : Location()
 *
 *     data object SunCreamSelector: Location()
 *
 *     sealed class EuropeanLocations : Location() {
 *
 *         data object London : EuropeanLocations()
 *
 *         data object Paris : EuropeanLocations()
 *     }
 * }
 *
 * NavigationModel(homeLocation: London)
 *
 *
 * A. Regular forward navigation
 *
 * navigateTo(Paris)
 * navigateTo(NewYork)
 *
 * backstack: London > Paris > NewYork
 * currentLocation: NewYork
 *
 *
 * B. Regular back navigation
 *
 * navigateTo(Paris)
 * navigateTo(NewYork)
 * popBackStack()
 *
 * backstack: London > Paris
 * currentLocation: Paris
 *
 *
 * C. Back navigation multiple steps
 *
 * navigateTo(Paris)
 * navigateTo(NewYork)
 * popBackStack(times = 2)
 *
 * backstack: London
 * currentLocation: London
 *
 *
 * D. Visiting locations more than once
 *
 * navigateTo(Paris)
 * navigateTo(NewYork)
 * navigateTo(Paris)
 * navigateTo(Tokyo)
 *
 * backstack: London > Paris > NewYork > Paris > Tokyo
 * currentLocation: Tokyo
 *
 *
 * E. Recycling previous locations
 *
 * navigateTo(Paris)
 * navigateTo(NewYork)
 * navigateBackTo(Paris)
 * navigateTo(Tokyo)
 *
 * backstack: London > Paris > Tokyo
 * currentLocation: Tokyo
 *
 *
 * F. Recycling previous locations when they were never visited in the first place
 *
 * navigateTo(Sydney)
 * navigateTo(NewYork)
 * navigateBackTo(Paris)
 * navigateTo(Tokyo)
 *
 * backstack: London > Sydney > NewYork > Paris > Tokyo
 * currentLocation: Tokyo
 *
 *
 * G. Visiting a location that you don't want added to the back stack
 *
 * navigateTo(Sydney)
 * navigateTo(NewYork)
 * navigateTo(Paris, addToHistory = false)
 * navigateTo(Tokyo)
 *
 * backstack: London > Sydney > NewYork > Tokyo
 * currentLocation: Tokyo
 *
 *
 * H. Returning data from the current Location to a Location further back in the stack
 *
 * navigateTo(Paris)
 * navigateTo(NewYork)
 * navigateTo(Sydney)
 * navigateTo(SunCreamSelector)
 * popBackStack { it -> //Sydney
 *    when(it){
 *      Sydney -> it.copy(withSunCreamFactor = 30)
 *      else -> it
 *    }
 * }
 *
 * backstack: London > Paris > NewYork > Sydney(30)
 * currentLocation: Sydney(30)
 *
 *
 * I. Arbitrarily rewriting the entire backstack
 *
 * updateBackStack(
 *    listOf(
 *      Paris,
 *      London,
 *      Sydney(50),
 *      Tokyo,
 *      London
 *    )
 * )
 *
 * backstack: Paris > London > Sydney(50) > Tokyo > London
 * currentLocation: London
 *
 *
 *
 * Copyright © 2015-2023 early.co. All rights reserved.
 */
class NavigationModel(
    private val perSista: PerSista,
    homeLocation: Location = HOME_LOCATION,
) : ReadableStateCanLoad<NavigationState>, Observable by ObservableImp() {

    override var state = NavigationState(backStack = listOf(homeLocation))
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

    fun navigateTo(location: Location, addToHistory: Boolean = true) {
        Fore.i("navigateTo() ${location.javaClass.simpleName} addToHistory:$addToHistory")
        val stack = state.backStack.toMutableList()
        if (!state.currentLocationWillBeAddedToHistoryOnNextNavigation) {
            stack.removeLast()
        }
        stack.add(location)
        updateState(
            state.copy(
                backStack = stack,
                currentLocationWillBeAddedToHistoryOnNextNavigation = addToHistory
            )
        )
    }

    fun navigateBackTo(location: Location, addToHistory: Boolean = true) {
        Fore.i("navigateBackTo() ${location.javaClass.simpleName} addToHistory:$addToHistory")

        val indexInBackStack = state.backStack.indexOfLast {
            it.javaClass.canonicalName == location.javaClass.canonicalName
        }

        val newBackStack = if (indexInBackStack >= 0) {
            state.backStack.subList(0, indexInBackStack)
        } else {
            Fore.i(" >>> ${location.javaClass.simpleName} not found in back stack, adding new location: ${location.javaClass.simpleName}")
            state.backStack
        }.toMutableList()

        newBackStack.add(location)
        updateState(
            state.copy(
                backStack = newBackStack,
                currentLocationWillBeAddedToHistoryOnNextNavigation = addToHistory
            )
        )
    }

    /**
     * @setData - use this to pass data to locations further back in the backstack. Once the
     * backStack has been popped the required number of times, setData{} will be run with the
     * new current location passed in as a parameter. This gives the caller an opportunity to
     * set data on the new location before it is set at the new top of the backstack
     *
     * returns false if we cannot go back any further (i.e. we are already at the home location)
     */
    fun popBackStack(times: Int = 1, setData: (Location) -> Location = { it }): Boolean {
        Fore.i("popBackStack() times:$times")
        if (state.backStack.size == 1) {
            return false
        }

        val stack = state.backStack.toMutableList()
        if (state.backStack.size > times) {
            for (count in 1..times) {
                stack.removeLast()
            }
        } else {
            stack.clear()
            stack.add(state.backStack.first())
        }
        val locationWithData = setData(stack.last())
        stack.removeLast()
        stack.add(locationWithData)
        updateState(
            state.copy(
                backStack = stack,
                currentLocationWillBeAddedToHistoryOnNextNavigation = true
            )
        )
        return true
    }

    /**
     * @newBackStack - [0] represents the home location, [size-1] is the current page
     */
    fun updateBackStack(newBackStack: List<Location>, currentLocationAddToHistory: Boolean = true) {
        Fore.i("updateBackStack()")
        require(newBackStack.isNotEmpty()) {
            Fore.e("newBackStack is empty")
            "newBack stack cannot be empty, it needs to contain at least one location"
        }
        updateState(
            state.copy(
                backStack = newBackStack,
                currentLocationWillBeAddedToHistoryOnNextNavigation = currentLocationAddToHistory
            )
        )
    }

    override fun toString(): String = toString(" > ")

    fun toString(breadCrumbIndicator: String): String {
        return buildString {
            state.backStack.forEach {
                append(it.javaClass.simpleName)
                append(breadCrumbIndicator)
            }
            deleteRange(length - breadCrumbIndicator.length, length - 1)
        }
    }

    private fun updateState(newState: NavigationState) {
        state = newState
        perSista.write(state) {
            notifyObservers()
        }
    }
}
