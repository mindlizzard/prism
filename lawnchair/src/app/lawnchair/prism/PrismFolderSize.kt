package app.lawnchair.prism

import androidx.annotation.StringRes
import com.android.launcher3.R

enum class PrismFolderSize(
    val spanX: Int,
    val spanY: Int,
    @StringRes val labelResource: Int,
) {
    COMPACT(1, 1, R.string.prism_folder_size_compact),
    WIDE(2, 1, R.string.prism_folder_size_wide),
    LARGE(2, 2, R.string.prism_folder_size_large),
    SHOWCASE(3, 2, R.string.prism_folder_size_showcase),
    IMMERSIVE(3, 3, R.string.prism_folder_size_immersive),
    ;

    companion object {
        @JvmStatic
        fun fromSpans(spanX: Int, spanY: Int): PrismFolderSize = values().firstOrNull { it.spanX == spanX && it.spanY == spanY } ?: COMPACT

        @JvmStatic
        fun normalizeSpanX(spanX: Int, spanY: Int): Int = fromSpans(spanX, spanY).spanX

        @JvmStatic
        fun normalizeSpanY(spanX: Int, spanY: Int): Int = fromSpans(spanX, spanY).spanY
    }
}
