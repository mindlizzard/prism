package app.lawnchair.prism

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import app.lawnchair.theme.UiColorMode
import app.lawnchair.theme.color.tokens.ColorTokens

object PrismTheme {

    /**
     * Applies a Prism profile to a seed color before the Monet palette is generated.
     * Keeping this transformation at the seed level makes the complete launcher palette follow
     * the selected profile instead of tinting only a handful of controls.
     */
    @ColorInt
    fun transformSeed(
        @ColorInt color: Int,
        profile: PrismBrand.ThemeProfile,
    ): Int {
        return when (profile) {
            PrismBrand.ThemeProfile.VIBRANT -> vibrant(color)

            PrismBrand.ThemeProfile.PASTEL -> pastel(color)

            PrismBrand.ThemeProfile.GLASS -> ColorUtils.blendARGB(color, Color.WHITE, 0.18f)

            PrismBrand.ThemeProfile.SYSTEM,
            PrismBrand.ThemeProfile.WALLPAPER,
            PrismBrand.ThemeProfile.AMOLED,
            PrismBrand.ThemeProfile.CUSTOM,
            -> color
        }
    }

    @ColorInt
    fun accent(
        context: Context,
        darkTheme: Boolean,
    ): Int {
        val mode = if (darkTheme) {
            UiColorMode.Dark
        } else {
            UiColorMode.Light
        }

        // ThemeProvider already applies the profile to the complete Monet palette.
        return ColorTokens.ColorAccent.resolveColor(context, mode)
    }

    @ColorInt
    private fun vibrant(@ColorInt color: Int): Int {
        val hsl = FloatArray(3)

        ColorUtils.colorToHSL(color, hsl)

        hsl[1] = (hsl[1] * 1.35f).coerceAtMost(1f)
        hsl[2] = (hsl[2] * 1.04f).coerceAtMost(1f)

        return ColorUtils.HSLToColor(hsl)
    }

    @ColorInt
    private fun pastel(@ColorInt color: Int): Int {
        val hsl = FloatArray(3)

        ColorUtils.colorToHSL(color, hsl)
        hsl[1] = (hsl[1] * 0.58f).coerceIn(0.18f, 0.62f)
        hsl[2] = hsl[2].coerceAtLeast(0.64f)

        return ColorUtils.blendARGB(ColorUtils.HSLToColor(hsl), Color.WHITE, 0.24f)
    }
}
