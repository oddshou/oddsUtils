/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.kotlin_language

class Main {
    /**
     * 隐式转换测试：
     * Int，继承自 抽象类 Number，Number 定义一系列抽象方法 toDouble,toInt,toChar...
     * Byte,Short,Int,Flot,Long,Double 都继承Number
     */
    fun implicitConver() {
        val aInt: Int = 5
//        val aLong: Long = aInt  //error
        val bLong: Long = aInt.toLong()
        val s:String = "hello"
        s.toFloat()
    }
}