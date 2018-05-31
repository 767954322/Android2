package com.tuwan.yuewan.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.ui.dialog.CustomDialogManager;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.Codes;
import com.tuwan.yuewan.entity.MakeOrderRecentOrderBean;
import com.tuwan.yuewan.entity.OrderCancel;
import com.tuwan.yuewan.entity.OrderDetails;
import com.tuwan.yuewan.entity.PayEntity;
import com.tuwan.yuewan.nim.uikit.NimUIKit;
import com.tuwan.yuewan.ui.activity.order.CancelOrderActivity;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.Urls;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.functions.Action1;

public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener {

    private String payType = "0";
    private int during;
    private String typeflag;
    private int timePrice;
    private String qq;
    private MakeOrderRecentOrderBean mHttpResult;
    private int dtid;
    private int number;
    private int mChosedGamePostion;
    private int pjz=0;
    private ImageView mImgPop;
    private ImageView mBackIcon;
    private ImageView image_pingjia;
    private ImageView mDetaiksIcon;
    private TextView mDetaiksHeader;
    private TextView mDetaiksName;
    private TextView mDetaiksZs;
    private ImageView mDetaiksLevel; 

    private RelativeLayout mRela;
    private TextView mYysm;

    private TextView mOrderNumber;
    private TextView mStartOrder;
    private TextView mPriceTitle;
    private TextView mPriceValue;
    private TextView mOrderPrice;
    private TextView mPriceIntro;
    private TextView mServicePrice;
    private TextView mOrderTime;
    private LinearLayout layout1;
    private RelativeLayout mComment;
    private TextView mCommentContent;

    private TextView mContactUser;
    private TextView mContactKefu;
    private Button mOrderStates;
    private boolean mLoop = true;

    private String tradeno;
    private String cookie = "";
    private int status = 0;
    private int flag = 0;
    HashMap<String, String> ma;
    private OrderDetails orderDetails;
    private TextView mTitle;
    HashMap<String, String> map;
    private String timeFormat;
    private Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 22:
                    mOrderStates.setText(timeFormat);
                    break;
                case 33:
                    mOrderStates.setText("已取消");
                    break;
                case 1011:
                    initDatas();
                    setStatus();
                    break;
                case 1012:
                    setStatus();
                    break;
                case 201:
                    mOrderStates.setText(timeFormat);
                    break;
                case 202:
                    mOrderStates.setText(timeFormat);
                    mOrderStates.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initJieDan();
                        }
                    });
                    break;
            }
        }
    };
    private int page;
    private RelativeLayout pr;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_order_details;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));
        Intent intent = getIntent();
        tradeno = intent.getStringExtra("tradeno");
        if (intent.hasExtra("payType")){
            payType = intent.getStringExtra("payType");
        }
        if (payType.equals("1")){
            showLeaveDialog();
        }
        initView();

        initData();
        bindClick();
    }

    private void initView() {
        TextView mBook = (TextView) findViewById(R.id.yy);
        Drawable drawable1 = getResources().getDrawable(R.drawable.appointment_icon2);
        drawable1.setBounds(0, 0, (int) getApplicationContext().getResources().getDimension(R.dimen.dimen_15), (int) getApplicationContext().getResources().getDimension(R.dimen.dimen_15));
        mBook.setCompoundDrawables(drawable1, null, null, null);//只放左边

        mOrderNumber = (TextView) findViewById(R.id.order_number);
        Drawable drawable2 = getResources().getDrawable(R.drawable.order_icon2);
        drawable2.setBounds(0, 0, (int) getApplicationContext().getResources().getDimension(R.dimen.dimen_13), (int) getApplicationContext().getResources().getDimension(R.dimen.dimen_14));
        mOrderNumber.setCompoundDrawables(drawable2, null, null, null);//只放左边

        TextView mCommentTitle = (TextView) findViewById(R.id.comment_title);
        Drawable drawable3 = getResources().getDrawable(R.drawable.commentaries_icon2);
        drawable3.setBounds(0, 0, (int) getApplicationContext().getResources().getDimension(R.dimen.dimen_14), (int) getApplicationContext().getResources().getDimension(R.dimen.dimen_13));
        mCommentTitle.setCompoundDrawables(drawable3, null, null, null);//只放左边

        mContactUser = (TextView) findViewById(R.id.contact_user);
        Drawable drawable4 = getResources().getDrawable(R.drawable.customerservice);
        drawable4.setBounds(0, 0, (int) getApplicationContext().getResources().getDimension(R.dimen.dimen_18), (int) getApplicationContext().getResources().getDimension(R.dimen.dimen_16));
        mContactUser.setCompoundDrawables(drawable4, null, null, null);//只放左边

        mContactKefu = (TextView) findViewById(R.id.contact_kefu);
        Drawable drawable5 = getResources().getDrawable(R.drawable.customerservice);
        drawable5.setBounds(0, 0, (int) getApplicationContext().getResources().getDimension(R.dimen.dimen_18), (int) getApplicationContext().getResources().getDimension(R.dimen.dimen_16));
        mContactKefu.setCompoundDrawables(drawable5, null, null, null);//只放左边

        //头部
        mBackIcon = (ImageView) findViewById(R.id.iv_titlebar_back);
        mTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        mTitle.setText("订单详情");

        mImgPop = (ImageView) findViewById(R.id.img_pop);
        mDetaiksIcon = (ImageView) findViewById(R.id.detaiks_icon);
        mDetaiksHeader = (TextView) findViewById(R.id.detaiks_header);
        mDetaiksName = (TextView) findViewById(R.id.detaiks_name);
        mDetaiksZs = (TextView) findViewById(R.id.detaiks_zs);
        mDetaiksLevel = (ImageView) findViewById(R.id.detaiks_level);

        mRela = (RelativeLayout) findViewById(R.id.rela);
        mYysm = (TextView) findViewById(R.id.yysm);


        mOrderNumber = (TextView) findViewById(R.id.order_number);
        mStartOrder = (TextView) findViewById(R.id.start_order);

        mPriceTitle = (TextView) findViewById(R.id.price_title);
        mPriceValue = (TextView) findViewById(R.id.price_value);
        mOrderPrice = (TextView) findViewById(R.id.order_price);
        mPriceIntro = (TextView) findViewById(R.id.price_intro);
        mServicePrice = (TextView) findViewById(R.id.service_price);
        mOrderTime = (TextView) findViewById(R.id.order_time);

        mComment = (RelativeLayout) findViewById(R.id.comment);
        mCommentContent = (TextView) findViewById(R.id.comment_content);

        mContactUser = (TextView) findViewById(R.id.contact_user);
        mContactKefu = (TextView) findViewById(R.id.contact_kefu);
        mOrderStates = (Button) findViewById(R.id.order_states);
        pr = (RelativeLayout) findViewById(R.id.pr);
    }

    private void bindClick() {
        RxView.clicks(mBackIcon)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
        RxView.clicks(mStartOrder)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        initEvaluate();
                    }
                });
        RxView.clicks(pr)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                            try{

//                                Observable<ServiceCommentBean> observableComment = ServiceFactory.getNoCacheInstance()
//                                        .createService(YService.class)
//                                        .getCommentListApi_Content("json", mSid, pageIndex, 10)
//                                        .subscribeOn(Schedulers.io())
//                                        .observeOn(AndroidSchedulers.mainThread());
//                                Observable<ServiceDetialBean> observableServiceDetial = ServiceFactory.getShortCacheInstance()
//                                        .createService(YService.class)
//                                        .getServiceInfo_Content("json", orderDetails.getTeacherID())
//                                        .subscribeOn(Schedulers.io());
//                                Observable.zip(observableServiceDetial, observableServiceDetial, new BiFunction<ServiceDetialBean, ServiceDetialBean, Object>() {
//                                })

                                Log.e("yzshshshshhshs",orderDetails.toString()+"");
                                if (orderDetails.getFlag() == 1) {
                                    TeacherMainActivity.show(OrderDetailsActivity.this, orderDetails.getTeacherID(),orderDetails.getFlag());
                                } else {
                                    TeacherMainActivity.show(OrderDetailsActivity.this, orderDetails.getStudentID());
                                }

                            }catch (Exception e){

                        }
                    }
                });

        //联系TA
        RxView.clicks(mContactUser)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (orderDetails.getFlag() == 1) {
                            SharedPreferences mySharedPreferences = getSharedPreferences("namess", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mySharedPreferences.edit();
                            editor.putString("names", orderDetails.getNickname());
                            editor.commit();
                            NimUIKit.startP2PSession(OrderDetailsActivity.this, orderDetails.getTeacherID() + "");
                        } else {
                            SharedPreferences mySharedPreferences = getSharedPreferences("namess", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mySharedPreferences.edit();
                            editor.putString("names", orderDetails.getNickname());
                            editor.commit();
                            NimUIKit.startP2PSession(OrderDetailsActivity.this, orderDetails.getStudentID() + "");

                        }
                    }
                });
        //联系客服
        RxView.clicks(mContactKefu)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        SharedPreferences mySharedPreferences = getSharedPreferences("namess", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putString("names", "我的客服");
                        editor.commit();
                        NimUIKit.startP2PSession(OrderDetailsActivity.this, 107714 + "");
                        finish();
                    }
                });

        //更多
        RxView.clicks(mImgPop)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isFinishing()){
                            return;
                        }
                        View popupView = View.inflate(OrderDetailsActivity.this, R.layout.order_pop, null);
                        final PopupWindow mPopupWindow = new PopupWindow(popupView, PercentLinearLayout.LayoutParams.WRAP_CONTENT, PercentLinearLayout.LayoutParams.WRAP_CONTENT);
                        mPopupWindow.setTouchable(true);
                        mPopupWindow.setOutsideTouchable(true);
                        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
                        mPopupWindow.showAsDropDown(mImgPop);
                        final TextView menu_jj = (TextView) popupView.findViewById(R.id.menu_jj);
                        if (orderDetails.getFlag() == 1) {
                            menu_jj.setText("取消");
                            if (orderDetails.getStatus() == 2 || orderDetails.getStatus() == 1) {
                                if (orderDetails.getStatus() == 1) {
                                    int startTime = orderDetails.getStartTime();
                                    String str = System.currentTimeMillis() + "";
                                    String s = str.substring(0, 10);
                                    int nowTime = Integer.parseInt(s);
                                    if (startTime >= nowTime) {
                                    } else {
                                        menu_jj.setTextColor(getResources().getColor(R.color.text2));
                                    }
                                } else {
                                    menu_jj.setTextColor(getResources().getColor(R.color.text2));
                                }
                            }
                        }
                        LinearLayout order_complaints = (LinearLayout) popupView.findViewById(R.id.order_complaints);
                        RxView.clicks(menu_jj)
                                .throttleFirst(1, TimeUnit.SECONDS)
                                .subscribe(new Action1<Void>() {
                                    @Override
                                    public void call(Void aVoid) {
                                        if (orderDetails.getFlag() == 2) {
                                            initPopup();
                                            mPopupWindow.dismiss();
                                        } else {
                                            if (orderDetails.getStatus() == 2 || orderDetails.getStatus() == 1) {
                                                if (orderDetails.getStatus() == 1) {
                                                    int startTime = orderDetails.getStartTime();
                                                    String str = System.currentTimeMillis() + "";
                                                    String s = str.substring(0, 10);
                                                    int nowTime = Integer.parseInt(s);
                                                    if (startTime >= nowTime) {
                                                        Intent intent = new Intent(OrderDetailsActivity.this, CancelOrderActivity.class);
                                                        startActivityForResult(intent, 2221);
                                                        mPopupWindow.dismiss();
                                                    } else {
                                                        Toast.makeText(OrderDetailsActivity.this, "现在无法取消订单", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(OrderDetailsActivity.this, "现在无法取消订单", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Intent intent = new Intent(OrderDetailsActivity.this, CancelOrderActivity.class);
                                                startActivityForResult(intent, 2221);
                                                mPopupWindow.dismiss();
                                            }
                                        }
                                    }
                                });
                        RxView.clicks(order_complaints)
                                .throttleFirst(1, TimeUnit.SECONDS)
                                .subscribe(new Action1<Void>() {
                                    @Override
                                    public void call(Void aVoid) {
                                        mPopupWindow.dismiss();
                                        Intent intent = new Intent(OrderDetailsActivity.this, ComplaintsActivity.class);
                                        Bundle bundle = new Bundle();
                                        intent.putExtras(bundle);
                                        startActivityForResult(intent, 3);
                                    }
                                });
                    }
                });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        ma = new HashMap<>();
        ma.put("format", "json");
        ma.put("order", tradeno);
        OkManager.getInstance().getSendGift(OrderDetailsActivity.this, Urls.ORDER_DETAILS, ma, true, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                OrderDetailsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Gson gson = new Gson();
                            orderDetails = gson.fromJson(result, OrderDetails.class);
                            status = orderDetails.getStatus();
                            flag = orderDetails.getFlag();
                            hand.sendEmptyMessage(1011);
//                            setStatus(orderDetails.getStatus(), orderDetails.getFlag(), startTime, endTime, orderDetails.getTime(), orderDetails.getBookTime(), orderDetails.getNowtime());
                        } catch (Exception e) {
                            Log.d("test2", e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void initDatas() {
        try {
            if (flag == 1) {
                if (status == 1) {
                    mStartOrder.setVisibility(View.VISIBLE);
                } else {
                    mStartOrder.setVisibility(View.GONE);
                }
            } else {
                mStartOrder.setVisibility(View.GONE);
            }
            Glide.with(OrderDetailsActivity.this).load(orderDetails.getAvatar()).into(mDetaiksIcon);
            mDetaiksHeader.setText(orderDetails.getGamename());
            mDetaiksName.setText(orderDetails.getNickname());
            if (orderDetails.getGrading().isEmpty()) {
                mDetaiksZs.setVisibility(View.GONE);
                mDetaiksLevel.setVisibility(View.GONE);
            } else {
                if (orderDetails.getGrading().equals("正式") & orderDetails.getGrading().toString() == "正式") {
                    mDetaiksLevel.setVisibility(View.GONE);
                } else {
                    mDetaiksZs.setText(orderDetails.getGrading());
                }
            }
            if (orderDetails.getDesc().isEmpty()) {
                mRela.setVisibility(View.GONE);
            } else {
                mYysm.setText(orderDetails.getDesc());
            }

            mOrderNumber.setText("订单号：" + orderDetails.getTradeno());

            orderDetails.getGrading();
            int startTime = orderDetails.getStartTime();
            int endTime = orderDetails.getEndTime();
            DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date d1 = format2.parse(chargeSecondsToNowTime(endTime + ""));
            Date d2 = format2.parse(chargeSecondsToNowTime(startTime + ""));
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            mOrderTime.setText("预约时间：" + chargeSecondsToNowTime(startTime + "") + "-" + chargeSecondsToNowTime2(endTime + "") + "(" + hours + orderDetails.getTypeflag() + ")");

            //导师
            if (orderDetails.getFlag() == 2) {
                mPriceTitle.setText("到帐：");
                mPriceValue.setText(orderDetails.getIncome() + "");
                mOrderPrice.setText("订单金额：" + (orderDetails.getIncome() + orderDetails.getDiscount()) + "元");
                mPriceIntro.setText("实际收入=订单金额-平台服务费");
                mServicePrice.setText("平台服务费：" + orderDetails.getDiscount() + "元");
            } else {
                mPriceTitle.setText("支付：");
                mPriceValue.setText(orderDetails.getPrice() + "");
                mOrderPrice.setText("订单金额：" + (hours * orderDetails.getTimePrice() / 100) + "元");
                mPriceIntro.setText("实际支付=订单金额-优惠减免");
                mServicePrice.setText("优惠减免： " + orderDetails.getDiscount() + "元");
            }
            if (orderDetails.getCommentScore() <= 0) {
                mComment.setVisibility(View.GONE);
            } else {
                mCommentContent.setText(orderDetails.getCommentDesc());
            }
        } catch (Exception e) {

        }
    }

    private CountDownTimer timeCount;
    private CountDownTimer timeCount1;
    private CountDownTimer timeCount2;

    //设置状态
    private void setStatus() {
        String str = System.currentTimeMillis() + "";
        String s = str.substring(0, 10);
        int nowTime = orderDetails.getNowtime();// Integer.parseInt(s);

        int startTime = orderDetails.getStartTime();
        int endTime = orderDetails.getEndTime();
        int time = orderDetails.getTime();
        int bookTime = orderDetails.getBookTime();
        switch (status) {
            case 0:
                if (flag == 1) {
                    int i = nowTime - time;
                    timeCount = new CountDownTimer((1800 - i) * 1000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            long s = millisUntilFinished / 1000;
                            long k = s / 60;
                            s = s % 60;
                            long m = s;
                            timeFormat = "等待接单(" + k + "分" + m + "秒)";
                            hand.sendEmptyMessage(201);
                        }

                        @Override
                        public void onFinish() {
                            mOrderStates.setText("已取消");
                            mOrderStates.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.color_A9A9A9));
                        }
                    }.start();
                } else {
                    int i = nowTime - time;
                    timeCount1 = new CountDownTimer((1800 - i) * 1000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            long s = millisUntilFinished / 1000;
                            long k = s / 60;
                            s = s % 60;
                            long m = s;
                            timeFormat = "接单(" + k + "分" + m + "秒)";
                            hand.sendEmptyMessage(202);
                        }

                        @Override
                        public void onFinish() {
                            mOrderStates.setText("已取消");
                            mOrderStates.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.color_A9A9A9));
                        }
                    }.start();
                }
                break;
            case 1:
                long l = System.currentTimeMillis();
                int times = (int) (l / 1000);
                if (startTime > times) {
                    mOrderStates.setText("己接单");
                } else if (endTime > times && times >= startTime) {
                    mOrderStates.setText("进行中");
                } else {
                    mOrderStates.setText("服务结束");
                    mOrderStates.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initEvaluate();//弹出评价框
                        }
                    });
                }
                break;

            case 2:
                mOrderStates.setText("已完成");
                mOrderStates.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.color_A9A9A9));
                break;

            case -2000:
                int i = nowTime - time;
                during = i;
                if (i < 600) {
                    timeCount2 = new CountDownTimer((600 - i) * 1000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            long s = millisUntilFinished / 1000;
                            long k = s / 60;
                            s = s % 60;
                            long m = s;
                            mOrderStates.setText("去支付(还剩" + k + "分" + m + "秒)");
                            mOrderStates.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PayActivity.action(orderDetails.getTradeno(), OrderDetailsActivity.this);
                                }
                            });
                        }

                        public void onFinish() {
                            mOrderStates.setText("已取消");
                            mOrderStates.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.color_A9A9A9));
                        }
                    }.start();
                    mOrderStates.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            timeCount.cancel();
                            for (int i = 0; i < mHttpResult.gamelist.size(); i++) {
                                MakeOrderRecentOrderBean.GamelistBean gamelistBean = mHttpResult.gamelist.get(i);
                                if (gamelistBean.id == dtid) {
                                    //目前选中的就是这个服务
                                    mChosedGamePostion = i;
                                    break;
                                }
                            }
                            PayActivity.show(new PayEntity(tradeno, (timePrice / 100), number, typeflag, mHttpResult, mChosedGamePostion), OrderDetailsActivity.this);
                        }
                    });
                } else {
                    mOrderStates.setText("已取消");
                    mOrderStates.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.color_A9A9A9));
                }
                break;

            default:
                mOrderStates.setText("已取消");
                mOrderStates.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.color_A9A9A9));
                break;
        }
    }

    //评价栏
    private void initEvaluate() {
//        View view = View.inflate(OrderDetailsActivity.this, R.layout.oreder_appraise, null);
//        WindowManager wm = getWindowManager();
//        final int width = wm.getDefaultDisplay().getWidth();
//        int height = wm.getDefaultDisplay().getHeight();
//        final PopupWindow window = new PopupWindow(view, width, height);
//        window.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//
////        final PopupWindow window = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, 800);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        window.setOutsideTouchable(true);
//        window.showAsDropDown(mTitle, 0, 0);

        View view = View.inflate(OrderDetailsActivity.this, R.layout.oreder_appraise, null);
        LayoutInflater inflater = LayoutInflater.from(OrderDetailsActivity.this);
        final Dialog dialog = new Dialog(OrderDetailsActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (PercentLinearLayout.LayoutParams.MATCH_PARENT); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(view);
        ImageView close = (ImageView) view.findViewById(R.id.popup_close2);
        final ImageView image1 = (ImageView) view.findViewById(R.id.image1);
        final ImageView image2 = (ImageView) view.findViewById(R.id.image2);
        final ImageView image3 = (ImageView) view.findViewById(R.id.image3);
        final ImageView image4 = (ImageView) view.findViewById(R.id.image4);
        final ImageView image5 = (ImageView) view.findViewById(R.id.image5);
        final EditText edit = (EditText) view.findViewById(R.id.edit);
        image_pingjia = (ImageView) view.findViewById(R.id.image_pingjia);
        layout1 = (LinearLayout) view.findViewById(R.id.layout1);
        Button pop_know = (Button) view.findViewById(R.id.pop_know);
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

//        if(edit.requestFocus()){
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////得到InputMethodManager的实例
//            if (imm.isActive()) {
////如果开启
//                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
////关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
//            }
//        }
        pop_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

                edit.requestFocus();/*获取焦点*/

                String edits = edit.getText().toString();
                if (edits.equals("") && edits == null) {
                    Toast.makeText(OrderDetailsActivity.this, "请填写评价", Toast.LENGTH_SHORT).show();
                } else {
                    if (orderDetails.getTradeno() == null){
                        return;
                    }
                    map = new HashMap<String, String>();
                    map.put("format", "json");
                    map.put("order", orderDetails.getTradeno());
                    map.put("decs", edits);
                    map.put("star", page + "");
                    Log.e("yzshshhssh",pjz+"");
                    map.put("type", pjz+"");
                    OkManager.getInstance().getSendGift(OrderDetailsActivity.this, Urls.ADD_COMMENT + "?a=1", map, true, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String result = response.body().string();
                            OrderDetailsActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                            Log.e("----------",result.toString());
                            Gson gson = new Gson();
                            try {
                                Codes codes = gson.fromJson(result, Codes.class);
                                if (codes.getCode() == 0) {
                                    dialog.cancel();
                                    initData();
                                    hand.sendEmptyMessage(1012);
                                    OrderDetailsActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            initEvaluateFK();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                            }
                        }

                    });
                        }
                    });
                }
                }
                catch (Exception e){

                }
            }

        });

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
    //评价成功反馈
    private void initEvaluateFK() {
        View view = View.inflate(OrderDetailsActivity.this, R.layout.cancel_pop, null);
        WindowManager wm = getWindowManager();
        final int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        final PopupWindow window = new PopupWindow(view, width, height);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
//        window.showAsDropDown(mTitle, 0, 0);
        window.showAtLocation(view, Gravity.CENTER, width, height + 500);
        ImageView popup_close3 = (ImageView) view.findViewById(R.id.popup_close3);
        Button pop_know2 = (Button) view.findViewById(R.id.pop_know2);
        TextView kq = (TextView) view.findViewById(R.id.pop_kq);
        TextView js = (TextView) view.findViewById(R.id.pop_js);
        kq.setText("评价成功");
        js.setVisibility(View.GONE);
        pop_know2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                initData();
                hand.sendEmptyMessage(1012);
            }
        });
        popup_close3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                initData();
                hand.sendEmptyMessage(1012);
            }
        });
    }

    //导师接单
    private void initJieDan() {
        View view = View.inflate(OrderDetailsActivity.this, R.layout.order_receiving, null);
        WindowManager wm = getWindowManager();
        final int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        final PopupWindow window = new PopupWindow(view, width, height);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
//        window.showAsDropDown(mTitle, 0, 0);
        window.showAtLocation(view, Gravity.CENTER, width, height + 500);
        ImageView close = (ImageView) view.findViewById(R.id.popup_close2);
        Button qx = (Button) view.findViewById(R.id.receiving_qx);
        Button jd = (Button) view.findViewById(R.id.receiving_jd);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        jd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map = new HashMap<>();
                map.put("format", "json");
                map.put("order", orderDetails.getTradeno());
                OkManager.getInstance().getSendGift(OrderDetailsActivity.this, Urls.TEACHER_JIEDAN, map, true, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String ss = response.body().string();
                        Log.e("接单成功", ss.toString());
                        Gson gson = new Gson();
                        final Codes codes = gson.fromJson(ss, Codes.class);
                        OrderDetailsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (codes.getCode() == 0) {
                                    initReceivingTriumph();
                                    timeCount1.cancel();
                                    mOrderStates.setText("已接单");
                                    initData();
                                }
                            }
                        });
                    }
                });
                window.dismiss();
            }
        });
    }

    //接单成功反馈
    private void initReceivingTriumph() {
        if (isFinishing()){
            return;
        }
        View view = View.inflate(OrderDetailsActivity.this, R.layout.cancel_pop, null);
        WindowManager wm = getWindowManager();
        final int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        final PopupWindow window = new PopupWindow(view, width, height);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
//        window.showAsDropDown(mTitle, 0, 0);
        window.showAtLocation(view, Gravity.CENTER, width, height + 500);
        ImageView popup_close3 = (ImageView) view.findViewById(R.id.popup_close3);
        Button pop_know2 = (Button) view.findViewById(R.id.pop_know2);
        TextView kq = (TextView) view.findViewById(R.id.pop_kq);
        TextView js = (TextView) view.findViewById(R.id.pop_js);
        kq.setText("接单成功");
        js.setVisibility(View.GONE);
        pop_know2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                initData();
                hand.sendEmptyMessage(1012);
            }
        });
        popup_close3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                initData();
                hand.sendEmptyMessage(1012);
            }
        });
    }

    //时间戳转化为时间
    public static String formatData(String dataFormat, long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        timeStamp = timeStamp * 1000;
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat(dataFormat);
        result = format.format(new Date(timeStamp));
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2221) {
            String backString = data.getStringExtra("cause");
            String url = "https://y.tuwan.com/m/Order/getOffOrderApi" + "?order=" + tradeno + "&format=" + "json" + "&decs=" + backString;

            OkManager okManager = OkManager.getInstance();
            okManager.getAsync(this, url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //Log.d("test2", e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result = response.body().string();
                    Gson gson = new Gson();
                    OrderCancel cancel = gson.fromJson(result, OrderCancel.class);
                    if (cancel.getCode() == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initData();
                                initCancel();
                            }
                        });
                    }
                }
            }, true);
        }
        if (requestCode == 3 & resultCode == 300) {
            Bundle bundle = data.getExtras();
            String content = bundle.getString("content");
            Log.e("投诉原因", content);
            final OkHttpClient client = new OkHttpClient();
            String url = "https://y.tuwan.com/m/Compliant/add" + "?content=" + content + "&format=" + "json";
            final Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Cookie", cookie)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("提交投诉返回数据错误", e.toString());
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String result = response.body().string();
                    System.out.println("提交投诉返回数据：" + result);

                }
            });
        }
//        }
    }

    //拒绝订单通知
    private void initPopup() {
        View view = View.inflate(OrderDetailsActivity.this, R.layout.order_details, null);
        WindowManager wm = getWindowManager();
        final int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        final PopupWindow window = new PopupWindow(view, 290, 320);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.showAsDropDown(mTitle, 0, 0);
        ImageView close = (ImageView) view.findViewById(R.id.popup_close2);
        Button bt = (Button) view.findViewById(R.id.details_bt);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
    }

    //取消订单通知
    private void initCancel() {
        View view = View.inflate(OrderDetailsActivity.this, R.layout.cancel_pop, null);
        WindowManager wm = getWindowManager();
        final int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        final PopupWindow window = new PopupWindow(view, width, height);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.showAsDropDown(mTitle, 0, 0);
        ImageView popup_close3 = (ImageView) view.findViewById(R.id.popup_close3);
        Button pop_know2 = (Button) view.findViewById(R.id.pop_know2);
        popup_close3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                initData();
//                timeCount.cancel();
//                mOrderStates.setText("已取消");
//                hand.sendEmptyMessage(1012);
                window.dismiss();
                if (timeCount != null) {
                    timeCount.cancel();
                }
                if (timeCount1 != null) {
                    timeCount1.cancel();
                }
                if (timeCount2 != null) {
                    timeCount2.cancel();
                }
                mOrderStates.setText("已取消");
            }
        });
        pop_know2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                if (timeCount != null) {
                    timeCount.cancel();
                }
                if (timeCount1 != null) {
                    timeCount1.cancel();
                }
                if (timeCount2 != null) {
                    timeCount2.cancel();
                }
                mOrderStates.setText("已取消");

            }
        });
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

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.start_order) {
            final OkHttpClient client = new OkHttpClient();
            String url = " https://y.tuwan.com/m/Order/getOffOrderApi" + "?order=" + tradeno + "&format=" + "json" + "&decs" + "取消原因";
            SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
            String cookie = preferences.getString("Cookie", null);


            final Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Cookie", cookie)
                    .header("format", "json")
                    .header("order", tradeno)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(OrderDetailsActivity.this, "失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(OrderDetailsActivity.this, "成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //orderManager.canCelOrder(true);
                }
            });
        }
    }

    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private void showLeaveDialog() {
        final CustomDialogManager.CustomDialog customDialog = CustomDialogManager.getInstance().getDialog(this, R.layout.dialog_pay_submit).setSizeOnDP(290, 320);

        customDialog.findViewById(R.id.img_pays).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        customDialog.findViewById(R.id.tv_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mLoop = false;
    }
}