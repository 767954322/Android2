package com.tuwan.yuewan.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.ServiceBean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */

public class Service extends BaseAdapter {
    private Context context;
    private List<ServiceBean> been;
    private ViewHolder holder;

    public Service(Context context, List<ServiceBean> been) {
        this.context = context;
        this.been = been;
    }

    @Override
    public int getCount() {
        return been.size();
    }

    @Override
    public Object getItem(int position) {
        return been.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.myservice, null);
            holder.jx =(RelativeLayout)convertView.findViewById(R.id.jx);
            holder.money_more =(TextView)convertView.findViewById(R.id.money_more);
            holder.money =(RelativeLayout)convertView.findViewById(R.id.money);
            holder.show =(ImageView)convertView.findViewById(R.id.show);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        return convertView;
    }


    class ViewHolder {

        ImageView show;
        RelativeLayout jx;
        TextView money_more;
        RelativeLayout money;

    }


}