package app.lawnchair.prism

import android.content.Context
import app.lawnchair.preferences2.PreferenceManager2
import com.patrykmichalik.opto.core.firstBlocking

object PrismFolderStyle {

    @JvmStatic
    fun isLargePreviewEnabled(context: Context): Boolean {
        return PreferenceManager2
            .getInstance(context)
            .prismLargeFolderPreview
            .firstBlocking()
    }

    @JvmStatic
    fun scale(context: Context): Float {
        return PreferenceManager2
            .getInstance(context)
            .prismFolderScale
            .firstBlocking()
            .coerceIn(1f, 1.35f)
    }
}
