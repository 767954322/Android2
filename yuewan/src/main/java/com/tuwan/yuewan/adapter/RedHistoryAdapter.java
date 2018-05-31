package com.tuwan.yuewan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.RedEnvelopes;
import com.tuwan.yuewan.ui.activity.YMainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/8.
 */
public class RedHistoryAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<RedEnvelopes.RedEnvelopsData> redEnvelopsData;
    public RedHistoryAdapter(Context context) {
        this.mContext = context;
        redEnvelopsData = new ArrayList<>();
    }

    public void setData(List<RedEnvelopes.RedEnvelopsData> data){
        this.redEnvelopsData.clear();
        this.redEnvelopsData.addAll(data);
    }



    @Override
    public int getCount() {
        return redEnvelopsData.size();
    }

    @Override
    public Object getItem(int position) {
        return redEnvelopsData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.red_history_item, null);
            viewHolder.img_my_red = (ImageView) convertView.findViewById(R.id.img_my_red);
            viewHolder.tv_my_red_money = (TextView) convertView.findViewById(R.id.tv_my_red_money);
            viewHolder.tv_my_red_name = (TextView) convertView.findViewById(R.id.tv_my_red_name);
            viewHolder.tv_my_red_time = (TextView) convertView.findViewById(R.id.tv_my_red_time);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_my_red_money.setText(redEnvelopsData.get(position).getPrice() + "");
        viewHolder.tv_my_red_name.setText(redEnvelopsData.get(position).getDesc());
        viewHolder.tv_my_red_time.setText(redEnvelopsData.get(position).getSdate() + "-" + redEnvelopsData.get(position).getEdate());
        if (redEnvelopsData.get(position).getExpire() == 1){
            viewHolder.img_my_red.setImageResource(R.drawable.icon_overdue);
        }else {
            viewHolder.img_my_red.setImageResource(R.drawable.icon_used);
        }
        return convertView;
    }


    class ViewHolder {
        ImageView img_my_red;
        TextView tv_my_red_money;
        TextView tv_my_red_name;
        TextView tv_my_red_time;
    }

}
