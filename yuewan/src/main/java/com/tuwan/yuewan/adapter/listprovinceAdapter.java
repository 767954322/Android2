package com.tuwan.yuewan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.addinfobean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/30.
 */

public class listprovinceAdapter extends BaseAdapter {

        private Context context;
        private List<addinfobean.DataBean.ProvinceBean> list;
    private int selectedPosition = 0;// 选中的位置
    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }
    public listprovinceAdapter(Context context, List<addinfobean.DataBean.ProvinceBean> list, int pos) {
        this.context = context;
        this.list = list;
        this.selectedPosition = pos;
    }

    @Override
        public int getCount() {
        return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_left, null);
                holder = new ViewHolder();

                holder.nameTV = (TextView) convertView.findViewById(R.id.list_view_item_text_left);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.nameTV.setText(list.get(position).getArea());
            if (selectedPosition == position) {
//                itemlayoutb.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.nameTV.setTextColor(Color.parseColor("#FFCDE449"));
            } else {
//                itemlayoutb.setBackgroundColor(Color.TRANSPARENT);
                holder.nameTV.setTextColor(Color.parseColor("#393939"));
            }
            return convertView;
        }

    private class ViewHolder {
        TextView nameTV;
    }
}
