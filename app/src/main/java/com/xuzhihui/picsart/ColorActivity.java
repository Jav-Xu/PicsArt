package com.xuzhihui.picsart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

/**
 * Created by xuzhh on 2016/6/5.
 */
public class ColorActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,View.OnClickListener{

    private Context context;
    private static final String TAG = "";
    private Bitmap bitmap;
    private ImageView pic_before;
    private ImageView pic_after;
    private Button button_enhance;
    private Button button_sharp;
    private Button button_old;
    private Button button_negative;
    private Button button_relief;
    private SeekBar seekBar1;
    private SeekBar seekBar2;
    private SeekBar seekBar3;
    private static int MAX_VALUE = 255;
    private static int MID_VALUE = 127;
    private float mHue, mSaturation, mBrightness; //色调，饱和度，亮度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        Intent intent = getIntent();
        if(intent != null) {
            byte [] bis = intent.getByteArrayExtra("bitmap");
            bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
        }
        //bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_image);

        pic_before = (ImageView)findViewById(R.id.pic_before);
        pic_before.setImageBitmap(bitmap);
        pic_after = (ImageView) findViewById(R.id.pic_after);
        pic_after.setImageBitmap(bitmap);

        button_enhance = (Button)findViewById(R.id.button_enhance);
        button_sharp = (Button)findViewById(R.id.button_sharp);
        button_old = (Button)findViewById(R.id.button_old);
        button_negative = (Button)findViewById(R.id.button_negative);
        button_relief = (Button)findViewById(R.id.button_relief);

        button_enhance.setOnClickListener(this);
        button_sharp.setOnClickListener(this);
        button_old.setOnClickListener(this);
        button_negative.setOnClickListener(this);
        button_relief.setOnClickListener(this);

        seekBar1 = (SeekBar) findViewById(R.id.id_seekbar1);
        seekBar2 = (SeekBar) findViewById(R.id.id_seekbar2);
        seekBar3 = (SeekBar) findViewById(R.id.id_seekbar3);

        seekBar1.setOnSeekBarChangeListener(this);
        seekBar2.setOnSeekBarChangeListener(this);
        seekBar3.setOnSeekBarChangeListener(this);

        seekBar1.setMax(MAX_VALUE);
        seekBar2.setMax(MAX_VALUE);
        seekBar3.setMax(MAX_VALUE);

        seekBar1.setProgress(MID_VALUE);
        seekBar2.setProgress(MID_VALUE);
        seekBar3.setProgress(MID_VALUE);

    }

    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.id_seekbar1:
                mHue = (progress - MID_VALUE) * 1.0f / MID_VALUE *180;
                break;
            case R.id.id_seekbar2:
                mSaturation = progress * 1.0f / MID_VALUE;
                break;
            case R.id.id_seekbar3:
                mBrightness = progress * 1.0f / MID_VALUE;
                break;
        }
        pic_after.setImageBitmap(ImageColorTools.setHSB(bitmap, mHue, mSaturation, mBrightness));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_enhance:
                pic_after.setImageBitmap(ImageColorTools.enhance(bitmap));
                break;
            case R.id.button_sharp:
                pic_after.setImageBitmap(ImageColorTools.sharpenImage(bitmap));
                break;
            case R.id.button_old:
                pic_after.setImageBitmap(ImageColorTools.oldImage(bitmap));
                break;
            case R.id.button_negative:
                pic_after.setImageBitmap(ImageColorTools.negativeImage(bitmap));
                break;
            case R.id.button_relief:
                pic_after.setImageBitmap(ImageColorTools.reliefImage(bitmap));
                break;
        }
    }
}
