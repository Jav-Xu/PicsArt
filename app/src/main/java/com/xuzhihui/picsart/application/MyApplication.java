package com.xuzhihui.picsart.application;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.xuzhihui.picsart.util.BitmapCache;

/**
 * Project Name:  PicsArt
 * Package Name:  com.xuzhihui.picsart.application
 * File Name:     MyApplication
 * Creator:       Jav-Xu
 * Create Time:   2017/4/20 15:21
 * Description:   TODO
 */

public class MyApplication extends Application {

    public static ImageLoader mLoader;
    public static RequestQueue mQueue;
    public static BitmapCache mCache;

    @Override
    public void onCreate() {
        super.onCreate();
        mCache = new BitmapCache();
        mQueue = Volley.newRequestQueue(getApplicationContext());
        mLoader = new ImageLoader(mQueue, mCache);
    }
}
