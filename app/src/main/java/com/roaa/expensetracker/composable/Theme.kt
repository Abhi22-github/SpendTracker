package com.roaa.expensetracker.composable


import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.edit
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.roaa.expensetracker.composable.harmonize.palettes.CorePalette
import com.roaa.expensetracker.utilities.preferenceManger.THEME_MODE
import com.roaa.expensetracker.utilities.preferenceManger.dataStore
import com.roaa.expensetracker.viewModels.PreferencesViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


enum class ThemeMode { LIGHT, NIGHT, SYSTEM }

//
//fun darkColorScheme(): ColorScheme {
//    val palette = CorePalette.contentOf(colorSeed.toArgb())
//
//    return darkColorScheme(
//        primary = Color(palette.a1.tone(80)),
//        onPrimary = Color(palette.a1.tone(20)),
//        primaryContainer = Color(palette.a1.tone(30)),
//        onPrimaryContainer = Color(palette.a1.tone(90)),
//        inversePrimary = Color(palette.a1.tone(40)),
//        secondary = Color(palette.a2.tone(80)),
//        onSecondary = Color(palette.a2.tone(20)),
//        secondaryContainer = Color(palette.a2.tone(30)),
//        onSecondaryContainer = Color(palette.a2.tone(90)),
//        tertiary = Color(palette.a3.tone(80)),
//        onTertiary = Color(palette.a3.tone(20)),
//        tertiaryContainer = Color(palette.a3.tone(30)),
//        onTertiaryContainer = Color(palette.a3.tone(90)),
//        background = Color(palette.n1.tone(10)),
//        onBackground = Color(palette.n1.tone(90)),
//        surface = Color(palette.n1.tone(10)),
//        onSurface = Color(palette.n1.tone(90)),
//        surfaceVariant = Color(palette.n1.tone(30)),
//        onSurfaceVariant = Color(palette.n1.tone(80)),
//        surfaceTint = Color(palette.n1.tone(90)), //
//        inverseSurface = Color(palette.n1.tone(90)),
//        inverseOnSurface = Color(palette.n1.tone(20)),
//        error = Color(palette.error.tone(80)),
//        onError = Color(palette.error.tone(20)),
//        errorContainer = Color(palette.error.tone(30)),
//        onErrorContainer = Color(palette.error.tone(80)),
//        outline = Color(palette.n2.tone(60)),
//        outlineVariant = Color(palette.n2.tone(50)), //
//        scrim = Color(palette.n1.tone(30)), //
//        )
//}
//
//fun lightColorScheme(): ColorScheme {
//    val palette = CorePalette.contentOf(colorSeed.toArgb())
//
//    return lightColorScheme(
//        primary = Color(palette.a1.tone(40)),
//        onPrimary = Color(palette.a1.tone(100)),
//        primaryContainer = Color(palette.a1.tone(90)),
//        onPrimaryContainer = Color(palette.a1.tone(10)),
//        inversePrimary = Color(palette.a1.tone(80)),
//        secondary = Color(palette.a2.tone(40)),
//        onSecondary = Color(palette.a2.tone(100)),
//        secondaryContainer = Color(palette.a2.tone(90)),
//        onSecondaryContainer = Color(palette.a2.tone(10)),
//        tertiary = Color(palette.a3.tone(40)),
//        onTertiary = Color(palette.a3.tone(100)),
//        tertiaryContainer = Color(palette.a3.tone(90)),
//        onTertiaryContainer = Color(palette.a3.tone(10)),
//        background = Color(palette.n1.tone(99)),
//        onBackground = Color(palette.n1.tone(10)),
//        surface = Color(palette.n1.tone(99)),
//        onSurface = Color(palette.n1.tone(10)),
//        surfaceVariant = Color(palette.n1.tone(90)),
//        onSurfaceVariant = Color(palette.n1.tone(30)),
//        surfaceTint = Color(palette.n1.tone(10)), //
//        inverseSurface = Color(palette.n1.tone(20)),
//        inverseOnSurface = Color(palette.n1.tone(95)),
//        error = Color(palette.error.tone(40)),
//        onError = Color(palette.error.tone(100)),
//        errorContainer = Color(palette.error.tone(90)),
//        onErrorContainer = Color(palette.error.tone(10)),
//        outline = Color(palette.n2.tone(50)),
//        outlineVariant = Color(palette.n2.tone(50)), //
//        scrim = Color(palette.n1.tone(90)), //
//    )
//}
//
//


private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

fun darkColorScheme(): ColorScheme {
    val palette = CorePalette.contentOf(colorSeedTry.toArgb())

    return darkColorScheme(
        primary = Color(palette.a1.tone(80)),
        onPrimary = Color(palette.a1.tone(20)),
        primaryContainer = Color(palette.a1.tone(30)),
        onPrimaryContainer = Color(palette.a1.tone(90)),
        inversePrimary = Color(palette.a1.tone(40)),
        secondary = Color(palette.a2.tone(80)),
        onSecondary = Color(palette.a2.tone(20)),
        secondaryContainer = Color(palette.a2.tone(30)),
        onSecondaryContainer = Color(palette.a2.tone(90)),
        tertiary = Color(palette.a3.tone(80)),
        onTertiary = Color(palette.a3.tone(20)),
        tertiaryContainer = Color(palette.a3.tone(30)),
        onTertiaryContainer = Color(palette.a3.tone(90)),
        background = Color(palette.n1.tone(10)),
        onBackground = Color(palette.n1.tone(90)),
        surface = Color(palette.n1.tone(10)),
        onSurface = Color(palette.n1.tone(90)),
        surfaceVariant = Color(palette.n1.tone(30)),
        onSurfaceVariant = Color(palette.n1.tone(80)),
        surfaceTint = Color(palette.n1.tone(90)), //
        inverseSurface = Color(palette.n1.tone(90)),
        inverseOnSurface = Color(palette.n1.tone(20)),
        error = Color(palette.error.tone(80)),
        onError = Color(palette.error.tone(20)),
        errorContainer = Color(palette.error.tone(30)),
        onErrorContainer = Color(palette.error.tone(80)),
        outline = Color(palette.n2.tone(60)),
        outlineVariant = Color(palette.n2.tone(50)), //
        scrim = Color(palette.n1.tone(30)), //
    )
}

fun lightColorScheme(): ColorScheme {
    val palette = CorePalette.contentOf(colorSeedTry.toArgb())

    return lightColorScheme(
        primary = Color(palette.a1.tone(40)),
        onPrimary = Color(palette.a1.tone(100)),
        primaryContainer = Color(palette.a1.tone(90)),
        onPrimaryContainer = Color(palette.a1.tone(10)),
        inversePrimary = Color(palette.a1.tone(80)),
        secondary = Color(palette.a2.tone(40)),
        onSecondary = Color(palette.a2.tone(100)),
        secondaryContainer = Color(palette.a2.tone(90)),
        onSecondaryContainer = Color(palette.a2.tone(10)),
        tertiary = Color(palette.a3.tone(40)),
        onTertiary = Color(palette.a3.tone(100)),
        tertiaryContainer = Color(palette.a3.tone(90)),
        onTertiaryContainer = Color(palette.a3.tone(10)),
        background = Color(palette.n1.tone(99)),
        onBackground = Color(palette.n1.tone(10)),
        surface = Color(palette.n1.tone(99)),
        onSurface = Color(palette.n1.tone(10)),
        surfaceVariant = Color(palette.n1.tone(90)),
        onSurfaceVariant = Color(palette.n1.tone(30)),
        surfaceTint = Color(palette.n1.tone(10)), //
        inverseSurface = Color(palette.n1.tone(20)),
        inverseOnSurface = Color(palette.n1.tone(95)),
        error = Color(palette.error.tone(40)),
        onError = Color(palette.error.tone(100)),
        errorContainer = Color(palette.error.tone(90)),
        onErrorContainer = Color(palette.error.tone(10)),
        outline = Color(palette.n2.tone(50)),
        outlineVariant = Color(palette.n2.tone(50)), //
        scrim = Color(palette.n1.tone(90)), //
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ExpenseTrackerTheme(
    darkTheme: Boolean = isNightMode(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable() () -> Unit
) {

    SetStatusBarColor(darkTheme)
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        syncTheme(context)
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }

//    val view = LocalView.current
//    if(!view.isInEditMode){
//        SideEffect {
//            val window = (view.context as Activity).window
//            window.statusBarColor = Color.Transparent.toArgb()
//            WindowCompat.getInsetsController(window,view).isAppearanceLightStatusBars = !darkTheme
//        }
//    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography(LocalContext.current),
        content = content
    )

}

@Composable
fun SetStatusBarColor(isDarkTheme: Boolean) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = Color.Transparent,
        darkIcons = !isDarkTheme
    )
}


suspend fun switchTheme(context: Context, mode: ThemeMode) {
    context.dataStore.edit {
        it[THEME_MODE] = mode.toString()
    }
    syncTheme(context)
}

fun syncTheme(context: Context) {
    val currentValue = runBlocking { context.dataStore.data.first() }
    val mode = runBlocking {
        context.dataStore.data
            .map { preferences ->
                preferences[THEME_MODE] ?: ThemeMode.SYSTEM.toString()
            }.first()
    }
    changeThemeSystemWide(mode)
}

@Composable
fun isNightMode(preferencesViewModel: PreferencesViewModel = hiltViewModel()): Boolean {
    val themeMode by preferencesViewModel.getThemeMode.collectAsState(ThemeMode.SYSTEM)
    val p = when (themeMode) {
        ThemeMode.LIGHT.toString() -> false
        ThemeMode.NIGHT.toString() -> true
        else -> isSystemInDarkTheme()
    }
    return p
}

fun changeThemeSystemWide(mode: String) {
    when (mode) {
        ThemeMode.LIGHT.toString() -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        ThemeMode.SYSTEM.toString() -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        ThemeMode.NIGHT.toString() -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}


//@Composable
//fun ExpenseTrackerTheme(
//    darkTheme: Boolean = isNightMode(),
//    dynamicColor: Boolean = true,
//    content: @Composable () -> Unit,
//) {
//
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> darkColorScheme()
//        else -> lightColorScheme()
//    }
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        shapes = shapes,
//        typography = typography(LocalContext.current),
//        content = content
//    )
//}

