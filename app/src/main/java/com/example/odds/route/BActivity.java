package com.example.odds.route;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.odds.odds.R;

public class BActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
    }

    public void clickBtn(View view) {
        Intent intent = new Intent(this, CActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        finish();
    }
}
