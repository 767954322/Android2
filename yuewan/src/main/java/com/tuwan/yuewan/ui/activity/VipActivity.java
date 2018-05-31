package com.tuwan.yuewan.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.VipDj;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.functions.Action1;

import static com.tuwan.yuewan.nim.uikit.core.NimUIKitImpl.getContext;

public class VipActivity extends BaseActivity {
    //    private TextView tx_help;
//    private ImageView iv_titlebar_back;
    private TextView tv_vip_gxz;
    private HorizontalScrollView scrollvieww;
    private TextView tv_vip_grade;
    private TextView vip_gxz;
    private int currDevote;
    private int currVip;
    private int myDevote;
    private int nextDevote;
    private int nextVip;
    private int prevDevote;
    private int prevVip;
    private TextView tv_vip_next;
    private ImageView iv_hyzsqy_icon1;
    private ImageView iv_hyzsqy_icon2;
    private ImageView iv_hyzsqy_icon3;
    private ImageView iv_hyzsqy_icon4;
    private ImageView iv_hyzsqy_icon5;
    private ImageView iv_hyzsqy_icon6;
    private ImageView iv_hyzsqy_icon7;
    private ImageView iv_hyzsqy_icon8;
    private ImageView iv_hyzsqy_icon9;
    private ImageView iv_hyzsqy_icon10;
    private Handler handler;
    private ProgressBar prog1;
    private ProgressBar progtou;
    private ProgressBar prog2;
    private ProgressBar prog3;
    private ProgressBar prog4;
    private ProgressBar prog5;
    private ProgressBar prog6;
    private ProgressBar prog7;
    private ProgressBar prog8;
    private ProgressBar prog9;
    private ProgressBar prog10;
    private ImageView vip_image1;
    private ImageView vip_image2;
    private TextView vip_tv_devote1;
    private ImageView vip_image3;
    private TextView vip_tv_devote2;
    private ImageView vip_image4;
    private TextView vip_tv_devote3;
    private int widthp;
    private RelativeLayout rel;
    private ImageView vip_image5;
    private TextView vip_tv_devote4;
    private ImageView vip_image6;
    private TextView vip_tv_devote5;
    private ImageView vip_image7;
    private TextView vip_tv_devote6;
    private ImageView vip_image8;
    private TextView vip_tv_devote7;
    private ImageView vip_image9;
    private TextView vip_tv_devote8;
    private ImageView vip_image10;
    private TextView vip_tv_devote9;
    private ImageView vip_image11;
    private TextView vip_tv_devote10;
    private RelativeLayout hyzx;
    private TextView pop_know;
    private TextView pop_js;
    private TextView pop_kq;
    private ImageView popup_icon;
    private ImageView popup_close2;
    private LinearLayout hy_1;
    private LinearLayout hy_2;
    private LinearLayout hy_3;
    private LinearLayout hy_4;
    private LinearLayout hy_5;
    private LinearLayout hy_6;
    private LinearLayout hy_7;
    private LinearLayout hy_8;
    private LinearLayout hy_9;
    private LinearLayout hy_10;
    private RelativeLayout dj1;
    private RelativeLayout dj2;
    private RelativeLayout dj3;
    private RelativeLayout dj4;
    private RelativeLayout dj5;
    private RelativeLayout dj6;
    private RelativeLayout dj7;
    private RelativeLayout dj8;
    private WindowManager wm;
    private RelativeLayout dj9;
    private RelativeLayout dj10;
    private RelativeLayout dj11;
    private ImageView iv_titlebar_back;
    private TextView tv_titlebar_title;
    private int i = 0;
    private int width = 0;
    private int height = 0;
    private TextView tv_vip_openhide1;
    private TextView tv_vip_openhide2;
    private TextView tv_vip_openhide3;
    private TextView tv_vip_openhide4;
    private TextView tv_vip_openhide5;
    private TextView tv_vip_openhide6;
    private TextView tv_vip_openhide7;
    private TextView tv_vip_openhide8;
    private TextView tv_vip_openhide9;
    private TextView tv_vip_openhide10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_vip;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
    }

    @SuppressLint("WrongViewCast")
    private void initView() {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        tv_vip_gxz = (TextView) findViewById(R.id.tv_vip_gxz);
        tv_vip_grade = (TextView) findViewById(R.id.tv_vip_grade);
        vip_gxz = (TextView) findViewById(R.id.vip_gxz);
        tv_vip_next = (TextView) findViewById(R.id.tv_vip_next);
        iv_hyzsqy_icon1 = (ImageView) findViewById(R.id.iv_hyzsqy_icon1);
        iv_hyzsqy_icon2 = (ImageView) findViewById(R.id.iv_hyzsqy_icon2);
        iv_hyzsqy_icon3 = (ImageView) findViewById(R.id.iv_hyzsqy_icon3);
        iv_hyzsqy_icon4 = (ImageView) findViewById(R.id.iv_hyzsqy_icon4);
        iv_hyzsqy_icon5 = (ImageView) findViewById(R.id.iv_hyzsqy_icon5);
        iv_hyzsqy_icon6 = (ImageView) findViewById(R.id.iv_hyzsqy_icon6);
        wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        iv_hyzsqy_icon7 = (ImageView) findViewById(R.id.iv_hyzsqy_icon7);
        iv_hyzsqy_icon8 = (ImageView) findViewById(R.id.iv_hyzsqy_icon8);
        iv_hyzsqy_icon9 = (ImageView) findViewById(R.id.iv_hyzsqy_icon9);
        iv_hyzsqy_icon10 = (ImageView) findViewById(R.id.iv_hyzsqy_icon10);
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        initData();
        prog1 = (ProgressBar) findViewById(R.id.prog1);
        prog2 = (ProgressBar) findViewById(R.id.prog2);
        prog3 = (ProgressBar) findViewById(R.id.prog3);
        scrollvieww = (HorizontalScrollView) findViewById(R.id.scrollvieww);
        progtou = (ProgressBar) findViewById(R.id.progtou);
        prog4 = (ProgressBar) findViewById(R.id.prog4);
        prog5 = (ProgressBar) findViewById(R.id.prog5);
        prog6 = (ProgressBar) findViewById(R.id.prog6);
        prog7 = (ProgressBar) findViewById(R.id.prog7);
        prog8 = (ProgressBar) findViewById(R.id.prog8);
        prog9 = (ProgressBar) findViewById(R.id.prog9);
        prog10 = (ProgressBar) findViewById(R.id.prog10);
        hyzx = (RelativeLayout) findViewById(R.id.hyzx);
        hy_1 = (LinearLayout) findViewById(R.id.hy_1);
        vip_image1 = (ImageView) findViewById(R.id.vip_image1);
        vip_image2 = (ImageView) findViewById(R.id.vip_image2);
        vip_tv_devote1 = (TextView) findViewById(R.id.vip_tv_devote1);
        vip_image3 = (ImageView) findViewById(R.id.vip_image3);
        vip_tv_devote2 = (TextView) findViewById(R.id.vip_tv_devote2);
        vip_image4 = (ImageView) findViewById(R.id.vip_image4);
        vip_tv_devote3 = (TextView) findViewById(R.id.vip_tv_devote3);
        vip_image5 = (ImageView) findViewById(R.id.vip_image5);
        vip_tv_devote4 = (TextView) findViewById(R.id.vip_tv_devote4);
        vip_image6 = (ImageView) findViewById(R.id.vip_image6);
        vip_tv_devote5 = (TextView) findViewById(R.id.vip_tv_devote5);
        vip_image7 = (ImageView) findViewById(R.id.vip_image7);
        vip_tv_devote6 = (TextView) findViewById(R.id.vip_tv_devote6);
        vip_image8 = (ImageView) findViewById(R.id.vip_image8);
        vip_tv_devote7 = (TextView) findViewById(R.id.vip_tv_devote7);
        vip_image9 = (ImageView) findViewById(R.id.vip_image9);
        vip_tv_devote8 = (TextView) findViewById(R.id.vip_tv_devote8);
        vip_image10 = (ImageView) findViewById(R.id.vip_image10);
        vip_tv_devote9 = (TextView) findViewById(R.id.vip_tv_devote9);
        vip_image11 = (ImageView) findViewById(R.id.vip_image11);
        vip_tv_devote10 = (TextView) findViewById(R.id.vip_tv_devote10);
        tv_vip_openhide1 = (TextView) findViewById(R.id.tv_hyzsqy_kq1);
        tv_vip_openhide2 = (TextView) findViewById(R.id.tv_hyzsqy_kq2);
        tv_vip_openhide3 = (TextView) findViewById(R.id.tv_hyzsqy_kq3);
        tv_vip_openhide4= (TextView) findViewById(R.id.tv_hyzsqy_kq4);
        tv_vip_openhide5 = (TextView) findViewById(R.id.tv_hyzsqy_kq5);
        tv_vip_openhide6 = (TextView) findViewById(R.id.tv_hyzsqy_kq6);
        tv_vip_openhide7 = (TextView) findViewById(R.id.tv_hyzsqy_kq7);
        tv_vip_openhide8 = (TextView) findViewById(R.id.tv_hyzsqy_kq8);
        tv_vip_openhide9 = (TextView) findViewById(R.id.tv_hyzsqy_kq9);
        tv_vip_openhide10 = (TextView) findViewById(R.id.tv_hyzsqy_kq10);
        hy_2 = (LinearLayout) findViewById(R.id.hy_2);
        hy_3 = (LinearLayout) findViewById(R.id.hy_3);
        hy_4 = (LinearLayout) findViewById(R.id.hy_4);
        hy_5 = (LinearLayout) findViewById(R.id.hy_5);
        hy_6 = (LinearLayout) findViewById(R.id.hy_6);
        hy_7 = (LinearLayout) findViewById(R.id.hy_7);
        hy_8 = (LinearLayout) findViewById(R.id.hy_8);
        hy_9 = (LinearLayout) findViewById(R.id.hy_9);
        hy_10 = (LinearLayout) findViewById(R.id.hy_10);
        dj1 = (RelativeLayout) findViewById(R.id.dj1);
        dj2 = (RelativeLayout) findViewById(R.id.dj2);
        dj3 = (RelativeLayout) findViewById(R.id.dj3);
        dj4 = (RelativeLayout) findViewById(R.id.dj4);
        dj5 = (RelativeLayout) findViewById(R.id.dj5);
        dj6 = (RelativeLayout) findViewById(R.id.dj6);
        dj7 = (RelativeLayout) findViewById(R.id.dj7);
        dj8 = (RelativeLayout) findViewById(R.id.dj8);
        dj9 = (RelativeLayout) findViewById(R.id.dj9);
        dj10 = (RelativeLayout) findViewById(R.id.dj10);
        dj11 = (RelativeLayout) findViewById(R.id.dj11);
        rel = (RelativeLayout) findViewById(R.id.rel);
        widthp = wm.getDefaultDisplay().getWidth();
    }

    private void initData() {
        RxView.clicks(iv_titlebar_back).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });

        tv_titlebar_title.setText("会员中心");
        getDj();
    }

    public void getDj() {
        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("format", "json");
        RequestBody formBody = builder.build();
        SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
        String cookie = preferences.getString("Cookie", null);
        System.out.println("从SharedPreferences的得到的Cookie" + cookie);
        final Request request = new Request.Builder()
                .url("https://y.tuwan.com/m/User/members")
                .addHeader("Cookie", cookie)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onLoginDone();
                Log.e("获取失败原因", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onLoginDone();
                String result = response.body().string();
                System.out.println("获取返回的数据_得到VIP等级" + result);
                Gson gson = new Gson();
                VipDj fromJson = gson.fromJson(result, VipDj.class);
                VipDj.DataBean data = fromJson.getData();
                currDevote = data.getCurrDevote();//当前等级贡献值
                currVip = data.getCurrVip();//当前等级，也是我的等级
                myDevote = data.getMyDevote();//我的贡献值
                int scrollviewwwidth = vip_image1.getWidth() * 11 + prog1.getWidth() * 10 + progtou.getWidth();
                prog1.setSecondaryProgress(myDevote);

                prog2.setMax(10000);
                prog3.setMax(15000);
                prog4.setMax(36600);
                prog5.setMax(42280);
                prog6.setMax(80000);
                prog7.setMax(100000);
                prog8.setMax(300000);
                prog9.setMax(1300000);
                prog10.setMax(1777780);

                if (myDevote > 5000) {
                    int max = prog1.getMax();
                    int i1 = myDevote - max;
                    prog2.setSecondaryProgress(i1);
                }
                if (myDevote > 15000) {
                    int max1 = prog1.getMax();
                    int max2 = prog2.getMax();
                    int i2 = myDevote - max2 - max1;
                    prog3.setSecondaryProgress(i2);
                }
                if (myDevote > 30000) {
                    int max1 = prog1.getMax();
                    int max2 = prog2.getMax();
                    int max3 = prog3.getMax();
                    int i3 = myDevote - max3 - max2 - max1;
                    prog4.setSecondaryProgress(i3);

                }
                if (myDevote > 66600) {
                    int max1 = prog1.getMax();
                    int max2 = prog2.getMax();
                    int max3 = prog3.getMax();
                    int max4 = prog4.getMax();
                    int i4 = myDevote - max4 - max3 - max2 - max1;
                    prog5.setSecondaryProgress(i4);

                }
                if (myDevote > 108880) {
                    int max1 = prog1.getMax();
                    int max2 = prog2.getMax();
                    int max3 = prog3.getMax();
                    int max4 = prog4.getMax();
                    int max5 = prog5.getMax();
                    int i5 = myDevote - max5 - max4 - max3 - max2 - max1;
                    prog6.setSecondaryProgress(i5);
                }
                if (myDevote > 188880) {
                    int max1 = prog1.getMax();
                    int max2 = prog2.getMax();
                    int max3 = prog3.getMax();
                    int max4 = prog4.getMax();
                    int max5 = prog5.getMax();
                    int max6 = prog6.getMax();
                    int i6 = myDevote - max6 - max5 - max4 - max3 - max2 - max1;
                    prog7.setSecondaryProgress(i6);

                }
                if (myDevote > 288880) {
                    int max1 = prog1.getMax();
                    int max2 = prog2.getMax();
                    int max3 = prog3.getMax();
                    int max4 = prog4.getMax();
                    int max5 = prog5.getMax();
                    int max6 = prog6.getMax();
                    int max7 = prog7.getMax();

                    int i7 = myDevote - max7 - max6 - max5 - max4 - max3 - max2 - max1;
                    prog8.setSecondaryProgress(i7);

                }
                if (myDevote > 588880) {
                    int max1 = prog1.getMax();
                    int max2 = prog2.getMax();
                    int max3 = prog3.getMax();
                    int max4 = prog4.getMax();
                    int max5 = prog5.getMax();
                    int max6 = prog6.getMax();
                    int max7 = prog7.getMax();
                    int max8 = prog8.getMax();

                    int i8 = myDevote - max8 - max7 - max6 - max5 - max4 - max3 - max2 - max1;
                    prog9.setSecondaryProgress(i8);
                }
                if (myDevote > 1888880) {
                    int max1 = prog1.getMax();
                    int max2 = prog2.getMax();
                    int max3 = prog3.getMax();
                    int max4 = prog4.getMax();
                    int max5 = prog5.getMax();
                    int max6 = prog6.getMax();
                    int max7 = prog7.getMax();
                    int max8 = prog8.getMax();
                    int max9 = prog9.getMax();
                    int i9 = myDevote - max9 - max8 - max7 - max6 - max5 - max4 - max3 - max2 - max1;
                    prog10.setSecondaryProgress(i9);
                }
                nextDevote = data.getNextDevote();//下一级贡献值
                nextVip = data.getNextVip();//下一等级，如果为0，则到最高级了
                prevDevote = data.getPrevDevote();//上一级贡献值
                prevVip = data.getPrevVip();//上一等级
                final List<VipDj.DataBean.ViplistBean> viplist = data.getViplist();
                for (int i = 0; i < data.getViplist().size(); i++) {
                    int vip = viplist.get(i).getVip();
                    int devote = viplist.get(i).getDevote();
                    System.out.println("等级：" + viplist.get(5).getVip() + "---------贡献值是：" + devote);
                }
                int width_image1 = vip_image1.getWidth();
                if (currVip == 1) {
                    scrollvieww.scrollTo(width_image1, 0);
                } else if (currVip == 2) {
                    scrollvieww.scrollTo(progtou.getWidth() + prog1.getWidth() * 2 + width_image1 * 3 - widthp / 2 - 20, 0);
                } else if (currVip == 3) {
                    scrollvieww.scrollTo(progtou.getWidth() + prog1.getWidth() * 3 + width_image1 * 4 - widthp / 2 - 20, 0);
                } else if (currVip == 4) {
                    scrollvieww.scrollTo(progtou.getWidth() + prog1.getWidth() * 4 + width_image1 * 5 - widthp / 2 - 20, 0);
                } else if (currVip == 5) {
                    scrollvieww.scrollTo(progtou.getWidth() + prog1.getWidth() * 5 + width_image1 * 6 - widthp / 2 - 20, 0);
                } else if (currVip == 6) {
                    scrollvieww.scrollTo(progtou.getWidth() + prog1.getWidth() * 6 + width_image1 * 7 - widthp / 2 - 20, 0);
                } else if (currVip == 7) {
                    scrollvieww.scrollTo(progtou.getWidth() + prog1.getWidth() * 7 + width_image1 * 8 - widthp / 2 - 20, 0);
                } else if (currVip == 8) {
                    scrollvieww.scrollTo(progtou.getWidth() + prog1.getWidth() * 8 + width_image1 * 9 - widthp / 2 - 20, 0);
                } else if (currVip == 9) {
                    scrollvieww.scrollTo(progtou.getWidth() + prog1.getWidth() * 9 + width_image1 * 10 - widthp / 2 - 20, 0);
                } else if (currVip == 10) {
                    scrollvieww.scrollTo(dj1.getWidth() * 10 + prog1.getWidth() * 9, 0);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (nextVip == 0) {
                            if (currDevote - myDevote < 0) {
                                tv_vip_next.setText("当前贡献值为");
                                tv_vip_gxz.setText("" + myDevote);
                            } else {
                                tv_vip_next.setText("当前贡献值为");
                                tv_vip_gxz.setText("" + myDevote);
                            }

                        } else if (nextVip != 0) {
                            tv_vip_next.setText("距离升VIP" + nextVip + "还差");
                            tv_vip_gxz.setText("" + (nextDevote - myDevote));
                        }
                        tv_vip_grade.setText("VIP" + currVip);
                        vip_gxz.setText(myDevote + "");
                        vip_tv_devote1.setText(viplist.get(1).getDevote() + "");
                        vip_tv_devote2.setText(viplist.get(2).getDevote() + "");
                        vip_tv_devote3.setText(viplist.get(3).getDevote() + "");
                        vip_tv_devote4.setText(viplist.get(4).getDevote() + "");
                        vip_tv_devote5.setText(viplist.get(5).getDevote() + "");
                        vip_tv_devote6.setText(viplist.get(6).getDevote() + "");
                        vip_tv_devote7.setText(viplist.get(7).getDevote() + "");
                        vip_tv_devote8.setText(viplist.get(8).getDevote() + "");
                        vip_tv_devote9.setText(viplist.get(9).getDevote() + "");
                        vip_tv_devote10.setText(viplist.get(10).getDevote() + "");
                        hideDevoteText(currVip);
                        if (currVip == 0) {
                            vip_image1.setImageResource(R.drawable.commoner2);
                        }
                        if (currVip == 1) {
                            iv_hyzsqy_icon1.setImageResource(R.drawable.medal_active);
                            vip_image2.setImageResource(R.drawable.knight2);
                        }
                        if (currVip == 2) {
                            vip_image3.setImageResource(R.drawable.baron2);
                            iv_hyzsqy_icon1.setImageResource(R.drawable.medal_active);
                            iv_hyzsqy_icon2.setImageResource(R.drawable.present_active);

                        }
                        if (currVip == 3) {
                            vip_image4.setImageResource(R.drawable.viscount2);
                            iv_hyzsqy_icon1.setImageResource(R.drawable.medal_active);
                            iv_hyzsqy_icon2.setImageResource(R.drawable.present_active);
                            iv_hyzsqy_icon3.setImageResource(R.drawable.chatroom_active);
                        }
                        if (currVip == 4) {
                            vip_image5.setImageResource(R.drawable.earl2);
                            iv_hyzsqy_icon1.setImageResource(R.drawable.medal_active);
                            iv_hyzsqy_icon2.setImageResource(R.drawable.present_active);
                            iv_hyzsqy_icon3.setImageResource(R.drawable.chatroom_active);
                            iv_hyzsqy_icon4.setImageResource(R.drawable.customerservice_active);
                            int width_image4 = vip_image4.getWidth();
                        }
                        if (currVip == 5) {
                            vip_image6.setImageResource(R.drawable.marquis2);
                            iv_hyzsqy_icon1.setImageResource(R.drawable.medal_active);
                            iv_hyzsqy_icon2.setImageResource(R.drawable.present_active);
                            iv_hyzsqy_icon3.setImageResource(R.drawable.chatroom_active);
                            iv_hyzsqy_icon4.setImageResource(R.drawable.customerservice_active);
                            iv_hyzsqy_icon5.setImageResource(R.drawable.birth_active);
                        }
                        if (currVip == 6) {
                            vip_image7.setImageResource(R.drawable.duke2);
                            iv_hyzsqy_icon1.setImageResource(R.drawable.medal_active);
                            iv_hyzsqy_icon2.setImageResource(R.drawable.present_active);
                            iv_hyzsqy_icon3.setImageResource(R.drawable.chatroom_active);
                            iv_hyzsqy_icon4.setImageResource(R.drawable.customerservice_active);
                            iv_hyzsqy_icon5.setImageResource(R.drawable.birth_active);
                            iv_hyzsqy_icon6.setImageResource(R.drawable.stealth_active);
                        }
                        if (currVip == 7) {
                            vip_image8.setImageResource(R.drawable.prince2);
                            iv_hyzsqy_icon1.setImageResource(R.drawable.medal_active);
                            iv_hyzsqy_icon1.setImageResource(R.drawable.medal_active);
                            iv_hyzsqy_icon2.setImageResource(R.drawable.present_active);
                            iv_hyzsqy_icon3.setImageResource(R.drawable.chatroom_active);
                            iv_hyzsqy_icon4.setImageResource(R.drawable.customerservice_active);
                            iv_hyzsqy_icon5.setImageResource(R.drawable.birth_active);
                            iv_hyzsqy_icon6.setImageResource(R.drawable.stealth_active);
                            iv_hyzsqy_icon7.setImageResource(R.drawable.contrubution_active);
                        }
                        if (currVip == 8) {
                            vip_image9.setImageResource(R.drawable.king12);
                            iv_hyzsqy_icon1.setImageResource(R.drawable.medal_active);
                            iv_hyzsqy_icon1.setImageResource(R.drawable.medal_active);
                            iv_hyzsqy_icon2.setImageResource(R.drawable.present_active);
                            iv_hyzsqy_icon3.setImageResource(R.drawable.chatroom_active);
                            iv_hyzsqy_icon4.setImageResource(R.drawable.customerservice_active);
                            iv_hyzsqy_icon5.setImageResource(R.drawable.birth_active);
                            iv_hyzsqy_icon6.setImageResource(R.drawable.stealth_active);
                            iv_hyzsqy_icon7.setImageResource(R.drawable.contrubution_active);
                            iv_hyzsqy_icon8.setImageResource(R.drawable.banned_active);
                        }
                        if (currVip == 9) {
                            vip_image10.setImageResource(R.drawable.king22);
                            iv_hyzsqy_icon1.setImageResource(R.drawable.medal_active);
                            iv_hyzsqy_icon1.setImageResource(R.drawable.medal_active);
                            iv_hyzsqy_icon2.setImageResource(R.drawable.present_active);
                            iv_hyzsqy_icon3.setImageResource(R.drawable.chatroom_active);
                            iv_hyzsqy_icon4.setImageResource(R.drawable.customerservice_active);
                            iv_hyzsqy_icon5.setImageResource(R.drawable.birth_active);
                            iv_hyzsqy_icon6.setImageResource(R.drawable.stealth_active);
                            iv_hyzsqy_icon7.setImageResource(R.drawable.contrubution_active);
                            iv_hyzsqy_icon8.setImageResource(R.drawable.banned_active);
                            iv_hyzsqy_icon9.setImageResource(R.drawable.diygift_active);
                        }
                        if (currVip >= 10) {
                            vip_image11.setImageResource(R.drawable.emperor2);
                            iv_hyzsqy_icon1.setImageResource(R.drawable.medal_active);
                            iv_hyzsqy_icon1.setImageResource(R.drawable.medal_active);
                            iv_hyzsqy_icon2.setImageResource(R.drawable.present_active);
                            iv_hyzsqy_icon3.setImageResource(R.drawable.chatroom_active);
                            iv_hyzsqy_icon4.setImageResource(R.drawable.customerservice_active);
                            iv_hyzsqy_icon5.setImageResource(R.drawable.birth_active);
                            iv_hyzsqy_icon6.setImageResource(R.drawable.stealth_active);
                            iv_hyzsqy_icon7.setImageResource(R.drawable.contrubution_active);
                            iv_hyzsqy_icon8.setImageResource(R.drawable.banned_active);
                            iv_hyzsqy_icon9.setImageResource(R.drawable.diygift_active);
                            iv_hyzsqy_icon10.setImageResource(R.drawable.hot_active);
                        }
                        hy_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showVip("VIP1开启", "可以获得贵族勋章标识");
                            }
                        });
                        hy_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showVip("VIP2开启", "可以发送专属贵族礼物");
                            }
                        });
                        hy_3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showVip("VIP3开启", "可以在进入聊天室有特效");
                            }
                        });
                        hy_4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showVip("VIP4开启", "可以获得专属客服");
                            }
                        });
                        hy_5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showVip("VIP5开启", "可以获得生日的祝福");
                            }
                        });
                        hy_6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showVip("VIP6开启", "可以在进入聊天室隐身");
                            }
                        });
                        hy_7.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showVip("VIP7开启", "可以在贡献榜上隐身");
                            }
                        });
                        hy_8.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showVip("VIP8开启", "可以防止被禁言");
                            }
                        });
                        hy_9.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showVip("VIP9开启", "可以获得定制礼物的权利");
                            }
                        });
                        hy_10.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showVip("VIP10开启", "可以推荐导师上热门");
                            }
                        });
                    }
                });
            }
        });
    }

    private void showVip(String vipLevel, String content) {
        View view = View.inflate(VipActivity.this, R.layout.pop_hyqi, null);
        final PopupWindow window = new PopupWindow(view, width, height);
        LinearLayout lly_vip = (LinearLayout) view.findViewById(R.id.lly_vip);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width / 4 * 3, ViewGroup.LayoutParams.WRAP_CONTENT);
        lly_vip.setLayoutParams(params);
        pop_know = (TextView) view.findViewById(R.id.pop_know);
        pop_js = (TextView) view.findViewById(R.id.pop_js);
        pop_kq = (TextView) view.findViewById(R.id.pop_kq);
        popup_icon = (ImageView) view.findViewById(R.id.popup_icon);
        popup_close2 = (ImageView) view.findViewById(R.id.popup_close2);
        pop_kq.setText(vipLevel);
        pop_js.setText(content);
        RxView.clicks(popup_close2)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        window.dismiss();
                    }
                });
        RxView.clicks(pop_know)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        window.dismiss();
                    }
                });
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        window.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }

    //隐藏（vip开启）文字提示
    private void hideDevoteText(int currVip) {

        switch (currVip) {
            case 0:
                tv_vip_openhide1.setVisibility(View.VISIBLE);
                tv_vip_openhide2.setVisibility(View.VISIBLE);
                tv_vip_openhide3.setVisibility(View.VISIBLE);
                tv_vip_openhide4.setVisibility(View.VISIBLE);
                tv_vip_openhide5.setVisibility(View.VISIBLE);
                tv_vip_openhide6.setVisibility(View.VISIBLE);
                tv_vip_openhide7.setVisibility(View.VISIBLE);
                tv_vip_openhide8.setVisibility(View.VISIBLE);
                tv_vip_openhide9.setVisibility(View.VISIBLE);
                tv_vip_openhide10.setVisibility(View.VISIBLE);
                break;
            case 1:
                tv_vip_openhide1.setVisibility(View.GONE);
                tv_vip_openhide2.setVisibility(View.VISIBLE);
                tv_vip_openhide3.setVisibility(View.VISIBLE);
                tv_vip_openhide4.setVisibility(View.VISIBLE);
                tv_vip_openhide5.setVisibility(View.VISIBLE);
                tv_vip_openhide6.setVisibility(View.VISIBLE);
                tv_vip_openhide7.setVisibility(View.VISIBLE);
                tv_vip_openhide8.setVisibility(View.VISIBLE);
                tv_vip_openhide9.setVisibility(View.VISIBLE);
                tv_vip_openhide10.setVisibility(View.VISIBLE);
                break;
            case 2:
                tv_vip_openhide1.setVisibility(View.GONE);
                tv_vip_openhide2.setVisibility(View.GONE);
                tv_vip_openhide3.setVisibility(View.VISIBLE);
                tv_vip_openhide4.setVisibility(View.VISIBLE);
                tv_vip_openhide5.setVisibility(View.VISIBLE);
                tv_vip_openhide6.setVisibility(View.VISIBLE);
                tv_vip_openhide7.setVisibility(View.VISIBLE);
                tv_vip_openhide8.setVisibility(View.VISIBLE);
                tv_vip_openhide9.setVisibility(View.VISIBLE);
                tv_vip_openhide10.setVisibility(View.VISIBLE);
                break;
            case 3:
                tv_vip_openhide1.setVisibility(View.GONE);
                tv_vip_openhide2.setVisibility(View.GONE);
                tv_vip_openhide3.setVisibility(View.GONE);
                tv_vip_openhide4.setVisibility(View.VISIBLE);
                tv_vip_openhide5.setVisibility(View.VISIBLE);
                tv_vip_openhide6.setVisibility(View.VISIBLE);
                tv_vip_openhide7.setVisibility(View.VISIBLE);
                tv_vip_openhide8.setVisibility(View.VISIBLE);
                tv_vip_openhide9.setVisibility(View.VISIBLE);
                tv_vip_openhide10.setVisibility(View.VISIBLE);
                break;
            case 4:
                tv_vip_openhide1.setVisibility(View.GONE);
                tv_vip_openhide2.setVisibility(View.GONE);
                tv_vip_openhide3.setVisibility(View.GONE);
                tv_vip_openhide4.setVisibility(View.GONE);
                tv_vip_openhide5.setVisibility(View.VISIBLE);
                tv_vip_openhide6.setVisibility(View.VISIBLE);
                tv_vip_openhide7.setVisibility(View.VISIBLE);
                tv_vip_openhide8.setVisibility(View.VISIBLE);
                tv_vip_openhide9.setVisibility(View.VISIBLE);
                tv_vip_openhide10.setVisibility(View.VISIBLE);
                break;
            case 5:
                tv_vip_openhide1.setVisibility(View.GONE);
                tv_vip_openhide2.setVisibility(View.GONE);
                tv_vip_openhide3.setVisibility(View.GONE);
                tv_vip_openhide4.setVisibility(View.GONE);
                tv_vip_openhide5.setVisibility(View.GONE);
                tv_vip_openhide6.setVisibility(View.VISIBLE);
                tv_vip_openhide7.setVisibility(View.VISIBLE);
                tv_vip_openhide8.setVisibility(View.VISIBLE);
                tv_vip_openhide9.setVisibility(View.VISIBLE);
                tv_vip_openhide10.setVisibility(View.VISIBLE);
                break;
            case 6:
                tv_vip_openhide1.setVisibility(View.GONE);
                tv_vip_openhide2.setVisibility(View.GONE);
                tv_vip_openhide3.setVisibility(View.GONE);
                tv_vip_openhide4.setVisibility(View.GONE);
                tv_vip_openhide5.setVisibility(View.GONE);
                tv_vip_openhide6.setVisibility(View.GONE);
                tv_vip_openhide7.setVisibility(View.VISIBLE);
                tv_vip_openhide8.setVisibility(View.VISIBLE);
                tv_vip_openhide9.setVisibility(View.VISIBLE);
                tv_vip_openhide10.setVisibility(View.VISIBLE);
                break;
            case 7:
                tv_vip_openhide1.setVisibility(View.GONE);
                tv_vip_openhide2.setVisibility(View.GONE);
                tv_vip_openhide3.setVisibility(View.GONE);
                tv_vip_openhide4.setVisibility(View.GONE);
                tv_vip_openhide5.setVisibility(View.GONE);
                tv_vip_openhide6.setVisibility(View.GONE);
                tv_vip_openhide7.setVisibility(View.GONE);
                tv_vip_openhide8.setVisibility(View.VISIBLE);
                tv_vip_openhide9.setVisibility(View.VISIBLE);
                tv_vip_openhide10.setVisibility(View.VISIBLE);
                break;
            case 8:
                tv_vip_openhide1.setVisibility(View.GONE);
                tv_vip_openhide2.setVisibility(View.GONE);
                tv_vip_openhide3.setVisibility(View.GONE);
                tv_vip_openhide4.setVisibility(View.GONE);
                tv_vip_openhide5.setVisibility(View.GONE);
                tv_vip_openhide6.setVisibility(View.GONE);
                tv_vip_openhide7.setVisibility(View.GONE);
                tv_vip_openhide8.setVisibility(View.GONE);
                tv_vip_openhide9.setVisibility(View.VISIBLE);
                tv_vip_openhide10.setVisibility(View.VISIBLE);
                break;
            case 9:
                tv_vip_openhide1.setVisibility(View.GONE);
                tv_vip_openhide2.setVisibility(View.GONE);
                tv_vip_openhide3.setVisibility(View.GONE);
                tv_vip_openhide4.setVisibility(View.GONE);
                tv_vip_openhide5.setVisibility(View.GONE);
                tv_vip_openhide6.setVisibility(View.GONE);
                tv_vip_openhide7.setVisibility(View.GONE);
                tv_vip_openhide8.setVisibility(View.GONE);
                tv_vip_openhide9.setVisibility(View.GONE);
                tv_vip_openhide10.setVisibility(View.VISIBLE);
                break;
            case 10:
                tv_vip_openhide1.setVisibility(View.GONE);
                tv_vip_openhide2.setVisibility(View.GONE);
                tv_vip_openhide3.setVisibility(View.GONE);
                tv_vip_openhide4.setVisibility(View.GONE);
                tv_vip_openhide5.setVisibility(View.GONE);
                tv_vip_openhide6.setVisibility(View.GONE);
                tv_vip_openhide7.setVisibility(View.GONE);
                tv_vip_openhide8.setVisibility(View.GONE);
                tv_vip_openhide9.setVisibility(View.GONE);
                tv_vip_openhide10.setVisibility(View.GONE);
                break;

        }

    }

}
