/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.route

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.example.odds.R
import kotlinx.android.synthetic.main.activity_custom_view.*
import android.graphics.Shader.TileMode
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Handler
import android.os.Message
import android.view.View
import java.util.*


class CustomViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view)
        //progress
        progress()



//        val b = BitmapFactory.decodeResource(resources, R.drawable.progress)
//        val bitmapDrawable = BitmapDrawable(resources, b)
//        bitmapDrawable.tileModeX = TileMode.REPEAT;





//        initAnimation();


        //直接设置帧动画
        testImg.setImageResource(android.R.drawable.progress_indeterminate_horizontal)

        val drawable = testImg.drawable
        if (drawable is AnimationDrawable) {
            drawable.start()
        }
//        mHandler.sendEmptyMessage(MSG_1)

    }
    companion object {
        const val MSG_1 = 1
    }
//    val mHandler:Handler = object :Handler(){
//        override fun handleMessage(msg: Message?) {
//            super.handleMessage(msg)
//            roundImage.progress += 1
//            this.sendEmptyMessageDelayed(MSG_1,100)
//        }
//    }

    fun testImgClick(v: View){
        v.layoutParams.width = 20 + v.layoutParams.width
        v.requestLayout();
    }

    fun progress(){
//        head_progressBar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate))
    }

    //通过 一张图片切出帧动画
    private fun initAnimation() {
        //      R.drawable.tile1 is PNG
        val b = BitmapFactory.decodeResource(resources, R.drawable.progress)
        val shiftedAnimation = getAnimation(b)

        //      R.id.img_3 is ImageView in my application

//        testImg.setBackground(shiftedAnimation)
        testImg.setImageDrawable(shiftedAnimation)
        shiftedAnimation.start()
    }

    private fun getShiftedBitmap(bitmap: Bitmap, shiftX: Int): Bitmap {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val newBitmapCanvas = Canvas(newBitmap)

        val srcRect1 = Rect(shiftX, 0, bitmap.width, bitmap.height)
        val destRect1 = Rect(srcRect1)
        destRect1.offset(-shiftX, 0)
        newBitmapCanvas.drawBitmap(bitmap, srcRect1, destRect1, null)

        val srcRect2 = Rect(0, 0, shiftX, bitmap.height)
        val destRect2 = Rect(srcRect2)
        destRect2.offset(bitmap.width - shiftX, 0)
        newBitmapCanvas.drawBitmap(bitmap, srcRect2, destRect2, null)

        return newBitmap
    }

    private fun getShiftedBitmaps(bitmap: Bitmap): List<Bitmap> {
        val shiftedBitmaps = ArrayList<Bitmap>()
        val fragments = 10
        val shiftLength = bitmap.width / fragments

        for (i in 0 until fragments) {
            shiftedBitmaps.add(getShiftedBitmap(bitmap, shiftLength * i))
        }

        return shiftedBitmaps
    }

    private fun getAnimation(bitmap: Bitmap): AnimationDrawable {
        val animation = AnimationDrawable()
        animation.isOneShot = false

        val shiftedBitmaps = getShiftedBitmaps(bitmap)
        val duration = 50

        for (image in shiftedBitmaps) {
            val navigationBackground = BitmapDrawable(resources, image)
            navigationBackground.tileModeX = TileMode.REPEAT
            navigationBackground.tileModeY = TileMode.REPEAT

            animation.addFrame(navigationBackground, duration)
        }
        return animation
    }
}
