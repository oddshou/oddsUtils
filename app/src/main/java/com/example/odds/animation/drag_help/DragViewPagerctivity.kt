/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.animation.drag_help

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.odds.R
import com.example.odds.animation.phote_view.PhotoView
import kotlinx.android.synthetic.main.activity_drag_view_pagerctivity.*

class DragViewPagerctivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_view_pagerctivity)
        val viewList = ArrayList<View>()
        val container1 = DragConstraintLayout(this)
        container1.setBackgroundColor(Color.parseColor("#FF000000"))
        val view1 = PhotoView(this)
        view1.setImageResource(R.drawable.img_logo)
//        container1.addView(view1, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT))
        viewList.add(view1)
        val container2 = DragConstraintLayout(this)
        container2.setBackgroundColor(Color.parseColor("#FF000000"))
        val view2 = PhotoView(this)
        view2.setImageResource(R.drawable.hsgame_btn_hot_press)
//        container2.addView(view2, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT))
        viewList.add(view2)


        viewPager.adapter = InnerPagerAdapter(viewList)
    }

    inner class InnerPagerAdapter(private val viewList: List<View>) : PagerAdapter() {

        override fun isViewFromObject(view: View, anyObject: Any): Boolean {
            return view == anyObject
        }

        override fun getCount(): Int {
            return viewList.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(viewList.get(position), ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT))
            return viewList.get(position)
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View?)

        }

    }


}
