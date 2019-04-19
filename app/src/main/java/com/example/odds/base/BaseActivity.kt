/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.ContentFrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.odds.R

/**
 * base Activity
 */
abstract class BaseActivity : AppCompatActivity() {

    /**
     * 添加介绍文字，需要设置根目录为 root
     */
    fun createIntroduction(text: String){
        val textView = TextView(this)
        textView.setText(text)
        textView.textSize = 16F
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.setPadding(20,0,20,20)
        val layoutParams = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.endToEnd = ConstraintSet.PARENT_ID
        layoutParams.bottomToBottom = ConstraintSet.PARENT_ID

        val root = findViewById<ConstraintLayout>(R.id.root)
        root.addView(textView, layoutParams)

    }
}
