package app.lawnchair.prism

import android.content.Context
import androidx.annotation.ColorInt
import app.lawnchair.theme.UiColorMode
import app.lawnchair.theme.color.tokens.ColorTokens

object PrismTheme {
    @ColorInt
    fun accent(
        context: Context,
        profile: PrismBrand.ThemeProfile = PrismBrand.ThemeProfile.SYSTEM,
        darkTheme: Boolean,
    ): Int {
        val colorMode = if (darkTheme) UiColorMode.Dark else UiColorMode.Light

        // All profiles currently preserve Lawnchair's proven color resolution.
        // Prism profiles can diverge here later without changing every caller.
        return ColorTokens.ColorAccent.resolveColor(context, colorMode)
    }
}
