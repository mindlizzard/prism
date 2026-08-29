package app.lawnchair.prism

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import app.lawnchair.preferences2.PreferenceManager2
import app.lawnchair.theme.UiColorMode
import app.lawnchair.theme.color.tokens.ColorTokens
import com.patrykmichalik.opto.core.firstBlocking

object PrismTheme {

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

        val base = ColorTokens.ColorAccent.resolveColor(context, mode)

        val profile = PreferenceManager2
            .getInstance(context)
            .prismThemeProfile
            .firstBlocking()

        return when (profile) {
            PrismBrand.ThemeProfile.SYSTEM,
            PrismBrand.ThemeProfile.WALLPAPER,
            -> base

            PrismBrand.ThemeProfile.VIBRANT -> vibrant(base)

            PrismBrand.ThemeProfile.PASTEL -> {
                ColorUtils.blendARGB(base, Color.WHITE, 0.32f)
            }

            PrismBrand.ThemeProfile.AMOLED -> {
                if (darkTheme) {
                    ColorUtils.blendARGB(base, Color.WHITE, 0.08f)
                } else {
                    base
                }
            }

            PrismBrand.ThemeProfile.GLASS -> {
                ColorUtils.blendARGB(
                    base,
                    Color.WHITE,
                    if (darkTheme) 0.12f else 0.22f,
                )
            }

            PrismBrand.ThemeProfile.CUSTOM -> base
        }
    }

    @ColorInt
    private fun vibrant(@ColorInt color: Int): Int {
        val hsl = FloatArray(3)

        ColorUtils.colorToHSL(color, hsl)

        hsl[1] = (hsl[1] * 1.35f).coerceAtMost(1f)
        hsl[2] = (hsl[2] * 1.04f).coerceAtMost(1f)

        return ColorUtils.HSLToColor(hsl)
    }
}
