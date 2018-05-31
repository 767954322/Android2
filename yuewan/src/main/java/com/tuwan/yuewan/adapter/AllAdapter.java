package com.tuwan.yuewan.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.AllOrderBean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by TUWAN on 2017/11/27.
 */

public class AllAdapter extends BaseAdapter {
    List<AllOrderBean.DataBean> list = new ArrayList();
    Context context;
    private ViewHolder holder;


    private int flag;
    private int status;
    private long countdownTime;//倒计时的总时间(单位:毫秒)
    private String timefromServer;//从服务器获取的订单生成时间
    private long chaoshitime;//从服务器获取订单有效时长(单位:毫秒)
    private Handler handler;
    private Runnable runnable;
    private int startTime;
    private int endTime;

    public AllAdapter(List<AllOrderBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
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
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.order_item, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.order_icon);
            holder.icon_teacher = (ImageView) convertView.findViewById(R.id.orderlist_teacher_icon);
            holder.name = (TextView) convertView.findViewById(R.id.order_name);
            holder.uid = (TextView) convertView.findViewById(R.id.order_uid);
            holder.timer = (TextView) convertView.findViewById(R.id.order_time);
            holder.money = (TextView) convertView.findViewById(R.id.order_money);
            holder.state1 = (TextView) convertView.findViewById(R.id.order_state1);
            holder.state2 = (Button) convertView.findViewById(R.id.order_state2);
            holder.state3 = (Button) convertView.findViewById(R.id.order_state3);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.order_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AllOrderBean.DataBean allOrderBean = list.get(position);

        flag = allOrderBean.getFlag();
        status = allOrderBean.getStatus();
        startTime = allOrderBean.getStartTime();
        endTime = allOrderBean.getEndTime();
        if (status == 2) {
            String sb = allOrderBean.getPrice() + "";
            String moneyss = sb.substring(0, 2);
            String startDate = allOrderBean.getStartTime() + "";
            String endDate = allOrderBean.getEndTime() + "";
            String xd = allOrderBean.getTime() + "";
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            System.out.println(df.format(new Date()));//
            try {
                Date d1 = format2.parse(chargeSecondsToNowTime(endDate));
                Date d2 = format2.parse(chargeSecondsToNowTime(startDate));

           /*  days + "天" + hours + "小时" + minutes + "分"*/
                long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
                long days = diff / (1000 * 60 * 60 * 24);
                long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                holder.timer.setText(chargeSecondsToNowTime(startDate) + "-" + chargeSecondsToNowTime2(endDate) + "(" + hours + "小时)");


            } catch (Exception e) {
                Log.e("onClick: ", e.toString());
            }
        /*
        flag   订单类型 1 用户订单 2导师订单
        status 状态码
        用户
        0 已付款（等待接单）
        1 已接单（包含等待接单,正在进行中和已经结束）
        2 已完成
                -2 付款后取消（导师未接单）
        -2001 未付款取消
                -2000（大于15分钟就是未取消付款，小于15分钟就是去支付）
        -1001 导师接单后用户退款
         导师
        0 已付款（接单）
        1 已接单（判断当前时间在开始时间和结束时间之内的是正在进行，超过结束时间的是已结束）
        2 已完成(判断是否评论 如果没有评论则可以展示评论)
                -2000（当前时间减去下单时间小于900秒就是去支付，大于900秒就是取消订单）
        -2001 未付款取消*/

            if (flag == 1) {
                holder.money.setText("未支付：" + moneyss + "元");
//                if (status == -2001) {
                holder.money.setText("已支付：" + moneyss + "元");
                holder.state3.setVisibility(View.GONE);
                holder.state2.setVisibility(View.GONE);
                holder.state1.setVisibility(View.VISIBLE);
                holder.state1.setText("已取消");
//                } else if (status == -1001) {
//                    holder.money.setText("未支付：" + moneyss + "元");
//                    holder.state3.setVisibility(View.GONE);
//                    holder.state2.setVisibility(View.GONE);
//                    holder.state1.setVisibility(View.VISIBLE);
//                    holder.state1.setText("已取消");
//                } else if (status == -2000) {
//
//                    Date d3 = null;
//                    try {
//                        //下单时间
//                        d3 = format2.parse(chargeSecondsToNowTime3(xd));
//                        //new Date()为获取当前系统时间
//                        Date d4 = format2.parse((df.format(new Date())));
//                        long diff2 = d4.getTime() - d3.getTime();//这样得到的差值是微秒级别
//                        long days2 = diff2 / (1000 * 60 * 60 * 24);
//                        long hours2 = (diff2 - days2 * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
//                        long minutes = (diff2 - days2 * (1000 * 60 * 60 * 24) - hours2 * (1000 * 60 * 60)) / (1000 * 60);
//                        Log.e("下单过去: ", days2 + "天" + hours2 + "时" + minutes + "分");
//                        if (minutes > 15 & hours2 != 0) {
//                            holder.state3.setVisibility(View.GONE);
//                            holder.state2.setVisibility(View.GONE);
//                            holder.state1.setVisibility(View.VISIBLE);
//                            holder.state1.setText("已取消");
//                        } else if (minutes < 15 & hours2 == 0) {
//                            try {
//                                handler = new Handler();
//                                final Handler finalHandler = handler;
//                                //倒计时总时间减1
//                                //格式化倒计时的总时间
//                                runnable = new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        countdownTime -= 1000;//倒计时总时间减1
//                                        SimpleDateFormat minforamt = new SimpleDateFormat("mm:ss");
//                                        String hms = minforamt.format(countdownTime);//格式化倒计时的总时间
//                                        holder.state1.setVisibility(View.GONE);
//                                        holder.state2.setVisibility(View.GONE);
//                                        holder.state3.setVisibility(View.VISIBLE);
//                                        holder.state3.setText("去支付" + hms);
//                                        finalHandler.postDelayed(this, 1000);
//                                    }
//                                };
//                                getTimeDuring(chargeSecondsToNowTime3(xd));
//                            } catch (Exception e) {
//                                Log.e("getView: ", "已取消");
//                            }
//                        }
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    holder.money.setText("未支付：" + moneyss + "元");
//                } else if (status == 0) {
//                    holder.money.setText("已支付：" + moneyss + "元");
//                    holder.state2.setVisibility(View.GONE);
//                    holder.state3.setVisibility(View.GONE);
//                    holder.state1.setVisibility(View.VISIBLE);
//                    holder.state1.setText("已付款");
//                } else if (status == 1) {
//                    holder.money.setText("已支付：" + moneyss + "元");
//                    holder.state1.setText("已接单");
//                    holder.state1.setVisibility(View.VISIBLE);
//                    holder.state2.setVisibility(View.GONE);
//                    holder.state1.setVisibility(View.GONE);
//                } else if (status == 2) {
//                    holder.money.setText("已支付：" + moneyss + "元");
//                    holder.state3.setText("马上评价");
//                    holder.state2.setText("再来一单");
//                    holder.state1.setText("已完成");
//                    holder.state1.setVisibility(View.VISIBLE);
//                    holder.state2.setVisibility(View.VISIBLE);
//                    holder.state3.setVisibility(View.VISIBLE);
//                } else if (status == -2) {
//                    holder.money.setText("已支付：" + moneyss + "元");
//                    holder.state3.setVisibility(View.GONE);
//                    holder.state2.setVisibility(View.GONE);
//                    holder.state1.setText("已取消");
//                    holder.state1.setVisibility(View.VISIBLE);
//                }
//                holder.icon_teacher.setVisibility(View.GONE);

            } else if (flag == 2) {
                holder.icon_teacher.setVisibility(View.GONE);
                holder.money.setText("已到账：" + moneyss + "元");
                holder.state3.setVisibility(View.GONE);
                holder.state2.setVisibility(View.GONE);
                holder.state1.setVisibility(View.VISIBLE);
                holder.state1.setText("已取消");
            }
            Glide.with(context).load(allOrderBean.getAvatar()).into(holder.icon);
            holder.name.setText(allOrderBean.getDtidname());
            holder.uid.setText("导师：" + allOrderBean.getNickname() + "");
        } else {
            holder.layout.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView icon;
        ImageView icon_teacher;
        TextView name;
        TextView uid;
        TextView timer;
        TextView money;
        TextView state1;
        Button state2;
        Button state3;
        LinearLayout layout;
    }


    public static String chargeSecondsToNowTime(String seconds) {
        long time = Long.parseLong(seconds) * 1000;
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format2.format(new Date(time));
    }

    public static String chargeSecondsToNowTime2(String seconds) {
        long time = Long.parseLong(seconds) * 1000;
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
        return format2.format(new Date(time));
    }

    public static String chargeSecondsToNowTime3(String seconds) {
        long time = Long.parseLong(seconds) * 1000;
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format2.format(new Date(time));
    }

    private void getTimeDuring(String time) {
        chaoshitime = 15 * 60 * 1000;//应该从服务器获取
        timefromServer = time;//应该从服务器获取
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date serverDate = df.parse(timefromServer);
            long duringTime = new Date().getTime() - serverDate.getTime();//计算当前时间和从服务器获取的订单生成时间的时间差
            countdownTime = chaoshitime - duringTime;//计算倒计时的总时间
            handler.postDelayed(runnable, 1000);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
