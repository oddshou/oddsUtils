package com.example.odds.material.view;

import android.content.Context;
import android.util.AttributeSet;


import com.example.odds.material.interf.ICommHeader;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by odds, 2021/5/10 16:12
 * Desc:
 */
public class CommHeader2 extends ConstraintLayout implements ICommHeader {

    public CommHeader2(Context context) {
        super(context);
    }

    public CommHeader2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommHeader2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getCalculateHeight(boolean includeMain, boolean includeSearch) {
        //当前类型不包含search，第二个参数可以忽略
        //返回包含状态栏和内容
        return getHeight();
    }

    @Override
    public int getMainContainerTopMargin() {
        return 0;
    }

    @Override
    public void onTabChanged(String key) {
        //切换tab
    }

    @Override
    public void animToAlpha() {

    }

    @Override
    public void animToColor() {

    }

    @Override
    public boolean refreshMainData(boolean isForce) {
        return false;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean isImmerseHeader(String key) {
        return false;
    }

    @Override
    public int getCurrentHeaderType() {
        return ICommHeader.TYPE_1;
    }

    @Override
    public void refreshSearch() {
        //当前不包含，可忽略
    }

    @Override
    public boolean isSearchBottom() {
        //当前不包含，可忽略
        return false;
    }

    @Override
    public void changeHeaderMarginTop(int marginTop) {
        //当前不包含，可忽略
    }
}
