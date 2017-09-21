package com.xuzhihui.picsart.cache;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader;

/**
 * Project Name:  PicsArt
 * Package Name:  com.xuzhihui.picsart.util
 * File Name:     MyDoubleCache
 * Creator:       Jav-Xu
 * Create Time:   2017/9/21 14:43
 * Description:   内存 + 硬盘 = 二级缓存
 */

public class MyDoubleCache implements ImageLoader.ImageCache {

    private MyMemoryCache mMemoryCache;
    private MyDiskCache mDiskCache;

    public MyDoubleCache() {
        initCache();
    }

    private void initCache() {
        mMemoryCache = new MyMemoryCache();
        mDiskCache = new MyDiskCache();
    }

    @Override
    public Bitmap getBitmap(String url) {
        Bitmap bitmap;
        if ((bitmap = mMemoryCache.getBitmap(url)) == null) {
            bitmap = mDiskCache.getBitmap(url);
        }
        return bitmap;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mMemoryCache.putBitmap(url, bitmap);
        mDiskCache.putBitmap(url, bitmap);
    }
}
