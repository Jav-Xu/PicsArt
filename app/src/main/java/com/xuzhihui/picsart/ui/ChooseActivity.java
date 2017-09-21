package com.xuzhihui.picsart.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.xuzhihui.picsart.R;
import com.xuzhihui.picsart.application.MyApplication;
import com.xuzhihui.picsart.util.FileUtil;
import com.xuzhihui.picsart.util.ImageUtil;
import com.xuzhihui.picsart.view.CustomDialog;

import java.io.File;

import static android.view.WindowManager.LayoutParams;

/**
 * Created by xuzhh on 2016/6/10.
 */
public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton mImageButton;
    private Button mGalleryButton;
    private Button mCameraButton;
    private Button mDownloadButton;

    private Button mCoordButton;
    private Button mColorButton;

    private CustomDialog mChooseDialog;

    private File mImageFile;
    private Uri mImageUri;
    private String mPath;
    private StringBuilder mPathBuild;
    private Bitmap mBitmap;

    public static final int REQUEST_PERMISSION = 0;
    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_GALLERY = 2;
    public static final int REQUEST_DOWNLOAD = 3;

    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        initView();
        initRequestPermissions();
    }

    private void initView() {

        mImageButton = (ImageButton) findViewById(R.id.image_button);
        mCoordButton = (Button) findViewById(R.id.part1);
        mColorButton = (Button) findViewById(R.id.part2);

        mImageButton.setOnClickListener(this);
        mCoordButton.setOnClickListener(this);
        mColorButton.setOnClickListener(this);
    }

    private void initDialogView(CustomDialog chooseDialog) {

        mGalleryButton = (Button) chooseDialog.findViewById(R.id.gallery);
        mCameraButton = (Button) chooseDialog.findViewById(R.id.camera);
        mDownloadButton = (Button) chooseDialog.findViewById(R.id.download);

        mGalleryButton.setOnClickListener(ChooseActivity.this);
        mCameraButton.setOnClickListener(ChooseActivity.this);
        mDownloadButton.setOnClickListener(ChooseActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_button:
                mChooseDialog = new CustomDialog(
                        ChooseActivity.this, R.style.dialog_theme, R.layout.dialog_choose,
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
                        Gravity.BOTTOM, R.style.pop_anim);
                initDialogView(mChooseDialog);
                mChooseDialog.show();
                break;
            case R.id.gallery:
                mChooseDialog.dismiss();
                toGallery();
                break;
            case R.id.camera:
                mChooseDialog.dismiss();
                if (initFileAndUriSuccess()) {
                    toCamera();
                }
                break;
            case R.id.download:
                if (initFileAndUriSuccess()) {
                    mChooseDialog.dismiss();
                    startActivityForResult(new Intent(ChooseActivity.this, WallPaperActivity.class), REQUEST_DOWNLOAD);
                }
                break;
            case R.id.part1:
                if (mPath != null) {
                    Intent intent_coord = CoordActivity.getIntent(ChooseActivity.this, mPath);
                    startActivity(intent_coord);
                } else {
                    Toast.makeText(getApplicationContext(), "请先导入或下载一张图片", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.part2:
                if (mPath != null) {
                    Intent intent_color = ColorActivity.getIntent(ChooseActivity.this, mPath);
                    startActivity(intent_color);
                } else {
                    Toast.makeText(getApplicationContext(), "请先导入或下载一张图片", Toast.LENGTH_SHORT).show();
                }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (resultCode != RESULT_OK) {
                    mImageFile.delete();
                    return;
                }
                mPath = mImageFile.getAbsolutePath();
                mPathBuild = new StringBuilder("");
                mPathBuild.append(mPath);
                mBitmap = ImageUtil.getScaledBitmap(mPath, mImageButton.getWidth(), mImageButton.getHeight());
                mImageButton.setImageBitmap(mBitmap);
                break;
            case REQUEST_GALLERY:
                if (resultCode != RESULT_OK) {
                    return;
                }
                Uri uri_gallery = data.getData();
                mPath = FileUtil.getPathFromContentUri(ChooseActivity.this, uri_gallery);
                mPathBuild = new StringBuilder("");
                mPathBuild.append(mPath);
                mBitmap = ImageUtil.getScaledBitmap(mPath, mImageButton.getWidth(), mImageButton.getHeight());
                mImageButton.setImageBitmap(mBitmap);
                break;
            case REQUEST_DOWNLOAD:
                if (resultCode != RESULT_OK) {
                    mImageFile.delete();
                    return;
                }
                String url = data.getStringExtra("CHOOSE_IMAGE_URL");
                loadImageAndSetIntoImageView(url, mImageButton);
                break;
        }
    }

    private void loadImageAndSetIntoImageView(final String url, final ImageButton imageView) {

        if (initFileAndUriSuccess()) {

            mPath = mImageFile.getAbsolutePath();

            MyApplication.mLoader.get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    Bitmap bitmap = response.getBitmap();
                    if (bitmap != null) { // 这里一定要进行控制判断！！！！！！！！！！！！！
                        ImageUtil.saveBitmapToPath(bitmap, mPath);
                        imageView.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    imageView.setImageResource(R.mipmap.ic_launcher);
                }
            }, 500, 500, ImageView.ScaleType.CENTER_CROP);
        }
    }

    private void initRequestPermissions() {
        // 应用已启动加载就开始申请权限，包括文件读写（要为照片准备文件）和相机拍照权限，这里由检查文件读写这一项权限触发，来申请两项权限
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(ChooseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ChooseActivity.this, permissions, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    Toast.makeText(ChooseActivity.this, "你拒绝了一些权限，之后将不能进行相片操作", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private boolean initFileAndUriSuccess() {

        boolean flag = false;
        mImageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "PicsArt_Temp" + ".jpg");
        try {
            if (mImageFile.exists()) {
                mImageFile.delete();
            }
            mImageFile.createNewFile();
            mImageUri = FileUtil.getOriginalUriFromFile(this, mImageFile);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "为照片预备文件出错", Toast.LENGTH_SHORT).show();
        } finally {
            return flag;
        }
    }

    private void toCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cameraIntent.putExtra("return-data", false);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    private void toGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }

}
