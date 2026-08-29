package app.lawnchair

import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.RemoteViews
import app.lawnchair.preferences2.PreferenceManager2
import app.lawnchair.preferences2.firstCached
import app.lawnchair.smartspace.SmartspaceAppWidgetProvider
import com.android.launcher3.R
import com.android.launcher3.celllayout.CellLayoutLayoutParams
import com.android.launcher3.util.Themes
import com.android.launcher3.widget.LauncherAppWidgetHostView
import kotlin.math.abs

class LawnchairAppWidgetHostView @JvmOverloads constructor(
    context: Context,
    private var previewMode: Boolean = false,
) : LauncherAppWidgetHostView(context) {

    private var customView: ViewGroup? = null
    private val prefs by lazy { PreferenceManager2.getInstance(context) }
    private val stackSwipeThreshold = ViewConfiguration.get(context).scaledTouchSlop * 2f
    private var stackSwipeStartY = Float.NaN
    private var stackSwipeHandled = false

    override fun setAppWidget(appWidgetId: Int, info: AppWidgetProviderInfo) {
        inflateCustomView(info)
        super.setAppWidget(appWidgetId, info)
    }

    fun disablePreviewMode() {
        previewMode = false
        inflateCustomView(appWidgetInfo)
    }

    private fun inflateCustomView(info: AppWidgetProviderInfo) {
        customView = inflateCustomView(context, info, previewMode)
        if (customView == null) {
            return
        }
        customView!!.setOnLongClickListener(this)
        removeAllViews()
        addView(customView, MATCH_PARENT, MATCH_PARENT)
    }

    override fun updateAppWidget(remoteViews: RemoteViews?) {
        if (customView != null) return
        super.updateAppWidget(remoteViews)
    }

    override fun getDefaultView(): View {
        if (customView != null) return getEmptyView()
        return super.getDefaultView()
    }

    override fun getErrorView(): View {
        if (customView != null) return getEmptyView()
        return super.getErrorView()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (!prefs.allowWidgetOverlap.firstCached()) {
            return super.dispatchTouchEvent(event)
        }

        if (stackSwipeHandled) {
            if (event.actionMasked == MotionEvent.ACTION_UP || event.actionMasked == MotionEvent.ACTION_CANCEL) {
                resetStackSwipe()
            }
            return true
        }

        when (event.actionMasked) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount == STACK_POINTER_COUNT) {
                    stackSwipeStartY = averagePointerY(event)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (event.pointerCount >= STACK_POINTER_COUNT && !stackSwipeStartY.isNaN()) {
                    val deltaY = averagePointerY(event) - stackSwipeStartY
                    if (abs(deltaY) >= stackSwipeThreshold && cycleWidgetStack(deltaY)) {
                        stackSwipeHandled = true
                        val cancelEvent = MotionEvent.obtain(event).apply {
                            action = MotionEvent.ACTION_CANCEL
                        }
                        super.dispatchTouchEvent(cancelEvent)
                        cancelEvent.recycle()
                        return true
                    }
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> resetStackSwipe()
        }

        return super.dispatchTouchEvent(event)
    }

    private fun cycleWidgetStack(deltaY: Float): Boolean {
        val container = parent as? ViewGroup ?: return false
        val ownLayout = layoutParams as? CellLayoutLayoutParams ?: return false
        val widgets = buildList {
            repeat(container.childCount) { index ->
                val child = container.getChildAt(index) as? LawnchairAppWidgetHostView
                    ?: return@repeat
                val childLayout = child.layoutParams as? CellLayoutLayoutParams
                    ?: return@repeat
                if (
                    childLayout.cellX == ownLayout.cellX &&
                    childLayout.cellY == ownLayout.cellY &&
                    childLayout.cellHSpan == ownLayout.cellHSpan &&
                    childLayout.cellVSpan == ownLayout.cellVSpan
                ) {
                    add(child)
                }
            }
        }
        if (widgets.size < 2) return false

        val currentIndex = widgets.indexOf(this).takeIf { it >= 0 } ?: return false
        val direction = if (deltaY < 0) 1 else -1
        val nextIndex = (currentIndex + direction + widgets.size) % widgets.size
        val nextWidget = widgets[nextIndex]

        widgets.forEach { widget ->
            widget.animate().cancel()
            widget.alpha = 1f
            widget.visibility = if (widget === nextWidget) View.VISIBLE else View.INVISIBLE
        }
        nextWidget.alpha = 0f
        nextWidget.bringToFront()
        nextWidget.animate()
            .alpha(1f)
            .setDuration(STACK_CROSSFADE_DURATION)
            .start()
        container.requestLayout()
        return true
    }

    private fun averagePointerY(event: MotionEvent): Float {
        var total = 0f
        repeat(event.pointerCount) { pointerIndex ->
            total += event.getY(pointerIndex)
        }
        return total / event.pointerCount
    }

    private fun resetStackSwipe() {
        stackSwipeStartY = Float.NaN
        stackSwipeHandled = false
    }

    private fun getEmptyView(): View {
        return View(context)
    }

    companion object {

        private const val STACK_POINTER_COUNT = 2
        private const val STACK_CROSSFADE_DURATION = 180L

        private val customLayouts = mapOf(
            SmartspaceAppWidgetProvider.componentName to R.layout.smartspace_widget,
        )

        @JvmStatic
        fun inflateCustomView(context: Context, info: AppWidgetProviderInfo, previewMode: Boolean): ViewGroup? {
            val layoutId = customLayouts[info.provider] ?: return null

            val inflationContext = if (previewMode) Themes.createWidgetPreviewContext(context) else context
            return LayoutInflater.from(inflationContext)
                .inflate(layoutId, null, false) as ViewGroup
        }
    }
}
