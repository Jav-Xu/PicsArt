package com.xuzhihui.picsart.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xuzhihui.picsart.R;
import com.xuzhihui.picsart.bean.Image;

import java.util.List;

/**
 * Project Name:  PicsArt
 * Package Name:  com.xuzhihui.picsart.adapter
 * File Name:     ImageAdapter
 * Creator:       Jav-Xu
 * Create Time:   2017/4/18 15:05
 * Description:   TODO
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {


    private Context mContext;
    private List<Image> mImageList;
    private LayoutInflater mLayoutInflater;
    private WindowManager mWindowManager;
    private int width;

    public ImageAdapter(Context context, List<Image> imageList) {
        mContext = context;
        mImageList = imageList;
        mLayoutInflater = LayoutInflater.from(context);
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = mWindowManager.getDefaultDisplay().getWidth();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View mItemView;
        private ImageView mImageView;
        private Image mImage;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mImageView = (ImageView) mItemView.findViewById(R.id.item_imageView);
            mImageView.setOnClickListener(this);
        }

        public void bindHolder(Image image) {
            mImage = image;
            Glide.with(mContext).load(image.getUrl()).override(width / 2, 300).into(mImageView);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_imageView:
                    Intent intent = new Intent();
                    intent.putExtra("CHOOSE_IMAGE_URL", mImage.getUrl());
                    AppCompatActivity appCompatActivity = (AppCompatActivity) mContext;
                    appCompatActivity.setResult(Activity.RESULT_OK, intent);
                    appCompatActivity.finish();
                    break;
            }
        }
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Image image = mImageList.get(position);
        holder.bindHolder(image);
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

}
