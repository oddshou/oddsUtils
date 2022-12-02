package com.example.odds.kotlin_language

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import java.io.File
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.math.log
import kotlin.system.measureTimeMillis

/**
 * ClassName:CouroutinesTest
 * Author:oddshou
 * Description:
 * 2022/11/18 3:33 PM
 * Wiki:
 */

suspend fun doSomethingUsefulOne(): Int {
    delay(1000L) // 假设我们在这里做了些有用的事
    println("doSomethingUsefulOne" + System.currentTimeMillis())
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L) // 假设我们在这里也做了一些有用的事
    println("doSomethingUsefulTwo" + System.currentTimeMillis())
    return 29
}

fun main() =runBlocking{
    println("start")
    val time = measureTimeMillis {
        val one = async { doSomethingUsefulOne() }
        launch { doSomethingUsefulTwo() }
        one.await()
    }
    println("Completed in $time ms")
}

class  CoroutinesTest {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            MainScope()
            GlobalScope.launch {  }
        }

        fun t01(){


        }




    }
}