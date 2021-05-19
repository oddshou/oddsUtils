package com.example.odds.material.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.example.odds.R;
import com.handmark.pulltorefresh.library.extras.DefaultLoadingCircleViewDrawable;
import com.handmark.pulltorefresh.library.extras.LoadingCircleViewDrawable;

import in.srain.cube.views.wt.PtrFrameLayout;
import in.srain.cube.views.wt.PtrUIHandler;
import in.srain.cube.views.wt.indicator.PtrIndicator;

/**
 * @Description: 默认刷新头部
 * @Author: canzhang
 * @CreateDate: 2019/2/26 10:30
 */
public class FqlDefaultRefreshHeader extends RelativeLayout implements PtrUIHandler {


    private DefaultLoadingCircleViewDrawable pullToRefreshDrawable;
    private LoadingCircleViewDrawable refreshingDrawable;
    private ImageView ivHeaderRefresh;
    private boolean isRefreshing = false;


    public FqlDefaultRefreshHeader(Context context) {
        super(context, null);
    }

    public FqlDefaultRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.fql_default_refresh_header, this);
        ivHeaderRefresh = findViewById(R.id.iv_header_refresh);
        pullToRefreshDrawable = new DefaultLoadingCircleViewDrawable(getContext());
        refreshingDrawable = new LoadingCircleViewDrawable(getContext());
        isRefreshing = false;
    }


    @Override
    public void onUIReset(PtrFrameLayout frame) {
        if (refreshingDrawable != null) {
            refreshingDrawable.stop();
        }
        isRefreshing = false;
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        isRefreshing = true;
        if (refreshingDrawable != null) {
            ivHeaderRefresh.setImageDrawable(refreshingDrawable);
            refreshingDrawable.start();
        }
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        isRefreshing = false;
        if (refreshingDrawable != null) {
            refreshingDrawable.stop();
        }
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        float currentPercent = ptrIndicator.getCurrentPercent();
        if (pullToRefreshDrawable != null) {
            pullToRefreshDrawable.computeRender(currentPercent - 0.5f < 0 ? 0 : (currentPercent - 0.5f) * 2);
            if (!isRefreshing) {
                ivHeaderRefresh.setImageDrawable(pullToRefreshDrawable);
            }
        }
    }

    @Override
    public void onUIRefreshGoDown(PtrFrameLayout frame, boolean begin, boolean finish) {
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

}
