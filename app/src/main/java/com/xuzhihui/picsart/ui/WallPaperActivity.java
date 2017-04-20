package com.xuzhihui.picsart.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.xuzhihui.picsart.R;
import com.xuzhihui.picsart.adapter.ImageAdapter;
import com.xuzhihui.picsart.bean.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WallPaperActivity extends AppCompatActivity {

    public static final String IMAGE_URL = "http://gank.io/api/search/query/listview/category/%E7%A6%8F%E5%88%A9/count/50/page/1";

    private RecyclerView mRecyclerView;
    private List<Image> mImageList = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutmanager;
    private ImageAdapter mImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);
        initData();
        initView();
    }

    private void initData() {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(IMAGE_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        try {
                            JSONArray array = response.getJSONArray("results");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject imageObject = array.getJSONObject(i);
                                String imageUrl = imageObject.getString("url");
                                String imageName = imageObject.getString("who");
                                String imageTime = imageObject.getString("publishedAt");
                                Image image = new Image();
                                image.setName(imageName);
                                image.setTime(imageTime);
                                image.setUrl(imageUrl);
                                mImageList.add(image);
                            }
                            mImageAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(jsonObjectRequest);
    }


    private void initView() {

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager manager = new GridLayoutManager(WallPaperActivity.this, 2);
        mRecyclerView.setLayoutManager(manager);

        mImageAdapter = new ImageAdapter(WallPaperActivity.this, mImageList);
        mRecyclerView.setAdapter(mImageAdapter);
    }

}
