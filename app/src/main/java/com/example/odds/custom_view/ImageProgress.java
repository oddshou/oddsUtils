/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.Utils;

import androidx.appcompat.widget.AppCompatImageView;

public class ImageProgress extends AppCompatImageView {

    private Paint mPaint;
    private Xfermode mXfermode;
    private int mBorderRadius;
    private int mCurrentProgress = 2;
    private RectF mRectF;
    private Drawable drawable;

    public ImageProgress(Context context) {
        this(context, null);
    }

    public ImageProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        mBorderRadius = ConvertUtils.dp2px(20);
        mRectF = new RectF();
        drawable = getDrawable();
    }

    /**
     *  最大值为100，根据当前传入值计算百分比
     */
    public void setProgress(int progress) {
        mCurrentProgress = progress < 0 ? 0 : (progress > 100 ? 100 : progress);
    }

    public int getProgress(){
        return mCurrentProgress;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (drawable == null){
            return;
        }
        int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        int w = (int) (getWidth()* 0.01 * mCurrentProgress);
        //画源图像，为一个圆角矩形
        mRectF.set(0,0,w,getHeight());
        canvas.drawRoundRect(mRectF, mBorderRadius, mBorderRadius, mPaint);
        //设置混合模式
        mPaint.setXfermode(mXfermode);
        //画目标图像
        canvas.drawBitmap(drawableToBitamp(drawable), 0, 0, mPaint);
        // 还原混合模式
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);


    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (drawable instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable).start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (drawable instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable).stop();
        }
    }

    /**
     * 图片拉升
     *
     * @param drawable
     * @return
     */
    private Drawable exChangeSize(Drawable drawable){
        float scale = 1.0f;
        scale = Math.max(getWidth() * 1.0f / drawable.getIntrinsicWidth(), getHeight()
                * 1.0f / drawable.getIntrinsicHeight());
        drawable.setBounds(0, 0, (int) (scale * drawable.getIntrinsicWidth()),
                (int) (scale * drawable.getIntrinsicHeight()));
        return drawable;
    }


    private Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        // 当设置不为图片，为颜色时，获取的drawable宽高会有问题，所有当为颜色时候获取控件的宽高
//        int w = drawable.getIntrinsicWidth() <= 0 ? getWidth() : drawable.getIntrinsicWidth();
//        int h = drawable.getIntrinsicHeight() <= 0 ? getHeight() : drawable.getIntrinsicHeight();

        int w = (int) (getWidth()* 0.01 * mCurrentProgress);
        int h = drawable.getIntrinsicHeight();

        //这里没有使用图片尺寸，全部采用控件尺寸，以使适配控件图片填充
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;

    }
}
