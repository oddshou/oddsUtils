package com.odds.othertest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.customview.widget.ViewDragHelper;

public class DragHelperLinearLayout extends LinearLayout {

    private ViewDragHelper mDragger;

    public DragHelperLinearLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public DragHelperLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);

    }

    public DragHelperLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init(context);

    }

    private void init(Context context){
         mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            
            @Override
            public boolean tryCaptureView(View arg0, int arg1) {
                // TODO Auto-generated method stub
                return true;
            }
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx)
            {
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy)
            {
                return top;
            }
        });
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return mDragger.shouldInterceptTouchEvent(ev);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        mDragger.processTouchEvent(event);
        return true;
    }
    
}
