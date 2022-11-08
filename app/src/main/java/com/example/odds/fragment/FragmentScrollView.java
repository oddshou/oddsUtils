package com.example.odds.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.odds.Logger;
import com.example.odds.R;

public class FragmentScrollView extends Fragment {
    protected static final String TAG = "FragmentScrollView";
    private int mScrollViewBgColor;
    private Button mBtnContent;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.scroll_view, null);
        mBtnContent = (Button) rootView.findViewById(R.id.btnContent);
        mBtnContent.setOnClickListener(mViewClickListen);
        if (mScrollViewBgColor != 0) {
            mBtnContent.setBackgroundColor(mScrollViewBgColor);
        }
        return rootView;
    }
    
    public void setColor(int color){
        mScrollViewBgColor = color;
    }
    
    private OnClickListener mViewClickListen = new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Logger.i(TAG, "mViewClickListen "+ "mBtnContent", "odds");
        }
    };

}
