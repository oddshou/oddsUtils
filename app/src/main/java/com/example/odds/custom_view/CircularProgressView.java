package com.example.odds.custom_view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.odds.R;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * Created by odds, 2021/1/11 17:49
 * Desc:
 * 参考 https://www.jianshu.com/p/93d9a2a65bde
 */

public class CircularProgressView extends androidx.appcompat.widget.AppCompatImageView {
    private Paint mBackPaint, mProgressPaint;   // 绘制画笔
    private final RectF mRectF = new RectF();       // 绘制区域
    private int[] mColorArray;  // 圆环渐变色
    private int mProgress;      // 圆环进度(0-100)
    private int mMeasuredWidth;
    private int mBgColor;
    private int mProgressColor;
    private float mBgWidth;
    private float mProgressWidth;
    private ValueAnimator mAnimator;
    private float mProgressPadding;

    public CircularProgressView(Context context) {
        this(context, null);
    }

    public CircularProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressView);
        mBgColor = typedArray.getColor(R.styleable.CircularProgressView_backColor, 0);
        mBgWidth = typedArray.getDimension(R.styleable.CircularProgressView_backWidth, 5);
        mProgressPadding = typedArray.getDimension(R.styleable.CircularProgressView_progPadding, 0);
        // 初始化背景圆环画笔
        mBackPaint = new Paint();
        mBackPaint.setStyle(Paint.Style.STROKE);    // 只描边，不填充
        mBackPaint.setStrokeCap(Paint.Cap.ROUND);   // 设置圆角
        mBackPaint.setAntiAlias(true);              // 设置抗锯齿
        mBackPaint.setDither(true);                 // 设置抖动
        mBackPaint.setStrokeWidth(mBgWidth);
        mBackPaint.setColor(mBgColor);

        // 初始化进度圆环画笔
        mProgressWidth = typedArray.getDimension(R.styleable.CircularProgressView_progWidth, 10);
        mProgressPaint = new Paint();
        mProgressPaint.setStyle(Paint.Style.STROKE);    // 只描边，不填充
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);   // 设置圆角
        mProgressPaint.setAntiAlias(true);              // 设置抗锯齿
        mProgressPaint.setDither(true);                 // 设置抖动
        mProgressPaint.setStrokeWidth(mProgressWidth);
        mProgressColor = typedArray.getColor(R.styleable.CircularProgressView_progColor, Color.RED);


        // 初始化进度圆环渐变色
        int startColor = typedArray.getColor(R.styleable.CircularProgressView_progStartColor, 0);
        int firstColor = typedArray.getColor(R.styleable.CircularProgressView_progFirstColor, 0);
        if (startColor != 0 && firstColor != 0) {
            mColorArray = new int[]{startColor, firstColor};
        } else {
            mProgressPaint.setColor(mProgressColor);
        }

        // 初始化进度
        mProgress = typedArray.getInteger(R.styleable.CircularProgressView_progress, 0);
        typedArray.recycle();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        float viewWide = mMeasuredWidth - mProgressPadding * 2;
        float viewHigh = getMeasuredHeight() - mProgressPadding * 2;
        float mRectLength = (int) ((Math.min(viewWide, viewHigh)) - (Math.max(mBackPaint.getStrokeWidth(), mProgressPaint.getStrokeWidth())));
        float mRectL = mProgressPadding + (viewWide - mRectLength) / 2;
        float mRectT = mProgressPadding + (viewHigh - mRectLength) / 2;
        mRectF.left = mRectL;
        mRectF.top = mRectT;
        mRectF.right = mRectL + mRectLength;
        mRectF.bottom = mRectT + mRectLength;

        // 设置进度圆环渐变色
        if (mColorArray != null && mColorArray.length > 1 && measuredWidth != mMeasuredWidth) {
            mProgressPaint.setShader(new LinearGradient(0, 0, 0, measuredWidth, mColorArray, null, Shader.TileMode.MIRROR));
        }
        mMeasuredWidth = measuredWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBgColor != 0) {
            canvas.drawArc(mRectF, 0, 360, false, mBackPaint);
        }
        canvas.drawArc(mRectF, 270, 360 * mProgress / 1000, false, mProgressPaint);
    }

    /**
     * 获取当前进度
     *
     * @return 当前进度（0-100）
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * 设置当前进度
     *
     * @param progress 当前进度（0-100）
     */
    public void setProgress(int progress) {
        this.mProgress = progress;
        invalidate();
    }

    /**
     * 设置当前进度，并展示进度动画。如果动画时间小于等于0，则不展示动画
     *
     * @param progress 当前进度（0-100）
     * @param animTime 动画时间（毫秒）
     */
    public void setProgress(int progress, long animTime, Animator.AnimatorListener listener) {
        if (animTime <= 0) {
            setProgress(progress);
        } else {
            mAnimator = ValueAnimator.ofInt(mProgress, progress);
            mAnimator.addUpdateListener(animation -> {
                mProgress = (int) animation.getAnimatedValue();
                invalidate();
            });
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addListener(listener);
            mAnimator.setDuration(animTime);
            mAnimator.start();
        }
    }

    public void pause(){
        if (mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }

    public boolean isRunning(){
        return mAnimator != null && mAnimator.isRunning();
    }

    /**
     * 设置背景圆环宽度
     *
     * @param width 背景圆环宽度
     */
    public void setBackWidth(int width) {
        mBackPaint.setStrokeWidth(width);
        invalidate();
    }

    /**
     * 设置背景圆环颜色
     *
     * @param color 背景圆环颜色
     */
    public void setBackColor(@ColorRes int color) {
        mBackPaint.setColor(ContextCompat.getColor(getContext(), color));
        invalidate();
    }

    /**
     * 设置进度圆环宽度
     *
     * @param width 进度圆环宽度
     */
    public void setProgressWidth(int width) {
        mProgressPaint.setStrokeWidth(width);
        invalidate();
    }

    /**
     * 设置进度圆环颜色
     *
     * @param color 景圆环颜色
     */
    public void setProgressColor(@ColorRes int color) {
        mProgressPaint.setColor(ContextCompat.getColor(getContext(), color));
        mProgressPaint.setShader(null);
        invalidate();
    }

    /**
     * 设置进度圆环颜色(支持渐变色)
     *
     * @param startColor 进度圆环开始颜色
     * @param firstColor 进度圆环结束颜色
     */
    public void setProgressColor(@ColorRes int startColor, @ColorRes int firstColor) {
        mColorArray = new int[]{ContextCompat.getColor(getContext(), startColor), ContextCompat.getColor(getContext(), firstColor)};
        mProgressPaint.setShader(new LinearGradient(0, 0, 0, getMeasuredWidth(), mColorArray, null, Shader.TileMode.MIRROR));
        invalidate();
    }

    /**
     * 设置进度圆环颜色(支持渐变色)
     *
     * @param colorArray 渐变色集合
     */
    public void setProgressColor(@ColorRes int[] colorArray) {
        if (colorArray == null || colorArray.length < 2) return;
        mColorArray = new int[colorArray.length];
        for (int index = 0; index < colorArray.length; index++)
            mColorArray[index] = ContextCompat.getColor(getContext(), colorArray[index]);
        mProgressPaint.setShader(new LinearGradient(0, 0, 0, getMeasuredWidth(), mColorArray, null, Shader.TileMode.MIRROR));
        invalidate();
    }
}
