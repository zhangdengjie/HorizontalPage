package com.zhuguohui.horizontalpage.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.view.NestedScrollingParent
import androidx.core.view.NestedScrollingParent2
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView

/**
 * 实现 NestedScrollingParent 接口
 * 和 itemView中包含 [androidx.core.widget.NestedScrollView] 配合
 * 达到列表且套滚动布局的效果
 *
 * 解决 横向滑动 不跟手 问题
 */
open class NestedRecyclerView : RecyclerView, NestedScrollingParent2 {

    private var nestedScrollTarget: View? = null
    private var nestedScrollTargetIsBeingDragged = false
    private var nestedScrollTargetWasUnableToScroll = false

    /**
     * 跳过触摸拦截
     */
    private var skipsTouchInterception = false


    constructor(context: Context) :
            super(context)


    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs)


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)


    private var downX: Float = 0f
    private var downY: Float = 0f

    /**
     * 事件传递第一步调用的方法
     * 在某些场景下 发送了两次事件
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

//        Log.i(TAG, "dispatchTouchEvent: ${ev.action}")
//        var x: Float = ev.x
//        var y: Float = ev.y
//
//        if (ev.action == MotionEvent.ACTION_DOWN) {
//            downX = x
//            downY = y
//        } else if (ev.action == MotionEvent.ACTION_MOVE) {
//            var currX: Float = ev.x
//            var currY: Float = ev.y
//            var deltaX: Float = Math.abs(currX - downX)
//            var deltaY: Float = Math.abs(currY - downY)
//            downX = currX
//            downY = currY
//            if (deltaX >= deltaY) {
//                Log.i(TAG, "dispatchTouchEvent: 横向滑动")
////                return true
//            } else {
//                Log.i(TAG, "dispatchTouchEvent: 纵向滑动")
//            }
//        }
        // 嵌套滑动 目标view 不是空,说明存在嵌套滑动
        val temporarilySkipsInterception = nestedScrollTarget != null
        if (temporarilySkipsInterception) {
            // If a descendent view is scrolling we set a flag to temporarily skip our onInterceptTouchEvent implementation
            skipsTouchInterception = true
        }

        // 第一次分发事件
        // First dispatch, potentially skipping our onInterceptTouchEvent
        var handled = super.dispatchTouchEvent(ev)

        if (temporarilySkipsInterception) {
            skipsTouchInterception = false

            // If the first dispatch yielded no result or we noticed that the descendent view is unable to scroll in the
            // direction the user is scrolling, we dispatch once more but without skipping our onInterceptTouchEvent.
            // Note that RecyclerView automatically cancels active touches of all its descendents once it starts scrolling
            // so we don't have to do that.
            if (!handled || nestedScrollTargetWasUnableToScroll) {
                handled = super.dispatchTouchEvent(ev)
            }
        }

        return handled
    }


    // Skips RecyclerView's onInterceptTouchEvent if requested
    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        // 如果跳过,则不拦截事件
        if (skipsTouchInterception) {
            return false
        }
        return super.onInterceptTouchEvent(e)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
    ) {
        Log.i(TAG, "onNestedScroll: $dxConsumed $dxUnconsumed $dyConsumed $dyUnconsumed")
        if (target === nestedScrollTarget && !nestedScrollTargetIsBeingDragged) {
            if (dyConsumed != 0) {
                // The descendent was actually scrolled, so we won't bother it any longer.
                // It will receive all future events until it finished scrolling.

                // 让可滑动子view自己处理滑动事件
                nestedScrollTargetIsBeingDragged = true
                nestedScrollTargetWasUnableToScroll = false
            } else if (dyConsumed == 0 && dyUnconsumed != 0) {
                // The descendent tried scrolling in response to touch movements but was not able to do so.
                // We remember that in order to allow RecyclerView to take over scrolling.

                // 让recyclerView 处理滑动事件
                nestedScrollTargetWasUnableToScroll = true
                target.parent?.requestDisallowInterceptTouchEvent(false)
            }
        }
    }

    override fun onNestedPreScroll(
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int,
    ) {
        // 开始滑动前回调的方法
        Log.i(TAG, "onNestedPreScroll: ")
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        Log.i(TAG, "onNestedScrollAccepted: ")

        if (axes and View.SCROLL_AXIS_VERTICAL != 0) {
            // A descendent started scrolling, so we'll observe it.
            nestedScrollTarget = target
            nestedScrollTargetIsBeingDragged = false
            nestedScrollTargetWasUnableToScroll = false
        }
    }


    // We only support vertical scrolling.
    /**
     * parent 支持哪些方向的滑动
     */
    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int, type: Int): Boolean {
        return nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
//        return false
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        // The descendent finished scrolling. Clean up!
        nestedScrollTarget = null
        nestedScrollTargetIsBeingDragged = false
        nestedScrollTargetWasUnableToScroll = false
    }

    companion object {
        private const val TAG = "NestedRecyclerView"
    }
}


