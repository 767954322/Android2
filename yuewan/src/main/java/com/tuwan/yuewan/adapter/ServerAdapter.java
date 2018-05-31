package com.tuwan.yuewan.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.teacher.Servers;
import com.tuwan.yuewan.ui.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class ServerAdapter extends BaseAdapter {
    List<Servers.DataBean> been = new ArrayList<>();
    Context context;
    private ViewHolder holder;
    private int switchX;
    View linlay;
    private WindowManager wm;
    String cookie;
    private boolean b1;

    public ServerAdapter(List<Servers.DataBean> been, Context context, View linlay, WindowManager wm, String cookie) {
        this.been = been;
        this.context = context;
        this.linlay = linlay;
        this.wm = wm;
        this.cookie = cookie;
    }

    private OnItemClickleteners onItemClickleteners;

    public void setOnItemClickleteners(OnItemClickleteners onItemClickleteners) {
        this.onItemClickleteners = onItemClickleteners;
    }

    public interface OnItemClickleteners {
        void onClicks(int pos, int po);
        void onDJ(int pos, int po);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.myservice, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.my_name);
            holder.money_more = (TextView) convertView.findViewById(R.id.money_more);
            holder.text_danjia = (TextView) convertView.findViewById(R.id.text__nihao);
            holder.show = (SwitchButton) convertView.findViewById(R.id.show);
            holder.layout = (RelativeLayout) convertView.findViewById(R.id.money);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Servers.DataBean dataBean = been.get(position);
        holder.name.setText(dataBean.getName());
        int id = dataBean.getId();
        switchX = dataBean.getSwitchX();
//        holder.show.setBackColorRes(R.color.colorYellow);
        holder.show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.e("是否点击_1", b + "");
                if (b == true) {
                    Log.e("是否点击_1", "是");
                    b1 = b;
//                    holder.show.setBackColorRes(R.color.colorYellow);
                    //     holder.layout.setVisibility(View.);
                    been.get(position).setSwitchX(1);
                    onItemClickleteners.onClicks(position, 1);
                    notifyDataSetChanged();
                } else {
                    Log.e("是否点击_1", "否");
                    b1 = b;
//                    holder.show.setBackColorRes(R.color.colorYellow);
                    been.get(position).setSwitchX(0);
                    onItemClickleteners.onClicks(position, 0);
                    notifyDataSetChanged();
                }
            }
        });
        if (switchX == 1) {
            holder.show.setChecked(true);
            holder.layout.setVisibility(View.VISIBLE);
            if(dataBean.getName().equals("声优热线")){
                holder.money_more.setText(dataBean.getTprice() + "钻石");
                holder.text_danjia.setText("单价/分钟");
            }else if(dataBean.getName().equals("线下LOL")){
                holder.money_more.setText(dataBean.getTprice() + "元");
                holder.text_danjia.setText("单价/小时");

            }else if(dataBean.getName().equals("王者荣耀")){
                holder.money_more.setText(dataBean.getTprice() + "元");
                holder.text_danjia.setText("单价/每局");

            }
            else if(dataBean.getName().equals("线上歌手")){
                holder.money_more.setText(dataBean.getTprice() + "元");
                holder.text_danjia.setText("单价/每首");

            }
            else if(dataBean.getName().equals("声优聊天")){
                holder.money_more.setText(dataBean.getTprice() + "元");
                holder.text_danjia.setText("单价/小时");

            }
            else if(dataBean.getName().equals("ASMR")){
                holder.money_more.setText(dataBean.getTprice() + "元");
                holder.text_danjia.setText("单价/小时");

            }
            else if(dataBean.getName().equals("线上LOL")){
                holder.money_more.setText(dataBean.getTprice() + "元");
                holder.text_danjia.setText("单价/小时");

            }  else if(dataBean.getName().equals("绝地求生")){
                holder.money_more.setText(dataBean.getTprice() + "元");
                holder.text_danjia.setText("单价/小时");

            }
            else if(dataBean.getName().equals("荒野行动")){
                holder.money_more.setText(dataBean.getTprice() + "元");
                holder.text_danjia.setText("单价/小时");

            }
            else if(dataBean.getName().equals("视频聊天")){
                holder.money_more.setText(dataBean.getTprice() + "元");
                holder.text_danjia.setText("单价/小时");

            }
            else if(dataBean.getName().equals("虚拟恋人")){
                holder.money_more.setText(dataBean.getTprice() + "元");
                holder.text_danjia.setText("单价/小时");

            }
            else if(dataBean.getName().equals("哄睡觉")){
                holder.money_more.setText(dataBean.getTprice() + "元");
                holder.text_danjia.setText("单价/小时");

            }
            else if(dataBean.getName().equals("叫醒")){
                holder.money_more.setText(dataBean.getTprice() + "元");
                holder.text_danjia.setText("单价/每次");

            }
            notifyDataSetChanged();
        } else if (switchX == 0) {
            holder.show.setChecked(false);
            holder.layout.setVisibility(View.GONE);
            notifyDataSetChanged();
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickleteners.onDJ(position,0);
//                View view = View.inflate(context, R.layout.my_pop_money, null);
//                final int width = wm.getDefaultDisplay().getWidth();
//                int height = wm.getDefaultDisplay().getHeight();
//                final PopupWindow window = new PopupWindow(view, width, height + 700);
//                // 设置PopupWindow的背景
//                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                // 设置PopupWindow是否能响应外部点击事件
//                window.setOutsideTouchable(true);
////                window.showAsDropDown(linlay, 0, 0);
//                window.showAtLocation();
//                GridView my_pop_grid = (GridView) view.findViewById(R.id.my_pop_grid);
//                TextView pop_actual = (TextView) view.findViewById(R.id.pop_actual);
//                Button money_sore = (Button) view.findViewById(R.id.money_sore);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView name;
        SwitchButton show;
        RelativeLayout layout;
        TextView money_more;
        TextView text_danjia;
    }

}
