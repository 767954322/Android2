package com.tuwan.yuewan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.FollowBean;
import com.tuwan.yuewan.entity.RedEnvelopes;
import com.tuwan.yuewan.ui.activity.RegisterDataActivity;
import com.tuwan.yuewan.ui.activity.TeacherMainActivity;
import com.tuwan.yuewan.ui.activity.YMainActivity;
import com.tuwan.yuewan.utils.ChineseToEnglish;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/8.
 */
public class RedAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<RedEnvelopes.RedEnvelopsData> redEnvelopsData;
    public RedAdapter(Context context) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_my_red_item, null);
            viewHolder.tv_my_red_money = (TextView) convertView.findViewById(R.id.tv_my_red_money);
            viewHolder.tv_my_red_name = (TextView) convertView.findViewById(R.id.tv_my_red_name);
            viewHolder.tv_my_red_time = (TextView) convertView.findViewById(R.id.tv_my_red_time);
            viewHolder.tv_my_red_submit = (TextView) convertView.findViewById(R.id.tv_my_red_submit);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_my_red_money.setText(redEnvelopsData.get(position).getPrice() + "");
        viewHolder.tv_my_red_name.setText(redEnvelopsData.get(position).getDesc());
        viewHolder.tv_my_red_time.setText(redEnvelopsData.get(position).getSdate() + "-" + redEnvelopsData.get(position).getEdate());
        viewHolder.tv_my_red_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YMainActivity.sInstance.finish();
                Intent intent = new Intent(mContext, YMainActivity.class);
                mContext.startActivity(intent);
                if(Activity.class.isInstance(mContext)) {
                    // 转化为activity，然后finish就行了
                    Activity activity = (Activity) mContext;
                    activity.finish();
                }
            }
        });
        return convertView;
    }


    class ViewHolder {
        TextView tv_my_red_money;
        TextView tv_my_red_name;
        TextView tv_my_red_time;
        TextView tv_my_red_submit;
    }

}
