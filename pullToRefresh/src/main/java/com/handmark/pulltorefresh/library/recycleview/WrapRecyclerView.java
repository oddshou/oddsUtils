package com.handmark.pulltorefresh.library.recycleview;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Candice on 2016/12/19.
 */

public class WrapRecyclerView extends RecyclerView {

    private RecyclerView.Adapter mAdapter;

    private RecyclerWrapAdapter mWrapRecyclerAdapter;

    public WrapRecyclerView(Context context) {
        this(context, null);
    }

    public WrapRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 如果没有Adapter那么就不添加，也可以选择抛异常提示
     * 让他必须先设置Adapter然后才能添加，这里是仿照ListView的处理方式
     *
     * @param view
     */
    public void addHeaderView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.addHeaderView(view);
        }
    }

    /**
     * 如果没有Adapter那么就不添加，也可以选择抛异常提示
     * 让他必须先设置Adapter然后才能添加，这里是仿照ListView的处理方式
     *
     * @param view
     */
    public void addFooterView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.addFooterView(view);
        }
    }

    public void removeHeaderView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.removeHeaderView(view);
        }
    }

    // 移除底部
    public void removeFooterView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.removeFooterView(view);
        }
    }

    public int getHeaderViewsCount() {
        if (mWrapRecyclerAdapter != null) {
            return mWrapRecyclerAdapter.getHeadersCount();
        }
        return 0;

    }

    public int getFooterViewsCount() {
        if (mWrapRecyclerAdapter != null) {
            return mWrapRecyclerAdapter.getFootersCount();
        }
        return 0;
    }


    public Adapter getOriginAdapter() {
        return mAdapter;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        // 为了防止多次设置Adapter
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
            mAdapter = null;
        }

        this.mAdapter = adapter;

        if (adapter instanceof RecyclerWrapAdapter) {
            mWrapRecyclerAdapter = (RecyclerWrapAdapter) adapter;
        } else {
            mWrapRecyclerAdapter = new RecyclerWrapAdapter(adapter);
        }

        super.setAdapter(mWrapRecyclerAdapter);

        // 注册一个观察者
        mAdapter.registerAdapterDataObserver(mDataObserver);

        // 解决GridLayout添加头部和底部也要占据一行
        mWrapRecyclerAdapter.adjustSpanSize(this);
    }

    public int findFirstVisibleItemPosition() {
        RecyclerView.LayoutManager layoutManager = this.getLayoutManager();
        // GridLayoutManager 是 LinearLayoutManager 子类
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            return linearManager.findFirstVisibleItemPosition();
        }
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] into = staggeredGridLayoutManager.findFirstVisibleItemPositions(null);
            if (into != null && into.length > 0) {
                return into[0];
            } else {
                return -1;
            }
        }
        return -1;
    }

    public int findFirstCompletelyVisibleItemPosition() {
        RecyclerView.LayoutManager layoutManager = this.getLayoutManager();
        // GridLayoutManager 是 LinearLayoutManager 子类
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            return linearManager.findFirstCompletelyVisibleItemPosition();
        }
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] into = staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(null);
            if (into != null && into.length > 0) {
                return into[0];
            } else {
                return -1;
            }
        }
        return -1;
    }

    public int getLastVisiblePosition() {
        RecyclerView.LayoutManager layoutManager = this.getLayoutManager();
        // GridLayoutManager 是 LinearLayoutManager 子类
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            return linearManager.findLastVisibleItemPosition();
        }
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] into = staggeredGridLayoutManager.findLastVisibleItemPositions(null);
            if (into != null && into.length > 0) {
                return into[into.length - 1];
            } else {
                return -1;
            }
        }
        return -1;
    }

    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mAdapter != null && mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (mAdapter != null && mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemRangeRemoved(positionStart + getHeaderViewsCount(), itemCount);
            }
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (mAdapter != null && mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemMoved(fromPosition + getHeaderViewsCount(), toPosition + getHeaderViewsCount());
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (mAdapter != null && mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemRangeChanged(positionStart + getHeaderViewsCount(),
                        itemCount);
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (mAdapter != null && mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemRangeChanged(positionStart + getHeaderViewsCount(),
                        itemCount, payload);
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (mAdapter != null && mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemRangeInserted(positionStart + getHeaderViewsCount(), itemCount);
            }
        }
    };


    public int getScrollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) this.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisibleChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisibleChildView.getHeight();
        return (position) * itemHeight - firstVisibleChildView.getTop();
    }


    /**
     * 判断 CustomRecyclerView 是否处于顶部 可用于判断是否可下拉
     */
    public boolean isOnTop() {
        LayoutManager layoutManager = this.getLayoutManager();
        if (layoutManager == null) {
            return false;
        }
        if (getChildCount() == 0) {
            return true;
        }
        View topView = layoutManager.findViewByPosition(0);
        if (topView != null) {
            ViewGroup.LayoutParams params = topView.getLayoutParams();
            if (params instanceof RecyclerView.LayoutParams) {
                if (((RecyclerView.LayoutParams) params).topMargin == topView.getTop()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isLastItemVisible() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int last = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            return last != NO_POSITION && last == getAdapter().getItemCount() - 1;
        }
        return false;
    }

    /**
     * 表示是否能向下滚动，哪怕一个像素，false表示已经滚动到顶部
     */
    public boolean isOnStrictTop() {
        return !canScrollVertically(-1);
    }

    /**
     * 表示是否能向上滚动，哪怕一个像素，false表示已经滚动到底部
     */
    public boolean isOnStrictBottom() {
        return !canScrollVertically(1);
    }

    public boolean isOnStrictLeft() {
        return !canScrollHorizontally(-1);
    }

    public boolean isOnStrictRight() {
        return !canScrollHorizontally(1);
    }

}
