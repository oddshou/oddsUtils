
package com.example.odds.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WaveView extends View {
    private int mViewWidth;
    private int mViewHeight;

    /**
     * 当前水位线
     */
    private float mCurrentLine;
    /**
     * 目标水位线，动画目标
     */
    private float mDestLine;
    /**
     * 目标剩余比例(0-1)
     */
    private float mdestRate;

    /**
     * 波浪起伏幅度
     */
    private float mWaveHeight = 40;
    /**
     * 波长
     */
    private float mWaveWidth = 200;
    /**
     * 被隐藏的最左边的波形
     */
    private float mLeftSide;
    /**
     * 水平移动距离 从0到波长
     */
    private float mMoveLen;
    /**
     * 水波平移速度
     */
    public static final float SPEED_MOVE = 2f;

    public static final float WAVE2WIDTH = 2f;
    /**
     * 水波上升速度
     */
    public static final float SPEED_UP = 1.2f;

    public static int RUN_TIME = 0;

    private List<Point> mPointsList;
    private Paint mPaint;
    // private Paint mTextPaint;
    private Path mWavePath;

    private Timer timer;
    private MyTimerTask mTask;
    Handler updateHandler = new Handler(){

        @Override
        public void handleMessage(Message msg){
            // 记录平移总位移
            mMoveLen += SPEED_MOVE;
            if (mCurrentLine > mDestLine) {
                // 水位上升
                mCurrentLine -= SPEED_UP;
                mCurrentLine = Math.max(mCurrentLine, mDestLine);
            } else if (mCurrentLine < mDestLine) {
                // 水位下降
                mCurrentLine += SPEED_UP;
                mCurrentLine = Math.min(mCurrentLine, mDestLine);
            }
            mLeftSide += SPEED_MOVE;
            // 波形平移
            for (int i = 0; i < mPointsList.size(); i++){
                mPointsList.get(i).x = mPointsList.get(i).x + SPEED_MOVE;
                switch (i % 4){
                    case 0:
                    case 2:
                        mPointsList.get(i).y = mCurrentLine;
                        break;
                    case 1:
                        mPointsList.get(i).y = mCurrentLine + mWaveHeight;
                        break;
                    case 3:
                        mPointsList.get(i).y = mCurrentLine - mWaveHeight;
                        break;
                }
            }
            if (mMoveLen >= mWaveWidth){
                // 波形平移超过一个完整波形后复位
                mMoveLen = 0;
                resetPoints();
            }
            invalidate();
        }

    };

    public WaveView(Context context){
        super(context);
        init();
    }

    public WaveView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init();
    }

    private PorterDuffXfermode mProClean = new PorterDuffXfermode(Mode.CLEAR);
    private Drawable mDrawableBG;
    private Paint mPaintBitmap;
    private Bitmap mWavePathBitmap;

    private void init(){
        try {
            if (android.os.Build.VERSION.SDK_INT >= 11){
                setLayerType(LAYER_TYPE_HARDWARE, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mPointsList = new ArrayList<Point>();
        timer = new Timer();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Style.FILL);
        mPaint.setColor(/* Color.BLUE */Color.RED);
        mPaint.setTextSize(40);

        mPaintBitmap = new Paint();
        mPaintBitmap.setAntiAlias(true);
        mPaintBitmap.setStyle(Style.FILL);
        mPaintBitmap.setColor(Color.BLUE);
        mDrawableBG = getBackground();
        // setBackground(null);

        mWavePath = new Path();
    }

    /**
     * 设置剩余比例(0-1)
     * 
     * @param destRate
     */
    public void setDestRate(float destRate) {
        if (destRate >= 0 && destRate <= 1) {
            mdestRate = destRate;
            if (mViewHeight != 0) {
                mDestLine = (1 - mdestRate) * mViewHeight;
            }
        }
    }

    public void setWaterColor(int color) {
        mPaint.setColor(color);
    }

    /**
     * 所有点的x坐标都还原到初始状态，也就是一个周期前的状态
     */
    private void resetPoints(){
        mLeftSide = -mWaveWidth;
        for (int i = 0; i < mPointsList.size(); i++){
            mPointsList.get(i).x = i * mWaveWidth / 4 - mWaveWidth;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus){
        super.onWindowFocusChanged(hasWindowFocus);
        // 开始波动
        start();
    }

    private void start(){
        if (mTask != null){
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // TODO Auto-generated method stub
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mViewHeight = getMeasuredHeight();
            mViewWidth = getMeasuredWidth();
            // 水位线从最底下开始上升
            mCurrentLine = mViewHeight;
//            mDestLine = mViewHeight;
             mDestLine = (1 - mdestRate) * mViewHeight;
             if(mMaskBitmap == null){
                 // 画圆以长宽中的较小者为半径，矩形中心为圆心
                 int minRadius = Math.min(mViewHeight, mViewWidth) >> 1;
                 mMaskBitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Config.ARGB_8888);
                 mWavePathBitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Config.ARGB_8888);
                 Canvas cc = new Canvas(mMaskBitmap);
                 cc.drawCircle(minRadius, minRadius, minRadius, mPaint);
             }
            // mDestLine = mViewHeight;
            // 根据View宽度计算波形峰值
            mWaveHeight = mViewWidth / 8;
            // 波长等于四倍View宽度也就是View中只能看到四分之一个波形，这样可以使起伏更明显
            mWaveWidth = mViewWidth * WAVE2WIDTH/* * 4 */;
            // 左边隐藏的距离预留一个波形
            mLeftSide = -mWaveWidth;
            // 这里计算在可见的View宽度中能容纳几个波形，注意n上取整
            int n = (int) Math.round(mViewWidth / mWaveWidth);
            // n个波形需要4n+1个点，但是我们要预留一个波形在左边隐藏区域，所以需要4n+5个点
            for (int i = 0; i < (4 * n + 5); i++){
                // 从P0开始初始化到P4n+4，总共4n+5个点
                float x = i * mWaveWidth / 4 - mWaveWidth;
                float y = 0;
                switch (i % 4){
                    case 0:
                    case 2:
                        // 零点位于水位线上
                        y = mCurrentLine;
                        break;
                    case 1:
                        // 往下波动的控制点
                        y = mCurrentLine + mWaveHeight;
                        break;
                    case 3:
                    default:
                        // 往上波动的控制点
                        y = mCurrentLine - mWaveHeight;
                        break;
                }
                mPointsList.add(new Point(x, y));
            }
        }
    }
    
    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        if(mMaskBitmap != null){
            mMaskBitmap.recycle();
            mMaskBitmap = null;
        }
        if(mWavePathBitmap != null){
            mWavePathBitmap.recycle();
            mWavePathBitmap = null;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // mDrawableBG = getBackground();
        if (mDrawableBG != null) {
            int height = mDrawableBG.getIntrinsicHeight();
            int width = mDrawableBG.getIntrinsicWidth();
            // Get the max possible width given our constraints
            int widthSize = resolveAdjustedSize(width /* + pleft + pright */, Integer.MAX_VALUE,
                    widthMeasureSpec);

            // Get the max possible height given our constraints
            int heightSize = resolveAdjustedSize(height /* + ptop + pbottom */, Integer.MAX_VALUE,
                    heightMeasureSpec);
            setMeasuredDimension(widthSize, heightSize);
        }

    }

    private int resolveAdjustedSize(int desiredSize, int maxSize,
            int measureSpec) {
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                /*
                 * Parent says we can be as big as we want. Just don't be larger
                 * than max size imposed on ourselves.
                 */
                result = Math.min(desiredSize, maxSize);
                break;
            case MeasureSpec.AT_MOST:
                // Parent says we can be as big as we want, up to specSize.
                // Don't be larger than specSize, and don't be larger than
                // the max size imposed on ourselves.
                result = Math.min(Math.min(desiredSize, specSize), maxSize);
                break;
            case MeasureSpec.EXACTLY:
                // No choice. Do what we are told.
                result = specSize;
                break;
        }
        return result;
    }

    public void setDuff(PorterDuffXfermode mode) {
        mProDuff = mode;
    }

    private PorterDuffXfermode mProDuff = new PorterDuffXfermode(Mode.DST_IN);
    private Bitmap mMaskBitmap;

    @Override
    protected void onDraw(Canvas canvas)
    {
//        // 画圆以长宽中的较小者为半径，矩形中心为圆心
//        int minRadius = Math.min(mViewHeight, mViewWidth) >> 1;
//        mMaskBitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Config.ARGB_8888);
//        Canvas cc = new Canvas(mMaskBitmap);
//        cc.drawCircle(minRadius, minRadius, minRadius, mPaint);

        Bitmap bitmap = CreateWavePathBitMap();
        canvas.drawBitmap(bitmap, 0, 0, mPaintBitmap);
        mPaintBitmap.setXfermode(mProDuff);
        canvas.drawBitmap(mMaskBitmap, 0, 0, mPaintBitmap);
        mPaintBitmap.setXfermode(null);

    }

    private Bitmap CreateWavePathBitMap() {
        mWavePath.reset();
        int i = 0;
        mWavePath.moveTo(mPointsList.get(0).x, mPointsList.get(0).y);
        for (; i < mPointsList.size() - 2; i = i + 2)
        {
            mWavePath.quadTo(mPointsList.get(i + 1).x,
                    mPointsList.get(i + 1).y, mPointsList.get(i + 2)
                    .x, mPointsList.get(i + 2).y);
        }
        mWavePath.lineTo(mPointsList.get(i).x, mViewHeight);
        mWavePath.lineTo(mLeftSide, mViewHeight);

        // mWavePath.moveTo(50, 50);
        // mWavePath.lineTo(50, 200);
        // mWavePath.lineTo(200, 400);

        mWavePath.close();

//        Bitmap bitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Config.ARGB_8888);
        
        // Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
        // R.drawable.ic_launcher);
        Canvas canvas = new Canvas(mWavePathBitmap);
        canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR); 
        // canvas.drawText("Hello world !", 200, 200, mPaint);
        canvas.drawPath(mWavePath, mPaint);
        // canvas.drawRect(50, 50, 350, 350, mPaint);
//        try {
//            saveToFile(bitmap);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        return mWavePathBitmap;

    }

    private void saveToFile(Bitmap bmp) throws IOException {
        if (RUN_TIME > 0) {
            return;
        }
        RUN_TIME++;

        File file = new File("/storage/emulated/0/Pictures/bg.png");
        // File file = new File( dir , "bg" );
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            if (fos != null) {
                // 第一参数是图片格式，第二个是图片质量，第三个是输出流
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                // 用完关闭
                fos.flush();
                fos.close();
                Log.i(VIEW_LOG_TAG, "saveToFile ");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class MyTimerTask extends TimerTask
    {
        Handler handler;

        public MyTimerTask(Handler handler)
        {
            this.handler = handler;
        }

        @Override
        public void run()
        {
            handler.sendMessage(handler.obtainMessage());
        }

    }

    Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    class Point
    {
        public float x;
        public float y;

        public Point(float x, float y) {
            super();
            this.x = x;
            this.y = y;
        }
    }
}
