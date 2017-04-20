package com.xuzhihui.picsart.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Project Name:  NaivePhotoDemo
 * Package Name:  com.javxu.naivephotodemo
 * File Name:     ImageUtil
 * Creator:       Jav-Xu
 * Create Time:   2017/4/15 15:44
 * Description:   图像工具类
 */

public class ImageUtil {

    /**
     * 获取缩放比例合适的Bitmap图像
     *
     * @param path      文件路径
     * @param aimWidth  目标宽度
     * @param aimHeight 目标高度
     * @return 缩放后的图像
     */
    public static Bitmap getScaledBitmap(String path, int aimWidth, int aimHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options); // 通过解码file为options赋值，C风格

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;

        if (srcHeight > aimHeight || srcWidth > aimWidth) {
            int widthSize = Math.round(srcWidth / aimWidth);
            int heightSize = Math.round(srcHeight / aimHeight);
            inSampleSize = (widthSize > heightSize) ? widthSize : heightSize; // 挑缩放比例大者
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 在目标宽高度未知的情况下,根据设备屏幕尺寸获取缩放比例合适的Bitmap图像
     *
     * @param path              文件路径
     * @param appCompatActivity 当前上下文
     * @return 缩放后的图像
     */
    public static Bitmap getScaledBitmap(String path, AppCompatActivity appCompatActivity) {
        Point point = new Point();
        appCompatActivity.getWindowManager().getDefaultDisplay().getSize(point);
        return getScaledBitmap(path, point.x, point.y);
    }

    public static void saveBitmapToPath(Bitmap bitmap, String path) {
        if (bitmap != null) {
            try {
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(path);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
