package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * @项目名称: fenqile_2.6
 * @包名: com.fenqile.ui.shopping
 * @类名: MListView
 * @创建者: Carl
 * @创建时间: 2015/5/28 17:25
 * @描述: 复写ListView，自定义头布局
 */

public class MListView extends ListView {

    private OnScrollYChanged mOnScrollYChanged;

    public void setOnScrollYChanged(OnScrollYChanged mOnScrollYChanged) {
        this.mOnScrollYChanged = mOnScrollYChanged;
    }

    public MListView(Context context) {
        this(context, null);

    }

    public MListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDivider(new BitmapDrawable());
        setDividerHeight(0);
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mOnScrollYChanged != null) {
            mOnScrollYChanged.onScrollYChange(getScrolledY());
        }

    }

    public int getScrolledY() {
        View c = getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

    public interface OnScrollYChanged {
        void onScrollYChange(int scrollY);
    }

}
