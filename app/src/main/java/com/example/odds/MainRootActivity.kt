/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.odds.adapter.SimpleAdapter
import kotlinx.android.synthetic.main.activity_main_root.*
import java.text.Collator
import java.util.*
import java.util.Collections.sort

/**
 * 目录页面：
 * 生成目录规则，Activity
 * 1. 设置 固定的 intent filter
 * 2. Activity label 用 "/" 分隔目录层级：ex：route/CActivity哈哈
 */
class MainRootActivity : AppCompatActivity() {

    val CATEGORY_ODDS_TESTALL = "com.example.odds.set_root"
    private val EXTRA_TEST_PATH = "com.oddshou.testall.Path"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_root)
        val intent = intent
        var path: String? = intent.getStringExtra(EXTRA_TEST_PATH)

        if (path == null) {
            path = ""
        }
        recycleView.adapter = SimpleAdapter(getData(path))
        recycleView.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        recycleView.layoutManager = LinearLayoutManager(this)
    }

    protected fun getData(prefix: String): ArrayList<Map<String, *>> {
        val myData = ArrayList<Map<String, *>>()

        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(CATEGORY_ODDS_TESTALL)

        val pm = packageManager
        val list = pm.queryIntentActivities(mainIntent, 0) ?: return myData

        val prefixPath: Array<String>?

        if (prefix == "") {
            prefixPath = null
        } else {
            // prefix.split("/")
            prefixPath = prefix.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        }

        val len = list.size

        val entries = HashMap<String, Boolean>()

        list.forEach {info ->
            val labelSeq = info.loadLabel(pm)
            val label = labelSeq?.toString() ?: info.activityInfo.name

            if (prefix.length == 0 || label.startsWith(prefix)) {

                val labelPath = label.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                val nextLabel = if (prefixPath == null) labelPath[0] else labelPath[prefixPath.size]

                if (prefixPath?.size ?: 0 == labelPath.size - 1) {
                    //最后一个隔断，跳转指定Activity
                    addItem(myData, nextLabel, activityIntent(
                            info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.name))
                } else {
                    //非最后一个隔断
                    if (entries[nextLabel] == null) {
                        //同一层级不重复添加
                        addItem(myData, nextLabel, browseIntent(if (prefix == "") nextLabel else "$prefix/$nextLabel"))
                        entries[nextLabel] = true
                    }
                }
            }
        }
        sort<Map<String,*>>(myData, sDisplayNameComparator)

        return myData
    }

    protected fun addItem(data: MutableList<Map<String, *>>, name: String, intent: Intent) {
        val temp = HashMap<String, Any>()
        temp["title"] = name
        temp["intent"] = intent
        data.add(temp)
    }

    protected fun activityIntent(pkg: String, componentName: String): Intent {
        val result = Intent()
        result.setClassName(pkg, componentName)
        return result
    }

    protected fun browseIntent(path: String): Intent {
        val result = Intent()
        result.setClass(this, MainRootActivity::class.java!!)
        result.putExtra(EXTRA_TEST_PATH, path)
        return result
    }

    private val sDisplayNameComparator = object : Comparator<Map<String, *>> {
        private val collator = Collator.getInstance()

        override fun compare(map1: Map<String, *>, map2: Map<String, *>): Int {
            return collator.compare(map1["title"], map2["title"])
        }
    }
}
