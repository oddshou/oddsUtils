package com.handmark.pulltorefresh.library.extras;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by Carl on 2017/2/8.
 * 作用：
 */

public class LoadingCircleViewDrawable extends Drawable implements Animatable {

    private static final Interpolator MATERIAL_INTERPOLATOR = new LinearInterpolator();
    private static final long ANIMATION_DURATION = 1200;
    private static final float DEFAULT_CENTER_RADIUS = 12.5f;
    private static final float DEFAULT_STROKE_WIDTH = 0.5f;
    private static final float END_TRIM_DURATION_OFFSET = 1.0f;
    private static final float DEFAULT_SIZE = 56.0f;
    private static final float START_TRIM_DURATION_OFFSET = 0.5f;
    private static final float START_TRIM_DURATION_QUARTER_OFFSET = 0.75f;
    private static final float MAX_SWIPE_DEGREES = 360f;
    private final Context mContext;
    protected final Rect mBounds = new Rect();
    private RectF mTempBounds = new RectF();
    private final Paint mPaint = new Paint();
    private float mStrokeWidth;
    private float mCenterRadius;
    private float mStrokeInset = 1.0f;
    private float mWidth;
    private float mHeight;
    private ValueAnimator mRenderAnimator;

    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;
    private float mSwipeDegrees;
    private float mOriginEndDegrees = -90f;
    private float mOriginStartDegrees = -90f;
    private float mEndDegrees = 270f;
    private float mStartDegrees = 315f;
    private int mPaintColor;
    private boolean isPlay = false;

    public void showDefaultLoadCircle() {
        mPaint.setColor(0xffC0DDFF);
        invalidateSelf();
    }

    public void setBottomPullUpColor() {
        mPaintColor = 0xffDDE0E4;
        mPaint.setColor(mPaintColor);
        invalidateSelf();
    }

    public void computeRender(float renderProgress) {
        if (renderProgress <= START_TRIM_DURATION_OFFSET) {
            float startTrimProgress = renderProgress / START_TRIM_DURATION_OFFSET;
            mStartDegrees = mOriginStartDegrees + MAX_SWIPE_DEGREES / 2 * MATERIAL_INTERPOLATOR.getInterpolation(startTrimProgress);
            mEndDegrees = mOriginEndDegrees;
        }

        if (renderProgress > START_TRIM_DURATION_OFFSET && renderProgress <= START_TRIM_DURATION_QUARTER_OFFSET) {
            float startTrimProgress = (renderProgress - START_TRIM_DURATION_OFFSET) / (END_TRIM_DURATION_OFFSET - START_TRIM_DURATION_OFFSET);
            mStartDegrees = 90f + MAX_SWIPE_DEGREES / 2 * MATERIAL_INTERPOLATOR.getInterpolation(startTrimProgress);

            float endTrimProgress = (renderProgress - START_TRIM_DURATION_OFFSET) / (END_TRIM_DURATION_OFFSET - START_TRIM_DURATION_OFFSET);
            mEndDegrees = mOriginStartDegrees + MAX_SWIPE_DEGREES * MATERIAL_INTERPOLATOR.getInterpolation(endTrimProgress);
        }
        if (renderProgress >= START_TRIM_DURATION_QUARTER_OFFSET) {
            float startTrimProgress = (renderProgress - 0.5f) / (END_TRIM_DURATION_OFFSET - 0.5f);
//            mStartDegrees = 135f + 135f * MATERIAL_INTERPOLATOR.getInterpolation(startTrimProgress);
            mStartDegrees = 45f + 270f * MATERIAL_INTERPOLATOR.getInterpolation(startTrimProgress);
            if (renderProgress > 0.91f) {
                mStartDegrees = 270f;
            }
            float endTrimProgress = (renderProgress - 0.5f) / (END_TRIM_DURATION_OFFSET - 0.5f);
            mEndDegrees = mOriginStartDegrees + MAX_SWIPE_DEGREES * MATERIAL_INTERPOLATOR.getInterpolation(endTrimProgress);
        }


        if (Math.abs(mEndDegrees - mStartDegrees) > 0) {
            mSwipeDegrees = mEndDegrees - mStartDegrees;
        }
    }

    private void resetOriginals() {
        mOriginEndDegrees = -90f;
        mOriginStartDegrees = -90f;
        mSwipeDegrees = 0;
        mEndDegrees = 270f;
        mStartDegrees = 270f;
    }


    public LoadingCircleViewDrawable(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        if (mAnimatorUpdateListener == null) {
            mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    computeRender((float) animation.getAnimatedValue());
                    invalidateSelf();
                }
            };
        }
        setupAnimators();
        setupPaint();
        mStrokeWidth = ScreenUtil.dip2px(mContext, DEFAULT_STROKE_WIDTH);
    }


    private void setupPaint() {
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaintColor = 0xff2D8EFF;
        mPaint.setColor(mPaintColor);
    }

    private void initStrokeInset(float width, float height) {
        float minSize = Math.min(width, height);
        float strokeInset = minSize / 2.0f - mCenterRadius;
        float minStrokeInset = (float) Math.ceil(mStrokeWidth / 2.0f);
        mStrokeInset = strokeInset < minStrokeInset ? minStrokeInset : strokeInset;
    }


    @Override
    public void draw(Canvas canvas) {
        mTempBounds.set(mBounds);
        mTempBounds.inset(mStrokeInset, mStrokeInset);

        if (!getBounds().isEmpty()) {
            if (mSwipeDegrees != 0) {
                canvas.drawArc(mTempBounds, mStartDegrees, mSwipeDegrees, false, mPaint);
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        // 获取控件宽高
        mBounds.set(bounds);
    }

    @Override
    public void start() {
        if (isPlay)
            return;
        isPlay = true;
        mPaint.setColor(mPaintColor);
        resetOriginals();
        mRenderAnimator.addUpdateListener(mAnimatorUpdateListener);
        mRenderAnimator.start();

    }

    public void setPaintColor(int paintColor) {
        mPaintColor = paintColor;
        mPaint.setColor(paintColor);
    }


    @Override
    public void stop() {
        if (!isPlay)
            return;
        isPlay = false;
        mRenderAnimator.removeAllUpdateListeners();
//        mRenderAnimator.setRepeatCount(0);
//        mRenderAnimator.setDuration(0);
        mPaint.setColor(0x00000000);
        resetOriginals();
        invalidateSelf();
        mRenderAnimator.end();
        mRenderAnimator.removeAllListeners();
    }


    @Override
    public boolean isRunning() {
        return false;
    }


    private void setupAnimators() {
        mRenderAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        mRenderAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mRenderAnimator.setRepeatMode(ValueAnimator.RESTART);
        mRenderAnimator.setDuration(ANIMATION_DURATION);
        mRenderAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mPaint.setColor(0x000000);
                resetOriginals();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                resetOriginals();
            }
        });


    }

    public void cancel() {
        mPaint.setColor(0x0000);
        mRenderAnimator.end();
    }
}
