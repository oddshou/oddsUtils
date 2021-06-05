package com.handmark.pulltorefresh.library.recycleview;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.R;

import java.util.ArrayList;

/**
 * Created by Candice on 2016/6/23.
 */
public class PullToRefreshRecycleView extends PullToRefreshBase<WrapRecyclerView> {

    private ArrayList<View> mHeaderViews = new ArrayList<>();

    private ArrayList<View> mFootViews = new ArrayList<>();

    private RecyclerView.Adapter mAdapter;

    public PullToRefreshRecycleView(Context context) {
        super(context);
    }

    public PullToRefreshRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshRecycleView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshRecycleView(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    private RecyclerView recyclerView;
    private boolean isScrollOnHeader = true;
    private boolean isScrollOnFooter = false;

    @Override
    protected WrapRecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        recyclerView = new WrapRecyclerView(context, attrs);
        recyclerView.setId(R.id.straggereddGridLayout);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int[] lastVisibleItem;
            int[] fistVisibleItem;

            int mLlFirstVisibleItem = 0;
            int mLlLastVisibleItem = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    if (null != fistVisibleItem) {
                        isScrollOnHeader = 0 == fistVisibleItem[0];
                    } else {
                        isScrollOnHeader = true;
                    }
                    if (null != lastVisibleItem) {
                        boolean isLast = recyclerView.getLayoutManager().getItemCount() - 1 == lastVisibleItem[0]
                                || recyclerView.getLayoutManager().getItemCount() == lastVisibleItem[1]
                                || recyclerView.getLayoutManager().getItemCount() - 1 == lastVisibleItem[1]
                                || recyclerView.getLayoutManager().getItemCount() == lastVisibleItem[0];
                        isScrollOnFooter = newState == RecyclerView.SCROLL_STATE_IDLE && isLast;
                    } else {
                        isScrollOnFooter = true;
                    }
                }

                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager
                        || recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    isScrollOnHeader = (mLlFirstVisibleItem == 0);
                    boolean isLast = recyclerView.getLayoutManager().getItemCount() - 1 == mLlLastVisibleItem
                            || recyclerView.getLayoutManager().getItemCount() == mLlLastVisibleItem
                            || recyclerView.getLayoutManager().getItemCount() - 1 == mLlLastVisibleItem
                            || recyclerView.getLayoutManager().getItemCount() == mLlLastVisibleItem;
                    isScrollOnFooter = newState == RecyclerView.SCROLL_STATE_IDLE && isLast;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                    fistVisibleItem = manager.findFirstCompletelyVisibleItemPositions(new int[manager.getSpanCount()]);
                    lastVisibleItem = manager.findLastCompletelyVisibleItemPositions(new int[manager.getSpanCount()]);
                }

                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    mLlFirstVisibleItem = manager.findFirstVisibleItemPosition();
                    mLlLastVisibleItem = manager.findLastVisibleItemPosition();
                }

                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
                    mLlFirstVisibleItem = manager.findFirstVisibleItemPosition();
                    mLlLastVisibleItem = manager.findLastVisibleItemPosition();
                }
            }
        });
        return (WrapRecyclerView) recyclerView;
    }

    @Override
    protected boolean isReadyForPullStart() {
        return isScrollOnHeader;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        return isScrollOnFooter;
    }
}
