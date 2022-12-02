/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.R;
import com.handmark.pulltorefresh.library.extras.CircleProgressView;
import com.handmark.pulltorefresh.library.extras.DefaultLoadingCircleViewDrawable;
import com.handmark.pulltorefresh.library.extras.LoadingCircleViewDrawable;

@SuppressLint("ViewConstructor")
public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {

    static final String LOG_TAG = "PullToRefresh-LoadingLayout";

    static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
    protected final CircleProgressView mCircleProgressView;
    private ImageView mHeadViewBackgroundImg;
    private final Context mContext;

    private FrameLayout mInnerLayout;

    protected final ImageView mHeaderImage;
//    protected final ThreePointProgressView mHeaderProgress;

    private boolean mUseIntrinsicAnimation;

    protected final TextView mHeaderText;
    protected final TextView mSubHeaderText;

    protected final Mode mMode;
    protected final Orientation mScrollDirection;

    private CharSequence mPullLabel;
    private CharSequence mRefreshingLabel;
    private CharSequence mReleaseLabel;
    private boolean isShowRefreshText = true;
    protected LinearLayout mPullToRefreshLl;

    private Drawable pullToRefreshDrawable;
    private Drawable refreshingDrawable;
    private boolean mFootViewLayerInVisible = false;

    public LoadingLayout(Context context, final Mode mode, final Orientation scrollDirection, TypedArray attrs) {
        super(context);
        this.mContext = context;
        mMode = mode;
        mScrollDirection = scrollDirection;

        switch (scrollDirection) {
            case HORIZONTAL:
                LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_horizontal, this);
                break;
            case VERTICAL:
            default:
                LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_vertical, this);
                break;
        }

        // 添加下拉背景


        mInnerLayout = (FrameLayout) findViewById(R.id.fl_inner);
        mHeaderText = (TextView) mInnerLayout.findViewById(R.id.pull_to_refresh_text);
//        mHeaderProgress = (ThreePointProgressView) mInnerLayout.findViewById(R.id.pull_to_refresh_progress);
        mCircleProgressView = (CircleProgressView) mInnerLayout.findViewById(R.id.mCircleProgressView);
        mSubHeaderText = (TextView) mInnerLayout.findViewById(R.id.pull_to_refresh_sub_text);
        mHeaderImage = (ImageView) mInnerLayout.findViewById(R.id.pull_to_refresh_image);
        mPullToRefreshLl = (LinearLayout) mInnerLayout.findViewById(R.id.pull_to_refresh_ll);
//        final LoadingCircleViewDrawable loadingView = new LoadingCircleViewDrawable(mContext);
//        loadingView.initPullDownValue();
//        mHeaderProgress.setImageDrawable(loadingView);
        
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInnerLayout.getLayoutParams();

        switch (mode) {
            case PULL_FROM_END:
                lp.gravity = scrollDirection == Orientation.VERTICAL ? Gravity.TOP : Gravity.LEFT;

                // Load in labels
                mPullLabel = context.getString(R.string.pull_to_refresh_from_bottom_pull_label);
                mRefreshingLabel = context.getString(R.string.pull_to_refresh_from_bottom_refreshing_label);
                mReleaseLabel = context.getString(R.string.pull_to_refresh_from_bottom_release_label);
                break;

            case PULL_FROM_START:
            default:
                lp.gravity = scrollDirection == Orientation.VERTICAL ? Gravity.BOTTOM : Gravity.RIGHT;

                // Load in labels
                mPullLabel = context.getString(R.string.pull_to_refresh_pull_label);
                mRefreshingLabel = context.getString(R.string.pull_to_refresh_refreshing_label);
                mReleaseLabel = context.getString(R.string.pull_to_refresh_release_label);
                break;
        }

        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderBackground)) {
            Drawable background = attrs.getDrawable(R.styleable.PullToRefresh_ptrHeaderBackground);
            if (null != background) {
                ViewCompat.setBackground(this, background);
            }
        }

        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance)) {
            TypedValue styleID = new TypedValue();
            attrs.getValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance, styleID);
            setTextAppearance(styleID.data);
        }
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance)) {
            TypedValue styleID = new TypedValue();
            attrs.getValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance, styleID);
            setSubTextAppearance(styleID.data);
        }

        // Text Color attrs need to be set after TextAppearance attrs
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextColor)) {
            ColorStateList colors = attrs.getColorStateList(R.styleable.PullToRefresh_ptrHeaderTextColor);
            if (null != colors) {
                setTextColor(colors);
            }
        }
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderSubTextColor)) {
            ColorStateList colors = attrs.getColorStateList(R.styleable.PullToRefresh_ptrHeaderSubTextColor);
            if (null != colors) {
                setSubTextColor(colors);
            }
        }

        // Try and get defined drawable from Attrs
        Drawable imageDrawable = null;
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawable)) {
            imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawable);
        }
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrPullToRefreshDrawable)) {
            pullToRefreshDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrPullToRefreshDrawable);
        }
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrRefreshingDrawable)) {
            refreshingDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrRefreshingDrawable);
        }
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrShowPullToRefreshText)) {
            isShowRefreshText = attrs.getBoolean(R.styleable.PullToRefresh_ptrShowPullToRefreshText, true);
        }

        if (isShowRefreshText) {
            mPullToRefreshLl.setVisibility(View.VISIBLE);
        } else {
            mPullToRefreshLl.setVisibility(View.GONE);
        }
        // Check Specific Drawable from Attrs, these overrite the generic
        // drawable attr above
        switch (mode) {
            case PULL_FROM_START:
            default:
                if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableStart)) {
                    imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableStart);
                } else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableTop)) {
                    Utils.warnDeprecation("ptrDrawableTop", "ptrDrawableStart");
                    imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableTop);
                }
                break;

            case PULL_FROM_END:
                if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableEnd)) {
                    imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableEnd);
                } else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableBottom)) {
                    Utils.warnDeprecation("ptrDrawableBottom", "ptrDrawableEnd");
                    imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableBottom);
                }
                break;
        }

        // If we don't have a user defined drawable, load the default
        if (null == imageDrawable) {
            imageDrawable = context.getResources().getDrawable(getDefaultDrawableResId());
        }

        // Set Drawable, and save width/height
        setLoadingDrawable(imageDrawable);

        mHeadViewBackgroundImg = new ImageView(mContext);
        mHeadViewBackgroundImg.setVisibility(VISIBLE);
        addView(mHeadViewBackgroundImg);
        reset();
    }

    public final void setHeight(int height) {
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
        lp.height = height;
        requestLayout();
    }

    public final void setWidth(int width) {
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
        lp.width = width;
        requestLayout();
    }

    public final int getContentSize() {
        if (mInnerLayout.getMeasuredWidth() == 0 || mInnerLayout.getMeasuredHeight() == 0) {
            mInnerLayout.measure(0, 0);
            mInnerLayout.requestLayout();
        }
        switch (mScrollDirection) {
            case HORIZONTAL:
                return mInnerLayout.getMeasuredWidth();
            case VERTICAL:
            default:
                return mInnerLayout.getMeasuredHeight();
        }
    }

    public final void hideAllViews() {
        if (View.VISIBLE == mHeaderText.getVisibility()) {
            mHeaderText.setVisibility(View.INVISIBLE);
        }
        if (View.VISIBLE == mCircleProgressView.getVisibility()) {
            mCircleProgressView.setVisibility(View.INVISIBLE);
        }
        if (View.VISIBLE == mHeaderImage.getVisibility()) {
            mHeaderImage.setVisibility(View.INVISIBLE);
        }
        if (View.VISIBLE == mSubHeaderText.getVisibility()) {
            mSubHeaderText.setVisibility(View.INVISIBLE);
        }
//        if (View.VISIBLE == mIvRefreshingBg.getVisibility()){
//            mIvRefreshingBg.setVisibility(View.INVISIBLE);
//        }
    }

    public final void onPull(float scaleOfLayout) {
        if (mHeadViewBackgroundImg.getVisibility() == INVISIBLE) {
            mHeadViewBackgroundImg.setVisibility(VISIBLE);
        }
        if (!mUseIntrinsicAnimation) {
            onPullImpl(scaleOfLayout);
        }
//        mHeadViewBackgroundImg.setVisibility(VISIBLE);
        if (mFootViewLayerInVisible && mMode ==  Mode.PULL_FROM_END) {
            mHeaderImage.setVisibility(INVISIBLE);
        }
    }

    public final void pullToRefresh() {
        if (mFootViewLayerInVisible && mMode ==  Mode.PULL_FROM_END) {
            mHeaderImage.setVisibility(INVISIBLE);
        }
        if (pullToRefreshDrawable != null) {
            mHeaderImage.setImageDrawable(pullToRefreshDrawable);
        }

        if (null != mHeaderText) {
            mHeaderText.setText(mPullLabel);
        }

        // Now call the callback
        pullToRefreshImpl();
    }

    public final void refreshing() {
        if (null != mHeaderText) {
            mHeaderText.setText(mRefreshingLabel);
        }
//        if (View.VISIBLE == mIvRefreshingBg.getVisibility()){
//            mIvRefreshingBg.setVisibility(View.GONE);
//        }
        if (mUseIntrinsicAnimation) {
            if (refreshingDrawable != null ) {
                mHeaderImage.setImageDrawable(refreshingDrawable);
            }
            if (mHeaderImage.getDrawable() != null && mHeaderImage.getDrawable() instanceof AnimationDrawable) {
                ((AnimationDrawable) mHeaderImage.getDrawable()).start();
            }
        } else {
            // Now call the callback
            refreshingImpl();
        }

        if (null != mSubHeaderText) {
            mSubHeaderText.setVisibility(View.GONE);
        }
        if (mFootViewLayerInVisible && mMode ==  Mode.PULL_FROM_END) {
            mHeaderImage.setVisibility(INVISIBLE);
        }
    }

    public final void releaseToRefresh() {
        if (null != mHeaderText) {
            mHeaderText.setText(mReleaseLabel);
        }
        // Now call the callback
        releaseToRefreshImpl();
    }

    public final void reset() {
      
        if (null != mHeaderText) {
            mHeaderText.setText(mPullLabel);
        }
        if (mFootViewLayerInVisible && mMode ==  Mode.PULL_FROM_END) {
            mHeaderImage.setVisibility(INVISIBLE);
        }else {
            mHeaderImage.setVisibility(View.VISIBLE);
        }

        if (mUseIntrinsicAnimation) {
            if (mHeaderImage.getDrawable() != null && mHeaderImage.getDrawable() instanceof AnimationDrawable) {
                ((AnimationDrawable) mHeaderImage.getDrawable()).stop();
            }
        } else {
            // Now call the callback
            resetImpl();
        }

        if (null != mSubHeaderText) {
            if (TextUtils.isEmpty(mSubHeaderText.getText())) {
                mSubHeaderText.setVisibility(View.GONE);
            } else {
                mSubHeaderText.setVisibility(View.VISIBLE);
            }
        }
       
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        setSubHeaderText(label);
    }

    @Override
    public final void setLoadingDrawable(Drawable imageDrawable) {
        // Set Drawable
        mHeaderImage.setImageDrawable(imageDrawable);
        mUseIntrinsicAnimation = (imageDrawable instanceof AnimationDrawable);

        // Now call the callback
        onLoadingDrawableSet(imageDrawable);
    }

    @Override
    public void setPullLabel(CharSequence pullLabel) {
        mPullLabel = pullLabel;
    }

    @Override
    public void setRefreshingLabel(CharSequence refreshingLabel) {
        mRefreshingLabel = refreshingLabel;
    }

    @Override
    public void setReleaseLabel(CharSequence releaseLabel) {
        mReleaseLabel = releaseLabel;
    }

    @Override
    public void setTextTypeface(Typeface tf) {
        mHeaderText.setTypeface(tf);
    }

    public final void showInvisibleViews() {
        if (View.INVISIBLE == mHeaderText.getVisibility()) {
            mHeaderText.setVisibility(View.VISIBLE);
        }
        if (View.INVISIBLE == mCircleProgressView.getVisibility()) {
            mCircleProgressView.setVisibility(View.VISIBLE);
        }
        if (View.INVISIBLE == mHeaderImage.getVisibility()) {
            mHeaderImage.setVisibility(View.VISIBLE);
        }
        if (View.INVISIBLE == mSubHeaderText.getVisibility()) {
            mSubHeaderText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Callbacks for derivative Layouts
     */

    protected abstract int getDefaultDrawableResId();

    protected abstract void onLoadingDrawableSet(Drawable imageDrawable);

    protected abstract void onPullImpl(float scaleOfLayout);

    protected abstract void pullToRefreshImpl();

    protected abstract void refreshingImpl();

    protected abstract void releaseToRefreshImpl();

    protected abstract void resetImpl();

    private void setSubHeaderText(CharSequence label) {
        if (null != mSubHeaderText) {
            if (TextUtils.isEmpty(label)) {
                mSubHeaderText.setVisibility(View.GONE);
            } else {
                mSubHeaderText.setText(label);

                // Only set it to Visible if we're GONE, otherwise VISIBLE will
                // be set soon
                if (View.GONE == mSubHeaderText.getVisibility()) {
                    mSubHeaderText.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setSubTextAppearance(int value) {
        if (null != mSubHeaderText) {
            mSubHeaderText.setTextAppearance(getContext(), value);
        }
    }

    private void setSubTextColor(ColorStateList color) {
        if (null != mSubHeaderText) {
            mSubHeaderText.setTextColor(color);
        }
    }

    private void setTextAppearance(int value) {
        if (null != mHeaderText) {
            mHeaderText.setTextAppearance(getContext(), value);
        }
        if (null != mSubHeaderText) {
            mSubHeaderText.setTextAppearance(getContext(), value);
        }
    }

    private void setTextColor(ColorStateList color) {
        if (null != mHeaderText) {
            mHeaderText.setTextColor(color);
        }
        if (null != mSubHeaderText) {
            mSubHeaderText.setTextColor(color);
        }
    }

    /**
     * 获取背景大图所在控件
     * @return
     */
    public ImageView getHeadLoadingViewBackgroundView() {
        return mHeadViewBackgroundImg;
    }

    /**
     * 隐藏背景大图 
     */
    public void setHeadViewBackgroundImgInvisible() {
        mHeadViewBackgroundImg.setVisibility(INVISIBLE);
    }

    public void setLoadingImgGone(){
        mInnerLayout.setVisibility(GONE);
    }


    /**
     * 设置下拉控件底部不可见方法
     */
    public void setFootViewLayerInVisible(){
        mFootViewLayerInVisible = true;
    }

}
