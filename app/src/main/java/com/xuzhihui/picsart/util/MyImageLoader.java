package com.xuzhihui.picsart.util;

import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.io.File;

/**
 * Project Name:  PicsArt
 * Package Name:  com.xuzhihui.picsart.util
 * File Name:     MyImageLoader
 * Creator:       Jav-Xu
 * Create Time:   2017/4/20 17:14
 * Description:   TODO
 */

public class MyImageLoader extends ImageLoader {
    /**
     * Constructs a new ImageLoader.
     *
     * @param queue      The RequestQueue to use for making image requests.
     * @param imageCache The cache to use as an L1 cache.
     */
    public MyImageLoader(RequestQueue queue, ImageCache imageCache) {
        super(queue, imageCache);
    }

    public static ImageListener getImageListener(final ImageView view,
                                                 final int defaultImageResId,
                                                 final int errorImageResId,
                                                 final File file,
                                                 final StringBuilder path) {
        return new ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageResId != 0) {
                    view.setImageResource(errorImageResId);
                }
                file.delete();
                path.append("");
            }

            @Override
            public void onResponse(ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    view.setImageBitmap(response.getBitmap());
                    ImageUtil.saveBitmapToPath(response.getBitmap(), file.getAbsolutePath());
                    path.append(file.getAbsolutePath());
                } else if (defaultImageResId != 0) {
                    view.setImageResource(defaultImageResId);
                }
            }
        };
    }
}
