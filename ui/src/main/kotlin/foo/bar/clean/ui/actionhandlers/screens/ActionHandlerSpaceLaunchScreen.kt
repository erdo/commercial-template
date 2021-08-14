package foo.bar.clean.ui.actionhandlers.screens

import co.early.fore.kt.core.delegate.Fore
import foo.bar.clean.domain.features.auth.AuthModel
import foo.bar.clean.domain.features.navigation.Location
import foo.bar.clean.domain.features.navigation.NavigationModel
import foo.bar.clean.domain.features.spacelaunch.SpaceDetailModel
import foo.bar.clean.domain.features.spacelaunch.SpaceLaunchModel
import foo.bar.clean.ui.actionhandlers.Act
import foo.bar.clean.ui.actionhandlers.GlobalActionHandler
import foo.bar.clean.ui.actionhandlers.koinInject

class ActionHandlerSpaceLaunchScreen(
    private val spaceLaunchModel: SpaceLaunchModel = koinInject(),
    private val spaceDetailModel: SpaceDetailModel = koinInject(),
    private val authModel: AuthModel = koinInject(),
    private val navigationModel: NavigationModel = koinInject(),
) : GlobalActionHandler<Act.ScreenSpaceLaunch>() {

    override fun __handle(act: Act.ScreenSpaceLaunch) {

        Fore.i("_handle ScreenSpaceLaunch Action: $act")

        when (act) {
            is Act.ScreenSpaceLaunch.CancelBooking -> spaceDetailModel.cancelLaunch()
            is Act.ScreenSpaceLaunch.MakeBooking -> spaceDetailModel.bookLaunch()
            is Act.ScreenSpaceLaunch.SelectLaunch -> {
                spaceDetailModel.clearData()
                navigationModel.navigateTo(Location.SpaceLaunchLocations.SpaceDetailLocation(act.id))
            }
            Act.ScreenSpaceLaunch.RefreshLaunches -> spaceLaunchModel.refreshLaunchList()
            Act.ScreenSpaceLaunch.ClearErrors -> {
                spaceLaunchModel.clearError()
                spaceDetailModel.clearError()
            }
            Act.ScreenSpaceLaunch.SignIn -> authModel.signIn()
            Act.ScreenSpaceLaunch.SignOut -> authModel.signOut()
            Act.ScreenSpaceLaunch.ClearData -> spaceLaunchModel.clearData()
            is Act.ScreenSpaceLaunch.RefreshLaunchDetail -> {
                spaceDetailModel.setLaunch(act.id)
                spaceDetailModel.fetchLaunchDetail()
            }
        }
    }
}
