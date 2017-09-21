package com.xuzhihui.picsart.util;

import android.content.Context;
import android.content.pm.PackageInfo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Project Name:  PicsArt
 * Package Name:  com.xuzhihui.picsart.util
 * File Name:     AppUtil
 * Creator:       Jav-Xu
 * Create Time:   2017/8/8 00:07
 * Description:   TODO
 */

public class AppUtil {

    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
