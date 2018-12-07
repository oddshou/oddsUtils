package com.example.odds.java_main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.odds.R;
import com.example.odds.annotations.InitFile;
import com.example.odds.annotations.Route;
@Route
public class MainActivityJava extends AppCompatActivity {

    @InitFile
    public String mName;
//    @InitFile
//    public boolean isGood;
//    @InitFile
//    public int num;
//    @InitFile
//    public Bundle bundle;

    private int age;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("mName", mName);
//        outState.putBoolean("isGood", isGood);
//        outState.putInt("num", num);
//        outState.putBundle("paperId", bundle);
        super.onSaveInstanceState(outState);
    }

    public static void onSave(Bundle outState, MainActivityJava activityJava) {
        outState.putString("mName", activityJava.mName);
//        outState.putBoolean("isGood", activityJava.isGood);
//        outState.putInt("num", activityJava.num);
//        outState.putBundle("bundle", activityJava.bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null) {
//            mName = savedInstanceState.getString("mName");
//            isGood = savedInstanceState.getBoolean("isGood");
//            num = savedInstanceState.getInt("num");
//            bundle = savedInstanceState.getBundle("bundle");
//        }else {
//            Intent intent = getIntent();
//            mName = intent.getStringExtra("mName");
//            isGood = intent.getBooleanExtra("isGood",false);
//            num = intent.getIntExtra("num", -1);
//            bundle = intent.getBundleExtra("bundle");
//        }

        setContentView(R.layout.activity_main_java);

    }

//    public static void createSave(Bundle saveInstance, MainActivityJava activityJava) {
//        if (saveInstance != null) {
//            activityJava.mName = saveInstance.getString("mName");
//            activityJava.isGood = saveInstance.getBoolean("isGood");
//            activityJava.num = saveInstance.getInt("num");
//            activityJava.bundle = saveInstance.getBundle("bundle");
//        }else {
//            Intent intent = activityJava.getIntent();
//            activityJava.mName = intent.getStringExtra("mName");
//            activityJava.isGood = intent.getBooleanExtra("isGood",false);
//            activityJava.num = intent.getIntExtra("num", -1);
//            activityJava.bundle = intent.getBundleExtra("bundle");
//        }
//    }


}
