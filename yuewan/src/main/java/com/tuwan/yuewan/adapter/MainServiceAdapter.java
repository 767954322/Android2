package com.tuwan.yuewan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.ui.activity.MyServiceActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/15.
 */

public class MainServiceAdapter extends BaseAdapter {
    private ArrayList<MyServiceActivity.Person> list;
    private Context context;
    private ViewHolder holder;

    public MainServiceAdapter(ArrayList<MyServiceActivity.Person> maps, Context context) {
        this.list = maps;
        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.pop_serv2, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.serv2_name);
            holder.ischecked = (CheckBox) convertView.findViewById(R.id.show);
            holder.icon = (ImageView) convertView.findViewById(R.id.serv2_icon);
            holder.jx = (RelativeLayout) convertView.findViewById(R.id.jx);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getName());
        Glide.with(context).load(list.get(position).getIcon()).into(holder.icon);
        if (list.get(position).isBoo()) {
            holder.ischecked.setChecked(true);
        } else {
            holder.ischecked.setChecked(false);
        }
//        if (list.get(position).getMain() == 1) {
//            holder.ischecked.setChecked(true);
//        } else {
//            holder.ischecked.setChecked(false);
//        }

//        holder.jx.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.ischecked.setVisibility(View.VISIBLE);
//
//            }
//        });
        return convertView;
    }

    public int i = 0;

    class ViewHolder {
        TextView name;
        ImageView icon;
        CheckBox ischecked;
        RelativeLayout jx;
    }
}
