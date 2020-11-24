/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.pre_intent

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.odds.R
import com.odds.annotation.processor.PreIntent
import java.util.*

/**
 * 测试 preIntent 库
 * https://github.com/oddshou/AndroidSomeTest
 */
class PreIntentOriginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_intent_origin)
    }

    fun toNext(v: View){
        val bundle = Bundle()
        bundle.putString("bundle", "bundle-bundle")
        val list = ArrayList<String>()
        list.add("11")
        list.add("22")
        list.add("33")

        val serialize66 = TestPreIntentActivity.SerializeClass("Serialize66")
        val parcelable55 = TestPreIntentActivity.ParcelableClass("Parcelable55")
        val list1 = ArrayList<TestPreIntentActivity.ParcelableClass>()
        list1.add(parcelable55)
        val list2 = ArrayList<TestPreIntentActivity.SerializeClass>()
        list2.add(serialize66)
        val intent = PreIntent.preIntent_TestPreIntentActivity(this, "哈哈哈", true, 111, bundle,
                list, parcelable55, serialize66, arrayOf(parcelable55), arrayOf(serialize66),
                list1, list2)
        startActivity(intent)
    }
}
