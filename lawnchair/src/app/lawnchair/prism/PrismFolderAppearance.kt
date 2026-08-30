package app.lawnchair.prism

import android.graphics.Color
import androidx.annotation.StringRes
import com.android.launcher3.R

private const val STYLE_MASK = 0x00000300
private const val ICON_SIZE_MASK = 0x00000C00

enum class PrismFolderVisualStyle(
    val optionBits: Int,
    @StringRes val labelResource: Int,
) {
    CLASSIC(0x00000000, R.string.prism_folder_style_classic),
    SOFT(0x00000100, R.string.prism_folder_style_soft),
    GLASS(0x00000200, R.string.prism_folder_style_glass),
    OUTLINE(0x00000300, R.string.prism_folder_style_outline),
    ;

    fun applyTo(options: Int): Int = (options and STYLE_MASK.inv()) or optionBits

    companion object {
        fun fromOptions(options: Int): PrismFolderVisualStyle =
            entries.firstOrNull { it.optionBits == (options and STYLE_MASK) } ?: CLASSIC
    }
}

enum class PrismFolderIconSize(
    val optionBits: Int,
    val scale: Float,
    @StringRes val labelResource: Int,
) {
    SMALL(0x00000400, 0.84f, R.string.prism_folder_icon_size_small),
    BALANCED(0x00000000, 1f, R.string.prism_folder_icon_size_balanced),
    LARGE(0x00000800, 1.16f, R.string.prism_folder_icon_size_large),
    EXTRA_LARGE(0x00000C00, 1.30f, R.string.prism_folder_icon_size_extra_large),
    ;

    fun applyTo(options: Int): Int = (options and ICON_SIZE_MASK.inv()) or optionBits

    companion object {
        fun fromOptions(options: Int): PrismFolderIconSize =
            entries.firstOrNull { it.optionBits == (options and ICON_SIZE_MASK) } ?: BALANCED
    }
}

object PrismFolderAppearance {

    @JvmStatic
    fun usesRoundedShape(options: Int): Boolean =
        PrismFolderVisualStyle.fromOptions(options) != PrismFolderVisualStyle.CLASSIC

    @JvmStatic
    fun iconScale(options: Int): Float = PrismFolderIconSize.fromOptions(options).scale

    @JvmStatic
    fun backgroundColor(color: Int, options: Int): Int {
        val alphaMultiplier = when (PrismFolderVisualStyle.fromOptions(options)) {
            PrismFolderVisualStyle.CLASSIC -> 1f
            PrismFolderVisualStyle.SOFT -> 0.92f
            PrismFolderVisualStyle.GLASS -> 0.56f
            PrismFolderVisualStyle.OUTLINE -> 0.18f
        }
        return Color.argb(
            (Color.alpha(color) * alphaMultiplier).toInt().coerceIn(0, 255),
            Color.red(color),
            Color.green(color),
            Color.blue(color),
        )
    }

    @JvmStatic
    fun cornerRadiusFactor(options: Int): Float =
        when (PrismFolderVisualStyle.fromOptions(options)) {
            PrismFolderVisualStyle.CLASSIC -> 0.22f
            PrismFolderVisualStyle.SOFT -> 0.32f
            PrismFolderVisualStyle.GLASS -> 0.28f
            PrismFolderVisualStyle.OUTLINE -> 0.30f
        }

    @JvmStatic
    fun strokeAlpha(options: Int): Int =
        when (PrismFolderVisualStyle.fromOptions(options)) {
            PrismFolderVisualStyle.CLASSIC,
            PrismFolderVisualStyle.SOFT,
            -> 0
            PrismFolderVisualStyle.GLASS -> 88
            PrismFolderVisualStyle.OUTLINE -> 184
        }
}
