/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.features


import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.provider.Settings.Secure
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_screen.*


/**
 *
 */
class ScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.odds.R.layout.activity_screen)
        getScreenSize(this)
        getDevices()
    }

    fun getDevices(){
        val androidID = Secure.getString(getContentResolver(), Secure.ANDROID_ID)
        textDevice.text = "androidId: $androidID"
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
        val statusHeight = getStatusBarHeight(resources, "status_bar_height")
        val navigatHeight = getStatusBarHeight(resources, "navigation_bar_height")



        val texts = "densityDpi: " + densityDpi + "\n" +
                "字体scaledDensity: " + scaledDensity + "\n" +
                "density: " + density + "\n" +
                "xdpi: " + xdpi + "\n" +
                "ydpi: " + ydpi + "\n" +
                "widthPixels: " + widthPixels + "\n" +
                "heightPixels: " + heightPixels + "\n" +
                "density=densityDpi/160:= " + densityDpi / 160.0 + "\n" +
                "widthdp=widthPixels/density:= " + widthPixels / density +"\n" +
                "realH PX: " + getRealHight(this) + "\n" +
                "statusH PX: " + statusHeight +"\n" +
                "navigateH PX: " + navigatHeight + "\n"


        //        // 这样可以计算屏幕的物理尺寸
        //        float width2 = (width / xdpi)*(width / xdpi);
        //        float height2 = (height / ydpi)*(width / xdpi);
        //
        //        return (float) Math.sqrt(width2+height2);

        text.text = texts
    }

    //状态栏高度 "status_bar_height"
    //获取虚拟按键栏高度 "navigation_bar_height"
    fun getStatusBarHeight(resources: Resources, field: String): Int {
        val resourceId = resources.getIdentifier(field, "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 获取屏幕实际高度（也包含虚拟导航栏）
     *
     * @param context
     * @return
     */
    fun getRealHight(context: Context): Int {
        var displayMetrics: DisplayMetrics? = null
        if (displayMetrics == null) {
            displayMetrics = DisplayMetrics()
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                windowManager.defaultDisplay.getRealMetrics(displayMetrics)
            }
        }
        return displayMetrics.heightPixels
    }
}
