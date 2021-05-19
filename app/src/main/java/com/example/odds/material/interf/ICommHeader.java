package com.example.odds.material.interf;

/**
 * Created by odds, 2021/5/10 11:33
 * Desc: 首页header 公共接口
 */
public interface ICommHeader {

    int TYPE_0 = 0;
    int TYPE_1 = 1;


    int getCalculateHeight(boolean includeMain, boolean includeSearch);

    int getMainContainerTopMargin();

    void onTabChanged(String key);

    void animToAlpha();

    void animToColor();

    boolean refreshMainData(boolean isForce);

    void onResume();

    void onDestroy();

    boolean isImmerseHeader(String key);

    void refreshSearch();

    boolean isSearchBottom();

    void changeHeaderMarginTop(int marginTop);
    //旧版type = 0，新版type = 1
    int getCurrentHeaderType();
}
