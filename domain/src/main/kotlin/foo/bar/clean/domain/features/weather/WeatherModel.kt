package foo.bar.clean.domain.features.weather

import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.coroutine.launchIO
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.observer.ObservableImp
import co.early.fore.kt.core.type.Either.Companion.success
import co.early.fore.kt.core.type.Either.Fail
import co.early.fore.kt.core.type.Either.Success
import co.early.fore.kt.core.type.carryOn
import co.early.persista.PerSista
import foo.bar.clean.domain.services.api.WeatherService
import foo.bar.clean.domain.features.ReadableState

/**
 * The purpose of this model is to fetch a "weather report". It does this by making 3 service
 * (network) connections to fetch: a list of pollen counts, a list of temperatures, a list of
 * windSpeeds (for each list, one item is chosen at random to make the UI interesting).
 *
 * Each service connection returns an Either<Error, Data> the carryOn() function only continues if
 * the previous connection was a success. If the previous connection failed, then processing stops
 * and the Either<Error> is past back up to the result.
 *
 * This is an observable model that publishes state fetched from a service, it's initial state
 * is whatever stated it previously persisted.
 *
 * Open the Structure pane on the left to get an overview of the public API for this model
 *
 * Copyright © 2015-2023 early.co. All rights reserved.
 */
class WeatherModel(
    private val weatherService: WeatherService,
    private val perSista: PerSista,
) : ReadableState<WeatherState>, Observable by ObservableImp() {

    override var state = WeatherState(loading = true)
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

    /**
     * fetch weather reports using Ktor
     */
    fun fetchWeatherReport() {

        Fore.i("fetchWeatherReport() thread:" + Thread.currentThread().id)

        if (state.loading) {
            return
        }

        updateState(state.copy(loading = true, error = null))

        launchIO {

            Fore.i("in scope from Dispatchers.IO, thread:" + Thread.currentThread().id)

            var partialWeatherReport = WeatherReport()

            val weatherReport = weatherService.getPollenCount()
                .carryOn {
                    Fore.i("received pollenCount success")
                    partialWeatherReport = partialWeatherReport.copy(
                        pollenCount = it
                    )
                    weatherService.getTemperature()
                }
                .carryOn {
                    Fore.i("received temperature success")
                    partialWeatherReport = partialWeatherReport.copy(
                        temperature = it
                    )
                    weatherService.getWindSpeed()
                }
                .carryOn {
                    Fore.i("received windSpeed success")
                    partialWeatherReport = partialWeatherReport.copy(
                        windSpeed = it
                    )
                    success(partialWeatherReport)
                }

            Fore.i("requests are all complete, thread:" + Thread.currentThread().id)

            val newState = when (weatherReport) {
                is Success -> WeatherState(
                    weatherReport = weatherReport.value,
                    error = null,
                    loading = false
                )

                is Fail -> WeatherState(
                    error = weatherReport.value,
                    loading = false
                )
            }
            updateState(newState)
        }
    }

    private fun updateState(newState: WeatherState) {
        state = newState
        perSista.write(state) {
            notifyObservers()
        }
    }
}
