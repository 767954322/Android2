package com.tuwan.yuewan.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuwan.yuewan.R;

import java.util.List;

public class MenuListAdapter extends BaseAdapter {
    private Context context;

    private ViewHolder viewHolder;
    private List<String> list;

    private int mCheckedPosition = 0;

    public void setList(List list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public MenuListAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
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
        viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.act_item_popu_list, null);
            viewHolder.text1 = (TextView) convertView.findViewById(R.id.textname);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv_choose);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String o = list.get(position);
        viewHolder.text1.setText(list.get(position).toString());

        if (mCheckedPosition == position) {
            viewHolder.text1.setTextColor(0xFFFFC602);
            viewHolder.iv.setVisibility(View.VISIBLE);
        } else {
            viewHolder.text1.setTextColor(0xFF666666);
            viewHolder.iv.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView text1;
        private ImageView iv;

    }

    public boolean onItemClick(int position) {
        if (mCheckedPosition == position) {
            return false;
        }
        mCheckedPosition = position;
        notifyDataSetChanged();
        return true;
    }

    /**
     * typeid和position的定义是一样的，直接取值就行
     * 0智能推荐，1新人，2热度，3距离
     */
    public int getCheckedPosition() {
        return mCheckedPosition;
    }

    public void clear() {
        clear();
        notifyDataSetChanged();
    }


}
