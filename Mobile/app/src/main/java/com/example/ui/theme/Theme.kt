package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = RoyalViolet,
    onPrimary = PureWhite,
    primaryContainer = SoftLavender,
    onPrimaryContainer = DeepPlum,
    secondary = TerracottaAccent,
    onSecondary = PureWhite,
    secondaryContainer = WarmAmberTint,
    onSecondaryContainer = CharcoalInk,
    tertiary = RoyalVioletDark,
    onTertiary = PureWhite,
    background = WarmCream,
    onBackground = CharcoalInk,
    surface = PureWhite,
    onSurface = CharcoalInk,
    surfaceVariant = SoftOat,
    onSurfaceVariant = MutedSlate,
    outline = SubtleBorder,
    outlineVariant = SubtleBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = VioletLight,
    onPrimary = DeepPlum,
    primaryContainer = RoyalVioletDark,
    onPrimaryContainer = SoftLavender,
    secondary = TerracottaAccent,
    onSecondary = PureWhite,
    secondaryContainer = DarkCardElevated,
    onSecondaryContainer = DarkTextPrimary,
    background = DarkCanvas,
    onBackground = DarkTextPrimary,
    surface = DarkCard,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkCardElevated,
    onSurfaceVariant = DarkTextMuted,
    outline = DarkCardElevated,
    outlineVariant = DarkCardElevated
)

val PetCareShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun PetCareTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PetCareTypography,
        shapes = PetCareShapes,
        content = content
    )
}
