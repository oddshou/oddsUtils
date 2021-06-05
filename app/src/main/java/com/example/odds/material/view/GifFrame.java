package com.example.odds.material.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.SparseIntArray;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;


/**
 * Created by liveeili on 2020/4/26 > 17:23
 */
public class GifFrame {

    private static final String TAG = "GifFrame";

    private final Context mContext;
    private SparseIntArray mResMap;
    private Bitmap mInBitmap;
    private int mFrameDuration;
    private int mCurrentIndex = -1;
    private volatile boolean isRunning;
    private Handler mHandler;
    private Listener mListener;
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning && mListener != null) {
                long start = SystemClock.uptimeMillis();
                int num = frameNum();
                if (num > 0) {
                    mCurrentIndex++;
                    if (mCurrentIndex < 0) {
                        mCurrentIndex = 0;
                    }
                    final int index = mCurrentIndex % num;
                    mCurrentIndex = index;
                    getGlideBitmap(index, new OnBitmapLoad() {
                        @Override
                        public void onBitmapReady(Bitmap bitmap) {
                            if (index == mCurrentIndex && mListener != null) {
                                mListener.onChange(bitmap);
                            }
                        }
                    });
                    if (index == num - 1) {
                        mListener.onOneLoopEnd();
                    }
                    if (num > 1 && mHandler != null) {
                        long duration = SystemClock.uptimeMillis() - start;
                        duration = mFrameDuration - duration;
                        mHandler.postDelayed(mRunnable, duration > 0 ? duration : 0);
                    }
                }
            }
        }
    };


    public GifFrame(Context context) {
        mContext = context;
    }

    public void addRes(int index, @DrawableRes int resId) {
        if (mResMap == null) {
            mResMap = new SparseIntArray();
        }
        mResMap.put(index, resId);
    }

    public void setFrameDuration(int frameDuration) {
        mFrameDuration = frameDuration;
    }

    public int getFrameDuration() {
        return mFrameDuration;
    }

    public int getOneLoopDuration() {
        return mFrameDuration * frameNum();
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Listener getListener() {
        return mListener;
    }


    public int frameNum() {
        return mResMap == null ? 0 : mResMap.size();
    }

    public int getCurrentFrameIndex() {
        return mCurrentIndex;
    }

    public Bitmap getBitmap(int index, boolean reuse) {
        if (index >= 0 && mResMap != null) {
            int resId = mResMap.get(index, 0);
            if (resId == 0) {
                return null;
            }
            BitmapFactory.Options options = null;
            if (reuse && mInBitmap != null && !mInBitmap.isRecycled()) {
                options = new BitmapFactory.Options();
                options.inBitmap = mInBitmap;
            }
            try {
                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resId, options);
                if (reuse && mInBitmap == null && bitmap != null) {
                    mInBitmap = bitmap;
                }
                return bitmap;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void getGlideBitmap(int index, final OnBitmapLoad onBitmapLoad) {
        if (onBitmapLoad == null) {
            return;
        }
        if (index >= 0 && mResMap != null) {
            int resId = mResMap.get(index, 0);
            if (resId == 0) {
                return;
            }
//            if (ImageUtil.isActivityDestroyed(mContext)) {
//                return;
//            }
            Glide.with(mContext).asBitmap().load(resId)
                    .apply(new RequestOptions()
                            .useAnimationPool(true)
                            .priority(Priority.IMMEDIATE)
                            .diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            if (resource != null && !resource.isRecycled()) {
                                onBitmapLoad.onBitmapReady(resource);
                            } else {
                                onBitmapLoad.onLoadFailed();
                            }
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            onBitmapLoad.onLoadFailed();
                        }
                    });
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void startPlay() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        } else {
            mHandler.removeCallbacks(mRunnable);
        }
        mCurrentIndex = -1;
        mHandler.post(mRunnable);
    }

    public void stopPlay() {
        if (isRunning) {
            isRunning = false;
            if (mHandler != null) {
                mHandler.removeCallbacks(mRunnable);
            }
        }
    }

    public interface Listener {
        void onChange(Bitmap bitmap);

        void onOneLoopEnd();
    }

    public static abstract class OnBitmapLoad {
        public abstract void onBitmapReady(Bitmap bitmap);

        public void onLoadFailed() {
        }
    }

}
