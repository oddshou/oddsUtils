package com.example.odds.route;

import android.os.Bundle;
import android.util.Log;

import com.example.odds.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;

public class ThreadActivity extends AppCompatActivity {
    private String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);


    }

    private void cacheThreadPool(){
        ExecutorService executorService = Executors.newCachedThreadPool();
//        ExecutorService executorService1 = new ThreadPoolExecutor(2,5,30,TimeUnit.SECONDS
//        )
        for (int i = 0; i <10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "run thread " + Thread.currentThread().getName());

                }
            });
        }

    }
    private void fixThreadPool(){
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i <10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "run thread " + Thread.currentThread().getName());

                }
            });
        }

    }


    public interface Self{
        void get0();
        void get1();
        //java8 特性 需要api 24，7.0以上版本
//        static void get2();
//        default int get3(int i){
//            return i;
//        }
    }
}
