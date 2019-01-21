package com.example.odds.route;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.odds.R;

public class BActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

        //验证得 getIntent activity 销毁不会存储
        Toast.makeText(this, "获取数据" + getIntent().getStringExtra("extra1"),
                Toast.LENGTH_LONG).show();
        //fragment argument 会得到存储
    }

    public void clickBtn(View view) {
        Intent intent = new Intent(this, CActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        finish();
    }
}
