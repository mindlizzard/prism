package app.lawnchair.prism

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import app.lawnchair.icons.CustomAdaptiveIconDrawable
import app.lawnchair.icons.iconpack.CustomIconPack
import app.lawnchair.theme.color.tokens.ColorTokens
import java.util.concurrent.ConcurrentHashMap

class PrismForgeEngine(
    private val context: Context,
) {

    private val profileCache = ConcurrentHashMap<String, PackProfile>()

    fun forge(
        source: Drawable,
        iconPack: CustomIconPack,
        iconDpi: Int,
        style: PrismForgeStyle,
    ): Drawable {
        val sourceAccent = dominantColor(
            drawable = source,
            fallback = ColorTokens.ColorAccent.resolveColor(context),
        )
        val profile = profileCache.getOrPut("${iconPack.packPackageName}:$iconDpi") {
            analyzePack(iconPack, iconDpi)
        }
        val forgedStyle = createStyle(sourceAccent, profile, style)
        val foreground = createForeground(
            source = source,
            tint = forgedStyle.foregroundTint.takeIf { source is AdaptiveIconDrawable },
        )

        return CustomAdaptiveIconDrawable(
            forgedStyle.background,
            InsetDrawable(foreground, forgedStyle.inset),
        )
    }

    private fun analyzePack(iconPack: CustomIconPack, iconDpi: Int): PackProfile {
        val samples = iconPack.getRepresentativeIcons(iconDpi, SAMPLE_COUNT)
        if (samples.isEmpty()) return PackProfile.DEFAULT

        var saturation = 0f
        var lightness = 0f
        var colorCount = 0
        val hsl = FloatArray(3)
        samples.forEach { drawable ->
            val color = dominantColor(drawable, Color.TRANSPARENT)
            if (Color.alpha(color) == 0) return@forEach
            ColorUtils.colorToHSL(color, hsl)
            saturation += hsl[1]
            lightness += hsl[2]
            colorCount++
        }

        return if (colorCount == 0) {
            PackProfile.DEFAULT
        } else {
            PackProfile(
                saturation = (saturation / colorCount).coerceIn(0.18f, 0.9f),
                lightness = (lightness / colorCount).coerceIn(0.24f, 0.78f),
            )
        }
    }

    private fun createStyle(
        sourceAccent: Int,
        profile: PackProfile,
        style: PrismForgeStyle,
    ): ForgedStyle = when (style) {
        PrismForgeStyle.AUTO -> {
            val hsl = FloatArray(3)
            ColorUtils.colorToHSL(sourceAccent, hsl)
            hsl[1] = ((hsl[1] + profile.saturation) / 2f).coerceIn(0.3f, 0.88f)
            hsl[2] = profile.lightness
            val backgroundColor = ColorUtils.HSLToColor(hsl)
            ForgedStyle(
                background = ColorDrawable(backgroundColor),
                foregroundTint = if (ColorUtils.calculateLuminance(backgroundColor) > 0.52) {
                    Color.BLACK
                } else {
                    Color.WHITE
                },
                inset = AUTO_INSET,
            )
        }

        PrismForgeStyle.MATERIAL -> ForgedStyle(
            background = ColorDrawable(ColorTokens.ColorAccent.resolveColor(context)),
            foregroundTint = ColorTokens.TextColorPrimaryInverse.resolveColor(context),
            inset = MATERIAL_INSET,
        )

        PrismForgeStyle.MONOCHROME -> ForgedStyle(
            background = ColorDrawable(ColorTokens.ColorBackground.resolveColor(context)),
            foregroundTint = ColorTokens.ColorAccent.resolveColor(context),
            inset = MONOCHROME_INSET,
        )

        PrismForgeStyle.GLASS -> {
            val highlight = ColorUtils.setAlphaComponent(
                ColorUtils.blendARGB(sourceAccent, Color.WHITE, 0.42f),
                224,
            )
            val shade = ColorUtils.setAlphaComponent(
                ColorUtils.blendARGB(sourceAccent, Color.BLACK, 0.14f),
                196,
            )
            ForgedStyle(
                background = GradientDrawable(
                    GradientDrawable.Orientation.TL_BR,
                    intArrayOf(highlight, shade),
                ),
                inset = GLASS_INSET,
            )
        }
    }

    private fun createForeground(source: Drawable, tint: Int?): Drawable {
        val sourceForeground = if (source is AdaptiveIconDrawable) source.foreground else source
        val foreground = sourceForeground.constantState
            ?.newDrawable(context.resources)
            ?.mutate()
            ?: sourceForeground.mutate()
        val wrappedForeground = DrawableCompat.wrap(foreground)
        if (tint != null) DrawableCompat.setTint(wrappedForeground, tint)
        return wrappedForeground
    }

    private fun dominantColor(drawable: Drawable, fallback: Int): Int = runCatching {
        val bitmap = drawable.toBitmap(SAMPLE_SIZE, SAMPLE_SIZE)
        val palette = Palette.from(bitmap)
            .maximumColorCount(PALETTE_COLOR_COUNT)
            .generate()
        palette.vibrantSwatch?.rgb
            ?: palette.dominantSwatch?.rgb
            ?: palette.mutedSwatch?.rgb
            ?: fallback
    }.getOrDefault(fallback)

    private data class PackProfile(
        val saturation: Float,
        val lightness: Float,
    ) {
        companion object {
            val DEFAULT = PackProfile(saturation = 0.58f, lightness = 0.52f)
        }
    }

    private data class ForgedStyle(
        val background: Drawable,
        val foregroundTint: Int? = null,
        val inset: Float,
    )

    private companion object {
        const val SAMPLE_COUNT = 12
        const val SAMPLE_SIZE = 64
        const val PALETTE_COLOR_COUNT = 8
        const val AUTO_INSET = 0.16f
        const val MATERIAL_INSET = 0.18f
        const val MONOCHROME_INSET = 0.2f
        const val GLASS_INSET = 0.16f
    }
}
