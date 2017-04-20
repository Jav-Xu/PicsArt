package com.xuzhihui.picsart.util;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Project Name:  NaivePhotoDemo
 * Package Name:  com.javxu.naivephotodemo
 * File Name:     FileUtil
 * Creator:       Jav-Xu
 * Create Time:   2017/3/22 16:18
 * Description:   Uri 和 File 转换处理类
 */

public class FileUtil {

    /**
     * 原始File -> 原始Uri，供拍照时使用 cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
     *
     * @param context 上下文
     * @param file 原始文件 /storage/sdcard0/Pictures/Origin_1491973368473.jpg
     * @return 原始Uri /storage/sdcard0/Pictures/Origin_1491973368473.jpg
     */
    public static Uri getOriginalUriFromFile(Context context, File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "com.javxu.naivephotodemo.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * 原始File -> 封装Uri，供裁剪时使用 cropIntent.setDataAndType(uri, "image/*");
     *
     * @param context   上下文
     * @param imageFile 原始文件 /storage/sdcard0/Pictures/Origin_1491973368473.jpg
     * @return 封装Uri  /external/images/media/24
     */
    public static Uri getContentUriFromFile(Context context, File imageFile) {

        String filePath = imageFile.getAbsolutePath();

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 封装Uri -> 原始Path 从选择返回的图片 uri（可能是SDK19前返回的原始uri，也可能是SDK19后从文件等地方返回的封装uri）提取出原始path
     *
     * @param context  上下文
     * @param imageUri 选取返回的图片封装uri /external/images/media/24
     * @return 图片原始路径 /storage/sdcard0/Pictures/Origin_1491973368473.jpg
     */
    public static String getPathFromContentUri(Context context, Uri imageUri) {

        String imagePath = null;

        if (Build.VERSION.SDK_INT >= 19) {

            if (DocumentsContract.isDocumentUri(context, imageUri)) {
                // 如果是document类型的Uri，则通过document id处理
                String docId = DocumentsContract.getDocumentId(imageUri);
                if (imageUri.getAuthority().equals("com.android.providers.media.documents")) {
                    String id = docId.split(":")[1];// 解析出数字格式的id
                    String selection = MediaStore.Images.Media._ID + " = " + id;
                    imagePath = getImagePathNormally(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if (imageUri.getAuthority().equals("com.android.providers.downloads.documents")) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePathNormally(context, contentUri, null);
                }
            } else if ("cotent".equalsIgnoreCase(imageUri.getScheme())) {
                imagePath = getImagePathNormally(context, imageUri, null);
            } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
                imagePath = imageUri.getPath();
            }
        } else {
            imagePath = getImagePathNormally(context, imageUri, null);
        }
        return imagePath;
    }

    /**
     * 封装Uri -> 原始Path
     *
     * @param context 上下文
     * @param uri     选取返回的，封装图片uri /external/images/media/24
     * @return 图片原始路径 /storage/sdcard0/Pictures/Origin_1491973368473.jpg
     */
    public static String getImagePathNormally(Context context, Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取图片真实路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}