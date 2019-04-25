/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.animation

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.odds.R
import com.example.odds.base.BaseActivity
import kotlinx.android.synthetic.main.activity_anim.*

class animActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anim)
        createIntroduction("点击暂停继续")

        //---------------- first
        initAnim()
        imageView.setOnClickListener {
            //api level >= 19
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (animation.isPaused) {
                    animation.resume()
                }else if (animation.isStarted) {
                    animation.pause()
                }

            }
        }
        //---------------- first end

        //---------------- second
        vectorImg.setOnClickListener {
            /**
             * 针对api限制， 正统做法
             * 1. 可能的话使用其他方案绕过 高api 实现相同效果
             * 2. 应该在不影响低版本使用的情况下，酌情考虑低版本替代方案或者行为。
             * 尽量适配多的用户，使得高api带来更好的用户体验
             *
             */
            vectorAnim()
        }
        //---------------- second end


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun vectorAnim() {
        (vectorImg.drawable as? AnimatedVectorDrawable)?.start()
    }

    private lateinit var animation: ObjectAnimator

    /**
     * 自旋转，可暂停，继续
     */
    fun initAnim() {
        animation = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360.0f);
        animation.setDuration(15000);
        animation.setInterpolator(LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(ValueAnimator.RESTART);
        animation.start();
    }
}
