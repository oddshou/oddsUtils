package com.example.odds.java_main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.odds.R;
import com.example.odds.annotations.InitFile;
import com.example.odds.route.RxActivity;
import com.odds.annotation.processor.PreIntent;


public class TestPreIntentActivity extends AppCompatActivity {

    @InitFile
    public String mName;
    @InitFile
    public boolean isGood;
    @InitFile
    public int num;
    @InitFile
    public Bundle bundle = new Bundle();
    @InitFile
    public String age = "3333";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        PreIntent.onSave_MainActivityJava(outState, this);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreIntent.onCreate_MainActivityJava(savedInstanceState, this);
        setContentView(R.layout.activity_main_java);

        TextView view = findViewById(R.id.args);
        view.setText(String.format("mName: %s \n isGood: %s \n num: %d \n bundle: %s \n age: %s",
                mName, isGood, num, bundle, age));
    }


    public void toText(View view) {
        //进入下一个页面测试onSaveInstanceState 是否生效
        startActivity(new Intent(this, RxActivity.class));
    }
}
