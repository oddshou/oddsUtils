package com.example.odds.custom_view.activity

import android.animation.*
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.animation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.odds.R
import kotlinx.android.synthetic.main.activity_screen.*
import kotlinx.android.synthetic.main.activity_view.*
import java.lang.ref.WeakReference


class ViewActivity : AppCompatActivity(), View.OnClickListener {
    private val mHandler = MyHandler(this)
    private var mMoney = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)
        circleProgressView.progress = 0
        circleProgressView.setProgress(1000, 3000, listener)
        btnController.setOnClickListener(this)

    }

    private val listener = object : AnimatorListenerAdapter(){
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            if (circleProgressView.progress == 1000) {
                //已经结束，不是暂停
                //获得10金币
                mMoney = 10
                startAnimationSet()
            }
        }
    }

    private fun startAnimationSet(){
        //200ms缩放到中心0
        val scaleAnimation = ScaleAnimation(1f, 0f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        scaleAnimation.duration = 200
        scaleAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                circleProgressView.progress = 0
                circleProgressView.setImageDrawable(null)
                //todo 子线程加载
                val drawableAnim = ContextCompat.getDrawable(this@ViewActivity, R.drawable.consume_task_progress_anim) as AnimationDrawable
                circleProgressView.background = drawableAnim
                drawableAnim.start()
                mHandler.sendEmptyMessageDelayed(MyHandler.START_MONEY_ANIM, 1000)
                mHandler.sendEmptyMessageDelayed(MyHandler.END_ALL_ANIM, 3273)
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
        circleProgressView.startAnimation(scaleAnimation)
    }

    private fun endAnim(){
        val scaleAnimation = ScaleAnimation(1f, 0f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        scaleAnimation.duration = 200
        scaleAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                textMoney.visibility = View.GONE
                circleProgressView.setImageResource(R.drawable.ic_coin)
                circleProgressView.background = null
                //恢复到计时状态
                val animatorSet2 = AnimatorSet()
                val scaleX2: ObjectAnimator = ObjectAnimator.ofFloat(circleProgressView, "scaleX", 0.0f, 1.0f)
                val scaleY2: ObjectAnimator = ObjectAnimator.ofFloat(circleProgressView, "scaleY", 0.0f, 1.0f)
                animatorSet2.playTogether(scaleX2, scaleY2)
                animatorSet2.duration = 200
                animatorSet2.start()
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
        circleProgressView.startAnimation(scaleAnimation)
        val textScaleAnimation = ScaleAnimation(1f, 0f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f)
        textScaleAnimation.duration = 200
        textMoney.startAnimation(textScaleAnimation)


    }

    private fun startTextAnimation(){
        val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
        alphaAnimation.duration = 150
        textMoney.visibility = View.VISIBLE
        alphaAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                //todo 修改为money
                val objectAnimator = ObjectAnimator.ofInt(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                objectAnimator.duration = 300
                objectAnimator.addUpdateListener(AnimatorUpdateListener { animation: ValueAnimator ->
                    val value = animation.animatedValue as Int
                    textMoney.text = "+$value"
                })
                objectAnimator.setInterpolator(LinearInterpolator())
                objectAnimator.addListener(listener)
                objectAnimator.start()
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
        textMoney.startAnimation(alphaAnimation)

    }


    override fun onClick(v: View) {
        if (v.id == btnController.id) {
            if (circleProgressView.isRunning) {
                circleProgressView.pause()
            }else{
                circleProgressView.setProgress(1000, (1 - circleProgressView.progress / 1000) * 30000L, listener)
            }
        }
    }

    class MyHandler internal constructor(context: ViewActivity) : Handler() {
        private val activity: WeakReference<ViewActivity> = WeakReference<ViewActivity>(context)
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val activityView: ViewActivity? = activity.get()
            if (activityView != null) {
                when (msg.what) {
                    START_MONEY_ANIM -> activityView.startTextAnimation()
                    END_ALL_ANIM -> activityView.endAnim()
                }
            }
        }

        companion object {
            const val START_MONEY_ANIM = 1
            const val END_ALL_ANIM = 2
        }

    }
}