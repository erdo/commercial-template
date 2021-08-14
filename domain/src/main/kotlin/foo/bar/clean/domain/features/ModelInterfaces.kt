package foo.bar.clean.domain.features

import co.early.fore.core.observer.Observable
import co.early.fore.core.observer.Observer
import co.early.fore.kt.core.coroutine.asyncMain
import foo.bar.clean.domain.DomainError
import kotlinx.coroutines.delay

interface State

interface CanLoad {
    val loading: Boolean
}

interface CanError {
    val error: DomainError
}

interface ReadableState<S : State> : Observable {
    val state: S
}

/**
 * We use these more restrictive versions of [ReadableState] for models that are important for
 * initialisation, it helps us treat all of these models in the same way (we need to call load()
 * on all of them, initialisation is complete once all of them are loading = false, and
 * initialisation fails if any of them have an error)
 */
interface ReadableStateCanLoad<S> : ReadableState<S>,
    Observable where S : State, S : CanLoad {
    fun load()
}

interface ReadableStateCanLoadCanError<S> : ReadableStateCanLoad<S>,
    Observable where S : State, S : CanLoad, S : CanError


/**
 * We want to keep our model API clean and UDF style where possible (public, fast returning,
 * non-suspend functions for mutation, reply comes back via observed state change). But sometimes
 * this is awkward for client code, a good example is during initialisation where we want some
 * models to initialise in a certain order.
 *
 * We could just add callback listeners or suspend functions to our models to support this case,
 * but it would degrade our model's API for all the other state driven code. So we use these
 * extension functions instead.
 */
suspend fun ReadableStateCanLoad<*>.observeUntilLoaded() {
    load()
    return asyncMain {
        lateinit var temporaryObserver: Observer
        temporaryObserver = Observer {
            if ((state as CanLoad).loading.not()) {
                asyncMain {
                    removeObserver(temporaryObserver)
                }
            }
        }
        addObserver(temporaryObserver)

        while ((state as CanLoad).loading) {
            delay(100)
        }
    }.await()
}

suspend fun ReadableState<*>.observeUntil(continueWhenTrue: () -> Boolean) {
    return asyncMain {
        lateinit var temporaryObserver: Observer
        temporaryObserver = Observer {
            if (continueWhenTrue()) {
                asyncMain {
                    removeObserver(temporaryObserver)
                }
            }
        }
        addObserver(temporaryObserver)

        while (continueWhenTrue()) {
            delay(100)
        }
    }.await()
}
