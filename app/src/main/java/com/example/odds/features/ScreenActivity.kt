/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.features

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.View
import android.widget.TextView

import com.example.odds.R

/**
 *
 */
class ScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen)
        getScreenSize(this)
    }

    fun getScreenSize(mContext: Context) {

        val densityDpi = mContext.resources.displayMetrics.densityDpi
        val scaledDensity = mContext.resources.displayMetrics.scaledDensity
        val density = mContext.resources.displayMetrics.density
        val xdpi = mContext.resources.displayMetrics.xdpi
        val ydpi = mContext.resources.displayMetrics.ydpi
        val widthPixels = mContext.resources.displayMetrics.widthPixels
        val heightPixels = mContext.resources.displayMetrics.heightPixels
        //density = densitydpi/160
        //widthdp = widthPixels/density


        val texts = "densityDpi: " + densityDpi + "\n" +
                "scaledDensity: " + scaledDensity + "\n" +
                "density: " + density + "\n" +
                "xdpi: " + xdpi + "\n" +
                "ydpi: " + ydpi + "\n" +
                "widthPixels: " + widthPixels + "\n" +
                "heightPixels: " + heightPixels + "\n" +
                "density=densityDpi/160:= " + densityDpi / 160.0 + "\n" +
                "widthdp=widthPixels/density:= " + widthPixels / density


        //        // 这样可以计算屏幕的物理尺寸
        //        float width2 = (width / xdpi)*(width / xdpi);
        //        float height2 = (height / ydpi)*(width / xdpi);
        //
        //        return (float) Math.sqrt(width2+height2);

        (findViewById<View>(R.id.text) as TextView).text = texts
    }
}
