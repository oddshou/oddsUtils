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
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import com.example.odds.R
import com.example.odds.base.BaseActivity
import kotlinx.android.synthetic.main.activity_anim.*

class animActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anim)
        createIntroduction("点击暂停继续")
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
