/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.custom_view.activity

import android.animation.ValueAnimator
import android.os.Bundle
import com.example.odds.R
import kotlinx.android.synthetic.main.activity_custom_view.*
import android.graphics.drawable.AnimationDrawable
import android.view.View
import com.example.odds.base.BaseActivity

/**
 * custom View 一个公共的展示 Activity。测试控件：
 * 1. ImageProgress
 */
class CustomViewActivity : BaseActivity() {

    private lateinit var animator: ValueAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view)
        createIntroduction("控件：ImageProgress")


        //----------------------------- ImageProgress TimeProgressView---------
        time_progress_view.setShowCircle(true)

        //直接设置帧动画, ImageView 通过设置 AnimationDrawable 实现动画效果
        testImg.setImageResource(android.R.drawable.progress_indeterminate_horizontal)
        val drawable = testImg.drawable
        if (drawable is AnimationDrawable) {
            drawable.start()
        }

        //创建值动画模拟进度增加
        animator = ValueAnimator.ofInt(0, 100)
        animator.duration = 5000
        animator.repeatCount = -1
        animator.addUpdateListener {
            var value = it.animatedValue
            time_progress_view.setProgress(value as Int)
            if (value is Int && value < 10) {
                value = 10
            }
            roundImage.setProgress(value as Int)
            textView4.setText(value.toString())
        }


    }

    fun start(view: View){
        if (animator.isStarted) {
            animator.end()
        }else{
            animator.start()
        }
    }




}
