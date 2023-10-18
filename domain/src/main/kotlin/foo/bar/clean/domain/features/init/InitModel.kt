package foo.bar.clean.domain.features.init

import co.early.fore.kt.core.coroutine.launchMain
import co.early.fore.kt.core.delegate.Fore
import co.early.persista.PerSista
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.features.CanError
import foo.bar.clean.domain.features.CanLoad
import foo.bar.clean.domain.features.ObservableObserver
import foo.bar.clean.domain.features.ReadableStateCanLoad
import foo.bar.clean.domain.features.observeUntilLoaded

/**
 * The initialisation model is a little different to the rest, so it's not a good template
 * to use for your models.
 *
 * This model observes other models that we want to be ready before we allow the user to interact with the
 * application. This can include models that download config files, load view settings, check
 * session tokens etc.
 *
 * If you're new to these types of observable models, CounterModel is a good place to start
 *
 * The state of this model is observed by the SplashScreen composable, and the splash screen
 * remains in place until the state of this model switches to either Ready or Error
 *
 * Copyright © 2015-2023 early.co. All rights reserved.
 */
class InitModel(
    private val preInitModel: PreInitModel,
    private val perSista: PerSista,
    vararg models: ReadableStateCanLoad<*>,
) : ObservableObserver<InitState>(
    initialState = InitState(),
    preInitModel,
    *models,
) {

    private val loadableModels: List<ReadableStateCanLoad<*>> = listOf(*models)
    private var preInitComplete = false

    init {
        Fore.i("InitModel init()")
        require(models.isNotEmpty()) { "you must specify at least one model for initialisation" }
        preInitModel.addObserver {
            this@InitModel.notifyObservers()
        }
    }

    fun initialise() {
        Fore.i("initialise()")

        if (state.step is Step.Loading) {
            return
        }

        preInitComplete = false

        //perSista.wipeEverything { } // uncomment this line to clear persistent storage on start up

        launchMain {

            preInitModel.observeUntilLoaded()

            Fore.d("pre-init loaded")

            if (!preInitModel.state.preInitFailed()) {
                // start loading remaining models
                loadableModels.forEach { model ->
                    Fore.i("model load about to be called on:$model")
                    model.load()
                    Fore.i("model load called on:$model")
                }
            }

            Fore.d("pre-init complete, remaining init load started")

            preInitComplete = true
        }
    }

    fun retry() {
        if (state.step is Step.Loading) {
            return
        }
        initialise()
    }

    override fun deriveState(): InitState {

        Fore.i("deriveState()")

        val initError = loadableModels.findFirstError()
        val loadingCount = loadableModels.numberStillLoading()
        Fore.d("loadingCount:$loadingCount")
        val loadingTotal = loadableModels.size
        val overallProgress = overallProgress(
            preInitProgress = preInitModel.state.preInitProgress,
            preInitComplete = preInitComplete,
            initProgress = ((loadingTotal - loadingCount).toFloat() / loadingTotal.toFloat())
        )

        Fore.d("overallProgress:$overallProgress preInitProgress:${preInitModel.state.preInitProgress} preInitError:${preInitModel.state.error} preInitComplete:$preInitComplete loadingCount:$loadingCount preInitFailed():${preInitModel.state.preInitFailed()} modelsError:$initError")

        return preInitModel.state.run {
            when {
                preInitUninitialised() -> InitState(step = Step.Uninitialised)
                preInitFailed() -> InitState(step = Step.Error(error))
                initError != DomainError.NoError -> InitState(step = Step.Error(initError))
                !preInitComplete || loadingCount > 0 -> InitState(
                    step = Step.Loading(
                        overallProgress
                    )
                )

                else -> InitState(Step.Ready(nagBeforeStart = (error == DomainError.UpgradeNag)))
            }
        }
    }
}

private fun List<ReadableStateCanLoad<*>>.findFirstError(): DomainError {
    return mapNotNull { model ->
        if (model is CanError) {
            model.error
        } else null
    }.find { error -> error != DomainError.NoError } ?: DomainError.NoError
}

private fun List<ReadableStateCanLoad<*>>.numberStillLoading(): Int {
    return count { model ->
        Fore.d("model loading check:$model ${(model.state as CanLoad).loading}")
        (model.state as CanLoad).loading
    }
}

private fun overallProgress(
    preInitProgress: Float,
    preInitComplete: Boolean,
    initProgress: Float = 0f,
): Float {
    val overallProgress = preInitProgress * 0.5f +
            if (preInitComplete) {
                initProgress * 0.5f
            } else 0f
    return if (overallProgress > 0.65) 0.95f else overallProgress
}

