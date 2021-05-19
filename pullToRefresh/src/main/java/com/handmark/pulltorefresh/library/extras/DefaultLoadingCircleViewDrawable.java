package com.handmark.pulltorefresh.library.extras;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by Carl on 2017/8/21.
 * 作用：
 */

public class DefaultLoadingCircleViewDrawable extends Drawable {

    private static final Interpolator MATERIAL_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    protected final Rect mBounds = new Rect();
    private final Context mContext;
    private RectF mTempBounds = new RectF();
    private final Paint mPaint = new Paint();
    private float mStartDegrees = -90f;
    private float mSwipeDegrees = 0f;
    private int mPaintColor;

    public DefaultLoadingCircleViewDrawable(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        setupPaint();
    }

    private void setupPaint() {
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaintColor = 0xaa2D8EFF;
        mPaint.setColor(mPaintColor);
    }

    @Override
    public void draw(Canvas canvas) {
        mTempBounds.set(mBounds);
        mTempBounds.inset(1.0f, 1.0f);

        if (!getBounds().isEmpty()) {
            canvas.drawArc(mTempBounds, mStartDegrees, mSwipeDegrees, false, mPaint);
        }
    }


    long lastRenderTime = 0;
    float lastProgress = 0;

    public void computeRender(float renderProgress) {
        if(renderProgress < 0){
            renderProgress = 0;
        }else if(renderProgress > 1.0f){
            renderProgress = 1.0f;
        }
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastRenderTime < 16){
            return;
        }
        lastRenderTime = currentTime;
        mSwipeDegrees = 360.0f * MATERIAL_INTERPOLATOR.getInterpolation(renderProgress);
        invalidateSelf();
        lastProgress = renderProgress;
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
}
