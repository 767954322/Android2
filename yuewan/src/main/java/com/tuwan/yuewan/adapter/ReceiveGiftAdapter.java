package com.tuwan.yuewan.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.ui.fragment.record.gift.ReceiveGiftFragment;

import java.util.List;

/**
 * Created by Administrator on 2018/1/18.
 */

public class ReceiveGiftAdapter extends BaseAdapter {
    private Context context;
    private List<ReceiveGiftFragment.ReceiveGiftBean.DataBean> list;

    public ReceiveGiftAdapter(FragmentActivity activity, List<ReceiveGiftFragment.ReceiveGiftBean.DataBean> data) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.receive_gift_item, null);
            vh.pic = (ImageView) convertView.findViewById(R.id.receive_gift_item_img);
            vh.title = (TextView) convertView.findViewById(R.id.receive_gift_item_title);
            vh.name = (TextView) convertView.findViewById(R.id.receive_gift_item_name);
            vh.time = (TextView) convertView.findViewById(R.id.receive_gift_item_time);
            vh.charm = (TextView) convertView.findViewById(R.id.receive_gift_item_charm);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.title.setText(list.get(position).getTitle() + "x" + list.get(position).getNum());
        vh.time.setText(list.get(position).getCreate_time());
        vh.charm.setText("+" + list.get(position).getCharm() + "魅力值");
        vh.name.setText(list.get(position).getName());
        Glide.with(context).load(list.get(position).getPic()).into(vh.pic);
        return convertView;
    }

    class ViewHolder {
        TextView title, name, time, charm;
        ImageView pic;
    }


}
