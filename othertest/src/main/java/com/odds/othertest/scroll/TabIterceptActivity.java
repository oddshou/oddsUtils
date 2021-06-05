package com.odds.othertest.scroll;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.odds.othertest.R;
import com.odds.othertest.fragment.FragmentScrollView;

import java.util.ArrayList;

//import android.support.v13.app.FragmentPagerAdapter;

public class TabIterceptActivity extends FragmentActivity {

    private FragmentScrollView mFragmentScrollView1;
    private FragmentScrollView mFragmentScrollView2;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_itercept);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        initViewPager();
    }

    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();

    private void initViewPager() {
        if (mFragmentScrollView1 == null) {
            mFragmentScrollView1 = new FragmentScrollView();
        }
        if (mFragmentScrollView2 == null) {
            mFragmentScrollView2 = new FragmentScrollView();
            mFragmentScrollView2.setColor(Color.YELLOW);
        }
        mFragmentList.clear();
        mFragmentList.add(mFragmentScrollView1);
        mFragmentList.add(mFragmentScrollView2);
        mViewPager.setAdapter(new TabFragmentAdapter(getSupportFragmentManager(), mFragmentList));
    }

    /**
     * v4 包 的 FragmentPagerAdapter 不支持 android.app.FragmentManager，<p>
     * 需要使用FragmentActivity，并且使用getSupportFragmentManager()方法获取FragmentManager<p>
     * 但是 v13包支持
     *
     * @author odds
     */
    public class TabFragmentAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> mList;

        public TabFragmentAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            // TODO Auto-generated constructor stub
            mList = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "page0";
                case 1:
                    return "page1";
            }
            return null;
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            if (mList.size() > 0) {
                return mList.get(arg0);
            } else {
                return null;
            }
        }

    }
}
