package com.handmark.pulltorefresh.library.extras;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Carl on 2017/2/9.
 * 作用：界面加载组件
 */

public class CircleProgressView extends ImageView {
    private final Context mContext;
    private LoadingCircleViewDrawable loadingCircleViewDrawable;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        if (loadingCircleViewDrawable == null) {
            loadingCircleViewDrawable = new LoadingCircleViewDrawable(mContext);
        }
        setImageDrawable(loadingCircleViewDrawable);
    }

    public void computeRender(float renderProgress) {
        loadingCircleViewDrawable.computeRender(renderProgress);
        loadingCircleViewDrawable.invalidateSelf();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (loadingCircleViewDrawable == null)
            return;

        if (changedView.getVisibility() == GONE ||
                changedView.getVisibility() == INVISIBLE ||
                getVisibility() == GONE ||
                getVisibility() == INVISIBLE) {
            loadingCircleViewDrawable.stop();
        } else if (changedView.getVisibility() == VISIBLE
                && getVisibility() == VISIBLE) {
            View view = this;
            View rootView = this.getRootView();
            while (view.getParent() instanceof ViewGroup) {
                view = (View)view.getParent();
                if(view.getVisibility() == GONE || view.getVisibility() == INVISIBLE){
                    break;
                }
                if(view == rootView){
                    loadingCircleViewDrawable.start();
                    break;
                }
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (getVisibility() == View.VISIBLE) {
            loadingCircleViewDrawable.stop();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getVisibility() == View.VISIBLE) {
            View view = this;
            View rootView = this.getRootView();
            while (view.getParent() instanceof ViewGroup) {
                view = (View)view.getParent();
                if(view.getVisibility() == GONE || view.getVisibility() == INVISIBLE){
                    break;
                }
                if(view == rootView){
                    loadingCircleViewDrawable.start();
                    break;
                }
            }
        }
    }

    public void showDefaultLoadCircle() {
        loadingCircleViewDrawable.showDefaultLoadCircle();
    }

    public void setBottomPullUpColor() {
        loadingCircleViewDrawable.setBottomPullUpColor();
    }

    public void setPaintColor(int paintColor) {
        loadingCircleViewDrawable.setPaintColor(paintColor);
    }
}
