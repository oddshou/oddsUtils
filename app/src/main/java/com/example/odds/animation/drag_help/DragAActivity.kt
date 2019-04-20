/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.animation.drag_help

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.example.odds.R
import com.example.odds.base.BaseActivity
import kotlinx.android.synthetic.main.activity_drag_a.*

class DragAActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_a)
        createIntroduction("点击图片进入下个页面测试图片下拉返回")

        noAlphaImg.setOnClickListener {
            //这种方式系统动画回退到之前的位置会隐藏原控件
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val compat =  ActivityOptions
                        .makeSceneTransitionAnimation(this, it, "transition_photo_single")
                startActivity(Intent(this, DragActivity::class.java), compat.toBundle())

            } else {
                TODO("VERSION.SDK_INT < LOLLIPOP")
            }
        }
    }
}
