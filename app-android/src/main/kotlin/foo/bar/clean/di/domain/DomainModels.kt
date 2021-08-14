package foo.bar.clean.di.domain

import co.early.fore.kt.core.delegate.Fore
import foo.bar.clean.domain.features.auth.AuthModel
import foo.bar.clean.domain.features.config.ConfigModel
import foo.bar.clean.domain.features.counter.CounterModel
import foo.bar.clean.domain.features.favourites.FavouritesModel
import foo.bar.clean.domain.features.featureflags.FeatureFlagsModel
import foo.bar.clean.domain.features.fruit.FruitModel
import foo.bar.clean.domain.features.init.InitModel
import foo.bar.clean.domain.features.init.PreInitModel
import foo.bar.clean.domain.features.meta.MetaModel
import foo.bar.clean.domain.features.navigation.NavigationModel
import foo.bar.clean.domain.features.network.NetworkModel
import foo.bar.clean.domain.features.settings.SettingsModel
import foo.bar.clean.domain.features.spacelaunch.SpaceDetailModel
import foo.bar.clean.domain.features.spacelaunch.SpaceLaunchModel
import foo.bar.clean.domain.features.ticket.TicketModel
import foo.bar.clean.domain.features.todo.TodoModel
import foo.bar.clean.domain.features.weather.WeatherModel
import org.koin.dsl.module

/**
 * Domain Models (Features)
 */
val domainModels = module {

    single {
        MetaModel(
            metaService = get(),
        )
    }

    single {
        PreInitModel(
            metaModel = get(),
            phoneHomeService = get(),
            settingsModel = get(),
            endpointsService = get(),
        )
    }

    single {
        InitModel(
            preInitModel = get(),
            perSista = get(),
            get() as ConfigModel,
            get() as FeatureFlagsModel,
            get() as NavigationModel,
        )
    }

    single {
        ConfigModel(
            configService = get(),
            perSista = get(),
        )
    }

    single {
        TodoModel(
            todoItemService = get(),
            perSista = get(),
        )
    }

    single {
        FeatureFlagsModel(
            featureFlagsService = get(),
            perSista = get(),
        )
    }

    single {
        CounterModel(
            configModel = get(),
            perSista = get(),
        )
    }

    single {
        FavouritesModel(
            perSista = get(),
        )
    }

    single {
        FruitModel(
            fruitService = get(),
        )
    }

    single {
        NavigationModel(
            perSista = get(),
        )
    }

    single {
        NetworkModel(
            networkMonitorService = get(),
        )
    }

    single {
        SettingsModel(
            perSista = get(),
        )
    }

    single {
        TicketModel(
            fruitService = get(),
            ticketService = get(),
        )
    }

    single {
        WeatherModel(
            weatherService = get(),
            perSista = get(),
        )
    }

    single {
        AuthModel(
            authService = get(),
        )
    }

    single {
        SpaceLaunchModel(
            launchService = get(),
        )
    }

    single {
        SpaceDetailModel(
            launchService = get(),
        )
    }
}
