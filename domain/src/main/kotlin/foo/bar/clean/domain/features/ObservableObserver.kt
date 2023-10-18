package foo.bar.clean.domain.features

import co.early.fore.core.observer.Observable
import co.early.fore.core.observer.ObservableGroup
import co.early.fore.core.observer.Observer
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.observer.ObservableGroupImp
import co.early.fore.kt.core.observer.ObservableImp

/**
 * Convenience class to use when you want to publish an observable state derived from a number
 * of other observable states
 */
abstract class ObservableObserver<S : State>(
    initialState: S,
    vararg observablesList: Observable,
) : ReadableState<S>, Observable {

    final override var state = initialState
        private set

    private val downstreamObservable = ObservableImp()

    private val upstreamObservable: ObservableGroup = ObservableGroupImp(*observablesList)
    private val upstreamObserver: Observer = Observer { notifyObservers() }

    init {
        Fore.i("ObsObs init()")
    }

    override fun addObserver(observer: Observer) {
        downstreamObservable.addObserver(observer)
        upstreamObservable.addObserver(upstreamObserver)
    }

    override fun removeObserver(observer: Observer) {
        downstreamObservable.removeObserver(observer)
        upstreamObservable.removeObserver(upstreamObserver)
    }

    override fun hasObservers(): Boolean {
        return downstreamObservable.hasObservers()
    }

    override fun notifyObservers() {
        state = deriveState()
        downstreamObservable.notifyObservers()
    }

    abstract fun deriveState(): S
}
