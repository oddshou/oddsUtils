
package com.example.odds.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.odds.R;

public class WaveViewActivity extends Activity {

    private WaveView mWaveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_view);
        mWaveView = (WaveView)findViewById(R.id.waveView);
    }
    
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mWaveView.setDestRate(0.5f);
        mWaveView.setWaterColor(getResources().getColor(R.color.water_normal));
        mWaveView.setBackgroundResource(R.drawable.circle_normal);
    }
    
    public void doClick(View v){
        switch (Integer.valueOf((String)v.getTag())) {
            case 8:
                mWaveView.setDestRate(0.8f);
                break;
            case 2:
                mWaveView.setDestRate(0.2f);
                break;
            default:
                break;
        }
    }
}
