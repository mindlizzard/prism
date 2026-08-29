package app.lawnchair.prism

import androidx.annotation.StringRes
import com.android.launcher3.R

enum class PrismForgeStyle(
    val value: String,
    @StringRes val labelResource: Int,
) {
    AUTO("auto", R.string.prism_forge_style_auto),
    MATERIAL("material", R.string.prism_forge_style_material),
    MONOCHROME("monochrome", R.string.prism_forge_style_monochrome),
    GLASS("glass", R.string.prism_forge_style_glass),
    ;

    companion object {
        fun fromValue(value: String): PrismForgeStyle = entries.firstOrNull { it.value == value } ?: AUTO
    }
}
