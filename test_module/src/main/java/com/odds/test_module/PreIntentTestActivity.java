package com.odds.test_module;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.odds.annotations.InitFile;

public class PreIntentTestActivity extends AppCompatActivity {

    @InitFile
    public String name = "hello";
    @InitFile
    public boolean age = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_intent_test);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}