/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.adapter

import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * 单一的 recycleView adapter，只有一个 textView
 */
class SimpleAdapter(val list: ArrayList<Map<String, *>>): RecyclerView.Adapter<SimpleAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = TextView(parent.context)
        textView.textSize = 16F
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.setPadding(60,0,0,0)
        textView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150)
        return ViewHolder(textView)
    }

    override fun getItemCount(): Int {
        return list.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.setText(list[position]["title"].toString())
        holder.view.setOnClickListener{
            val intent = list[holder.adapterPosition]["intent"];
            holder.view.context.startActivity(intent as Intent?)
        }
    }

    /**
     *  单个view 直接传递过来
     */
    class ViewHolder(val view: TextView) : RecyclerView.ViewHolder(view) {}
}