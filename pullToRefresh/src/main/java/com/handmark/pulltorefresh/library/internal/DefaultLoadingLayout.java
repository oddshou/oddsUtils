package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.R;
import com.handmark.pulltorefresh.library.extras.DefaultLoadingCircleViewDrawable;
import com.handmark.pulltorefresh.library.extras.LoadingCircleViewDrawable;

/**
 * Created by garyth on 2016/10/11.
 *
 * @项目名称: fenqile_app_git
 * @包名: com.handmark.pulltorefresh.library.internal
 * @类名: DefaultLoadingLayout
 * @创建者: garyth
 * @创建时间: 2016/10/11 16:48
 */

public class DefaultLoadingLayout extends LoadingLayout {

    private boolean isRefreshing = false;
    private DefaultLoadingCircleViewDrawable pullToRefreshDrawable;
    private LoadingCircleViewDrawable refreshingDrawable;

    public DefaultLoadingLayout(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        pullToRefreshDrawable = new DefaultLoadingCircleViewDrawable(context);
        refreshingDrawable = new LoadingCircleViewDrawable(context);
        mPullToRefreshLl.setVisibility(View.GONE);
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.default_ptr_flip;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {

    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {
        if(pullToRefreshDrawable != null) {
            pullToRefreshDrawable.computeRender(scaleOfLayout - 0.5f < 0 ? 0 : (scaleOfLayout -0.5f)  *2);
        }
    }

    @Override
    protected void pullToRefreshImpl() {
        mHeaderImage.setImageDrawable(pullToRefreshDrawable);
    }

    @Override
    protected void refreshingImpl() {
        mHeaderImage.setImageDrawable(refreshingDrawable);
        refreshingDrawable.start();
        isRefreshing = true;
    }

    @Override
    protected void releaseToRefreshImpl() {
        if(isRefreshing) {
            refreshingDrawable.stop();
            isRefreshing = false;
        }
    }

    @Override
    protected void resetImpl() {
        if(isRefreshing) {
            refreshingDrawable.stop();
            isRefreshing = false;
        }
    }
}
