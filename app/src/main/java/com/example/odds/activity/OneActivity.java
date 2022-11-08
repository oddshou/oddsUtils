
package com.example.odds.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.odds.R;

public class OneActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
    }
    
    public void doClick(View v){
        switch (Integer.valueOf((String)v.getTag())) {
            case 10: //back
                finish();
                break;
            case 11:
                startActivity(new Intent(this, TwoActivity.class));
                break;

            default:
                break;
        }
    }
}
