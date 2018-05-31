package com.tuwan.yuewan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.InBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TUWAN on 2017/12/4.
 */

public class AppliItem2 extends BaseAdapter {
    Context context;
    private List<InBean.OnlinepBean> dataBeen = new ArrayList<>();
    private ViewHolder holder;


    public AppliItem2(Context context, List<InBean.OnlinepBean> dataBeen) {
        this.context = context;
        this.dataBeen = dataBeen;
    }

    @Override
    public int getCount() {
        return dataBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return dataBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.in_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.in_item_icon);
            holder.more = (ImageView) convertView.findViewById(R.id.more);
            holder.in_item_type = (TextView) convertView.findViewById(R.id.in_item_type);
            holder.item_name = (TextView) convertView.findViewById(R.id.in_item_name);
            holder.item_stats = (TextView) convertView.findViewById(R.id.in_item_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        InBean.OnlinepBean dataBean = dataBeen.get(position);
        Glide.with(context).load(dataBean.getGameicon()).into(holder.icon);
        holder.item_name.setText(dataBean.getTitle());
        //state 0正常 -1待审核 1审核成功
        if (dataBean.getState() == 0) {
            holder.item_stats.setText("");
            holder.in_item_type.setVisibility(View.GONE);
        } else if (dataBean.getState() == -1) {
            holder.item_stats.setVisibility(View.VISIBLE);
            holder.item_stats.setText("审核中");
            holder.more.setVisibility(View.GONE);
            holder.in_item_type.setVisibility(View.GONE);
        } else if (dataBean.getState() == 1) {
            holder.in_item_type.setBackgroundResource(R.drawable.in_item_yes);
            holder.in_item_type.setText("通过");
            holder.in_item_type.setVisibility(View.VISIBLE);
            holder.item_stats.setVisibility(View.GONE);
            holder.more.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView icon;
        ImageView more;
        TextView item_name;
        TextView in_item_type;
        TextView item_stats;
    }
}
