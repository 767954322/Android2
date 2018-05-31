package com.tuwan.yuewan.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.SwitchItem;
import com.tuwan.yuewan.ui.activity.TeacherMainActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */

public class SwitchAdapter2 extends BaseAdapter {
    List<SwitchItem.DataBean> been;
    Context context;
    private String editString;
    private ViewHolder holder;

    public SwitchAdapter2(List<SwitchItem.DataBean> been,String editString, Context context) {
        this.been = been;
        this.context = context;
        this.editString = editString;
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
        try {


        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.switch_item2, null);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.name = (TextView) convertView.findViewById(R.id.name_switch);
            holder.time = (TextView) convertView.findViewById(R.id.switch_time);
            holder.sexIcon = (ImageView) convertView.findViewById(R.id.switch_sexicon);
            holder.sex = (TextView) convertView.findViewById(R.id.switch_age);
            holder.content = (TextView) convertView.findViewById(R.id.switch_content);
            holder.icons = (GridView) convertView.findViewById(R.id.switch_icon);
            holder.layout = convertView.findViewById(R.id.switch_sexs);
            holder.lis=convertView.findViewById(R.id.lis);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SwitchItem.DataBean dataBean = been.get(position);
//        Log.e("Tag_A", dataBean.toString()+"");
        setTvColor(dataBean.getNickname(),editString,holder.name);
        holder.time.setText(dataBean.getCity().substring(0, 2) + " |" + dataBean.getRefreshTime());
        holder.sex.setText(dataBean.getAge()+"");
        holder.content.setText(dataBean.getShort_desc());
        if (dataBean.getSex() == 1) {
            holder.layout.setBackgroundResource(R.drawable.sex_man);
            Glide.with(context).load(R.drawable.sign_mal2x).into(holder.sexIcon);
        }
        if (dataBean.getSex() == 2) {
            holder.layout.setBackgroundResource(R.drawable.sex_woman);
            Glide.with(context).load(R.drawable.teacher_pic_femal2x).into(holder.sexIcon);
        }
        Glide.with(context).load(dataBean.getAvatar()).into(holder.avatar);
        final List<String> icons = dataBean.getIcons();
        holder.lis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherMainActivity.show((Activity) context,dataBean.getTeacherid(),dataBean.getOnline());

//                TeacherServiceDetialActivity.shows(dataBean.getTeacherid(),Integer.valueOf(dataBean.getTeacherInfoId()), context,dataBean.getOnline());
            }
        });
        ItemAdapter adapter=new ItemAdapter(context,icons);
        holder.icons.setAdapter(adapter);
        }catch (Exception e){

        }
        return convertView;
    }

    private void setTvColor(String str , String s , TextView tv){
        String string = str.replaceAll(s,"<font color='#FABD00'>" + s + "</font>");
        tv.setText(Html.fromHtml(string));
    }

    class ViewHolder {
        TextView name;
        TextView time;
        ImageView avatar;
        ImageView sexIcon;
        TextView sex;
        TextView content;
        GridView icons;
        View layout;
        View lis;
    }
}
