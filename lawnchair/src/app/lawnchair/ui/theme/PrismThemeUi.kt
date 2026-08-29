package app.lawnchair.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.lawnchair.prism.PrismBrand
import com.android.launcher3.R

@Composable
fun prismThemeProfileLabel(profile: PrismBrand.ThemeProfile): String {
    return stringResource(
        id = when (profile) {
            PrismBrand.ThemeProfile.SYSTEM -> R.string.prism_theme_system
            PrismBrand.ThemeProfile.WALLPAPER -> R.string.prism_theme_wallpaper
            PrismBrand.ThemeProfile.VIBRANT -> R.string.prism_theme_vibrant
            PrismBrand.ThemeProfile.PASTEL -> R.string.prism_theme_pastel
            PrismBrand.ThemeProfile.AMOLED -> R.string.prism_theme_amoled
            PrismBrand.ThemeProfile.GLASS -> R.string.prism_theme_glass
            PrismBrand.ThemeProfile.CUSTOM -> R.string.prism_theme_custom
        },
    )
}

@Composable
fun PrismThemeProfileSwatch(
    profile: PrismBrand.ThemeProfile,
    modifier: Modifier = Modifier,
    dotSize: Dp = 12.dp,
) {
    val colors = prismThemeProfileColors(profile)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        colors.forEach { color ->
            Surface(
                modifier = Modifier.size(dotSize),
                shape = CircleShape,
                color = color,
            ) {}
        }
    }
}

@Composable
private fun prismThemeProfileColors(profile: PrismBrand.ThemeProfile): List<Color> {
    val colors = MaterialTheme.colorScheme
    return when (profile) {
        PrismBrand.ThemeProfile.SYSTEM -> listOf(
            colors.primary,
            colors.secondary,
            colors.tertiary,
        )

        PrismBrand.ThemeProfile.WALLPAPER -> listOf(
            colors.primaryContainer,
            colors.primary,
            colors.onPrimaryContainer,
        )

        PrismBrand.ThemeProfile.VIBRANT -> listOf(
            colors.primary,
            colors.tertiary,
            colors.secondary,
        )

        PrismBrand.ThemeProfile.PASTEL -> listOf(
            colors.primaryContainer,
            colors.secondaryContainer,
            colors.tertiaryContainer,
        )

        PrismBrand.ThemeProfile.AMOLED -> listOf(
            Color.Black,
            colors.primary,
            Color.White,
        )

        PrismBrand.ThemeProfile.GLASS -> listOf(
            colors.surface.copy(alpha = 0.60f),
            colors.primaryContainer.copy(alpha = 0.72f),
            colors.tertiaryContainer.copy(alpha = 0.72f),
        )

        PrismBrand.ThemeProfile.CUSTOM -> listOf(
            colors.primary,
            colors.primaryContainer,
            colors.inversePrimary,
        )
    }
}
