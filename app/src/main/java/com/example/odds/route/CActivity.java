package com.example.odds.route;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.odds.R;

import androidx.appcompat.app.AppCompatActivity;

public class CActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);
    }

    public void clickBtn(View view) {
        Intent intent=new Intent();
        intent.putExtra("data","data");
        setResult(Activity.RESULT_OK,intent);
        finish();

    }
}
