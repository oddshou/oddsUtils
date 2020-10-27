package com.example.odds.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.odds.R
import com.example.odds.tools.TintStateBarUtil
import kotlinx.android.synthetic.main.activity_test_ui.*

class UiTestActivity : AppCompatActivity() {
    private val drawable = ColorDrawable()
    private var drawable2: Drawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_ui)
        TintStateBarUtil.setStatusBarTintCompat(this, true)
//        TintStateBarUtil.setStatusBarIconDark(this, true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        drawable2 = ContextCompat.getDrawable(this, R.drawable.superplayer_thumb)
        layoutBottom.setPadding(0, 0, 0, layoutBottom.paddingBottom)
//        seekBar.thumb = drawable

    }

    fun onChange(view: View) {
        if (seekBar.thumb != drawable) {
            seekBar.thumb = drawable;
        } else {
            seekBar.thumb = drawable2
        }
        test()

    }

    fun test(){
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
    }
}