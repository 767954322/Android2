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
import com.makeramen.roundedimageview.RoundedImageView;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.FollowBean;
import com.tuwan.yuewan.entity.TrystBean;
import com.tuwan.yuewan.framework.GlideRoundTransform;
import com.tuwan.yuewan.ui.activity.TeacherMainActivity;
import com.tuwan.yuewan.utils.ChineseToEnglish;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/8.
 */
public class TrystAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<TrystBean.TrystData> users;
    public TrystAdapter(Context context) {
        this.mContext = context;
        users = new ArrayList<>();
    }

    public void setData(List<TrystBean.TrystData> data){
        this.users.clear();
        this.users.addAll(data);
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new TrystAdapter.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_tryst_item, null);
            viewHolder.img_tryst_icon = (ImageView) convertView.findViewById(R.id.img_tryst_icon);
            viewHolder.tv_tryst_item_name = (TextView) convertView.findViewById(R.id.tv_tryst_item_name);
            viewHolder.tv_tryst_grading = (TextView) convertView.findViewById(R.id.tv_tryst_grading);
            viewHolder.tv_tryst_price = (TextView) convertView.findViewById(R.id.tv_tryst_price);
            viewHolder.tv_tryst_ordernum = (TextView) convertView.findViewById(R.id.tv_tryst_ordernum);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RequestOptions myOptions = new RequestOptions()
                .transform(new GlideRoundTransform(mContext,5));

        Glide.with(mContext)
                .load(users.get(position).getAvatar())
                .apply(myOptions)
                .into(viewHolder.img_tryst_icon);
        viewHolder.tv_tryst_item_name.setText(users.get(position).getNickname());
        viewHolder.tv_tryst_grading.setText(users.get(position).getGrading());
        viewHolder.tv_tryst_price.setText(users.get(position).getPrice() + "元/" + users.get(position).getUnit());
        viewHolder.tv_tryst_ordernum.setText("接单" + users.get(position).getOrdernum() + "次");

        return convertView;
    }

    class ViewHolder {
        ImageView img_tryst_icon;
        TextView tv_tryst_item_name;
        TextView tv_tryst_grading;
        TextView tv_tryst_price;
        TextView tv_tryst_ordernum;
    }


}
