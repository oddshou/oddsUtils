package com.example.odds.material.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;


import com.example.odds.R;

import androidx.annotation.RequiresApi;
import in.srain.cube.views.wt.PtrFrameLayout;

@SuppressLint("AppCompatCustomView")
public class HeaderPtrIv extends ImageView {

    private static final String TAG = "GifIvLoading";

    private boolean isLoading = false;
    private int mPullFrameNum = 0;
    private int mPullFrameIndex = -1;
    private int mLoadingFrameNum = 0;
    private float mMaxAlpha = 1f;
    private byte mStatus = PtrFrameLayout.PTR_STATUS_INIT;
    private GifFrame mPullGifFrame;
    private GifFrame mLoadingGifFrame;
    private GifFrame.Listener mListener = new GifFrame.Listener() {
        @Override
        public void onChange(Bitmap bitmap) {
            if (isLoading) {
                setImageBitmap(bitmap);
            }
        }

        @Override
        public void onOneLoopEnd() {
            // 确保刷新完收上去的时候保持在最后一帧图像
            if (mStatus == PtrFrameLayout.PTR_STATUS_COMPLETE) {
                mLoadingGifFrame.stopPlay();
            }
        }
    };


    public HeaderPtrIv(Context context) {
        super(context);
        init(context);
    }

    public HeaderPtrIv(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeaderPtrIv(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderPtrIv(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs, defStyle, defStyleRes);
        init(context);
    }


    private void init(Context context) {
        preparePull(context);
        prepareLoading(context);
    }

    public void setMaxAlpha(float maxAlpha) {
        if (maxAlpha >= 0 && maxAlpha <= 1) {
            mMaxAlpha = maxAlpha;
        }
    }

    private void preparePull(Context context) {
        mPullGifFrame = new GifFrame(context);

        mPullGifFrame.addRes(0, R.drawable.ic_pull_00);
        mPullGifFrame.addRes(1, R.drawable.ic_pull_01);
        mPullGifFrame.addRes(2, R.drawable.ic_pull_02);
        mPullGifFrame.addRes(3, R.drawable.ic_pull_03);
        mPullGifFrame.addRes(4, R.drawable.ic_pull_04);
        mPullGifFrame.addRes(5, R.drawable.ic_pull_05);
        mPullGifFrame.addRes(6, R.drawable.ic_pull_06);
        mPullGifFrame.addRes(7, R.drawable.ic_pull_07);
        mPullGifFrame.addRes(8, R.drawable.ic_pull_08);
        mPullGifFrame.addRes(9, R.drawable.ic_pull_09);
        mPullGifFrame.addRes(10, R.drawable.ic_pull_10);
        mPullGifFrame.addRes(11, R.drawable.ic_pull_11);
        mPullGifFrame.addRes(12, R.drawable.ic_pull_12);
        mPullGifFrame.addRes(13, R.drawable.ic_pull_13);
        mPullGifFrame.addRes(14, R.drawable.ic_pull_14);
        mPullGifFrame.addRes(15, R.drawable.ic_pull_15);
        mPullGifFrame.addRes(16, R.drawable.ic_pull_16);
        mPullGifFrame.addRes(17, R.drawable.ic_pull_17);
        mPullGifFrame.addRes(18, R.drawable.ic_pull_18);
        mPullGifFrame.addRes(19, R.drawable.ic_pull_19);
        mPullGifFrame.addRes(20, R.drawable.ic_pull_20);
        mPullGifFrame.addRes(21, R.drawable.ic_pull_21);
        mPullGifFrame.addRes(22, R.drawable.ic_pull_22);
        mPullGifFrame.addRes(23, R.drawable.ic_pull_23);
        mPullGifFrame.addRes(24, R.drawable.ic_pull_24);
        mPullGifFrame.addRes(25, R.drawable.ic_pull_25);
        mPullGifFrame.addRes(26, R.drawable.ic_pull_26);
        mPullGifFrame.addRes(27, R.drawable.ic_pull_27);
        mPullGifFrame.addRes(28, R.drawable.ic_pull_28);
        mPullGifFrame.addRes(29, R.drawable.ic_pull_29);
        mPullGifFrame.addRes(30, R.drawable.ic_pull_30);

        mPullFrameNum = mPullGifFrame.frameNum();
    }

    private void prepareLoading(Context context) {
        mLoadingGifFrame = new GifFrame(context);

        mLoadingGifFrame.addRes(0, R.drawable.ic_loading_00);
        mLoadingGifFrame.addRes(1, R.drawable.ic_loading_01);
        mLoadingGifFrame.addRes(2, R.drawable.ic_loading_02);
        mLoadingGifFrame.addRes(3, R.drawable.ic_loading_03);
        mLoadingGifFrame.addRes(4, R.drawable.ic_loading_04);
        mLoadingGifFrame.addRes(5, R.drawable.ic_loading_05);
        mLoadingGifFrame.addRes(6, R.drawable.ic_loading_06);
        mLoadingGifFrame.addRes(7, R.drawable.ic_loading_07);
        mLoadingGifFrame.addRes(8, R.drawable.ic_loading_08);
        mLoadingGifFrame.addRes(9, R.drawable.ic_loading_09);
        mLoadingGifFrame.addRes(10, R.drawable.ic_loading_10);
        mLoadingGifFrame.addRes(11, R.drawable.ic_loading_11);
        mLoadingGifFrame.addRes(12, R.drawable.ic_loading_12);
        mLoadingGifFrame.addRes(13, R.drawable.ic_loading_13);
        mLoadingGifFrame.addRes(14, R.drawable.ic_loading_14);
        mLoadingGifFrame.addRes(15, R.drawable.ic_loading_15);
        mLoadingGifFrame.addRes(16, R.drawable.ic_loading_16);
        mLoadingGifFrame.addRes(17, R.drawable.ic_loading_17);
        mLoadingGifFrame.addRes(18, R.drawable.ic_loading_18);
        mLoadingGifFrame.addRes(19, R.drawable.ic_loading_19);
        mLoadingGifFrame.addRes(20, R.drawable.ic_loading_20);
        mLoadingGifFrame.addRes(21, R.drawable.ic_loading_21);
        mLoadingGifFrame.addRes(22, R.drawable.ic_loading_22);
        mLoadingGifFrame.addRes(23, R.drawable.ic_loading_23);
        mLoadingGifFrame.addRes(24, R.drawable.ic_loading_24);
        mLoadingGifFrame.addRes(25, R.drawable.ic_loading_25);
        mLoadingGifFrame.addRes(26, R.drawable.ic_loading_26);
        mLoadingGifFrame.addRes(27, R.drawable.ic_loading_27);
        mLoadingGifFrame.addRes(28, R.drawable.ic_loading_28);
        mLoadingGifFrame.addRes(29, R.drawable.ic_loading_29);
        mLoadingGifFrame.addRes(30, R.drawable.ic_loading_30);
        mLoadingGifFrame.addRes(31, R.drawable.ic_loading_31);
        mLoadingGifFrame.addRes(32, R.drawable.ic_loading_32);
        mLoadingGifFrame.addRes(33, R.drawable.ic_loading_33);

        mLoadingGifFrame.setFrameDuration(33);
        mLoadingGifFrame.setListener(mListener);

        mLoadingFrameNum = mLoadingGifFrame.frameNum();
    }


    public void onUIPositionChange(byte status, float percent, int y) {
        mStatus = status;
        if (percent <= 0) {
            reset();
            return;
        }
        if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);
        }
        if (percent <= 1) {
            // 变更图片
            if (!isLoading) {
                syncPullImage(percent);
            } else if (status == PtrFrameLayout.PTR_STATUS_COMPLETE) {
                // 确保刷新完收上去的时候保持在最后一帧图像
                stopLoadingImage();
            }
            // 变更透明度
            syncAlpha(percent);
        }
    }

    private void syncPullImage(float percent) {
        if (mPullFrameNum > 0) {
            int index = Math.round(percent * mPullFrameNum);
            if (index != mPullFrameIndex && index < mPullFrameNum) {
                Bitmap bitmap = mPullGifFrame.getBitmap(index, true);
                if (bitmap != null) {
                    setImageBitmap(bitmap);
                    mPullFrameIndex = index;
                }
            }
        }
    }


    private void stopLoadingImage() {
        if (mLoadingGifFrame.isRunning()) {
            int index = mLoadingGifFrame.getCurrentFrameIndex();
            if (index == mLoadingFrameNum - 1 || index < mLoadingFrameNum / 4) {
                mLoadingGifFrame.stopPlay();
            }
        }
    }

    public long getDelayLoadingComplete(long delta) {
        long delay = 0;
        if (mLoadingFrameNum > 1 && mLoadingGifFrame.isRunning()) {
            try {
                int index = mLoadingGifFrame.getCurrentFrameIndex();
                delay = (mLoadingFrameNum - index - 1) * mLoadingGifFrame.getFrameDuration();
                long offset = delay - delta;
                delay = offset >= mLoadingGifFrame.getFrameDuration()
                        ? offset : offset + mLoadingGifFrame.getOneLoopDuration();
            } catch (Throwable e) {
            }
        } else {
            delay = 100;
        }
        return delay > 0 ? delay : 1000;
    }


    private void syncAlpha(float percent) {
        float v = Math.min(percent * 2, 1f);
        if (v > mMaxAlpha) {
            v = mMaxAlpha;
        }
        if (getAlpha() != v) {
            setAlpha(v);
        }
    }


    public void startLoading() {
        if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);
        }
        mLoadingGifFrame.stopPlay();
        mLoadingGifFrame.startPlay();
        isLoading = true;
    }


    public void refreshComplete() {
        mStatus = PtrFrameLayout.PTR_STATUS_COMPLETE;
    }


    public void reset() {
        if (getAlpha() != 1f) {
            setAlpha(1f);
        }
        setImageDrawable(null);
        mLoadingGifFrame.stopPlay();
        isLoading = false;
    }


}
