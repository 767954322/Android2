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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.NewData;
import com.tuwan.yuewan.entity.TrystRemarks;
import com.tuwan.yuewan.framework.GlideRoundTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/8.
 */
public class MainTrystGridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<TrystRemarks> users;
    private int width;
    public MainTrystGridAdapter(Context context) {
        this.mContext = context;
        users = new ArrayList<>();
    }

    public void setData(List<TrystRemarks> data, int width){
        this.users.clear();
        this.users.addAll(data);
        this.width = width;
    }



    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.main_tryst_grid_item, null);
            viewHolder.tvTrystRemarks = (TextView) convertView.findViewById(R.id.tv_main_tryst_remarks);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //当前的item的title与上一个item的title不同的时候回显示title(A,B,C......)

        viewHolder.tvTrystRemarks.setText(users.get(position).getName());
        if (users.get(position).isType()){
            viewHolder.tvTrystRemarks.setBackgroundResource(R.drawable.text_biankuang_2);
            viewHolder.tvTrystRemarks.setTextColor(Color.rgb(255, 198, 2));
        }else {
            viewHolder.tvTrystRemarks.setBackgroundResource(R.drawable.text_biankuang);
            viewHolder.tvTrystRemarks.setTextColor(Color.rgb(102,102,102));
        }


        return convertView;
    }


    class ViewHolder {
        TextView tvTrystRemarks;
    }

}
