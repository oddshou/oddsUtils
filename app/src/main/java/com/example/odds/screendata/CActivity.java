package com.example.odds.screendata;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
