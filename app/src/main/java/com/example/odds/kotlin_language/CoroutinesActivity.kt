package com.example.odds.kotlin_language

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.odds.R
import kotlinx.coroutines.*

/**
 * kotlin 协程
 */
class CoroutinesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutines)

        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
            println("World!") // 在延迟后打印输出
        }
        println("Hello,") // 协程已在等待时主线程还在继续
        Thread.sleep(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活

        runBlocking {
            launch {

            }
            coroutineScope {

            }
        }

    }
}