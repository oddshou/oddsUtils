
package com.odds.othertest;

import android.util.Log;

public class Logger
{

    private static boolean isDebug = /*false*/true;
    public static boolean isTrace = true;

    public static boolean canListAnimation = true;

    private static final int ERROR = 4;
    private static final int WARNING = 3;
    private static final int INFO = 2;
    private static final int DEBUG = 1;
    private static final int VIEW = 0;
    private static int mLevel = 99;

    public static void e(String TAG, Object obj)
    {
        if (isDebug && mLevel > ERROR)
        {
            Log.e(TAG, obj.toString());
        }
    }

    public static void w(String TAG, Object obj)
    {
        if (isDebug && mLevel > WARNING)
        {
            Log.w(TAG, obj.toString());
        }
    }

    public static void i(String TAG, Object obj)
    {
        if (isDebug && mLevel > INFO)
        {
            Log.i(TAG, obj.toString());
        }
    }

    public static void d(String TAG, Object obj)
    {
        if (isDebug && mLevel > DEBUG)
        {
            Log.d(TAG, obj.toString());
        }
    }

    public static void v(String TAG, Object obj)
    {
        if (isDebug && mLevel > VIEW)
        {
            Log.v(TAG, obj.toString());
        }
    }
    
    //################ TAG + obj + name 
    public static void e(String TAG, Object obj, String name)
    {
        if (isDebug && mLevel > ERROR)
        {
            Log.e(TAG, name + " " + obj.toString());
        }
    }

    public static void w(String TAG, Object obj, String name)
    {
        if (isDebug && mLevel > WARNING)
        {
            Log.w(TAG, name + " " + obj.toString());
        }
    }

    public static void i(String TAG, Object obj, String name)
    {
        if (isDebug && mLevel > INFO)
        {
            Log.i(TAG, name + " " + obj.toString());
        }
    }

    public static void d(String TAG, Object obj, String name)
    {
        if (isDebug && mLevel > DEBUG)
        {
            Log.d(TAG, name + " " + obj.toString());
        }
    }

    public static void v(String TAG, Object obj, String name)
    {
        if (isDebug && mLevel > VIEW)
        {
            Log.v(TAG, name + " " + obj.toString());
        }
    }

}
