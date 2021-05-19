package com.example.odds.material;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.odds.R;
import com.example.odds.material.view.FqlDefaultRefreshHeader;
import com.example.odds.material.view.PtrAppbarFrameLayout;
import com.example.odds.material.view.PtrScrollView;
import com.example.odds.tools.TintStateBarUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import in.srain.cube.views.wt.PtrFrameLayout;
import in.srain.cube.views.wt.PtrHandler;

public class AppbarActivity extends AppCompatActivity {

    private static final String TAG = "AppbarActivity";
    PtrAppbarFrameLayout mPtrLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appbar);
        TintStateBarUtil.setStatusBarTintCompat(this, false);

        ViewStub stub = findViewById(R.id.stubHead2);
        View headerView = stub.inflate();

//        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator);


        init();
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mPtrLayout.getLayoutParams();
        layoutParams.topToBottom = headerView.getId();

        appbarListener();
    }

    private void init() {
        mPtrLayout = findViewById(R.id.mPtrRootFrame);



        FqlDefaultRefreshHeader mPhvConsumeHeader = findViewById(R.id.mPtrFloorHeader);
        ViewTreeObserver vto = mPhvConsumeHeader.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mPtrLayout.setOffsetToRefresh(mPhvConsumeHeader.getMeasuredHeight());
                return true;
            }
        });


        mPtrLayout.setResistance(1.5f); //设置下拉的阻尼系数，值越大感觉越难下拉
        mPtrLayout.setDurationToClose(300); //设置下拉回弹的时间
        mPtrLayout.setDurationToCloseHeader(300); //设置刷新完成，头部回弹时间，注意和前一个进行区别
        mPtrLayout.setPullToRefresh(false); //设置下拉过程中执行刷新，我们一般设置为false
        mPtrLayout.setKeepHeaderWhenRefresh(true); //设置刷新的时候是否保持头部
        mPtrLayout.disableWhenHorizontalMove(true);
        mPtrLayout.addPtrUIHandler(mPhvConsumeHeader);
        mPtrLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                if (!RefreshUtils.isOnTop(mRv)) {
//                    return false;
//                } else {
//                    return true;
//                }
                return true;
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
//                mNewAdapter.setEnableLoadMore(false);
//                getCouponList(false, 0);
            }
        });
    }

    private void appbarListener() {
        TextView textExpand = findViewById(R.id.textExpand);
//        int textExpandHeight = textExpand.getHeight();
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);


        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        mPtrLayout.bindAppBarLayout(appBarLayout);

        PtrScrollView ptrScrollView = findViewById(R.id.ptrScrollView);
        ptrScrollView.bindAppBarLayout(appBarLayout);

//        int totalScrollRange = appBarLayout.getTotalScrollRange();
        mPtrLayout.setEnabled(false);
//        appBarLayout.getTotalScrollRange();
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
//                int textExpandHeight = 0;
//                int totalScrollRange = 0;
//                if (offset == 0) {
//                    //完全展开
//                    textExpandHeight = textExpand.getHeight();
//                    totalScrollRange = appBarLayout.getTotalScrollRange();
////                    mPtrLayout.setEnabled(true);
//                }
//                    Log.d(TAG, "onOffsetChanged: " + textExpandHeight + " ; " + offset + " ; " + totalScrollRange);
//
//
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPtrLayout.refreshComplete();
    }
}