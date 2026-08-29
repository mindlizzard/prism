package app.lawnchair.prism

import app.lawnchair.LawnchairLauncher
import com.android.launcher3.CellLayout
import com.android.launcher3.LauncherSettings.Favorites.CONTAINER_DESKTOP
import com.android.launcher3.celllayout.CellLayoutLayoutParams
import com.android.launcher3.folder.FolderIcon

object PrismFolderResizer {

    fun resize(
        launcher: LawnchairLauncher,
        folderIcon: FolderIcon,
        size: PrismFolderSize,
    ): Boolean {
        val info = folderIcon.mInfo
        if (info.container != CONTAINER_DESKTOP) return false

        val layout = folderIcon.parent?.parent as? CellLayout ?: return false
        val layoutParams = folderIcon.layoutParams as? CellLayoutLayoutParams ?: return false
        val presenterScreen = launcher.cellPosMapper.mapModelToPresenter(info).screenId
        val oldCellX = layoutParams.cellX
        val oldCellY = layoutParams.cellY

        layout.markCellsAsUnoccupiedForView(folderIcon)
        val target = findPlacement(
            layout = layout,
            anchorX = oldCellX,
            anchorY = oldCellY,
            spanX = size.spanX,
            spanY = size.spanY,
        )

        if (target == null) {
            layout.markCellsAsOccupiedForView(folderIcon)
            return false
        }

        layoutParams.cellX = target.first
        layoutParams.cellY = target.second
        layoutParams.cellHSpan = size.spanX
        layoutParams.cellVSpan = size.spanY

        launcher.modelWriter.modifyItemInDatabase(
            info,
            info.container,
            presenterScreen,
            target.first,
            target.second,
            size.spanX,
            size.spanY,
        )

        layout.markCellsAsOccupiedForView(folderIcon)
        folderIcon.onItemsChanged(false)
        folderIcon.requestLayout()
        layout.requestLayout()
        return true
    }

    private fun findPlacement(
        layout: CellLayout,
        anchorX: Int,
        anchorY: Int,
        spanX: Int,
        spanY: Int,
    ): Pair<Int, Int>? {
        for (shiftY in 0 until spanY) {
            for (shiftX in 0 until spanX) {
                val cellX = anchorX - shiftX
                val cellY = anchorY - shiftY
                if (layout.isRegionVacant(cellX, cellY, spanX, spanY)) {
                    return cellX to cellY
                }
            }
        }
        return null
    }
}
