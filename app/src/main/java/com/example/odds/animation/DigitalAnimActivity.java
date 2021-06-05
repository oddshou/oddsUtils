package com.example.odds.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.odds.R;
import com.handmark.pulltorefresh.library.extras.ScreenUtil;

import androidx.appcompat.app.AppCompatActivity;

public class DigitalAnimActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DigitalAnimActivity";
    private TextView textQuotaTip;
    private TextView textQuota;
    private TextView textGetQuotaSuccess;
    private ImageView imgQuota;
    private ImageView imgGetQuota;

    private float dp_36;
    private float dp_117;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_anim);

        textQuotaTip = findViewById(R.id.textQuotaTip);
        textQuota = findViewById(R.id.textQuota);
        textGetQuotaSuccess = findViewById(R.id.textGetQuotaSuccess);
        imgQuota = findViewById(R.id.imgQuota);
        imgGetQuota = findViewById(R.id.imgGetQuota);

        imgGetQuota.setOnClickListener(this);

        dp_36 = ScreenUtil.dip2px(this, 36);
        dp_117 = ScreenUtil.dip2px(this, 117);

    }

    @Override
    public void onClick(View v) {
        //1、按钮点击之后消失
        v.setVisibility(View.INVISIBLE);
        textQuotaTip.setVisibility(View.GONE);
        //2、200ms 图片向下消失
        ViewPropertyAnimator translation = imgQuota.animate().translationY(imgQuota.getHeight()).setDuration(200);
        translation.start();
        translation.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //3、先执行数字滚动动画
                textQuota.setVisibility(View.VISIBLE);
                ValueAnimator objectAnimator = ObjectAnimator.ofInt(0, 2000);
                objectAnimator.setDuration(2160);
                objectAnimator.addUpdateListener(valueAnimator -> {
                    int value = (int) valueAnimator.getAnimatedValue();
                    textQuota.setText("¥" + value);
                });
                objectAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        //3.2、数字滚动结束后，回到正确位置
                        ViewPropertyAnimator translationY = textQuota.animate().translationY(-dp_117)
                                .setDuration(200);
                        translationY.start();
                        translationY.setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                imgGetQuota.setVisibility(View.VISIBLE);
                            }
                        });
                        textGetQuotaSuccess.setTranslationY(-dp_36);
                        textGetQuotaSuccess.setVisibility(View.VISIBLE);
                        textGetQuotaSuccess.animate().translationY(-dp_117)
                                .setDuration(200).start();
                    }
                });
                objectAnimator.setInterpolator(new LinearInterpolator());
                objectAnimator.start();

                //3.1、同时执行从底部由小变大到中间的动画
                textQuota.setScaleX(0.5f);
                textQuota.setScaleY(0.5f);
                ViewPropertyAnimator transAlpha = textQuota.animate();
                transAlpha.translationY(-dp_36);
                transAlpha.scaleX(1);
                transAlpha.scaleY(1);
                transAlpha.setDuration(500).start();

            }
        });
        //3、文字从底部向上出现

    }


}