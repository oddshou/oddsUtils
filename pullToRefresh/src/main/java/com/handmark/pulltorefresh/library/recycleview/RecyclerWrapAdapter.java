package com.handmark.pulltorefresh.library.recycleview;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Created by Candice on 2016/12/19.
 */

@SuppressLint("Java  Def")
public class RecyclerWrapAdapter extends RecyclerView.Adapter implements WrapperAdapter {

    private static int BASE_ITEM_TYPE_HEADER = 10000000;
    private static int BASE_ITEM_TYPE_FOOTER = 20000000;

    private RecyclerView.Adapter mAdapter;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();


    public RecyclerWrapAdapter(RecyclerView.Adapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    /**
     * 获取头布局的数量
     */
    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFooterViews.size();
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mHeaderViews.get(viewType);
        if (view != null) {
            bugFix(view);
            return new EdgeViewHolder(view);
        }
        view = mFooterViews.get(viewType);
        if (view != null) {
            bugFix(view);
            return new EdgeViewHolder(view);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }


    private void bugFix(View view) {
        ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(view);
        }
        // 不能直接使用RecyclerView.LayoutParams 否则有各种attach/detach的bug
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params instanceof RecyclerView.LayoutParams) {
            view.setLayoutParams(new ViewGroup.MarginLayoutParams((ViewGroup.MarginLayoutParams) params));
        }
    }

    /**
     * 用于适配渲染数据到View中。方法提供给你了一个viewHolder，而不是原来的convertView。
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (isHeader(position) || isFooter(position)) {
            return;
        }
        if (mAdapter != null) {
            int adjPosition = position - getHeadersCount();
            if (adjPosition >= 0 && adjPosition < mAdapter.getItemCount()) {
                mAdapter.onBindViewHolder(holder, adjPosition);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return mHeaderViews.keyAt(position);   // 说明是Header所占用的空间
        }

        if (position >= getHeadersCount() + getRealItemCount()) { // 说明是Footer的所占用的空间
            return mFooterViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        int adjPosition = position - numHeaders;

        if (mAdapter != null) {
            int adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);
            }
        }
        return -2;

    }

    private int getRealItemCount() {
        if (mAdapter != null) {
            return mAdapter.getItemCount();
        }
        return 0;
    }

    /**
     * 将Header、Footer挂靠到RecyclerView
     */
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {   // 布局是GridLayoutManager所管理
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 如果是Header、Footer的对象则占据spanCount的位置，否则就只占用1个位置
                    return (isHeader(position) || isFooter(position)) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * 判断是否是Header的位置
     * 如果是Header的则返回true否则返回false
     */
    public boolean isHeader(int position) {
        return position >= 0 && position < mHeaderViews.size();
    }

    /**
     * 判断是否是Footer的位置
     * 如果是Footer的位置则返回true否则返回false
     */
    public boolean isFooter(int position) {
        return position < getItemCount() && position >= getItemCount() - mFooterViews.size();
    }

    @Override
    public int getItemCount() {
        if (mAdapter != null) {
            return getHeadersCount() + getFootersCount() + mAdapter.getItemCount();
        } else {
            return getHeadersCount() + getFootersCount();
        }
    }

    @Override
    public long getItemId(int position) {
        int numHeaders = getHeadersCount();
        if (mAdapter != null && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            int adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemId(adjPosition);// 不是Header和Footer则返回其itemId
            }
        }
        return -1;
    }

    @SuppressWarnings("all")
    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        // 用于StaggeredGridLayoutManager header footer 占据整行
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            int position = holder.getAdapterPosition();
            if (isHeader(position) || isFooter(position)) {
                params.setFullSpan(true);
            }
        }
    }

    @Override
    public RecyclerView.Adapter getWrappedAdapter() {
        return mAdapter;
    }

    public static class EdgeViewHolder extends RecyclerView.ViewHolder {
        public EdgeViewHolder(View itemView) {
            super(itemView);
        }
    }

    // 添加头部
    public void addHeaderView(View view) {
        int position = mHeaderViews.indexOfValue(view);
        if (position < 0) {
            mHeaderViews.put(BASE_ITEM_TYPE_HEADER++, view);
        }
        notifyDataSetChanged();
    }

    // 添加底部
    public void addFooterView(View view) {
        int position = mFooterViews.indexOfValue(view);
        if (position < 0) {
            mFooterViews.put(BASE_ITEM_TYPE_FOOTER++, view);
        }
        notifyDataSetChanged();
    }

    // 移除头部
    public void removeHeaderView(View view) {
        int index = mHeaderViews.indexOfValue(view);
        if (index < 0)
            return;
        mHeaderViews.removeAt(index);
        notifyDataSetChanged();
    }

    // 移除底部
    public void removeFooterView(View view) {
        int index = mFooterViews.indexOfValue(view);
        if (index < 0)
            return;
        mFooterViews.removeAt(index);
        notifyDataSetChanged();
    }

    /**
     * 解决GridLayoutManager添加头部和底部不占用一行的问题
     */
    public void adjustSpanSize(RecyclerView recycler) {
        RecyclerView.LayoutManager manager = recycler.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager) manager;
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter =
                            isHeader(position) || isFooter(position);
                    return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
                }
            });
        }
    }

}
