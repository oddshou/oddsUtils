package com.example.odds.java_main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.odds.R;
import com.example.odds.annotations.InitFile;
import com.example.odds.annotations.Route;
@Route
public class MainActivityJava extends AppCompatActivity {
    @InitFile
    private String mName;


    private int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_java);

    }
}
