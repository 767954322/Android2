package com.tuwan.yuewan.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.loader.ImageLoader;

import static com.umeng.socialize.utils.DeviceConfig.context;


/**
 * Created by Administrator on 2017/12/15.
 */

public class GlideLoader implements ImageLoader, com.jaiky.imagespickers.ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(context)
                .load(path)
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {

    }

    @Override
    public void displayImage(Context context, String path, ImageView imageView) {

    }
}
