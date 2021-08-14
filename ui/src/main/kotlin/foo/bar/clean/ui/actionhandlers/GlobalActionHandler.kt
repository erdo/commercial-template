package foo.bar.clean.ui.actionhandlers

import co.early.fore.kt.core.delegate.Fore
import foo.bar.clean.domain.features.favourites.FavouritesModel
import foo.bar.clean.domain.features.navigation.Location
import foo.bar.clean.domain.features.navigation.NavigationModel
import foo.bar.clean.domain.services.api.Fruit
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.mp.KoinPlatformTools

abstract class GlobalActionHandler<T : Act>(
    private val favouritesModel: FavouritesModel = koinInject(),
    private val navigationModel: NavigationModel = koinInject(),
) : ActionHandler<T>() {

    override fun _handle(act: Act.Global) {

        Fore.i("_handle Global Action: $act")

        when (act) {
            Act.Global.Back -> navigationModel.popBackStack()
            Act.Global.ToCounterScreen -> navigationModel.navigateBackTo(Location.CounterLocation)
            Act.Global.ToTicketScreen -> navigationModel.navigateBackTo(Location.TicketLocation)
            is Act.Global.ToFruitScreen -> navigationModel.navigateBackTo(
                Location.FruitLocation(overrideFruit = Fruit.FruitNone)
            )
            Act.Global.ToFavouriteScreen -> navigationModel.navigateBackTo(Location.FavouritesLocation)
            Act.Global.ToSettingsScreen -> navigationModel.navigateBackTo(Location.SettingsLocations.SettingsLocation())
            is Act.Global.ToggleFavourite -> favouritesModel.toggleFavourite(act.feature)
            Act.Global.ToSpaceLaunchScreen -> navigationModel.navigateBackTo(Location.SpaceLaunchLocations.SpaceLaunchLocation)
            Act.Global.ToTodoScreen -> navigationModel.navigateBackTo(Location.TodoLocations.TodoLocation)
            Act.Global.ToNavigationScreen -> navigationModel.navigateBackTo(Location.NavigationLocation)
        }
    }
}

/**
 * lets us access koin's inject() function from a default parameter in a non compose context
 */
inline fun <reified T : Any> koinInject(
    qualifier: Qualifier? = null,
    mode: LazyThreadSafetyMode = KoinPlatformTools.defaultLazyMode(),
    noinline parameters: ParametersDefinition? = null,
): T = KoinPlatformTools.defaultContext().get().inject<T>().value
