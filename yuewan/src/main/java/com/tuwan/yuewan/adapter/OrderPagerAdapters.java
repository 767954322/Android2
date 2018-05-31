package com.tuwan.yuewan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.Record;

import java.util.List;

/**
 * Created by TUWAN on 2017/11/27.
 */

public class OrderPagerAdapters extends BaseAdapter {
    private Context context;
    private List<Record.DataBean> list;

    public OrderPagerAdapters(FragmentActivity activity, List<Record.DataBean> data) {
        this.context = activity;
        this.list = data;
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
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.order_record, null);
            vh.date = (TextView) convertView.findViewById(R.id.order_record_item_date);
            vh.price = (TextView) convertView.findViewById(R.id.order_record_item_price);
            vh.remark = (TextView) convertView.findViewById(R.id.order_record_item_remark);
            vh.title = (TextView) convertView.findViewById(R.id.order_record_item_title);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        if (list.get(position).getMode()!=1) {
            vh.price.setText("-"+list.get(position).getPrice() + "");
            vh.price.setTextColor(context.getResources().getColor(R.color.color_FF4949));
        } else {
            vh.price.setText("+"+list.get(position).getPrice() + "");
            vh.price.setTextColor(context.getResources().getColor(R.color.color_5BD896));
        }
        vh.title.setText(list.get(position).getTitle() + "");
        vh.remark.setText(list.get(position).getRemark() + "");
        vh.date.setText(list.get(position).getDate() + "");
        return convertView;
    }

    class ViewHolder {
        TextView date, price, remark, title;
    }


}
