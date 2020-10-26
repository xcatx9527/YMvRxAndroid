package com.yzy.example

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.chenyang.lloglib.LLog
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * @author chenyang08
 * @date 2020/10/23
 * @Description:
 */
class SampleHeaderBehavior :
        CoordinatorLayout.Behavior<View> {
    // 界面整体向上滑动，达到列表可滑动的临界点
    private var upReach = false

    // 列表向上滑动后，再向下滑动，达到界面整体可滑动的临界点
    private var downReach = false

    // 列表上一个全部可见的item位置
    private val lastPosition = -1

    constructor() {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
            context,
            attrs
    ) {
    }

    override fun layoutDependsOn(
            parent: CoordinatorLayout,
            child: View,
            dependency: View
    ): Boolean {
        return dependency is RecyclerView
    }

    override fun onInterceptTouchEvent(
            parent: CoordinatorLayout,
            child: View,
            ev: MotionEvent
    ): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downReach = false
                upReach = false
            }
        }
        return super.onInterceptTouchEvent(parent, child, ev)
    }

    override fun onStartNestedScroll(
            coordinatorLayout: CoordinatorLayout,
            child: View,
            directTargetChild: View,
            target: View,
            axes: Int,
            type: Int
    ): Boolean {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedPreScroll(
            coordinatorLayout: CoordinatorLayout,
            child: View,
            target: View,
            dx: Int,
            dy: Int,
            consumed: IntArray,
            type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        LiveEventBus.get("target",Integer.TYPE).post(dy)

    }

    override fun onNestedScroll(
            coordinatorLayout: CoordinatorLayout,
            child: View,
            target: View,
            dxConsumed: Int,
            dyConsumed: Int,
            dxUnconsumed: Int,
            dyUnconsumed: Int,
            type: Int,
            consumed: IntArray
    ) {
        LLog.e("onNestedScroll" + target.javaClass + "dx:" + dxConsumed + "dy:" + dyConsumed + " dxUnconsumed:" + dxUnconsumed + "dyUnconsumed:" + dyUnconsumed)
        super.onNestedScroll(
                coordinatorLayout,
                child,
                target,
                dxConsumed,
                dyConsumed,
                dxUnconsumed,
                dyUnconsumed,
                type,
                consumed
        )
    }
}