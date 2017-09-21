package com.xuzhihui.picsart.application;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.xuzhihui.picsart.cache.MyDoubleCache;

/**
 * Project Name:  PicsArt
 * Package Name:  com.xuzhihui.picsart.application
 * File Name:     MyApplication
 * Creator:       Jav-Xu
 * Create Time:   2017/4/20 15:21
 * Description:   TODO
 */

public class MyApplication extends Application {

    public static Context mContext;
    public static ImageLoader mLoader;
    public static RequestQueue mQueue;
    public static MyDoubleCache mDoubleCache;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initVolley();
    }

    public static Context getContext() {
        return mContext;
    }

    private void initVolley() {
        mDoubleCache = new MyDoubleCache();
        mQueue = Volley.newRequestQueue(mContext);
        mLoader = new ImageLoader(mQueue, mDoubleCache);
    }
}
