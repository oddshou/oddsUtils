package com.example.odds.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import com.example.odds.R
import kotlinx.android.synthetic.main.activity_anim2.*

class Anim2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anim2)
    }


    /**
     * ValueAnimator 平移
     */
    fun clickText1(view: View) {
        ValueAnimator.ofFloat(0f, 100f).apply {
            duration = 1000
            start()
            addUpdateListener { updatedAnimation ->
                textView.translationX = updatedAnimation.animatedValue as Float
            }
        }
    }

    /**
     * ObjectAnimator 平移
     * 自动调用属性set 方法，没有set方法需要补充或者 通过组合间接调用set方法或者 使用 valueAnimator替代
     */
    fun clickText2(view: View) {
        ObjectAnimator.ofFloat(textView2, "translationX", 100f).apply {
            duration = 1000
            start()
        }
    }

    fun clickText3(view: View) {
//        val drawable = ColorDrawable()
//        textView3.background = drawable
//        drawable.color = Color.parseColor("#000000")
//        textView3.invalidate()

        ObjectAnimator.ofInt(textView3, "backgroundColor", Color.parseColor("#FFFFFF"), Color.parseColor("#0FFFFF")).apply {
            duration = 3000
            start()
//            addUpdateListener {
//                textView3.invalidate()
//            }
            //必须都实现
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                }

                override fun onAnimationStart(p0: Animator?) {
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

            })
            /**
             * 可以只实现部分方法
             */
            addListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationCancel(animation: Animator) {
//                    throw RuntimeException("Stub!")
//                }
//
//                override fun onAnimationEnd(animation: Animator) {
//                    throw RuntimeException("Stub!")
//                }
//
//                override fun onAnimationRepeat(animation: Animator) {
//                    throw RuntimeException("Stub!")
//                }
//
//                override fun onAnimationStart(animation: Animator) {
//                    throw RuntimeException("Stub!")
//                }
//
//                override fun onAnimationPause(animation: Animator) {
//                    throw RuntimeException("Stub!")
//                }
//
//                override fun onAnimationResume(animation: Animator) {
//                    throw RuntimeException("Stub!")
//                }
            })
        }
    }

    fun startAnim(btn: View) {
        val anim = AnimationUtils.loadAnimation(this, R.anim.loading)
        viewLoading.startAnimation(anim)
    }

    fun createAnim(){
        val animSet = AnimationSet(true)
        val scaleAnimation = ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f, 0.5f, 0.5f)
        animSet.addAnimation(scaleAnimation)
    }
}
