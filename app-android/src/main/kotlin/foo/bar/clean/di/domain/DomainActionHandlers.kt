package foo.bar.clean.di.domain

import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerCounterScreen
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerFavouritesScreen
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerFruitScreen
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerNavigationScreen
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerSettingsScreen
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerSpaceLaunchScreen
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerSplashScreen
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerTicketScreen
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerTodoScreen
import org.koin.dsl.module

/**
 * Domain Action handlers
 *
 * For this app, we are keeping the action handlers in the domain module because we share
 * the actions across multiple platforms, the navigational structures being identical.
 *
 * If your navigation is significantly different for different platforms (or might become so)
 * you can put this in the platform specific UI module
 */
val domainActionHandlers = module {

    single {
        ActionHandlerSplashScreen(
            initModel = get()
        )
    }

    single {
        ActionHandlerCounterScreen(
            counterModel = get()
        )
    }

    single { ActionHandlerTodoScreen() }

    single {
        ActionHandlerSpaceLaunchScreen(
            spaceLaunchModel = get()
        )
    }

    single {
        ActionHandlerFavouritesScreen(
            favouritesModel = get()
        )
    }

    single {
        ActionHandlerTicketScreen(
            ticketModel = get()
        )
    }

    single {
        ActionHandlerNavigationScreen(
            navigationModel = get()
        )
    }

    single {
        ActionHandlerSettingsScreen(
            settingsModel = get()
        )
    }

    single {
        ActionHandlerFruitScreen(
            fruitModel = get()
        )
    }
}
