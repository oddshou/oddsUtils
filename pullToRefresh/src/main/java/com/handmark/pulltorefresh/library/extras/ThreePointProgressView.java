package com.handmark.pulltorefresh.library.extras;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.handmark.pulltorefresh.library.R;

import java.util.ArrayList;

/**
 * 作者：garneau on 2016/11/15 14:49
 * 邮箱：garneau@fenqile.com
 */

public class ThreePointProgressView extends View {


    int mMinWidth;
    int mMaxWidth;
    int mMinHeight;
    int mMaxHeight;

    public static final float SCALE=1.0f;
    public static final int ALPHA=255;
    //scale x ,y
    private float[] scaleFloats=new float[]{SCALE,
            0.7f,
            0.4f};

    int[] alphas=new int[]{255,
            51,
            51,};

    private Paint mPaint=new Paint();
    private boolean isOpenAnimate;
    private int mPointColor;
    private ArrayList<ValueAnimator> mAnimators;

    public ThreePointProgressView(Context context) {
        this(context, null);
    }

    public ThreePointProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThreePointProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.LoadingIndicatorView, defStyleAttr, defStyleAttr);

        mMinWidth = typedArray.getDimensionPixelSize(R.styleable.LoadingIndicatorView_minWidth, 24);
        mMaxWidth = typedArray.getDimensionPixelSize(R.styleable.LoadingIndicatorView_maxWidth, 48);
        mMinHeight = typedArray.getDimensionPixelSize(R.styleable.LoadingIndicatorView_minHeight, 24);
        mMaxHeight = typedArray.getDimensionPixelSize(R.styleable.LoadingIndicatorView_maxHeight, 48);
        mPointColor = typedArray.getColor(R.styleable.LoadingIndicatorView_indicatorColor, Color.BLUE);

        typedArray.recycle();

        init();

    }


    private void init() {
        mPaint.setColor(mPointColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mAnimators = onCreateAnimators();
    }

    @Override
    public void setVisibility(int v) {
        if (getVisibility() != v) {
            super.setVisibility(v);
            if (v == GONE || v == INVISIBLE) {
                cancelAnimators();
            } else {
                startAnimation();
            }
        }
    }


    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == GONE || visibility == INVISIBLE) {
            cancelAnimators();
        } else {
            startAnimation();
        }
    }



    void startAnimation() {
        if (getVisibility() != VISIBLE) {
            return;
        }
        if (isOpenAnimate){
            start(0);
        }

        invalidate();
    }



    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float circleSpacing = 20;
        float radius=(Math.min(getWidth(), getHeight()) - circleSpacing * 2) / 6;
        float x = getWidth() / 2 - ( radius * 2 + circleSpacing);
        float y = getHeight() / 2;

        //画出每一个球
        for (int i = 0; i < 3; i++) {
            canvas.save();
            float translateX = x + (radius * 2) * i+ circleSpacing * i;
            canvas.translate(translateX, y);
            canvas.scale(scaleFloats[i], scaleFloats[i]);
            mPaint.setAlpha(alphas[i]);
            canvas.drawCircle(0, 0, radius, mPaint);
            canvas.restore();
        }

    }

    public void start(float scaleOfLayout) {


        if (mAnimators == null) {
            return;
        }
        if (isStarted()){
            return;
        }
        isOpenAnimate = true;
        startAnimators(scaleOfLayout);
        invalidate();
    }

    public void cancelAnimators(){

        if (mAnimators == null){
            return;
        }
        if (!isStarted()){
            return;
        }
        for (int i = 0; i < mAnimators.size(); i++) {
            ValueAnimator animator = mAnimators.get(i);
            animator.cancel();
        }

        invalidate();

    }

    private void startAnimators(float scaleOfLayout) {


          for (int i = 0; i < mAnimators.size(); i++) {
              ValueAnimator animator = mAnimators.get(i);

              animator.start();
              animator.setCurrentPlayTime((long) (scaleOfLayout * 1500));
          }


    }


    private boolean isStarted() {
        for (ValueAnimator animator : mAnimators) {
            return animator.isStarted();
        }

        return false;
    }




    public void setAnimatorsTime(float time){
        isOpenAnimate = false;
        for (int i = 0; i < mAnimators.size(); i++) {
            ValueAnimator animator = mAnimators.get(i);
            animator.setCurrentPlayTime((long) ( time * 1500));
        }

    }

    public void reset(){

        if (!isStarted()){
            return;
        }
        isOpenAnimate = false;
        stopAnimators();
        mAnimators = onCreateAnimators();

    }

    private void stopAnimators() {
        if (mAnimators!=null){
            for (ValueAnimator animator : mAnimators) {
                if (animator != null && animator.isStarted()) {
                    animator.removeAllUpdateListeners();
                    animator.end();
                }
            }
        }

        invalidate();
    }


    public ArrayList<ValueAnimator> onCreateAnimators() {
        ArrayList<ValueAnimator> animators=new ArrayList<>();

        //设置球的大小
        ValueAnimator scaleAnim= ValueAnimator.ofFloat(1, 0.4f, 1);
        scaleAnim.setRepeatCount(-1);
        scaleAnim.setInterpolator(new LinearInterpolator());
        scaleAnim.setDuration(2000);
        scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //利用第一个状态去求得其他球的状态，避免用3个动画
                scaleFloats[0] = (float) animation.getAnimatedValue();
                scaleFloats[1] = (float) (- 10 / 3 * Math.pow(scaleFloats[0] - 0.7, 2) + 1);
                scaleFloats[2] = 1.4f - scaleFloats[0];
                invalidate();
            }});


        //设置球的透明度
        ValueAnimator alphaAnim= ValueAnimator.ofInt(255, 51, 255);
        alphaAnim.setRepeatCount(-1);
        alphaAnim.setDuration(2000);
        alphaAnim.setInterpolator(new LinearInterpolator());
        alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //利用第一个状态去求得其他球的状态，避免用3个动画
                alphas[0] = (int) animation.getAnimatedValue();
                alphas[1] = (int) (- Math.pow(alphas[0] - 153, 2) / 51 + 255);
                alphas[2] = 306 - alphas[0];
                invalidate();
            }
        });

        animators.add(scaleAnim);
        animators.add(alphaAnim);
        return animators;
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dw;
        int dh;


        dw = Math.max(mMinWidth, Math.min(mMaxWidth, widthMeasureSpec));
        dh = Math.max(mMinHeight, Math.min(mMaxHeight,heightMeasureSpec));


        dw += getPaddingLeft() + getPaddingRight();
        dh += getPaddingTop() + getPaddingBottom();

        final int measuredWidth = resolveSizeAndState(dw, widthMeasureSpec, 0);
        final int measuredHeight = resolveSizeAndState(dh, heightMeasureSpec, 0);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }





}
