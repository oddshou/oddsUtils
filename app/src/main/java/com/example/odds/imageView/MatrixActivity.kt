/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.imageView

import android.graphics.Matrix
import android.os.Bundle
import com.example.odds.R
import com.example.odds.base.BaseActivity
import kotlinx.android.synthetic.main.activity_matrix.*


class MatrixActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matrix)
        createIntroduction("通过 matrix 使图片等比例缩放下面留空")

        //获取图片的原始宽度
//        val options = BitmapFactory.Options()
//        options.inJustDecodeBounds = true
//        BitmapFactory.decodeResource(resources, R.drawable.new_welcome1, options)
//        val imageWidth = options.outWidth
        //屏幕宽度：1080，屏幕密度 3，图片宽度 1080
        val scaleRate  = resources.displayMetrics.widthPixels * 1.0f / img.drawable.intrinsicWidth

        //设置matrix
        val matrix = Matrix()

        //设置放缩比例
        matrix.setScale(scaleRate, scaleRate)

        img.imageMatrix = matrix
    }
}
