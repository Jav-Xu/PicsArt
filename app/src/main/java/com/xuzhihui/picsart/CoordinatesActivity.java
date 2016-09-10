package com.xuzhihui.picsart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CoordinatesActivity extends AppCompatActivity {

    private ImageView imageShow;
    private ImageView imageCreate;
    private TextView textview1;
    private TextView textview2;
    private Bitmap bmp; //原始图片

    private float scaling = 1.0f; //缩放比例
    private int rotationAngle = 0;    //旋转角度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinates);

        Intent intent = getIntent();
        if(intent != null) {
            byte [] bis = intent.getByteArrayExtra("bitmap");
            bmp = BitmapFactory.decodeByteArray(bis, 0, bis.length);
        }

        imageShow = (ImageView) findViewById(R.id.imageView1);
        imageCreate = (ImageView) findViewById(R.id.imageView2);
        textview1 = (TextView) findViewById(R.id.textView1);
        textview2 = (TextView) findViewById(R.id.textView2);

        Button button2 = (Button) findViewById(R.id.button2);
        assert button2 != null;
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmallPicture();
            }
        });

        Button button3 = (Button) findViewById(R.id.button3);
        assert button3 != null;
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BigPicture();
            }
        });

        Button button4 = (Button) findViewById(R.id.button4);
        assert button4 != null;
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TurnPicture();
            }
        });

        Button button5 = (Button) findViewById(R.id.button5);
        assert button5 != null;
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReversePicture1();
            }
        });

        Button button6 = (Button) findViewById(R.id.button6);
        assert button6 != null;
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReversePicture2();
            }
        });

        imageShow.setImageBitmap(bmp);
        imageCreate.setImageBitmap(bmp);
    }

    private void SmallPicture() {
        Matrix matrix = new Matrix();
        //缩放区间 0.5-1.0
        if(scaling > 0.5f){
            scaling -= 0.1f;
        }else {
            scaling = 0.5f;
        }
        //x y 坐标同时缩放
        matrix.setScale(scaling, scaling, bmp.getWidth() / 2, bmp.getHeight() / 2);
        Bitmap createBmp = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(),bmp.getConfig());
        Canvas canvas = new Canvas(createBmp); //画布传入位图用于绘制
        Paint paint = new Paint(); //画刷改变颜色，对比度等属性
        canvas.drawBitmap(bmp,matrix,paint);
        //imageCreate.setBackgroundColor(Color.RED);
        imageCreate.setImageBitmap(createBmp);
    }

    private void BigPicture() {
        Matrix matrix = new Matrix();
        //缩放区间 0.5-1.0
        if(scaling < 1.5f){
            scaling += 0.1f;
        }else {
            scaling = 1.5f;
        }
        //x y 坐标同时缩放
        matrix.setScale(scaling, scaling,bmp.getWidth()/2,bmp.getHeight()/2);
        Bitmap createBmp = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(),bmp.getConfig());
        Canvas canvas = new Canvas(createBmp); //画布传入位图用于绘制
        Paint paint = new Paint(); //画刷改变颜色，对比度等属性
        canvas.drawBitmap(bmp,matrix,paint);
        //imageCreate.setBackgroundColor(Color.RED);
        imageCreate.setImageBitmap(createBmp);
    }

    private void TurnPicture() {
        Matrix matrix = new Matrix();
        rotationAngle += 15;
        // 选择角度绕(0,0)点旋转 正数顺时针 负数逆时针 中心旋转
        matrix.setRotate(rotationAngle, bmp.getWidth() / 2, bmp.getHeight() / 2);
        Bitmap createBmp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        Canvas canvas = new Canvas(createBmp);
        Paint paint = new Paint();
        canvas.drawBitmap(bmp, matrix, paint);
        //imageCreate.setBackgroundColor(Color.RED);
        imageCreate.setImageBitmap(createBmp);
    }

    private void ReversePicture1(){
        Matrix matrix = new Matrix();
        float[] floats = new float[] { -1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f };
        matrix.setValues(floats);
        Bitmap createBmp = Bitmap.createBitmap(bmp,
                0, 0, bmp.getWidth(),bmp.getHeight(), matrix, true);
        imageCreate.setImageBitmap(createBmp);
    }

    private void ReversePicture2(){
        Matrix matrix = new Matrix();
        float[] floats = new float[] { 1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f };
        matrix.setValues(floats);
        Bitmap createBmp = Bitmap.createBitmap(bmp,
                0, 0, bmp.getWidth(),bmp.getHeight(), matrix, true);
        imageCreate.setImageBitmap(createBmp);
    }

}

