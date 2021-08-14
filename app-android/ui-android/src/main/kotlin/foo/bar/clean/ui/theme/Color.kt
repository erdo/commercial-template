package foo.bar.clean.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

data class AppColors(
    val dark:Boolean,
    /**
     * For DARK mode, this is usually a dark color
     */
    val paper: Color,
    /**
     * For DARK mode, this is usually a light color
     */
    val textDefault: Color,
    /**
     * This colour is used for text which is displayed over the branding colours
     * such as primary, secondary and tertiary
     */
    val textOnBranding: Color,
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    /**
     * TopAppBar, BottomNavBar, StatusBar background
     */
    val navBackground: Color,
    /**
     * selected menu item background
     */
    val navSelectedBackground: Color,
    /**
     * selected menu item icon
     */
    val navSelectedIcon: Color,
    /**
     * TopAppBar title, burger menu, BottomNavBar selected text
     */
    val navTitles: Color,
    /**
     * BottomNavBar unselected text, toggle and switch colour when turned
     * off, menu item icons when unselected
     */
    val navTextAndIcons: Color,
    /**
     *  error color, usually a shade of red, the rest of the error colors will be derived if you
     *  don't specify them
     */
    val error: Color = Color.Red,
    /**
     *  modal start drawer menu background
     */
    val startNav: Color = navSelectedBackground,
    /**
     * modal start drawer menu selected item background
     */
    val startNavSelected: Color = navBackground,
    /**
     * modal start drawer menu selected item text
     */
    val startNavText: Color = navSelectedIcon,
    val paperOn: Color = textDefault,
    val primaryOn: Color = textOnBranding,
    val secondaryOn: Color = textOnBranding,
    val tertiaryOn: Color = textOnBranding,
    val errorOn: Color = textOnBranding, // ?
    val errorContainer: Color = lerp(error, paper, 0.9f),
    val errorContainerOn: Color = lerp(error, paperOn, 0.9f),
    /**
     * shadow caused by elevation
     */
    val scrimShadow: Color = Color.Black,
    val loadingShadow: Color = lerp(Color.Transparent, Color.White,0.50f),
    /**
     * switch background when turned off
     */
    val componentOffBackground: Color = lerp(navTextAndIcons, paper, 0.90f), //Color(0xFFF1F1F1),
    val btnPositive: Color = primary,
    val btnNegative: Color = secondary,
    val btnNeutral: Color = navTextAndIcons,
    val btnDanger: Color = error,
)

/**
 * Map to the M3 color scheme
 */
fun AppColors.toColorScheme(): ColorScheme {
    return ColorScheme(
        primary = primary,  // selected toggles and switches, enabled buttons
        onPrimary = primaryOn,
        primaryContainer = Color.Transparent,
        onPrimaryContainer = primaryOn,
        secondary = secondary,
        onSecondary = secondaryOn,
        secondaryContainer = navSelectedBackground,
        onSecondaryContainer = navSelectedIcon,
        tertiary = tertiary,
        onTertiary = tertiaryOn,
        tertiaryContainer = Color.Transparent,
        onTertiaryContainer = tertiaryOn,
        background = paper,  // main screen background
        onBackground = paperOn,
        surface = navBackground, // TopAppBar, BottomNavBar, StatusBar
        surfaceTint = navBackground,
        onSurface = navTitles,
        surfaceVariant = componentOffBackground, //background of toggle switch when turned off
        onSurfaceVariant = navTextAndIcons, //disabled icons and unselected nav bar text
        outline = navTextAndIcons, //switch border color
        outlineVariant = navTextAndIcons,
        inverseSurface = startNav,
        inversePrimary = startNavSelected,
        inverseOnSurface = startNavText,
        error = error,
        errorContainer = errorContainer,
        onError = errorOn,
        onErrorContainer = errorContainerOn,
        scrim = scrimShadow,
    )
}
