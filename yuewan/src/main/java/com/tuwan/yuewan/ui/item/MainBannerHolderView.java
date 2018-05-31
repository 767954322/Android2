package com.tuwan.yuewan.ui.item;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tuwan.common.ui.widget.convenientbanner.holder.Holder;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.framework.GlideRoundTransform;

/**
 * Created by zhangjie on 2017/10/13.
 */
public class MainBannerHolderView implements Holder<Object> {
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        View inflate = View.inflate(context, R.layout.item_main_banner, null);
        imageView = (ImageView) inflate.findViewById(R.id.iv_item_main_banner);
        return inflate;
    }

    @Override
    public void UpdateUI(Context context, int position, Object obj) {

        RequestOptions myOptions = new RequestOptions()
                .transform(new GlideRoundTransform(imageView.getContext(),5));

        Glide.with(imageView.getContext())
                .load(obj.toString())
                .apply(myOptions)
                .into(imageView);

    }
}
