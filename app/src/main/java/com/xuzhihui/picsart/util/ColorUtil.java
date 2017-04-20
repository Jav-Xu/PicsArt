package com.xuzhihui.picsart.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by xuzhh on 2016/6/5.
 */
public class ColorUtil {

    public static Bitmap setHSB(Bitmap bmp, float mHue, float mSaturation, float mBrightness) {
        // 参数bitmap是final 不可修改 所以新建一个bitmap
        Bitmap bitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        ColorMatrix hueMatrix = new ColorMatrix(); // 色调设置
        hueMatrix.setRotate(0, mHue);// R
        hueMatrix.setRotate(1, mHue);// G
        hueMatrix.setRotate(2, mHue);// B

        ColorMatrix saturationMatrix = new ColorMatrix(); // 饱和度设置
        saturationMatrix.setSaturation(mSaturation);

        ColorMatrix brightnessMatrix = new ColorMatrix(); // 亮度设置
        brightnessMatrix.setScale(mBrightness, mBrightness, mBrightness, 1);

        ColorMatrix colorMatrix = new ColorMatrix(); //对三个属性进行融合处理
        colorMatrix.postConcat(hueMatrix);
        colorMatrix.postConcat(saturationMatrix);
        colorMatrix.postConcat(brightnessMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix)); // 画笔设置
        canvas.drawBitmap(bmp, 0, 0, paint); // 在画布上进行绘制
        return bitmap;
    }

    public static Bitmap enhance(Bitmap bmp){
        Bitmap bitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        ColorMatrix colorMatrix = new ColorMatrix();
        float brightness = -25; //亮度
        float contrast = 2;     //对比度
        colorMatrix.set(new float[]{
                contrast, 0, 0, 0, brightness,
                0, contrast, 0, 0, brightness,
                0, 0, contrast, 0, brightness,
                0, 0, 0, contrast, 0 });
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bmp, 0, 0, paint);
        return bitmap;
    }

    public static Bitmap sharpenImage(Bitmap bmp) {

        long start = System.currentTimeMillis();
        // 拉普拉斯矩阵
        int[] laplacian = new int[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 };

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int idx = 0;
        float alpha = 0.3F;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++)
        {
            for (int k = 1, len = width - 1; k < len; k++)
            {
                idx = 0;
                for (int m = -1; m <= 1; m++)
                {
                    for (int n = -1; n <= 1; n++)
                    {
                        pixColor = pixels[(i + n) * width + k + m];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);

                        newR = newR + (int) (pixR * laplacian[idx] * alpha);
                        newG = newG + (int) (pixG * laplacian[idx] * alpha);
                        newB = newB + (int) (pixB * laplacian[idx] * alpha);
                        idx++;
                    }
                }

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        long end = System.currentTimeMillis();
        Log.d("may", "used time="+(end - start));
        return bitmap;
    }

    public static Bitmap reliefImage(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        int color,colorLast;

        int[] oldPx = new int[width * width];
        int[] newPx = new int[width * width];

        int r,g,b,a;
        int R,G,B,A;

        bmp.getPixels(oldPx,0,width,0,0,width,height);

        for (int i = 1; i < width * height; i++){

            color = oldPx[i];
            R = Color.red(color);
            G = Color.green(color);
            B = Color.blue(color);
            A = Color.alpha(color);

            colorLast = oldPx[i - 1];
            r = Color.red(colorLast);
            g = Color.green(colorLast);
            b = Color.blue(colorLast);
            a = Color.alpha(colorLast);

            r = Math.min(255, Math.max(0, r - R + 127));
            g = Math.min(255, Math.max(0, g - G + 127));
            b = Math.min(255, Math.max(0, b - B + 127));

            newPx[i] = Color.argb(a, r, g, b);
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(newPx,0,width,0,0,width,height);
        return bitmap;
    }

    public static Bitmap oldImage(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        int color;
        int[] oldPx = new int[width * width];
        int[] newPx = new int[width * width];
        int r,g,b,a;
        bmp.getPixels(oldPx,0,width,0,0,width,height);
        for (int i = 0; i < width * height; i++){

            color = oldPx[i];
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);

            r = Math.min(255, Math.max(0, (int) (0.393 * r + 0.769 * g + 0.189 * b)));
            g = Math.min(255, Math.max(0, (int) (0.349 * r + 0.686 * g + 0.168 * b)));
            b = Math.min(255, Math.max(0, (int) (0.272 * r + 0.534 * g + 0.131 * b)));

            newPx[i] = Color.argb(a, r, g, b);
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(newPx,0,width,0,0,width,height);
        return bitmap;
    }

    public static Bitmap negativeImage(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        int color;
        int[] oldPx = new int[width * width];
        int[] newPx = new int[width * width];
        int r,g,b,a;
        bmp.getPixels(oldPx,0,width,0,0,width,height);
        for (int i = 0; i < width * height; i++){

            color = oldPx[i];
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);

            r = Math.min(255, Math.max(0, 255 - r));
            g = Math.min(255, Math.max(0, 255 - g));
            b = Math.min(255, Math.max(0, 255 - b));

            newPx[i] = Color.argb(a, r, g, b);
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(newPx,0,width,0,0,width,height);
        return bitmap;
    }

}
