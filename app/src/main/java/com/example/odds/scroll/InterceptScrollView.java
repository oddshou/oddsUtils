package com.example.odds.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.example.odds.Logger;

public class InterceptScrollView extends ScrollView {

    private static final String TAG = "InterceptScrollView";

    public InterceptScrollView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public InterceptScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public InterceptScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        
        
        
        
        boolean onInterceptTouchEvent = super.onInterceptTouchEvent(ev);
        
        Logger.i(TAG, "onInterceptTouchEvent " + onInterceptTouchEvent + " " + ev.getAction(), "odds");
        return onInterceptTouchEvent;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        boolean onTouchEvent = super.onTouchEvent(ev);
        
        Logger.i(TAG, "onTouchEvent " + onTouchEvent +" " + ev.getAction() , "odds");
        return onTouchEvent;
        
    }

}
