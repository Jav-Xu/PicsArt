package com.xuzhihui.picsart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by xuzhh on 2016/6/10.
 */
public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {

    Bitmap bmp;
    Button button_choose;
    Button button_download;
    Button button_part1;
    Button button_part2;
    ImageView image;

    public static final String TAG = "ChooseActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            bmp = (Bitmap) savedInstanceState.getParcelable("BitmapImage");
        }
        setContentView(R.layout.activity_choose);

        Log.d(TAG, "onCreate-------------");

        button_choose = (Button) findViewById(R.id.button_choose);
        button_download = (Button) findViewById(R.id.button_download);
        button_part1 = (Button) findViewById(R.id.part1);
        button_part2 = (Button) findViewById(R.id.part2);
        image = (ImageView) findViewById(R.id.ImageView);

        button_choose.setOnClickListener(this);
        button_download.setOnClickListener(this);
        button_part1.setOnClickListener(this);
        button_part2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_choose:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
                break;
            case R.id.button_download:
                showDialog();
                break;
            case R.id.part1:
                if (bmp != null) {
                    Intent intent1 = new Intent(getApplicationContext(), CoordinatesActivity.class);
                    ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos1);
                    byte[] bitmapByte1 = baos1.toByteArray();
                    intent1.putExtra("bitmap", bitmapByte1);
                    startActivity(intent1);
                } else {
                    Toast.makeText(getApplicationContext(), "请先导入或下载一张图片", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.part2:
                if (bmp != null) {
                    Intent intent2 = new Intent(getApplicationContext(), ColorActivity.class);
                    ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos2);
                    byte[] bitmapByte2 = baos2.toByteArray();
                    intent2.putExtra("bitmap", bitmapByte2);
                    startActivity(intent2);
                } else {
                    Toast.makeText(getApplicationContext(), "请先导入或下载一张图片", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void showDialog() {

        final ArrayList<Image> list = new ArrayList<Image>();
        loadList(list);

        LayoutInflater layoutInflater = LayoutInflater.from(ChooseActivity.this);
        View dialog = layoutInflater.inflate(R.layout.dialog_download, null);
        ListView listView = (ListView) dialog.findViewById(R.id.dialog_listview);
        listView.setAdapter(new ImageAdapter(ChooseActivity.this, list));


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        int dialogHeight = (int) height / 2;


        final Dialog alertDialog = new AlertDialog.Builder(ChooseActivity.this).setView(dialog).create();
        alertDialog.setCancelable(true);
        alertDialog.show();
        alertDialog.getWindow().setLayout(width, dialogHeight);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> view, View view1, int i, long l) {
                Image image = list.get(i);
                String url = image.getUrl();
                downloadImage(url);
                alertDialog.dismiss();
            }
        });
    }

    private void downloadImage(String url) {

        RequestQueue queue = Volley.newRequestQueue(ChooseActivity.this);

//        BitmapCache cache = new BitmapCache();
//        ImageLoader loader = new ImageLoader(queue, cache);
//        ImageLoader.ImageListener listener = ImageLoader.getImageListener(image, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
//        loader.get(url, listener);
//        //bmp = cache.getBitmap(url);
//        image.setDrawingCacheEnabled(true);
//        bmp = image.getDrawingCache();
//        image.setDrawingCacheEnabled(false);


        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        bmp = response;
                        image.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        image.setImageResource(R.mipmap.ic_launcher);
                    }
                });
        queue.add(imageRequest);
    }

    private void loadList(ArrayList list) {

        String url1 = "http://image72.360doc.com/DownloadImg/2014/05/2605/42035151_6.jpg";
        String url2 = "http://img1.3lian.com/2015/w7/85/d/102.jpg";
        String url3 = "http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1209/10/c1/13758581_1347257278704.jpg";
        String url4 = "http://img15.3lian.com/2015/h1/10/d/13.jpg";
        String url5 = "http://img01.taopic.com/141002/240423-14100210124112.jpg";
        String url6 = "http://img1.3lian.com/2015/a2/239/d/1.jpg";

        list.add(new Image("火车", R.mipmap.ic_launcher, url1));
        list.add(new Image("兔子", R.mipmap.ic_launcher, url2));
        list.add(new Image("夕阳", R.mipmap.ic_launcher, url3));
        list.add(new Image("麋鹿", R.mipmap.ic_launcher, url4));
        list.add(new Image("郊外", R.mipmap.ic_launcher, url5));
        list.add(new Image("鲜花", R.mipmap.ic_launcher, url6));

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ShowPhotoByImageView(data); //显示照片1
        }
    }

    private void ShowPhotoByImageView(Intent data) {
        Uri imageFileUri = data.getData();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        try {
            BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
            //把inJustDecodeBounds设置为true,则完全不用分配内存就可以得到给位图文件bitmap的信息
            //如此得到大小后，就可以对其进行压缩，然后在内存中生成一个更小的bitmap，节省了内存
            bmpFactoryOptions.inJustDecodeBounds = true;
            bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageFileUri), null, bmpFactoryOptions);

            int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
            int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

            bmpFactoryOptions.inSampleSize = 1;

            if (heightRatio > 1 || widthRatio > 1) {
                // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
                // 一定都会大于等于目标的宽和高。
                bmpFactoryOptions.inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }

            //图像真正解码
            bmpFactoryOptions.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageFileUri), null, bmpFactoryOptions);
            image.setImageBitmap(bmp); //将剪裁后照片显示出来
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("BitmapImage", bmp);
    }

}
