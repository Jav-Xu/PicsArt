package com.xuzhihui.picsart.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.xuzhihui.picsart.R;
import com.xuzhihui.picsart.util.ImageUtil;

public class CoordActivity extends AppCompatActivity {

    private ImageView imageShow;
    private ImageView imageCreate;
    private Bitmap bmp_before; //原始图片
    private Bitmap bmp_after; //修改图片

    private float scaling = 1.0f; //缩放比例
    private int rotationAngle = 0;    //旋转角度

    public static final String EXTRA_PATH = "path";
    private String path;

    public static Intent getIntent(Context context, String path) {
        Intent intent = new Intent(context, CoordActivity.class);
        intent.putExtra(EXTRA_PATH, path);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coord);
        initData();
        initView();
    }

    private void initData() {
        if (getIntent() != null) {
            path = getIntent().getStringExtra(EXTRA_PATH);
            bmp_before = ImageUtil.getScaledBitmap(path, this);
        }
    }

    private void initView() {

        imageShow = (ImageView) findViewById(R.id.imageView1);
        imageCreate = (ImageView) findViewById(R.id.imageView2);

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmallPicture();
            }
        });

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BigPicture();
            }
        });

        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TurnPicture();
            }
        });

        Button button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReversePicture1();
            }
        });

        Button button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReversePicture2();
            }
        });

        imageShow.setImageBitmap(bmp_before);
        imageCreate.setImageBitmap(bmp_before);
    }

    private void SmallPicture() {
        Matrix matrix = new Matrix();
        //缩放区间 0.5-1.0
        if (scaling > 0.5f) {
            scaling -= 0.1f;
        } else {
            scaling = 0.5f;
        }
        //x y 坐标同时缩放
        matrix.setScale(scaling, scaling, bmp_before.getWidth() / 2, bmp_before.getHeight() / 2);
        Bitmap createBmp = Bitmap.createBitmap(bmp_before.getWidth(), bmp_before.getHeight(), bmp_before.getConfig());
        Canvas canvas = new Canvas(createBmp); //画布传入位图用于绘制
        Paint paint = new Paint(); //画刷改变颜色，对比度等属性
        canvas.drawBitmap(bmp_before, matrix, paint);
        //imageCreate.setBackgroundColor(Color.RED);
        imageCreate.setImageBitmap(createBmp);
    }

    private void BigPicture() {
        Matrix matrix = new Matrix();
        //缩放区间 0.5-1.0
        if (scaling < 1.5f) {
            scaling += 0.1f;
        } else {
            scaling = 1.5f;
        }
        //x y 坐标同时缩放
        matrix.setScale(scaling, scaling, bmp_before.getWidth() / 2, bmp_before.getHeight() / 2);
        Bitmap createBmp = Bitmap.createBitmap(bmp_before.getWidth(), bmp_before.getHeight(), bmp_before.getConfig());
        Canvas canvas = new Canvas(createBmp); //画布传入位图用于绘制
        Paint paint = new Paint(); //画刷改变颜色，对比度等属性
        canvas.drawBitmap(bmp_before, matrix, paint);
        //imageCreate.setBackgroundColor(Color.RED);
        imageCreate.setImageBitmap(createBmp);
    }

    private void TurnPicture() {
        Matrix matrix = new Matrix();
        rotationAngle += 15;
        // 选择角度绕(0,0)点旋转 正数顺时针 负数逆时针 中心旋转
        matrix.setRotate(rotationAngle, bmp_before.getWidth() / 2, bmp_before.getHeight() / 2);
        Bitmap createBmp = Bitmap.createBitmap(bmp_before.getWidth(), bmp_before.getHeight(), bmp_before.getConfig());
        Canvas canvas = new Canvas(createBmp);
        Paint paint = new Paint();
        canvas.drawBitmap(bmp_before, matrix, paint);
        //imageCreate.setBackgroundColor(Color.RED);
        imageCreate.setImageBitmap(createBmp);
    }

    private void ReversePicture1() {
        Matrix matrix = new Matrix();
        float[] floats = new float[]{-1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f};
        matrix.setValues(floats);
        Bitmap createBmp = Bitmap.createBitmap(bmp_before,
                0, 0, bmp_before.getWidth(), bmp_before.getHeight(), matrix, true);
        imageCreate.setImageBitmap(createBmp);
    }

    private void ReversePicture2() {
        Matrix matrix = new Matrix();
        float[] floats = new float[]{1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f};
        matrix.setValues(floats);
        Bitmap createBmp = Bitmap.createBitmap(bmp_before,
                0, 0, bmp_before.getWidth(), bmp_before.getHeight(), matrix, true);
        imageCreate.setImageBitmap(createBmp);
    }

}

