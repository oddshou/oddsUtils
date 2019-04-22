/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.animation.drag_help

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.Px

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.customview.widget.ViewDragHelper

/**
 * 第一种构造函数(首选)
 */
class DragConstraintLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    //第二种构造函数，
//    constructor(context: Context) : super(context) {}
//    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
//    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    init {
        init(context)
    }

    private lateinit var mLeftDragger: ViewDragHelper
    private var dragRate: Float = 1f
    private var point: Point = Point(0,0)

    private fun init(context: Context) {
        mLeftDragger = ViewDragHelper.create(this, Callback())
    }

    /**
     * 关于多指点击，onViewReleased 全部释放才会调用，tryCaptureView，onViewCaptured 释放一指会调用一次
     */
    inner class Callback : ViewDragHelper.Callback(){
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            //只作用域PhotoView
            return true
        }

        override fun onViewDragStateChanged(state: Int) {}

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, @Px dx: Int,
                                           @Px dy: Int) {
            dragRate = 1 - top.toFloat()/changedView.height
            mLeftDragger.capturedView?.scaleX = dragRate
            mLeftDragger.capturedView?.scaleY = dragRate
            this@DragConstraintLayout.background.alpha = (dragRate * 255).toInt()
        }
        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {}

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            //松手
            //dragRate > 0.7 回弹,else finish
            if (dragRate > 0.7) {
                //回调过程中没有平滑过渡背景alpha导致背景闪烁
                mLeftDragger.settleCapturedViewAt(point.x, point.y)
                invalidate()
            }else{
                mListener.mfinishAction?.invoke()
            }
            this@DragConstraintLayout.background.alpha = 255
        }

        override fun onEdgeTouched(edgeFlags: Int, pointerId: Int) {}

        override fun onEdgeLock(edgeFlags: Int): Boolean {
            return false
        }
        override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {}

        override fun getOrderedChildIndex(index: Int): Int {
            return index
        }
//            override fun getViewHorizontalDragRange(child: View): Int {
//                return 0
//            }
//
//            override fun getViewVerticalDragRange(child: View): Int {
//                return 0
//            }
        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return left
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return Math.max(top,0)
        }
    }

    //event
    private var mInitialMotionX: Float = 0.toFloat()
    private var mInitialMotionY: Float = 0.toFloat()
    //event

    private var isDraging = false

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.actionMasked

        // "|" used deliberately here; both methods should be invoked.
        val interceptForDrag = mLeftDragger.shouldInterceptTouchEvent(ev)

        var handle = false

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                val x = ev.x
                val y = ev.y
                mInitialMotionX = x
                mInitialMotionY = y
            }

            MotionEvent.ACTION_MOVE -> {
                // 判断竖向移动距离达到要求拦截
                if (isDraging) {
                    handle = true
                }else{
                    handle = mLeftDragger.checkTouchSlop(ViewDragHelper.DIRECTION_VERTICAL)
                }
                if (handle) {
                    parent.requestDisallowInterceptTouchEvent(true)
                    isDraging = true
                }
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                isDraging = false
            }
        }

        return interceptForDrag || handle
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        mLeftDragger.processTouchEvent(ev)
//
//        val action = ev.getAction()
//
//        when (action and MotionEvent.ACTION_MASK) {
//            MotionEvent.ACTION_DOWN -> {
//                val x = ev.getX()
//                val y = ev.getY()
//                mInitialMotionX = x
//                mInitialMotionY = y
//            }
//            MotionEvent.ACTION_MOVE ->{
//            }
//
//            MotionEvent.ACTION_UP -> {
//            }
//
//            MotionEvent.ACTION_CANCEL -> {
//            }
//        }

        return /*isDraging*/true
    }

    override fun computeScroll() {
        super.computeScroll()
        if(mLeftDragger.continueSettling(true))
        {
            invalidate();
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        point.x = getChildAt(0).left
        point.y = getChildAt(0).top
    }

    //---------- callback
    private lateinit var mListener: ListenerBuilder

    fun setListener(listenerBuilder: ListenerBuilder.() -> Unit) {
        mListener = ListenerBuilder().also(listenerBuilder)
    }

    inner class ListenerBuilder {
        internal var mfinishAction: (() -> Unit)? = null

        fun onfinish(action: () -> Unit) {
            mfinishAction = action
        }
    }
    //---------- callback end

}
