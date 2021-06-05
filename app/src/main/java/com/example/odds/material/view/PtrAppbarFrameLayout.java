package com.example.odds.material.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import in.srain.cube.views.wt.PtrFrameLayout;

/**
 * Created by odds, 2021/5/11 19:25
 * Desc: 嵌套Coordinator 的下拉刷新
 */
public class PtrAppbarFrameLayout extends PtrFrameLayout {

    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private boolean isDealTopBorder;

    private float mDownX;
    private float mDownY;

    public enum State {
        EXPANDED,//展开
        COLLAPSED,//折叠
        INTERMEDIATE//中间状态
    }

    private State mCurrentState = State.COLLAPSED;

    public PtrAppbarFrameLayout(Context context) {
        this(context, null);
    }

    public PtrAppbarFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrAppbarFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
//        coordinatorLayout = findViewById(R.id.coordinator);
//        appBarLayout = findViewById(R.id.appBarLayout);
//
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
//                if (offset == 0) {
//                    mCurrentState = State.EXPANDED;
//                } else if (Math.abs(offset) >= appBarLayout.getTotalScrollRange()) {
//                    mCurrentState = State.COLLAPSED;
//                } else {
//                    mCurrentState = State.INTERMEDIATE;
//                }
//            }
//        });

    }

    public void bindAppBarLayout(@NonNull AppBarLayout appBarLayout) {
        this.appBarLayout = appBarLayout;
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
                if (offset == 0) {
                    mCurrentState = State.EXPANDED;
                } else if (Math.abs(offset) >= appBarLayout.getTotalScrollRange()) {
                    mCurrentState = State.COLLAPSED;
                } else {
                    mCurrentState = State.INTERMEDIATE;
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //将按下时的坐标存储
                mDownX = event.getX();
                mDownY = event.getY();
                isDealTopBorder = true;
            case MotionEvent.ACTION_MOVE:
                //获取到距离差
                float dx = event.getX() - mDownX;
                float dy = event.getY() - mDownY;
                //通过距离差判断方向
                int orientation = getOrientation(dx, dy);
                switch (orientation) {
                    //左右滑动交给子view处理
                    case 'l':
                    case 'r':
                    case 't':
                        break;
                    case 'b':
                        if (isDealTopBorder) {
                            if (appBarLayout != null && mCurrentState == State.EXPANDED || appBarLayout.getVisibility() == View.GONE) {
                                isDealTopBorder = false;
                                setEnabled(true);
                                MotionEvent cancel = MotionEvent.obtain(event);
                                cancel.setAction(MotionEvent.ACTION_CANCEL);
                                super.dispatchTouchEvent(cancel);

                                MotionEvent down = MotionEvent.obtain(event);
                                down.setAction(MotionEvent.ACTION_DOWN);
                                return super.dispatchTouchEvent(down);
                            }else {
                                setEnabled(false);
                            }
                        }

                    default:
                        break;
                }
                break;
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                if (appBarLayout != null && mCurrentState == State.EXPANDED) {
//                    MotionEvent cancel = MotionEvent.obtain(event);
//                    cancel.setAction(MotionEvent.ACTION_CANCEL);
//                    super.dispatchTouchEvent(cancel);
//                    setEnabled(true);
//                }else {
//                    setEnabled(false);
//                }
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        return super.onInterceptTouchEvent(event);
    }

    private int getOrientation(float dx, float dy) {
        if (Math.abs(dx) > Math.abs(dy)) {
            //X轴移动
            return dx > 0 ? 'r' : 'l';//右,左
        } else {
            //Y轴移动
            return dy > 0 ? 'b' : 't';//下,上
        }
    }
}
