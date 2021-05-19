package com.example.odds.material.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.material.appbar.AppBarLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

/**
 * Created by odds, 2021/5/12 12:41
 * Desc:
 */
public class PtrScrollView extends NestedScrollView {
    private AppBarLayout appBarLayout;
    private float mDownX;
    private float mDownY;
    private float mLastX;
    private float mLastY;
    public enum State {
        EXPANDED,//展开
        COLLAPSED,//折叠
        INTERMEDIATE//中间状态
    }

    private State mCurrentState = State.COLLAPSED;

    public PtrScrollView(@NonNull Context context) {
        this(context, null);
    }

    public PtrScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                //将按下时的坐标存储
//                mDownX = event.getX();
//                mDownY = event.getY();
//                super.dispatchTouchEvent(event);
//                return true;
//
//            case MotionEvent.ACTION_MOVE:
//                //获取到距离差
//                float dx = event.getX() - mDownX;
//                float dy = event.getY() - mDownY;
//                //通过距离差判断方向
//                int orientation = getOrientation(dx, dy);
//                switch (orientation) {
//                    //左右滑动交给子view处理
//                    case 'l':
//                    case 'r':
//                    case 't':
//                        break;
//                    case 'b':
//                        if (appBarLayout != null && mCurrentState != State.EXPANDED) {
//                            super.dispatchTouchEvent(event);
//                            return true;
//                        }
//                    default:
//                        break;
//                }
//                break;
//        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                //将按下时的坐标存储
//                mDownX = event.getX();
//                mDownY = event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                //获取到距离差
//                float dx = event.getX() - mDownX;
//                float dy = event.getY() - mDownY;
//                //通过距离差判断方向
//                int orientation = getOrientation(dx, dy);
//                switch (orientation) {
//                    //左右滑动交给子view处理
//                    case 'l':
//                    case 'r':
//                        return false;
//                    default:
//                        break;
//                }
//                break;
//        }
        return super.onInterceptTouchEvent(event);
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

    private int getOrientation(float dx, float dy) {
        if (Math.abs(dx) > Math.abs(dy)) {
            //X轴移动
            return dx > 0 ? 'r' : 'l';//右,左
        } else {
            //Y轴移动
            return dy > 0 ? 'b' : 't';//下,上
        }
    }

    /**
     * 表示是否能向上滚动，哪怕一个像素，false表示已经滚动到底部
     */
    public boolean isOnStrictBottom() {
        return !canScrollVertically(1);
    }
}
