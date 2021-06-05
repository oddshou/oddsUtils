/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.custom_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.example.odds.utils.TextLayoutUtil;

import java.util.Locale;

import androidx.annotation.Nullable;


public class TimeProgressView extends View {

    private static final String TAG = "HPDTimeProgressView";

    private Context context;
    private Paint paint;
    private int line_height;
    private int line_color;
    private int seek_height;
    private int seek_width;
    private float text_size;

    private int themeColor;

    private int currentTime = 0;
    private int totalTime = 100;
    private float circleRadius;
    //是否显示前后两个小圆点
    private boolean showCircle;

    public void setShowCircle(boolean showCircle) {
        this.showCircle = showCircle;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private Callback callback;

    private boolean isMove = false;

    public TimeProgressView(Context context) {
        super(context);
        initView(context);
    }

    public TimeProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TimeProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //居中
        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //恢复原来的状态，3个-1是直接看源码
        ToastUtils.setGravity(-1, -1, -1);
    }

    private void initView(Context context) {

        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#ff0000"));
        paint.setTextSize(text_size);
        line_height = TextLayoutUtil.dp2px(context,2);
        line_color = Color.parseColor("#D0D0D0");
        themeColor = Color.parseColor("#00B5FF");
        seek_height = TextLayoutUtil.dp2px(context,15);
        seek_width = TextLayoutUtil.dp2px(context,69);
        text_size = TextLayoutUtil.sp2px(context,9);
        //new  rect
        circleRadius = 3 * line_height / 2;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        //背景条
        paint.setColor(line_color);
        Rect rect = new Rect(2 * line_height, (getHeight() - line_height) / 2, getWidth(), (getHeight() - line_height) / 2 + line_height);
        canvas.drawRect(rect, paint);
        //绘制小终点
        if (showCircle) {
            RectF endCircle = new RectF(getWidth() - 3 * line_height, getHeight() / 2 - circleRadius, getWidth(), getHeight() / 2 + circleRadius);
            canvas.drawRoundRect(endCircle, 3 * line_height / 2, 3 * line_height / 2, paint);
        }

        float currentTimePostion = (float) currentTime / totalTime * getWidth();

        //进度
        paint.setColor(themeColor);
        Rect progressRect = new Rect(0, (getHeight() - line_height) / 2, (int) currentTimePostion, (getHeight() - line_height) / 2 + line_height);
        canvas.drawRect(progressRect, paint);

        //绘制小起点
        if (showCircle) {
            RectF startCircle = new RectF(0, getHeight() / 2 - circleRadius, 3 * line_height, getHeight() / 2 + circleRadius);
            canvas.drawRoundRect(startCircle, 3 * line_height / 2, 3 * line_height / 2, paint);
        }

        float barLeft = currentTimePostion - seek_width/2;
        if (barLeft < 0) {
            barLeft = 0;
        } else if (barLeft > getWidth() - seek_width) {
            barLeft = getWidth() - seek_width;
        }
        //拖动条
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);//充满
        RectF seekBarRectF = new RectF(barLeft, (getHeight() - seek_height) / 2, barLeft + seek_width, (getHeight() - seek_height) / 2 + seek_height);// 设置个新的长方形
        canvas.drawRoundRect(seekBarRectF, seek_height / 2, seek_height / 2, paint);

        //时间
        paint.setColor(themeColor);
        paint.setTextSize(text_size);

        String timeText = intToTime(currentTime) + "/" + intToTime(totalTime);
        Rect textRect = new Rect();
        paint.getTextBounds(timeText, 0, timeText.length(), textRect);

        float leftTimeX = barLeft + seek_width / 2 - textRect.width() / 2;
        int leftTimeY = getHeight() / 2 + textRect.height() / 2 - 3;
        canvas.drawText(timeText.substring(0, 6), leftTimeX, leftTimeY, paint);

        String leftTimeText = timeText.substring(0, 6);
        paint.getTextBounds(leftTimeText, 0, leftTimeText.length(), textRect);

        paint.setColor(Color.WHITE);
        canvas.drawText(timeText.substring(6), leftTimeX + textRect.width(), leftTimeY, paint);


    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //为了解决滑动不到最大最小值
        float x = event.getX();
        if (x < 0) {
            x = 0;
        } else if (x > getWidth()) {
            x = getWidth();
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMove = true;
                break;

            case MotionEvent.ACTION_MOVE:
                isMove = true;
                if (callback != null) {
                    callback.seeking(intToTime(currentTime) + "/" + intToTime(totalTime));
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isMove = false;
                if (callback != null) {
                    callback.seek(currentTime);
                    callback.seeking(intToTime(currentTime) + "/" + intToTime(totalTime));
                }
                break;
        }

        currentTime = (int) (x / getWidth() * totalTime);
        invalidate();
        return true;
    }

    private String intToTime(int time) {

        int minute = time / 60;
        int second = time % 60;
        return String.format(Locale.CHINA, "%02d:%02d", minute, second);
    }

    public void setProgress(int time) {
        if (isMove) {
            return;
        }
        currentTime = time;
        invalidate();
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public interface Callback {

        void seek(int seekTime);

        void seeking(String seekTime);
    }
}
