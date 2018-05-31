package com.tuwan.yuewan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tuwan.yuewan.R;

import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */

public class ItemAdapter extends BaseAdapter {
    Context context;
    List<String> list;
    private ViewHolder holder;

    public ItemAdapter(Context context, List<String> icons) {
        this.context = context;
        this.list=icons;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView=View.inflate(context, R.layout.images,null);
            holder = new ViewHolder();
            holder.image= (ImageView) convertView.findViewById(R.id.switch_item_icon);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(list.get(position)).into(holder.image);
        return convertView;
    }
    class ViewHolder{
        ImageView image;
    }
}
