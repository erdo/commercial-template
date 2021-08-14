package foo.bar.clean.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.ui.size.WindowSize
import foo.bar.clean.domain.features.navigation.Location
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerCounterScreen
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerFavouritesScreen
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerFruitScreen
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerNavigationScreen
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerSettingsScreen
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerSpaceLaunchScreen
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerTicketScreen
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerTodoScreen
import foo.bar.clean.ui.navigation.AppNavigation
import foo.bar.clean.ui.navigation.NavHost
import foo.bar.clean.ui.common.splash.SplashScreen
import foo.bar.clean.ui.navigation.AppBottomBarItems
import foo.bar.clean.ui.theme.AppTheme
import org.koin.compose.koinInject

class Activity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)

        Fore.i("onCreate()")

        setContent {
            AppTheme {
                WindowSize {
                    SplashScreen {
                        NavHost { navigationState ->
                            val location = navigationState.currentPage()

                            Fore.i("Location is:$location")

                            AppNavigation(
                                currentLocation = location,
                                userActionHandler = when(location){
                                    Location.CounterLocation -> (koinInject() as foo.bar.clean.ui.actionhandlers.screens.ActionHandlerCounterScreen)
                                    is Location.TodoLocations -> (koinInject() as foo.bar.clean.ui.actionhandlers.screens.ActionHandlerTodoScreen)
                                    is Location.SpaceLaunchLocations -> (koinInject() as foo.bar.clean.ui.actionhandlers.screens.ActionHandlerSpaceLaunchScreen)
                                    Location.FavouritesLocation -> (koinInject() as foo.bar.clean.ui.actionhandlers.screens.ActionHandlerFavouritesScreen)
                                    is Location.SettingsLocations -> (koinInject() as foo.bar.clean.ui.actionhandlers.screens.ActionHandlerSettingsScreen)
                                    is Location.FruitLocation -> (koinInject() as foo.bar.clean.ui.actionhandlers.screens.ActionHandlerFruitScreen)
                                    is Location.TicketLocation -> (koinInject() as foo.bar.clean.ui.actionhandlers.screens.ActionHandlerTicketScreen)
                                    Location.NavigationLocation -> (koinInject() as foo.bar.clean.ui.actionhandlers.screens.ActionHandlerNavigationScreen)
                                },
                                bottomBarItems = when (location) {
                                    is Location.TicketLocation -> emptyList()
                                    else ->  AppBottomBarItems(location)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
