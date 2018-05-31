package com.tuwan.yuewan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.RedEnvelopes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/8.
 */
public class RedSelectAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<RedEnvelopes.RedEnvelopsData> redEnvelopsData;
    public RedSelectAdapter(Context context) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_select_red_item, null);
            viewHolder.lly_my_select = (LinearLayout) convertView.findViewById(R.id.lly_my_select);
            viewHolder.tv_my_red_moneys = (TextView) convertView.findViewById(R.id.tv_my_red_moneys);
            viewHolder.tv_my_red_money = (TextView) convertView.findViewById(R.id.tv_my_red_money);
            viewHolder.tv_my_red_name = (TextView) convertView.findViewById(R.id.tv_my_red_name);
            viewHolder.tv_my_red_time = (TextView) convertView.findViewById(R.id.tv_my_red_time);
            viewHolder.img_red_check = (ImageView) convertView.findViewById(R.id.img_red_check);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_my_red_money.setText(redEnvelopsData.get(position).getPrice() + "");
        viewHolder.tv_my_red_name.setText(redEnvelopsData.get(position).getDesc());
        viewHolder.tv_my_red_time.setText(redEnvelopsData.get(position).getSdate() + "-" + redEnvelopsData.get(position).getEdate());
        if (redEnvelopsData.get(position).getSelect()){
            viewHolder.tv_my_red_money.setTextColor(Color.rgb(255,73,73));
            viewHolder.tv_my_red_moneys.setTextColor(Color.rgb(255,73,73));
            viewHolder.img_red_check.setImageResource(R.drawable.pay_choose);
        }else {
            viewHolder.tv_my_red_money.setTextColor(Color.rgb(204,204,204));
            viewHolder.tv_my_red_moneys.setTextColor(Color.rgb(204,204,204));
            viewHolder.img_red_check.setImageResource(R.drawable.pay_normal3x);
        }
        return convertView;
    }


    class ViewHolder {
        LinearLayout lly_my_select;
        TextView tv_my_red_moneys;
        TextView tv_my_red_money;
        TextView tv_my_red_name;
        TextView tv_my_red_time;
        ImageView img_red_check;
    }

}
