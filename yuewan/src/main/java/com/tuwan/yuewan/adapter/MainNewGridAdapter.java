package com.tuwan.yuewan.adapter;

import android.content.Context;
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
import com.tuwan.yuewan.framework.GlideRoundTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/8.
 */
public class MainNewGridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<NewData.NewDataBean> users;
    private int width;
    public MainNewGridAdapter(Context context) {
        this.mContext = context;
        users = new ArrayList<>();
    }

    public void setData(List<NewData.NewDataBean> data, int width){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.main_grid_item, null);
            viewHolder.imgGridIcon = (ImageView) convertView.findViewById(R.id.img_grid_icon);
            viewHolder.tvGridName = (TextView) convertView.findViewById(R.id.tv_grid_name);
            viewHolder.tvGridGrading = (TextView) convertView.findViewById(R.id.tv_grid_grading);
            viewHolder.tvGridPrice = (TextView) convertView.findViewById(R.id.tv_grid_price);
            viewHolder.tvGridOrdernum = (TextView) convertView.findViewById(R.id.tv_grid_ordernum);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //当前的item的title与上一个item的title不同的时候回显示title(A,B,C......)
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width / 2 - DensityUtils.dp2px(mContext,15), width / 2 - DensityUtils.dp2px(mContext,15));
        viewHolder.imgGridIcon.setLayoutParams(params);
        RequestOptions myOptions = new RequestOptions()
                .transform(new GlideRoundTransform(mContext,5));

        Glide.with(mContext)
                .load(users.get(position).getAvatar())
                .apply(myOptions)
                .into(viewHolder.imgGridIcon);
        viewHolder.tvGridName.setText(users.get(position).getNickname());
        viewHolder.tvGridGrading.setText(users.get(position).getGrading());
        viewHolder.tvGridPrice.setText(users.get(position).getPrice() / 100 + "元/" + users.get(position).getUnit());
        viewHolder.tvGridOrdernum.setText("接单" + users.get(position).getOrdernum() + "次");


        return convertView;
    }


    class ViewHolder {
        ImageView imgGridIcon;
        TextView tvGridName;
        TextView tvGridGrading;
        TextView tvGridPrice;
        TextView tvGridOrdernum;
    }

}
