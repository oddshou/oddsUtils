package com.example.odds.screendata;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPingMuSize(this);
    }

    public void getPingMuSize(Context mContext) {


        int densityDpi = mContext.getResources().getDisplayMetrics().densityDpi;
        float scaledDensity = mContext.getResources().getDisplayMetrics().scaledDensity;
        float density = mContext.getResources().getDisplayMetrics().density;
        float xdpi = mContext.getResources().getDisplayMetrics().xdpi;
        float ydpi = mContext.getResources().getDisplayMetrics().ydpi;
        int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        int heightPixels = mContext.getResources().getDisplayMetrics().heightPixels;
        //density = densitydpi/160
        //widthdp = widthPixels/density


        String texts = "densityDpi: " + densityDpi + "\n"+
                "scaledDensity: " + scaledDensity + "\n"+
                "density: " + density + "\n"+
                "xdpi: " + xdpi + "\n"+
                "ydpi: " + ydpi + "\n"+
                "widthPixels: " + widthPixels + "\n"+
                "heightPixels: " + heightPixels + "\n"+
                "density=densityDpi/160:= "+(densityDpi/160.0)+ "\n" +
                "widthdp=widthPixels/density:= " + (widthPixels/density);


//        // 这样可以计算屏幕的物理尺寸
//        float width2 = (width / xdpi)*(width / xdpi);
//        float height2 = (height / ydpi)*(width / xdpi);
//
//        return (float) Math.sqrt(width2+height2);

        ((TextView)findViewById(R.id.text)).setText(texts);
    }

    public void clickBtn(View view) {
        Intent intent = new Intent(this, BActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 330);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "A:" + (data == null ? data : data.getStringExtra("data")), Toast.LENGTH_SHORT).show();
//        System.out.println("A:" + (data == null ? data : data.getStringExtra("data")));
    }

    public void toRxActivity(View view) {
        startActivity(new Intent(this, RxActivity.class));
    }

    public void toThreadActivity(View view) {
        startActivity(new Intent(this, ThreadActivity.class));
    }

    public void toDataBinding(View view) {
        startActivity(new Intent(this, DataBindingActivity.class));
    }
}
