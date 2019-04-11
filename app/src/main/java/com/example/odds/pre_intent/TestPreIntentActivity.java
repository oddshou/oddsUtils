/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.pre_intent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import com.example.odds.R;
import com.example.odds.annotations.InitFile;
import com.example.odds.route.RxActivity;
import com.odds.annotation.processor.PreIntent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class TestPreIntentActivity extends AppCompatActivity {
    //尽量用order 来规定参数顺序，避免因调整成员定义顺序导致调用异常
    //order 非必填
    @InitFile(order = 0)
    public String mName;
    @InitFile(order = 1)
    public boolean isGood;
    @InitFile(order = 2)
    public int num;
    @InitFile(order = 3)
    public Bundle bundle = new Bundle();
    @InitFile(Serializable = true, order = 4)
    public List<String> ages;
    @InitFile
    public ParcelableClass parcelableClass;
    @InitFile
    public SerializeClass serializeClass;

    @InitFile
    public Parcelable[] parcelableClasss;//此处类型不能使用ParcelableClass，只能使用基类引用，避免强转失败。
    @InitFile
    public SerializeClass[] serializeClasss;

    @InitFile(Parcelable = true)
    public ArrayList<ParcelableClass> ParcelableList;//此处需要类型匹配 ArrayList<? extends Parcelable> ArrayList 不能改用List
    @InitFile(Serializable = true)
    public List<SerializeClass> serializeList;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        PreIntent.onSave_TestPreIntentActivity(outState, this);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreIntent.onCreate_TestPreIntentActivity(savedInstanceState, this);
        setContentView(R.layout.activity_test_pre_intent);

        TextView view = findViewById(R.id.args);
        view.setText(String.format(new Locale("zh"),"mName: %s\n" +
                        "isGood: %s\n" +
                        "num: %d\n" +
                        "bundle: %s\n" +
                        "age: %s\n" +
                        "parcelable: %s\n" +
                        "serialize: %s\n" +
                        "parcelableList: %s\n" +
                        "serializeList: %s\n",
                mName, isGood, num, bundle.getString("bundle"), ages.toString(), parcelableClass,
                serializeClass, ParcelableList.toString(), serializeList.toString()));

    }

    public void toText(View view) {
        //进入下一个页面测试onSaveInstanceState 是否生效
        startActivity(new Intent(this, RxActivity.class));
    }

    public static class SerializeClass implements Serializable{
        public SerializeClass(String name) {
            this.name = name;
        }

        public String name;
    }

    public static class ParcelableClass implements Parcelable{
        public String things;

        public ParcelableClass(String in) {
            things = in;
        }

        public static final Creator<ParcelableClass> CREATOR = new Creator<ParcelableClass>() {
            @Override
            public ParcelableClass createFromParcel(Parcel in) {
                return new ParcelableClass(in);
            }

            @Override
            public ParcelableClass[] newArray(int size) {
                return new ParcelableClass[size];
            }
        };

        ParcelableClass(Parcel in) {
            things = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(things);
        }
    }
}
