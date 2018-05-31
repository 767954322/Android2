package com.tuwan.yuewan.ui.item;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tuwan.common.ui.widget.convenientbanner.holder.Holder;

/**
 * Created by zhangjie on 2017/10/13.
 */
public class NetworkImageHolderView implements Holder<Object> {
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, Object obj) {
        Glide.with(imageView.getContext()).load(obj.toString()).into(imageView);
    }
}
