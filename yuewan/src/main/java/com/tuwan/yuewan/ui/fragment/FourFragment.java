package com.tuwan.yuewan.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tuwan.common.LibraryApplication;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.Coupon;
import com.tuwan.yuewan.entity.ErrorBean;
import com.tuwan.yuewan.entity.IsCompletes;
import com.tuwan.yuewan.entity.MeInfo;
import com.tuwan.yuewan.nim.uikit.NimUIKit;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.activity.AddInformationActivity;
import com.tuwan.yuewan.ui.activity.ApplicationInActivity;
import com.tuwan.yuewan.ui.activity.BalanceActivity;
import com.tuwan.yuewan.ui.activity.ComplaintsActivity;
import com.tuwan.yuewan.ui.activity.DiamondActivity;
import com.tuwan.yuewan.ui.activity.MyRedActivity;
import com.tuwan.yuewan.ui.activity.MyServiceActivity;
import com.tuwan.yuewan.ui.activity.OrderActivity;
import com.tuwan.yuewan.ui.activity.SeetingActivity;
import com.tuwan.yuewan.ui.activity.TeacherMainInfoActivity;
import com.tuwan.yuewan.ui.activity.VipActivity;
import com.tuwan.yuewan.ui.activity.seeting.AuthenticationActivity;
import com.tuwan.yuewan.ui.activity.teacher.OneOnlineActivity;
import com.tuwan.yuewan.ui.activity.teacher.WithdrawalsActivity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.http.HTTP;
import rx.functions.Action1;

public class FourFragment extends BaseFragment {
    private ImageView me_setting;
    private RoundedImageView me_icon;
    private TextView me_nc;
    private ImageView me_touxian;
    private TextView me_uid;
    private TextView me_yue;
    private TextView me_zuanshi;
    private TextView me_wdfw;
    private LinearLayout me_fwzz;
    private LinearLayout me_yjsxx;
    private LinearLayout me_tx;
    private LinearLayout daoshi_service;
    private LinearLayout me_sqrz_li;
    private LinearLayout lly_my_red;
    private TextView tv_my_red;
    private TextView me_sqrz;
    private TextView me_ddzx;
    private TextView me_vip;
    private TextView me_tsjy;
    private TextView me_lxkf;
    private View me_xain;
    private int bind;
    private LinearLayout iv_bjzl;
    private LinearLayout top_up_ye;
    private LinearLayout top_up_zs;
    private TextView me_about;
    private TextView tv_sign;
    private Button btn_tczh;
    private double currency;
    private String diamond;
    private int isteacher;
    private int uid,vipuid;
    private int width = 0;
    private int height = 0;

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            me_yue.setText(msg.obj.toString());
            return false;
        }
    });


    private LinearLayout ff;
    private MeInfo meInfo;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_one;
    }

    @Override
    protected void setUpView() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        ff = (LinearLayout) getContentView().findViewById(R.id.fragment_linear);
        //SystemBarHelper.setHeightAndPadding(getContext(), ff);
        me_setting = (ImageView) getContentView().findViewById(R.id.me_setting);
        me_icon = (RoundedImageView) getContentView().findViewById(R.id.me_icon);
        me_nc = (TextView) getContentView().findViewById(R.id.me_nc);
        me_touxian = (ImageView) getContentView().findViewById(R.id.me_touxian);
        me_uid = (TextView) getContentView().findViewById(R.id.me_uid);
        me_yue = (TextView) getContentView().findViewById(R.id.me_yue);
        me_zuanshi = (TextView) getContentView().findViewById(R.id.me_zuanshi);
        me_wdfw = (TextView) getContentView().findViewById(R.id.me_wdfw);
        me_fwzz = (LinearLayout) getContentView().findViewById(R.id.me_fwzz);
        me_yjsxx = (LinearLayout) getContentView().findViewById(R.id.me_yjsxx);
        me_tx = (LinearLayout) getContentView().findViewById(R.id.me_tx);
        daoshi_service = (LinearLayout) getContentView().findViewById(R.id.daoshi_service);
        lly_my_red = (LinearLayout) getContentView().findViewById(R.id.lly_my_red);
        tv_my_red = (TextView) getContentView().findViewById(R.id.tv_my_red);
        tv_sign = (TextView) getContentView().findViewById(R.id.tv_sign);
        me_sqrz = (TextView) getContentView().findViewById(R.id.me_sqrz);
        me_sqrz_li = (LinearLayout) getContentView().findViewById(R.id.me_sqrz_li);
        me_ddzx = (TextView) getContentView().findViewById(R.id.me_ddzx);
        me_vip = (TextView) getContentView().findViewById(R.id.me_vip);
        me_tsjy = (TextView) getContentView().findViewById(R.id.me_tsjy);
        me_lxkf = (TextView) getContentView().findViewById(R.id.me_lxkf);
        iv_bjzl = (LinearLayout) getContentView().findViewById(R.id.iv_bjzl);
        me_xain = (View) getContentView().findViewById(R.id.view);
        top_up_ye = (LinearLayout) getContentView().findViewById(R.id.top_up_ye);
        top_up_zs = (LinearLayout) getContentView().findViewById(R.id.top_up_zs);
//        me_about = (TextView) getContentView().findViewById(R.id.me_about);
//        btn_tczh = (Button) getContentView().findViewById(R.id.btn_tczh);
        initdata();
        click();
    }

    @Override
    protected void setUpData() {
        initdata();
    }

    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }

    private void click() {
        //会员中心
        RxView.clicks(me_vip)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getActivity(), VipActivity.class));
                    }
                });
        //申请入驻

        try {


        RxView.clicks(me_sqrz)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        DialogMaker.showProgressDialog(getActivity(), null, "", true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                            }
                        }).setCanceledOnTouchOutside(false);
                        ServiceFactory.getNoCacheInstance()
                                .createService(YService.class)
                                .iscomplete("json","app")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CommonObserver<IsCompletes>() {
                                    @Override
                                    public void onNext(@NonNull IsCompletes result) {
                                        super.onNext(result);
                                        onLoginDone();
                                        Log.d("result", result.getCheck() + "");
                                        int check = result.getCheck();
//                                        iscomplete=check;
                                        if (check == 1) {
                                            startActivity(new Intent(getActivity(), ApplicationInActivity.class));
                                        } else if (check == 0) {
                                            Intent intent = new Intent(getActivity(), AddInformationActivity.class);
                                            if (meInfo != null) {
                                                intent.putExtra("img", meInfo.getAvatar());
                                                intent.putExtra("name", meInfo.getNickname());
                                                intent.putExtra("uid", meInfo.getUid());
                                                getActivity().startActivity(intent);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        super.onError(e);
                                        onLoginDone();
                                    }
                                });
                    }
                });

        }catch (Exception e){

        }
        //设置


        RxView.clicks(me_setting)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getActivity(), SeetingActivity.class);
                        intent.putExtra("isteacher", isteacher);
                        intent.putExtra("bind", bind);
                        Log.e("zhenguoli",uid+"");
                        intent.putExtra("uid",uid);
                        startActivity(intent);
                    }
                });
        //编辑资料
        RxView.clicks(iv_bjzl)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        if (isteacher == 1) {
                        Intent intent = new Intent(getActivity(), TeacherMainInfoActivity.class);
                        intent.putExtra("uid", uid);
                        intent.putExtra("isteacher",isteacher);
                        getActivity().startActivity(intent);
//                        }
//                        else {
//                            Intent inte = new Intent(getActivity(), EditdataActivity.class);
//                            inte.putExtra("isteacher", isteacher);
//                            getActivity().startActivity(inte);
//                        }
                    }
                });
        //余额充值
        RxView.clicks(top_up_ye)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getActivity(), BalanceActivity.class);
                        intent.putExtra("currency", currency);
                        startActivity(intent);
                    }
                });
        //钻石充值
        RxView.clicks(top_up_zs)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getActivity(), DiamondActivity.class);
                        intent.putExtra("diamond", diamond);
                        startActivity(intent);
                    }
                });
        //红包
        RxView.clicks(lly_my_red)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getActivity(), MyRedActivity.class);
                        startActivity(intent);
                    }
                });
//        //联系客服
//        RxView.clicks(me_lxkf)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        int qqNum = Integer.parseInt("775121240");
//                        if (checkApkExist(getActivity(), "com.tencent.mobileqq")) {
//                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum + "&version=1")));
//                        } else {
//                            ToastUtils.getInstance().showToast("本机未安装QQ应用");
//                        }
//                    }
//                });
//        联系客服
        RxView.clicks(me_lxkf)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        SharedPreferences mySharedPreferences = getContext().getSharedPreferences("namess", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putString("names", "我的客服");
                        editor.commit();
                        NimUIKit.startP2PSession(getContext(), 107714 + "");
                    }
                });

//        //关于点点约玩
//        RxView.clicks(me_about)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        startActivity(new Intent(getActivity(), AboutActivity.class));
//
////                        AVChatActivity.launch(getActivity(), "107833",1,1);
//
////                        if(isShowFloat){
////                            manager.show();
////                            manager.setSecond(600);
////                        }
////                        else{
////                            manager.hide();
////                        }
////                        isShowFloat = !isShowFloat;
//
//
//
//                    }
//                });
//        //退出账号
//        RxView.clicks(btn_tczh)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        SharedPreferencesHelper helper = new SharedPreferencesHelper(getActivity());
//                        helper.clear();
//                        NIMClient.getService(AuthService.class).logout();
//                        getActivity().finish();
//                        Intent intent = new Intent(getActivity(), WelcomeActivity.class);
//                        startActivity(intent);
//                        LibraryApplication.getInstance().clearActivitys();
//                    }
//                });

        //订单中心
        RxView.clicks(me_ddzx)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getActivity(), OrderActivity.class));
                    }
                });
        //投诉建议
        RxView.clicks(me_tsjy)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getActivity(), ComplaintsActivity.class));
                    }
                });
    }

    private void initdata() {
        try {


        OkHttpClient client = new OkHttpClient();
        final FormBody.Builder builder = new FormBody.Builder();
        builder.add("format", "json")
                .add("type", "app");
        RequestBody formBody = builder.build();
        SharedPreferences preferences = getActivity().getSharedPreferences("infos", Context.MODE_PRIVATE);

        String cookie = preferences.getString("Cookie", "");

        final Request request = new Request.Builder()
                .url("https://y.tuwan.com/m/User/indexInfo")
                .addHeader("Cookie", cookie)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("获取失败原因", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println("获取返回的数据" + result);
                Gson gson = new Gson();
                if (result != null) {
                    meInfo = gson.fromJson(result, MeInfo.class);
                }else {
                    return;
                }
                isteacher = meInfo.getIsteacher();
                uid = meInfo.getUid();
                vipuid = meInfo.getVipuserid();
                if (getActivity() != null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //余额
                            // Handler mHandler=new Handler();
//                        currency = meInfo.getCurrency();
                            tv_my_red.setText(meInfo.getCoupon().getCount() + "");
                            if (meInfo.getCoupon().getError_code() == 0){
                                tv_sign.setVisibility(View.VISIBLE);
                                tv_sign.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tv_sign.setText("更新中...");
                                        showSignInDialog(meInfo.getCoupon());
                                    }
                                });
                            }else if(meInfo.getCoupon().getError_code() == -3){
                                tv_sign.setVisibility(View.VISIBLE);
                                tv_sign.setText("今日已签到");
                                tv_sign.setOnClickListener(null);
                            }else {
                                tv_sign.setVisibility(View.GONE);
                            }
                            currency = meInfo.getCurrency();
                            BigDecimal bg = new BigDecimal(currency);
                            currency = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            Message message = mHandler.obtainMessage();
                            message.obj = currency + "";
                            mHandler.sendMessage(message);
                            bind = meInfo.getBind();
//                        me_yue.setText(meInfo.getCurrency() + "");
                            diamond = meInfo.getDiamond();
                            me_zuanshi.setText("" + diamond);//钻石
                            me_nc.setText(Html.fromHtml(meInfo.getNickname()));
                            if (meInfo.getVipuserid() == 0) {
                                me_uid.setText("UID：" + uid);//uid
                            } else {
                                if (meInfo.getViplevel() == 0) {
//                                me_uid.setText("UID：" + uid);
                                    //靓号
                                    me_uid.setText("靓 " + vipuid);
                                    me_uid.setBackgroundResource(R.drawable.bg_orange2);
                                    me_uid.setTextColor(Color.rgb(172, 88, 7));

                                } else if (meInfo.getViplevel() == 1) {
//                                me_uid.setText("UID：" + uid);
                                    //土豪
                                    me_uid.setText("豪 " + vipuid);
                                    me_uid.setBackgroundResource(R.drawable.bg_yellow2);
                                    me_uid.setTextColor(Color.rgb(172, 88, 7));

                                }

//                            me_uid.setText("UID：" + uid);
//                            //靓号，先不实现
//                            me_uid.setText("靓 " + uid);
//                            me_uid.setBackgroundResource(R.drawable.vipnumber_background);
                            }
                            int isteacher = meInfo.getIsteacher();
                            if (isteacher == 1) {
                                daoshi_service.setVisibility(View.VISIBLE);
                                me_sqrz_li.setVisibility(View.GONE);
                            } else {
                                daoshi_service.setVisibility(View.GONE);
                                me_sqrz_li.setVisibility(View.VISIBLE);
                            }
                            meInfo.getPlayvip();
                            String vipIcon = meInfo.getVipicon();
                            // String vipIcon = "http://res.tuwan.com/templet/play/m/images/vip" + meInfo.getPlayvip() + ".png";
                            if (getActivity() != null) {
                                Glide.with(getActivity()).load(meInfo.getAvatar()).into(me_icon);
                                Glide.with(getActivity()).load(vipIcon).into(me_touxian);
                            }
                            if (isteacher == 0) {
                                daoshi_service.setVisibility(View.GONE);
                                me_sqrz.setVisibility(View.VISIBLE);
//                                me_xain.setVisibility(View.GONE);
                                //订单中心
                                RxView.clicks(me_ddzx)
                                        .throttleFirst(1, TimeUnit.SECONDS)
                                        .subscribe(new Action1<Void>() {
                                            @Override
                                            public void call(Void aVoid) {
                                                startActivity(new Intent(getActivity(), OrderActivity.class));
                                            }
                                        });
                                //投诉建议
                                RxView.clicks(me_tsjy)
                                        .throttleFirst(1, TimeUnit.SECONDS)
                                        .subscribe(new Action1<Void>() {
                                            @Override
                                            public void call(Void aVoid) {
                                                startActivity(new Intent(getActivity(), ComplaintsActivity.class));
                                            }
                                        });

                            } else {
                                //我的服务
                                me_sqrz.setVisibility(View.GONE);
                                daoshi_service.setVisibility(View.VISIBLE);
                                RxView.clicks(me_wdfw)
                                        .throttleFirst(1, TimeUnit.SECONDS)
                                        .subscribe(new Action1<Void>() {
                                            @Override
                                            public void call(Void aVoid) {
                                                startActivity(new Intent(getActivity(), MyServiceActivity.class));
                                            }
                                        });
                                //一键上下线
                                RxView.clicks(me_yjsxx)
                                        .throttleFirst(1, TimeUnit.SECONDS)
                                        .subscribe(new Action1<Void>() {
                                            @Override
                                            public void call(Void aVoid) {
                                                startActivity(new Intent(getActivity(), OneOnlineActivity.class));
                                            }
                                        });
                                //服务资质
                                RxView.clicks(me_fwzz)
                                        .throttleFirst(1, TimeUnit.SECONDS)
                                        .subscribe(new Action1<Void>() {
                                            @Override
                                            public void call(Void aVoid) {
                                                startActivity(new Intent(getActivity(), ApplicationInActivity.class));
                                            }
                                        });
                                //提现
                                RxView.clicks(me_tx)
                                        .throttleFirst(1, TimeUnit.SECONDS)
                                        .subscribe(new Action1<Void>() {
                                            @Override
                                            public void call(Void aVoid) {
                                                if (bind == 1) {
                                                    startActivity(new Intent(getActivity(), WithdrawalsActivity.class));
                                                } else if (bind == -1) {
                                                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout2, null);
                                                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                                                    Button dialog_cancel = (Button) view.findViewById(R.id.dialog_cancel);

                                                    builder1.setView(view);
                                                    final AlertDialog show = builder1.show();
                                                    dialog_cancel.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            show.dismiss();
                                                        }
                                                    });

                                                } else if (bind == 0) {
                                                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout, null);
                                                    final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                                                    Button dialog_cancel = (Button) view.findViewById(R.id.dialog_cancel);
                                                    Button dialog_msg = (Button) view.findViewById(R.id.dialog_ok);
                                                    dialog.setView(view);
                                                    final AlertDialog show = dialog.show();
                                                    dialog_cancel.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            startActivity(new Intent(getActivity(), AuthenticationActivity.class));
                                                            show.dismiss();
                                                        }
                                                    });

                                                    dialog_msg.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            show.dismiss();
                                                        }
                                                    });
                                                }
                                            }
                                        });
                            }
                        }
                    });
            }
            }
        });
        }catch (Exception e){

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
    private void showSignInDialog(final Coupon coupon) {
        View view = View.inflate(getActivity(), R.layout.dialog_home_sign_two, null);
        final PopupWindow window = new PopupWindow(view, width, height);
        LinearLayout lly_sign_day = (LinearLayout) view.findViewById(R.id.lly_sign_day);
        final LinearLayout lly_state1 = (LinearLayout) view.findViewById(R.id.lly_state1);
        final LinearLayout lly_state2 = (LinearLayout) view.findViewById(R.id.lly_state2);
        final LinearLayout lly_state3 = (LinearLayout) view.findViewById(R.id.lly_state3);
        final TextView tv_state1 = (TextView) view.findViewById(R.id.tv_state1);
        final TextView tv_state2 = (TextView) view.findViewById(R.id.tv_state2);
        final TextView tv_state3 = (TextView) view.findViewById(R.id.tv_state3);
        final TextView tv_my_sign = (TextView) view.findViewById(R.id.tv_my_sign);
        final TextView tv_my_red_money = (TextView) view.findViewById(R.id.tv_my_red_money);
        final TextView tv_my_red_time = (TextView) view.findViewById(R.id.tv_my_red_time);
        final TextView tv_my_red_name = (TextView) view.findViewById(R.id.tv_my_red_name);
        tv_my_sign.setText("恭喜你获得" + coupon.getCouponinfo().getPrice()  + "元代金券");
        tv_my_red_money.setText(coupon.getCouponinfo().getPrice()  + "");
        tv_my_red_name.setText("本代金券消费满" + (coupon.getCouponinfo().getUse_price() ) + "立减" + (coupon.getCouponinfo().getPrice() ) + "元");
        tv_my_red_time.setText("有效期" + coupon.getCouponinfo().getExpire_day() + "天");
        if (coupon.getOnce() == 5){
            setView(0,lly_sign_day);
        }else {
            setView(coupon.getOnce() ,lly_sign_day);
        }
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.showAtLocation(view, Gravity.CENTER, 0, 0);
        ImageView img_sign_close = (ImageView) view.findViewById(R.id.img_sign_close);
        img_sign_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        tv_state1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMaker.showProgressDialog(getActivity(), null, "", true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                }).setCanceledOnTouchOutside(false);
                ServiceFactory.getNoCacheInstance()
                        .createService(YService.class)
                        .setUserSign("json")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CommonObserver<ErrorBean.ErrorSign>() {
                            @Override
                            public void onNext(ErrorBean.ErrorSign errorBean) {
                                super.onNext(errorBean);
                                onLoginDone();
                                if (errorBean.error_code == 0) {
                                    ToastUtils.getInstance().showToast("签到成功");

                                    lly_state1.setVisibility(View.GONE);
                                    tv_state1.setVisibility(View.GONE);
                                    if (coupon.getOnce() == 5) {
                                        tv_state2.setText("已连续签到1天");
                                        lly_state2.setVisibility(View.VISIBLE);
                                        tv_state2.setVisibility(View.VISIBLE);
                                    }else if(coupon.getOnce() == 4){
                                        lly_state3.setVisibility(View.VISIBLE);
                                        tv_state3.setVisibility(View.VISIBLE);
                                    }else{
                                        tv_state2.setText("已连续签到" + (coupon.getOnce() + 1) + "天");
                                        lly_state2.setVisibility(View.VISIBLE);
                                        tv_state2.setVisibility(View.VISIBLE);
                                    }

                                }else {
                                    ToastUtils.getInstance().showToast("签到失败 请重试");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                onLoginDone();
                                ToastUtils.getInstance().showToast("签到失败 请重试");
                            }
                        });

            }
        });
        tv_state2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lly_state2.setVisibility(View.GONE);
                tv_state2.setVisibility(View.GONE);
//                if (coupon.getOnce() + 1 == 4) {
//                    lly_state3.setVisibility(View.VISIBLE);
//                    tv_state3.setVisibility(View.VISIBLE);
//                }else {
                window.dismiss();
//                }
            }
        });
        tv_state3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
    }

    private void setView(int day,LinearLayout lly){
        for (int i = 0; i < 5; i++) {
            TextView textView = new TextView(getActivity());
            TextView imageView = new TextView(getActivity());
            LinearLayout.LayoutParams lp = null;
            LinearLayout.LayoutParams lpImg = null;
            textView.setTextSize(8);
            textView.setTextColor(Color.rgb(255,255,255));
            textView.setGravity(Gravity.CENTER);
            if (i < day) {
                textView.setBackgroundResource(R.drawable.bg_two_icon_days);
                lp = new LinearLayout.LayoutParams(DensityUtils.dp2px(LibraryApplication.getInstance(), 15), DensityUtils.dp2px(LibraryApplication.getInstance(), 15));
                if (day == day - 1) {
                    imageView.setBackgroundColor(Color.rgb(255,176,74));
                    lpImg = new LinearLayout.LayoutParams(DensityUtils.dp2px(LibraryApplication.getInstance(), 30), DensityUtils.dp2px(LibraryApplication.getInstance(), 4));
                }else {
                    imageView.setBackgroundColor(Color.rgb(231,85,77));
                    lpImg = new LinearLayout.LayoutParams(DensityUtils.dp2px(LibraryApplication.getInstance(), 30), DensityUtils.dp2px(LibraryApplication.getInstance(), 4));
                }
                textView.setText((i + 1) + "");
            }else if (i == day){
                textView.setBackgroundResource(R.drawable.icon_two_singin_success);
                lp = new LinearLayout.LayoutParams(DensityUtils.dp2px(LibraryApplication.getInstance(), 28), DensityUtils.dp2px(LibraryApplication.getInstance(), 28));
                lp.setMargins(-DensityUtils.dp2px(LibraryApplication.getInstance(), 5),0,0,0);
                imageView.setBackgroundColor(Color.rgb(255,176,74));
                lpImg = new LinearLayout.LayoutParams(DensityUtils.dp2px(LibraryApplication.getInstance(), 30), DensityUtils.dp2px(LibraryApplication.getInstance(), 4));
                lpImg.setMargins(-DensityUtils.dp2px(LibraryApplication.getInstance(), 5),0,0,0);
            }else {
                textView.setBackgroundResource(R.drawable.bg_two_icon_days_unarrive);
                lp = new LinearLayout.LayoutParams(DensityUtils.dp2px(LibraryApplication.getInstance(), 15), DensityUtils.dp2px(LibraryApplication.getInstance(), 15));
                textView.setText((i + 1) + "");
                imageView.setBackgroundColor(Color.rgb(255,176,74));
                lpImg = new LinearLayout.LayoutParams(DensityUtils.dp2px(LibraryApplication.getInstance(), 30), DensityUtils.dp2px(LibraryApplication.getInstance(), 4));
            }
            textView.setLayoutParams(lp);
            imageView.setLayoutParams(lpImg);
            lly.addView(textView);
            if (i != 4){
                lly.addView(imageView);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            initdata();
        }
    }

    @Override
    public void onResume() {
//        int id = getActivity().getIntent().getIntExtra("id", 0);
//        if(id==2){
//            vp.setCurrentItem(2);w
//        }
        super.onResume();
        initdata();
    }
}
