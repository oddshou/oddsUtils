/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.base

import android.content.Intent
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.odds.MainRootActivity
import com.example.odds.R
import com.example.odds.databinding.ActivityBaseBinding

/**
 * base Activity
 */
abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var binding: ActivityBaseBinding

    override fun setContentView(layoutResID: Int) {
        binding = ActivityBaseBinding.inflate(layoutInflater)
        super.setContentView(binding.root)
        if (layoutResID > 0) {
            val view = layoutInflater.inflate(layoutResID, binding.root, false)
            val layoutParams = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
            view.layoutParams = layoutParams
            layoutParams.bottomToTop = binding.llBottom.id
            layoutParams.topToTop = ConstraintSet.PARENT_ID
            binding.root.addView(view, 0, layoutParams)
        }
    }

    /**
     * 添加介绍文字
     */
    fun setTips(text: String){
        binding.tvTips.text = text
    }

    fun getBottomText(): TextView{
        val textView = TextView(this)
        addBottomView(textView)
        return textView
    }

    private fun addBottomView(textView: TextView) {
        textView.textSize = 16F
        textView.gravity = Gravity.CENTER
        textView.setPadding(0, 20, 0, 20)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.llBottom.addView(textView, 0, layoutParams)
    }

    fun getBottomBtn():Button{
        val button = Button(this)
        addBottomView(button)
        return button
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.base_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.navigation_home) {
            startActivity(Intent(this, MainRootActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
