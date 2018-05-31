package com.tuwan.yuewan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuwan.common.LibraryApplication;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.GiftListBean;
import com.tuwan.yuewan.utils.AppUtils;

/**
 * @author zhangjie
 */
public class GiftHelperAdapter extends BaseAdapter {

    private final GiftListBean mGiftListBean;
    private Context context;
    private int startIndex;
    private Drawable ywbDrawable;

    public GiftHelperAdapter(Context mContext, int startIndex, GiftListBean mGiftListBean) {
        this.context = mContext;
        this.startIndex = startIndex;
        this.mGiftListBean = mGiftListBean;

        this.ywbDrawable = mContext.getResources().getDrawable(R.drawable.daimon_icon2x);
        this.ywbDrawable.setBounds(0,0,28,22);

    }

    @Override
    public int getCount() {
        return mGiftListBean == null ? 0 : (mGiftListBean.data.size() - startIndex>8?8:mGiftListBean.data.size() - startIndex);
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return startIndex + position;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_gift_helper, null);
        ImageView iv = (ImageView) convertView.findViewById(R.id.iv_item_gift_helper);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_item_gift_helper_title);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_item_gift_helper_price);

        GiftListBean.DataBean bean = mGiftListBean.data.get(position + startIndex);

        tvTitle.setText(bean.title);
        tvPrice.setText(bean.diamond + "");
        tvPrice.setCompoundDrawables(null, null,ywbDrawable,null);

        Glide.with(context)
                .asBitmap()
                .load(bean.pic)
                .into(iv);
        return convertView;
    }


    int height = -1;
    int width = -1;
    public int getItemHeight(){
        if(height==-1){
            height = DensityUtils.dp2px(LibraryApplication.getInstance(),92);
        }

        return height;
    }

    public int getItemWidth(){
        if(width==-1){
            width = DensityUtils.dp2px(LibraryApplication.getInstance(),81);
        }
        return width;
    }



}