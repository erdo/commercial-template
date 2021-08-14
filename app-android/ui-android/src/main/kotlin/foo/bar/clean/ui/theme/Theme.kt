package foo.bar.clean.ui.theme

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import co.early.fore.compose.observeAsState
import foo.bar.clean.domain.features.settings.DarkMode
import foo.bar.clean.domain.features.settings.SettingsModel
import foo.bar.clean.domain.features.settings.SettingsState
import foo.bar.clean.domain.features.ReadableState
import org.koin.compose.koinInject

val lightColors = AppColors(
    dark = false,
    paper = Color(0xFFF5F5F5),
    textDefault = Color(0xFF352019),
    textOnBranding = Color(0xFFFFFFFF),
    primary = Color(0xFFFFAA00),
    secondary = Color(0xFFFF7700),
    tertiary = Color(0xFF03A9F4),
    navBackground = Color(0xFFC5C5C5),
    navSelectedBackground = Color(0xFFA0A0A0),
    navSelectedIcon = Color(0xFFFFFFFF),
    navTitles = Color(0xFF4B4B50),
    navTextAndIcons = Color(0xFF65646B),
//    error = Color(0xFFBA1A1A),
//    startNav = Color(0xFF00A5D6),
//    startNavSelected = Color(0xFF00C4FF),
//    errorContainer = Color(0xFFFFDAD6),
//    errorContainerOn = Color(0xFF410002),
//    componentOffBackground = Color(0xFFF1F1F1),
)
val darkColors = AppColors(
    dark = true,
    paper = Color(0xFF2E2E2E), // main screen background
    textOnBranding = Color(0xFFFFFFFF),
    textDefault = Color(0xFFC9C9C9),
    primary = Color(0xFFFFAA00), // selected toggles and switches, enabled buttons
    secondary = Color(0xFFFF7700),
    tertiary = Color(0xFF3F51B5),
    navBackground = Color(0xFF242323),
    navSelectedBackground = Color(0xFF3D3D3D),
    navSelectedIcon = Color(0xFFFFAA00),
    navTitles = Color(0xFFA7A7A7),
    navTextAndIcons = Color(0xFF999999), // toggle and switch colour when turned off, menu items when unselected
    error = Color(0xFFBA1A1A),
)

@Composable
fun AppTheme(
    settingsStateProvider: ReadableState<SettingsState> = (koinInject() as SettingsModel),
    isSystemDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {

    val settingsDarkMode by settingsStateProvider.observeAsState { settingsStateProvider.state.darkMode }

    val appColors = when (settingsDarkMode) {
        DarkMode.Dark -> darkColors
        DarkMode.Light -> lightColors
        DarkMode.System -> if (isSystemDark) {
            darkColors
        } else lightColors
    }

    adjustStatusBar(appColors, isSystemDark)

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = appColors.toColorScheme(),
            content = {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(appColors.paper)) {
                        content()
                    }
                }
            }
        )
    }
}

@Composable
fun adjustStatusBar(appColors: AppColors, isSystemDark: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = appColors.navBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                !isSystemDark
        }
    }
}

val LocalAppColors = compositionLocalOf<AppColors> {
    error(
        "To access LocalColors, your compose code must be wrapped in a AppTheme{}" +
                "block, we'd suggest somewhere high up in the UI tree/hierarchy, just inside" +
                "setContent{}"
    )
}
