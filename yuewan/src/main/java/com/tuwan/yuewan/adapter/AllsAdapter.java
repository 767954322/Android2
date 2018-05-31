package com.tuwan.yuewan.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.AllOrderBean;
import com.tuwan.yuewan.entity.Codes;
import com.tuwan.yuewan.entity.MakeOrderRecentOrderBean;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.activity.MakeOrderActivity;
import com.tuwan.yuewan.ui.activity.PayActivity;
import com.tuwan.yuewan.ui.widget.OrderManager;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.Urls;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by TUWAN on 2017/11/29.
 */

public class AllsAdapter extends RecyclerView.Adapter<AllsAdapter.ViewHolder> {

    OrderManager orderManager = OrderManager.getInstance();
    List<AllOrderBean.DataBean> list = new ArrayList();
    Context context;
    private LinearLayout layout1;
    private MakeOrderRecentOrderBean mHttpResult;
    private ImageView image_pingjia;
    private int flag;
    private int status;
    private ItemClickCallBack clickCallBack;
    private long during;
    private CountDownTimer timeCount;
    private View img_pop;
    private int page;
    private int pjz=0;
    HashMap<String, String> map;
    long l = System.currentTimeMillis();
    int time = (int) (l / 1000);
    public View view1;
    private int pos;
    public CountDownTimer downTimer;
    public CountDownTimer downTimer1;
    public CountDownTimer downTimer2;
    private int nowServerTime = 0;
    public void setNowTime(int ntime){
        this.nowServerTime = ntime;
    }

    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ItemClickCallBack {
        void onItemClick(int pos);

        void onRefresh();
    }

    public AllsAdapter(List<AllOrderBean.DataBean> list, Context context, View view1) {
        this.list = list;
        this.context = context;
        this.img_pop = view1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.order_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AllOrderBean.DataBean allOrderBean = list.get(position);

        holder.money.setTag(position);
        flag = allOrderBean.getFlag();
        status = allOrderBean.getStatus();
        final String moneyss = ((float) allOrderBean.getPrice() / 100) + "";
        String startDate = allOrderBean.getStartTime() + "";
        String endDate = allOrderBean.getEndTime() + "";
        String xd = allOrderBean.getTime() + "";
        String typeflag = allOrderBean.getTypeflag();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date d1 = format2.parse(chargeSecondsToNowTime(endDate));
            Date d2 = format2.parse(chargeSecondsToNowTime(startDate));

           /*  days + "天" + hours + "小时" + minutes + "分"*/
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            holder.timer.setText(chargeSecondsToNowTime(startDate) + "-" + chargeSecondsToNowTime2(endDate) + "(" + hours + typeflag + ")");

        } catch (Exception e) {
            Log.e("onClick: ", e.toString());
        }
        holder.state3.setTag(position);
        /*
        flag   订单类型 1 用户订单 2导师订单
        status 状态码
        用户
        0 已付款（等待接单）
        1 已接单（包含等待接单,正在进行中和已经结束）
        2 已完成
                -2 付款后取消（导师未接单）
        -2001 未付款取消
                -2000（大于10分钟就是未取消付款，小于10分钟就是去支付）
        -1001 导师接单后用户退款
         导师
        0 已付款（接单）
        1 已接单（判断当前时间在开始时间和结束时间之内的是正在进行，超过结束时间的是已结束）
        2 已完成(判断是否评论 如果没有评论则可以展示评论)
                -2000（当前时间减去下单时间小于900秒就是去支付，大于900秒就是取消订单）
        -2001 未付款取消*/

        //用户订单          自身是用户对方是导师

        if (flag == 1) {
            holder.uid.setText("导师：" + allOrderBean.getNickname() + "");
            if (status == -2001) {      //自己没付款到时间后自动取消了
                holder.money.setText("未支付：" + moneyss + "元");
                holder.state3.setVisibility(View.GONE);
                holder.state2.setVisibility(View.GONE);
                holder.state1.setVisibility(View.GONE);
                holder.tv_order_money.setText("已取消");
//                holder.state1.setText("已取消");
                setTextColor(holder.tv_order_money, 0);
            } else if (status == -1001 || status == -1 || status == -2) {       //付款后取消的
                holder.money.setText("己支付：" + moneyss + "元");
                holder.state3.setVisibility(View.GONE);
                holder.state2.setVisibility(View.GONE);
                holder.state1.setVisibility(View.GONE);
                holder.tv_order_money.setText("已取消");
                setTextColor(holder.tv_order_money, 0);
            } else if (status == -2000) {       //下好单了,大于15分钟就是已取消，小于15分钟就是去支付,点击去付款
                holder.money.setText("未支付：" + moneyss + "元");
                if (orderManager.isCancelOrdor()) {
                    holder.state3.setVisibility(View.GONE);
                    holder.state2.setVisibility(View.GONE);
                    holder.state1.setVisibility(View.GONE);
                    holder.tv_order_money.setText("已取消");
                    setTextColor(holder.tv_order_money, 0);
                } else {
                    //time 下单时间  nowTime 系统现在时间
                    int time = list.get(position).getTime();
                    String str = System.currentTimeMillis() + "";
                    String s = str.substring(0, 10);
                    int nowTime = nowServerTime;// Integer.parseInt(s);
                    int i = nowTime - time;
                    during = i;
                    if (i < 600) {
                        final Handler startTimehandler = new Handler() {
                            public void handleMessage(android.os.Message msg) {
                                if (holder.money.getTag().equals(msg.what)) {
                                    holder.state3.setVisibility(View.VISIBLE);
                                    holder.state2.setVisibility(View.GONE);
                                    holder.state1.setVisibility(View.GONE);
                                    holder.state3.setText((String) msg.obj);
                                    holder.state3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {//跳转到支付界面
                                            PayActivity.action(list.get(position).getTradeno(), (Activity) context);
                                        }
                                    });
                                }
                            }
                        };

                        downTimer1 = new CountDownTimer((600 - i) * 1000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                long s = millisUntilFinished / 1000;
                                long k = s / 60;
                                s = s % 60;
                                long m = s;
                                holder.tv_order_money.setText("待支付");
                                String timeFormat = "去支付(还剩" + k + "分" + m + "秒)";
                                Message msg = new Message();
                                msg.obj = timeFormat;
                                msg.what = position;
                                startTimehandler.sendMessage(msg);
                            }

                            public void onFinish() {
                                holder.state3.setVisibility(View.GONE);
                                holder.state2.setVisibility(View.GONE);
                                holder.state1.setVisibility(View.GONE);
                                holder.tv_order_money.setText("已取消");
                                setTextColor(holder.tv_order_money, 0);
                            }
                        }.start();
                    } else {
                        holder.state3.setVisibility(View.GONE);
                        holder.state2.setVisibility(View.GONE);
                        holder.state1.setVisibility(View.GONE);
                        holder.tv_order_money.setText("已取消");
                        setTextColor(holder.tv_order_money, 0);
                    }
                }
            } else if (status == 0) { //付过款了,等待接单
                final long time = (long) list.get(position).getBookTime() * 1000;
                holder.money.setText("已支付：" + moneyss + "元");

                int times = list.get(position).getTime();
                String str = System.currentTimeMillis() + "";
                String s = str.substring(0, 10);
                int nowTimes = Integer.parseInt(s);
                int is = nowTimes - times;
                during = is;

                final Handler startTimehandler = new Handler() {
                    public void handleMessage(android.os.Message msg) {
                        if (holder.money.getTag().equals(msg.what)) {
                            holder.state3.setVisibility(View.VISIBLE);
                            holder.state2.setVisibility(View.GONE);
                            holder.state1.setVisibility(View.GONE);
                            holder.state3.setText((String) msg.obj);
                        }
                    }
                };
                //                        holder.money.setText("己支付：" + moneyss + "元");
                downTimer2 = new CountDownTimer((1800 - is) * 1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        long s = millisUntilFinished / 1000;
                        long k = s / 60;
                        s = s % 60;
                        long m = s;
                        String timeFormat = "等待接单(" + k + "分" + m + "秒)";
                        Message msg = new Message();
                        msg.obj = timeFormat;
                        msg.what = position;
                        startTimehandler.sendMessage(msg);
                    }

                    @Override
                    public void onFinish() {
//                        holder.money.setText("己支付：" + moneyss + "元");
                        holder.state3.setVisibility(View.GONE);
                        holder.state2.setVisibility(View.GONE);
                        holder.state1.setVisibility(View.GONE);
                        holder.tv_order_money.setText("已取消");
                        setTextColor(holder.tv_order_money, 0);
                    }
                }.start();
            } else if (status == 1) {       //导师已接单,有三个状态: 1等待接单 2正在进行 3已经结束
                holder.money.setText("已支付：" + moneyss + "元");
                int endTime = list.get(position).getEndTime();
                int startTime = list.get(position).getStartTime();
                if (startTime > time) {
                    holder.state2.setVisibility(View.GONE);
                    holder.state1.setVisibility(View.GONE);
//                    holder.state1.setText("己接单");
                    holder.state3.setVisibility(View.VISIBLE);
                    holder.state3.setText("完成订单");
                    holder.tv_order_money.setText("己接单");
                    setTextColor(holder.tv_order_money, 1);
                    holder.state3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initPingJias(position);//此处应该是直接弹出评价及后续操作
                        }
                    });
                } else if (endTime > time && time >= startTime) {
                    holder.state2.setVisibility(View.GONE);
                    holder.state1.setVisibility(View.GONE);
//                    holder.state1.setText("进行中...");
                    holder.tv_order_money.setText("进行中...");
                    holder.state3.setVisibility(View.VISIBLE);
                    holder.state3.setText("完成订单");
                    holder.state3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initPingJias(position);//此处应该是直接弹出评价及后续操作
                        }
                    });
                    setTextColor(holder.tv_order_money, 2);
                } else {
                    holder.state1.setVisibility(View.GONE);
                    holder.state2.setVisibility(View.VISIBLE);
                    holder.state3.setVisibility(View.VISIBLE);
                    holder.state3.setText("马上评价");
                    holder.state2.setText("再来一单");
                    holder.tv_order_money.setText("已完成");
                    setTextColor(holder.tv_order_money, 0);
                    holder.state2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {       //再来一单
//                            initPingJias(position);
                            ServiceFactory.getNoCacheInstance()
                                    .createService(YService.class)
                                    .getRecentOrderApi_Order("json", "app", allOrderBean.getTeacherinfoID())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new CommonObserver<MakeOrderRecentOrderBean>() {
                                        @Override
                                        public void onNext(@NonNull MakeOrderRecentOrderBean result) {
                                            super.onNext(result);
                                            Log.d("test2", result.toString());
                                            if (result.code == 1) {
                                                mHttpResult = result;
                                                //从服务页打开会显示初始化数据
                                                MakeOrderActivity.show(list.get(position).getTeacherinfoID(), true, (Activity) context);

                                            } else {
                                                ToastUtils.getInstance().showToast("服务已下线，请选别的服务。");
                                            }
                                        }

                                        @Override
                                        public void onError(@NonNull Throwable e) {
                                            super.onError(e);
                                        }
                                    });
                        }
                    });
                    //评价弹出
                    holder.state3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {       //马上评价
                            initPingJias(position);
                        }
                    });
                }
            } else if (status == 2) {
                holder.money.setText("已支付：" + moneyss + "元");
                holder.state1.setVisibility(View.GONE);
                holder.state2.setVisibility(View.VISIBLE);
                holder.state3.setVisibility(View.GONE);
//                holder.state3.setText("马上评价");
//                holder.state2.setText("再来一单");
                holder.tv_order_money.setText("已完成");
                holder.state2.setText("再来一单");
                holder.state2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {       //再来一单
//                            initPingJias(position);
                        ServiceFactory.getNoCacheInstance()
                                .createService(YService.class)
                                .getRecentOrderApi_Order("json", "app", allOrderBean.getTeacherinfoID())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CommonObserver<MakeOrderRecentOrderBean>() {
                                    @Override
                                    public void onNext(@NonNull MakeOrderRecentOrderBean result) {
                                        super.onNext(result);
                                        Log.d("test2", result.toString());
                                        if (result.code == 1) {
                                            mHttpResult = result;
                                            //从服务页打开会显示初始化数据
                                            MakeOrderActivity.show(list.get(position).getTeacherinfoID(), true, (Activity) context);
                                        } else {
                                            ToastUtils.getInstance().showToast("服务已下线，请选别的服务。");
                                        }
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        super.onError(e);
                                    }
                                });
                    }
                });
                setTextColor(holder.tv_order_money, 0);

            }
            holder.icon_teacher.setVisibility(View.GONE);

        } else if (flag == 2) {     //自身是导师,对方是用户
            holder.uid.setText("用户：" + allOrderBean.getNickname() + "");
            holder.icon_teacher.setVisibility(View.VISIBLE);
            holder.state3.setVisibility(View.GONE);
            holder.state2.setVisibility(View.GONE);

            if (position == pos) {
                holder.state2s.setVisibility(View.GONE);
                holder.state3s.setVisibility(View.GONE);
                holder.state1.setVisibility(View.GONE);
                holder.tv_order_money.setText("已完成");
                setTextColor(holder.tv_order_money, 0);
            }
            //已付款 接单
            if (status == 0) {      //用户已付款
                if (orderManager.isCancelOrdor()) {     //用户取消订单了
                    holder.money.setText("预收入：" + moneyss + "元");
                    holder.state3s.setVisibility(View.GONE);
                    holder.state2s.setVisibility(View.GONE);
                    holder.state1.setVisibility(View.GONE);
                    holder.tv_order_money.setText("已取消");
                    setTextColor(holder.tv_order_money, 0);
                } else {            //还有多久接单时间
                    int time = list.get(position).getTime();
                    String str = System.currentTimeMillis() + "";
                    String s = str.substring(0, 10);
                    int nowTime = Integer.parseInt(s);
                    int i = nowTime - time;
                    during = i;
                    if (i < 1800) {
                        holder.money.setText("预收入：" + moneyss + "元");
                        final Handler startTimehandler = new Handler() {
                            public void handleMessage(android.os.Message msg) {
                                if (holder.money.getTag().equals(msg.what)) {
                                    holder.state3s.setVisibility(View.VISIBLE);
                                    holder.state2s.setVisibility(View.GONE);
                                    holder.state1.setVisibility(View.GONE);
                                    holder.state3s.setText((String) msg.obj);
                                    holder.state3s.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
//                                            Log.e("4801111", "点击弹出接单提示");
                                            initJieDans(position);
                                        }
                                    });
                                }
                            }
                        };

                        downTimer = new CountDownTimer((1800 - i) * 1000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                long s = millisUntilFinished / 1000;
                                long k = s / 60;
                                s = s % 60;
                                long m = s;

                                String timeFormat = "接单(还剩" + k + "分" + m + "秒)";
                                Message msg = new Message();
                                msg.obj = timeFormat;
                                msg.what = position;
                                startTimehandler.sendMessage(msg);
                            }

                            public void onFinish() {
                                holder.money.setText("己支付：" + moneyss + "元");
                                holder.state3s.setVisibility(View.GONE);
                                holder.state2s.setVisibility(View.GONE);
                                holder.tv_order_money.setVisibility(View.VISIBLE);
                                holder.tv_order_money.setText("已取消");
                                setTextColor(holder.tv_order_money, 0);
                            }
                        }.start();
                    } else {
                        holder.money.setText("己支付：" + moneyss + "元");
                        holder.state3s.setVisibility(View.VISIBLE);
                        holder.state2s.setVisibility(View.GONE);
                        holder.tv_order_money.setVisibility(View.GONE);
                        holder.tv_order_money.setText("已取消");
                        setTextColor(holder.tv_order_money, 0);
                    }
                }
            } else if (status == 1) {       //已经接单了
                //1 已接单（判断当前时间在开始时间和结束时间之内的是正在进行，超过结束时间的是已结束）

                int endTime = list.get(position).getEndTime();
                int startTime = list.get(position).getStartTime();
                holder.money.setText("预收入：" + moneyss + "元");

                if (startTime > time) {
                    holder.state3s.setVisibility(View.GONE);
                    holder.state2s.setVisibility(View.GONE);
                    holder.tv_order_money.setVisibility(View.VISIBLE);
                    holder.tv_order_money.setText("己接单");
                    setTextColor(holder.tv_order_money, 1);
                } else if (endTime > time && time >= startTime) {
                    holder.state3s.setVisibility(View.GONE);
                    holder.state2s.setVisibility(View.GONE);
                    holder.tv_order_money.setVisibility(View.VISIBLE);
                    holder.tv_order_money.setText("进行中...");
                    setTextColor(holder.tv_order_money, 2);
                } else {
                    holder.state3s.setVisibility(View.GONE);
                    holder.state2s.setVisibility(View.GONE);
                    holder.tv_order_money.setVisibility(View.VISIBLE);
                    holder.tv_order_money.setText("已结束");
                    setTextColor(holder.tv_order_money, 0);
                }
            } else if (status == 2) {       //订单完成了,钱已经到账了
                holder.money.setText("已到账：" + moneyss + "元");
                holder.state3s.setVisibility(View.GONE);
                holder.state2s.setVisibility(View.GONE);
                holder.tv_order_money.setVisibility(View.VISIBLE);
                holder.tv_order_money.setText("已完成");
                setTextColor(holder.tv_order_money, 0);
            } else {
                holder.money.setText("己支付：" + moneyss + "元");
                holder.state3s.setVisibility(View.GONE);
                holder.state2s.setVisibility(View.GONE);
                holder.tv_order_money.setVisibility(View.VISIBLE);
                holder.tv_order_money.setText("已取消");
                setTextColor(holder.tv_order_money, 0);
            }
        }
        Glide.with(context).load(allOrderBean.getAvatar()).into(holder.icon);
        holder.name.setText(allOrderBean.getDtidname());
        holder.order_item.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickCallBack != null) {
                            clickCallBack.onItemClick(position);
                        }
                    }
                }
        );
    }

    //接单通知
    private void initJieDans(final int position) {
        View popupView = View.inflate(context, R.layout.order_receiving, null);
        // 弹出自定义dialog
        LayoutInflater inflater = LayoutInflater.from(context);

        // 对话框
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        // 设置宽度为屏幕的宽度
//                WindowManager windowManager = context.getWindowManager();
//                Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (PercentLinearLayout.LayoutParams.MATCH_PARENT); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(popupView);
        ImageView close = (ImageView) popupView.findViewById(R.id.popup_close2);
        Button qx = (Button) popupView.findViewById(R.id.receiving_qx);
        Button jd = (Button) popupView.findViewById(R.id.receiving_jd);
        qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        jd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initJieDan(position);
                dialog.cancel();
            }
        });
    }

    //开始接单
    private void initJieDan(final int position) {
        map = new HashMap<>();
        map.put("format", "json");
        map.put("order", list.get(position).getTradeno());
        OkManager.getInstance().getSendGift(context, Urls.TEACHER_JIEDAN, map, true, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("接单失败", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ss = response.body().string();
                Log.e("接单成功", ss.toString());
                list.get(position).setStatus(1);
                han.sendEmptyMessage(898);
            }
        });
    }

    //评价
    private void initPingJias(final int position) {
        View popupView = View.inflate(context, R.layout.oreder_appraise, null);
        LayoutInflater inflater = LayoutInflater.from(context);
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (PercentLinearLayout.LayoutParams.MATCH_PARENT); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(popupView);
        ImageView close = (ImageView) popupView.findViewById(R.id.popup_close2);
        final ImageView image1 = (ImageView) popupView.findViewById(R.id.image1);
        final ImageView image2 = (ImageView) popupView.findViewById(R.id.image2);
        final ImageView image3 = (ImageView) popupView.findViewById(R.id.image3);
        final ImageView image4 = (ImageView) popupView.findViewById(R.id.image4);
        final ImageView image5 = (ImageView) popupView.findViewById(R.id.image5);
        final EditText edit = (EditText) popupView.findViewById(R.id.edit);
        Button pop_know = (Button) popupView.findViewById(R.id.pop_know);
        layout1 = (LinearLayout) popupView.findViewById(R.id.layout1);
        image_pingjia = (ImageView) popupView.findViewById(R.id.image_pingjia);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                image1.setImageResource(R.drawable.teacherappraise_star_light2x);
                image2.setImageResource(R.drawable.teacherappraise_star_grey2x);
                image3.setImageResource(R.drawable.teacherappraise_star_grey2x);
                image4.setImageResource(R.drawable.teacherappraise_star_grey2x);
                image5.setImageResource(R.drawable.teacherappraise_star_grey2x);
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 2;
                image1.setImageResource(R.drawable.teacherappraise_star_light2x);
                image2.setImageResource(R.drawable.teacherappraise_star_light2x);
                image3.setImageResource(R.drawable.teacherappraise_star_grey2x);
                image4.setImageResource(R.drawable.teacherappraise_star_grey2x);
                image5.setImageResource(R.drawable.teacherappraise_star_grey2x);
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 3;
                image1.setImageResource(R.drawable.teacherappraise_star_light2x);
                image2.setImageResource(R.drawable.teacherappraise_star_light2x);
                image3.setImageResource(R.drawable.teacherappraise_star_light2x);
                image4.setImageResource(R.drawable.teacherappraise_star_grey2x);
                image5.setImageResource(R.drawable.teacherappraise_star_grey2x);
            }
        });
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 4;
                image1.setImageResource(R.drawable.teacherappraise_star_light2x);
                image2.setImageResource(R.drawable.teacherappraise_star_light2x);
                image3.setImageResource(R.drawable.teacherappraise_star_light2x);
                image4.setImageResource(R.drawable.teacherappraise_star_light2x);
                image5.setImageResource(R.drawable.teacherappraise_star_grey2x);
            }
        });
        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 5;
                image1.setImageResource(R.drawable.teacherappraise_star_light2x);
                image2.setImageResource(R.drawable.teacherappraise_star_light2x);
                image3.setImageResource(R.drawable.teacherappraise_star_light2x);
                image4.setImageResource(R.drawable.teacherappraise_star_light2x);
                image5.setImageResource(R.drawable.teacherappraise_star_light2x);
            }
        });
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pjz=1;
                image_pingjia.setImageResource(R.drawable.icon_name_choose);
                nmpj();
            }
        });
        pop_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.requestFocus();/*获取焦点*/
                String edits = edit.getText().toString();
                Log.e("yzshshhssh",pjz+"");
                final String url = "https://y.tuwan.com/m/Order/getAddCommentApi?format=json&order=" + list.get(position).getTradeno() + "&decs=" + edits + "&star=" + page + "&type="+pjz+"";
                SharedPreferences preferences = context.getSharedPreferences("infos", Context.MODE_PRIVATE);
                String cookie = preferences.getString("Cookie", null);
                OkManager.getInstance().getString(url, cookie, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
//                        Log.e("yzshshhssh",response+"");
//                        Log.e("yzshshhssh",result+"");
                        try {


                        Gson gson = new Gson();
                        Codes codes = gson.fromJson(result, Codes.class);

                        if (codes.getCode() == 0) {
                            pos = position;
                            han.sendEmptyMessage(798);
                            dialog.cancel();
//                            clickCallBack.onRefresh();
                        }
                        }catch (Exception e){

                        }
                    }

                });
            }
        });
    }

    private Handler han = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 798:
                    list.get(pos).setStatus(2);
                    initPJOK();
                    notifyDataSetChanged();
                    break;
                case 898:
                    initJDOK();
                    break;
                case 998:
                    notifyDataSetChanged();
                    break;
                case 1098:
                    notifyDataSetChanged();
                    break;
            }
        }
    };

    //接单反馈
    private void initJDOK() {
        View popupView = View.inflate(context, R.layout.cancel_pop, null);
        // 对话框
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            dialog.show();

        }catch (Exception e){

        }
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (PercentLinearLayout.LayoutParams.MATCH_PARENT); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(popupView);
        ImageView popup_close3 = (ImageView) popupView.findViewById(R.id.popup_close3);
        Button pop_know2 = (Button) popupView.findViewById(R.id.pop_know2);
        TextView kq = (TextView) popupView.findViewById(R.id.pop_kq);
        TextView js = (TextView) popupView.findViewById(R.id.pop_js);
        kq.setText("接单成功");
        js.setVisibility(View.GONE);
        popup_close3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                clickCallBack.onRefresh();
                downTimer.cancel();
                han.sendEmptyMessage(1098);
                dialog.cancel();
            }
        });
        pop_know2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                clickCallBack.onRefresh();
                downTimer.cancel();
                han.sendEmptyMessage(1098);
                dialog.cancel();
            }
        });
    }

    //评价反馈
    private void initPJOK() {//cancel_pop
        View popupView = View.inflate(context, R.layout.cancel_pop, null);
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (PercentLinearLayout.LayoutParams.MATCH_PARENT); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(popupView);
        ImageView popup_close3 = (ImageView) popupView.findViewById(R.id.popup_close3);
        Button pop_know2 = (Button) popupView.findViewById(R.id.pop_know2);
        TextView kq = (TextView) popupView.findViewById(R.id.pop_kq);
        TextView js = (TextView) popupView.findViewById(R.id.pop_js);
        kq.setText("评价成功");
        js.setVisibility(View.GONE);
        popup_close3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                clickCallBack.onRefresh();
                han.sendEmptyMessage(1098);
                dialog.cancel();
            }
        });
        pop_know2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                clickCallBack.onRefresh();
                han.sendEmptyMessage(1098);
                dialog.cancel();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout order_item;
        ImageView icon;
        ImageView icon_teacher;
        TextView name;
        TextView uid;
        TextView timer;
        TextView money;
        TextView tv_order_money;
        TextView state1;
        Button state2;
        Button state3;
        Button state2s;
        Button state3s;

        public ViewHolder(View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.order_icon);
            icon_teacher = (ImageView) view.findViewById(R.id.orderlist_teacher_icon);
            name = (TextView) view.findViewById(R.id.order_name);
            uid = (TextView) view.findViewById(R.id.order_uid);
            timer = (TextView) view.findViewById(R.id.order_time);
            money = (TextView) view.findViewById(R.id.order_money);
            tv_order_money = (TextView) view.findViewById(R.id.tv_order_money);
            state1 = (TextView) view.findViewById(R.id.order_state1);
            state2 = (Button) view.findViewById(R.id.order_state2);
            state3 = (Button) view.findViewById(R.id.order_state3);
            state2s = (Button) view.findViewById(R.id.order_state2s);
            state3s = (Button) view.findViewById(R.id.order_state3s);
            order_item = (LinearLayout) view.findViewById(R.id.order_item);
        }
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

    private void setTextColor(TextView textView, int type) {
        int color = R.color.color_666666;
        if (type == 1) {
            color = R.color.color_FFB037;
        } else if (type == 2) {
            color = R.color.color_5BD896;
        }
        textView.setTextColor(context.getResources().getColor(color));
    }
    private void nmpj(){
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pjz=0;
                image_pingjia.setImageResource(R.drawable.icon_name);
                nmpj2();
            }
        });

    }
    private void nmpj2(){
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pjz=1;
                image_pingjia.setImageResource(R.drawable.icon_name_choose);
                nmpj();
            }
        });

    }
}
