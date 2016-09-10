package com.xuzhihui.picsart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jav-Xu on 16/9/4.
 */
public class ImageAdapter extends BaseAdapter{

    Context context;
    List<Image> mImageList;
    private LayoutInflater inflater;

    public ImageAdapter(Context context,List list){
        this.context = context;
        this.mImageList = list;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public Image getItem(int i) {
        return mImageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup group) {
        Image image = getItem(position);
        Holder holder;
        if (convertView == null){
            holder = new Holder();
            convertView = inflater.inflate(R.layout.dialog_listview_item,null);

            holder.name = (TextView)convertView.findViewById(R.id.image_name);
            holder.logo = (ImageView) convertView.findViewById(R.id.image_logo);

            convertView.setTag(holder);
        }else {
            holder = (Holder)convertView.getTag();
        }

        holder.name.setText(image.getName());
        holder.logo.setImageResource(image.getSrcId());

        return convertView;
    }

    protected class Holder{
        TextView name;
        ImageView logo;
    }
}
