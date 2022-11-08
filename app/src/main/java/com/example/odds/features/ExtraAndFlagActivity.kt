/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.features

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.odds.R
import com.example.odds.base.BaseActivity

class ExtraAndFlagActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra_and_flag)
        setTips("测试Activity传参，ActivityFalg")
    }

    fun toNext(view: View) {
        val intent = Intent(this, BActivity::class.java)
        //        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra("extra1", "fromeMain")
        startActivityForResult(intent, 330)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(this, "A:" + if (data == null) data else data.getStringExtra("data"), Toast.LENGTH_SHORT).show()
        //        System.out.println("A:" + (data == null ? data : data.getStringExtra("data")));
    }

}
