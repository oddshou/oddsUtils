package com.example.odds.custom_view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.odds.R;
import com.example.odds.custom_view.CircleProgress;

public class CircleProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_progress);
        CircleProgress circleProgress = findViewById(R.id.circleProgressView);
        circleProgress.setValue(100);
    }
}