/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.animation

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.example.odds.R
import com.example.odds.base.BaseActivity
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_interpolator.*
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue


/**
 *
 */
class InterpolatorActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interpolator)
        createIntroduction("使用 chip必须使用 MaterialComponents 相关主题, chip group singleSelection")
        val arrayList = ArrayList<PointValue>()
        val lines = ArrayList<Line>()
        val data = LineChartData()
        data.lines = lines

        val animator = ValueAnimator.ofInt(0, 1000).apply {
            duration = 3000
            addUpdateListener {
                arrayList.add(PointValue(it.currentPlayTime.toFloat(), it.animatedValue.toString().toFloat()))
                imageView2.top = it.animatedValue as Int
            }

        }
        animator.addListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                val line = Line(arrayList).setColor(Color.BLUE).setCubic(false).setStrokeWidth(1)
                lines.add(line)
                chart.lineChartData = data
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
                arrayList.clear()
                lines.clear()
            }

        })

        chipGroup2.setOnCheckedChangeListener { _, checkId ->
            val chip: Chip? = findViewById(checkId)
            chip?.let {
            animator.interpolator =  when (chip.text.toString()) {
                "AccelerateDecelerateInterpolator" -> android.view.animation.AccelerateDecelerateInterpolator()

                "AccelerateInterpolator" -> android.view.animation.AccelerateInterpolator()

                "AnticipateInterpolator" -> android.view.animation.AnticipateInterpolator()

                "AnticipateOvershootInterpolator" -> android.view.animation.AnticipateOvershootInterpolator()

                "BounceInterpolator" -> android.view.animation.BounceInterpolator()

                "CycleInterpolator" -> android.view.animation.CycleInterpolator(2f)

                "DecelerateInterpolator" -> android.view.animation.DecelerateInterpolator()

                "OvershootInterpolator" -> android.view.animation.OvershootInterpolator()

                else -> android.view.animation.LinearInterpolator()
            }
            if (animator.isStarted) {
                animator.cancel()
            }
            animator.start()
            ToastUtils.showShort(animator.interpolator.toString())
            }
        }

    }
}
