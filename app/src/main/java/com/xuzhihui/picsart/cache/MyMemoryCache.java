package com.xuzhihui.picsart.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Project Name:  PicsArt
 * Package Name:  com.xuzhihui.picsart.util
 * File Name:     MyMemoryCache
 * Creator:       Jav-Xu
 * Create Time:   2017/9/21 14:37
 * Description:   内存缓存
 */

public class MyMemoryCache implements ImageLoader.ImageCache {

    private LruCache<String, Bitmap> mMemoryCache;

    public MyMemoryCache() {
        initMemoryCache();
    }

    private void initMemoryCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 4;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mMemoryCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
    }
}
