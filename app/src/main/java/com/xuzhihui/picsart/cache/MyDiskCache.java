package com.xuzhihui.picsart.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.volley.toolbox.ImageLoader;
import com.xuzhihui.picsart.application.MyApplication;
import com.xuzhihui.picsart.util.AppUtil;
import com.xuzhihui.picsart.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Project Name:  PicsArt
 * Package Name:  com.xuzhihui.picsart.util
 * File Name:     MyDiskCache
 * Creator:       Jav-Xu
 * Create Time:   2017/9/21 14:37
 * Description:   硬盘缓存
 */

public class MyDiskCache implements ImageLoader.ImageCache {

    private Context mContext = MyApplication.getContext();
    private DiskLruCache mDiskLruCache;

    public MyDiskCache() {
        initDiskCache();
    }

    private void initDiskCache() {
        File cacheFile;
        try {
            cacheFile = FileUtil.getDiskCacheDir(mContext, "bitmap");
            if (!cacheFile.exists()) {
                cacheFile.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheFile, AppUtil.getAppVersion(mContext), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        String key = AppUtil.hashKeyForDisk(url);
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {
                InputStream inputStream = snapshot.getInputStream(0);
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        String key = AppUtil.hashKeyForDisk(url);
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                    editor.commit();
                } else {
                    editor.abort();
                }
            }
            mDiskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean removeBitmap(String url) throws IOException {
        String key = AppUtil.hashKeyForDisk(url);
        return mDiskLruCache.remove(key);
    }
}
