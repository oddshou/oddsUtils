package com.handmark.pulltorefresh.library.recycleview;

import android.content.Context;
import android.graphics.PointF;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by liveeili on 2017/9/13.
 */

public class WrapLinearLayoutManager extends LinearLayoutManager {

    private int mSmoothSnapType = LinearSmoothScroller.SNAP_TO_START;
    private float mSmoothMillsPerInch = 20f;
    private int mSmoothScrollTopMargin = 0;

    public WrapLinearLayoutManager(Context context) {
        super(context);
    }

    public WrapLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public WrapLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("WrapLinearLayoutManager", "IndexOutOfBoundsException in RecyclerView");
        }
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        MyLinearSmoothScroller myLinearSmoothScroller = new MyLinearSmoothScroller(recyclerView.getContext());
        myLinearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(myLinearSmoothScroller);
    }

    /**
     * 设置 smoothScrollToPosition 是滚动到顶部还是底部或者其它，默认顶部
     * <p>{@link LinearSmoothScroller#SNAP_TO_START}
     * 滚动到顶部
     * <p>{@link LinearSmoothScroller#SNAP_TO_ANY}
     * 当滚动位置在可见范围之内时 滚动距离为0，故不会滚动，
     * 当滚动位置在可见范围之前时 内容向上滚动且只能滚动到顶部，
     * 当滚动位置在可见范围距离之外时 内容向下滚动，且只能滚动到底部。
     * <p>{@link LinearSmoothScroller#SNAP_TO_END}
     * 滚动到底部
     */
    public void setSmoothSnapType(@SnapType int type) {
        mSmoothSnapType = type;
    }

    /**
     * 设置 smoothScroll 移动每英寸需要花费多少毫秒，默认25毫秒
     */
    public void setSmoothMillsPerInch(int mills) {
        if (mills > 0) {
            mSmoothMillsPerInch = (float) mills;
        }
    }

    public void setSmoothScrollTopMargin(int topMargin) {
        mSmoothScrollTopMargin = topMargin;
    }

    private class MyLinearSmoothScroller extends LinearSmoothScroller {

        MyLinearSmoothScroller(Context context) {
            super(context);
        }

        @Nullable
        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            return WrapLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
        }

        /**
         * millisecondsPerInch 默认为25，及移动每英寸需要花费25ms，
         * 如果你要速度变快一点，就直接设置设置小一点，注意这里的单位是float
         */
        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return mSmoothMillsPerInch / displayMetrics.densityDpi;
        }

        /**
         * 以下参数以LinearSmoothScroller解释
         *
         * @param viewStart      RecyclerView的top位置
         * @param viewEnd        RecyclerView的bottom位置
         * @param boxStart       Item的top位置
         * @param boxEnd         Item的bottom位置
         * @param snapPreference 判断滑动方向的标识（The edge which the view should snap to when entering the visible
         *                       area. One of {@link #SNAP_TO_START}, {@link #SNAP_TO_END} or
         *                       {@link #SNAP_TO_END}.）
         * @return 移动偏移量
         */
        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {

            if (mSmoothScrollTopMargin != 0 &&
                    mSmoothSnapType == LinearSmoothScroller.SNAP_TO_START) {
                return boxStart - viewStart + mSmoothScrollTopMargin;
            }
            return super.calculateDtToFit(viewStart, viewEnd, boxStart, boxEnd, snapPreference);
        }

        @Override
        protected int getVerticalSnapPreference() {
            if (getOrientation() == VERTICAL) {
                return mSmoothSnapType;
            }
            return super.getVerticalSnapPreference();
        }

        @Override
        protected int getHorizontalSnapPreference() {
            if (getOrientation() == HORIZONTAL) {
                return mSmoothSnapType;
            }
            return super.getHorizontalSnapPreference();
        }
    }


    @IntDef({LinearSmoothScroller.SNAP_TO_START,
            LinearSmoothScroller.SNAP_TO_END,
            LinearSmoothScroller.SNAP_TO_ANY})
    @Retention(RetentionPolicy.SOURCE)
    private @interface SnapType {
    }

}

