package com.tuwan.yuewan.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.AlipayBean;
import com.tuwan.yuewan.entity.Code;
import com.tuwan.yuewan.entity.Diamond;
import com.tuwan.yuewan.entity.PayResult;
import com.tuwan.yuewan.entity.pay.AlipayRecharge;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.activity.order.RechargeActivity;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.SharedPreferencesHelper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DiamondActivity extends BaseActivity {
    private TextView diamond_num;
    private TextView dia_mon1;
    private LinearLayout my_linear1;
    private TextView dia_mon2;
    private LinearLayout my_linear2;
    private TextView dia_mon3;
    private LinearLayout my_linear3;
    private TextView dia_mon4;
    private LinearLayout my_linear4;
    private TextView dia_mon5;
    private LinearLayout my_linear5;
    private TextView dia_mon6;
    private LinearLayout my_linear6;
    private ImageView backss;
    private TextView zh_jilu;
    private Button need_pays;
    private RadioButton rb_paytype_wechat;
    private RadioButton rb_paytype_alipay;
    private RadioButton rb_paytype_ywb;
    private String number;
    private String numbers;
    private String cookie;
    private TextView diamond1;
    private TextView diamond2;
    private TextView diamond3;
    private TextView diamond4;
    private TextView diamond5;
    private TextView diamond6;
    private String diamond;
    private Drawable diamondDrawable;
    private Drawable payDrawableWX;
    private Drawable payDrawableALI;
    private Drawable payDrawableTW;
    private Drawable select_nomal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        diamondDrawable = getResources().getDrawable(R.drawable.daimon_icon2x);
        diamondDrawable.setBounds(0,0,36,28);
        final int payWH = 72;
        payDrawableWX = getResources().getDrawable(R.drawable.pay_wechat2x);
        payDrawableWX.setBounds(0,0,payWH,payWH);
        payDrawableALI = getResources().getDrawable(R.drawable.pay_alipay2x);
        payDrawableALI.setBounds(0,0,payWH,payWH);
        payDrawableTW = getResources().getDrawable(R.drawable.pay_coin2x);
        payDrawableTW.setBounds(0,0,payWH,payWH);
        final int selectWH = 60;
        select_nomal = getResources().getDrawable(R.drawable.selector_paytype_right);
        select_nomal.setBounds(0,0,selectWH,selectWH);
        initView();
    }
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
    @Override
    protected int getContentViewId() {
        return R.layout.activity_diamond;
    }
    @Override
    protected void customInit(Bundle savedInstanceState) {
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.me_zuanshi_title));
    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    private void initView() {
        Intent intent = getIntent();
        diamond = intent.getStringExtra("diamond");
        SharedPreferencesHelper helper = new SharedPreferencesHelper(DiamondActivity.this);
        cookie = (String) helper.get("Cookie", null);
        numbers = 6 + "";
        diamond_num = (TextView) findViewById(R.id.diamond_num);
        dia_mon1 = (TextView) findViewById(R.id.dia_mon1);
        my_linear1 = (LinearLayout) findViewById(R.id.my_linear1);
        dia_mon2 = (TextView) findViewById(R.id.dia_mon2);
        my_linear2 = (LinearLayout) findViewById(R.id.my_linear2);
        dia_mon3 = (TextView) findViewById(R.id.dia_mon3);
        my_linear3 = (LinearLayout) findViewById(R.id.my_linear3);
        dia_mon4 = (TextView) findViewById(R.id.dia_mon4);
        my_linear4 = (LinearLayout) findViewById(R.id.my_linear4);
        dia_mon5 = (TextView) findViewById(R.id.dia_mon5);
        my_linear5 = (LinearLayout) findViewById(R.id.my_linear5);
        dia_mon6 = (TextView) findViewById(R.id.dia_mon6);
        my_linear6 = (LinearLayout) findViewById(R.id.my_linear6);
        backss = (ImageView) findViewById(R.id.backss);
        zh_jilu = (TextView) findViewById(R.id.zh_jilu);
        need_pays = (Button) findViewById(R.id.need_pays);
        rb_paytype_wechat = (RadioButton) findViewById(R.id.rb_paytype_wechat);
        rb_paytype_alipay = (RadioButton) findViewById(R.id.rb_paytype_alipay);
        rb_paytype_ywb = (RadioButton) findViewById(R.id.rb_paytype_ywb);
        diamond1 = (TextView) findViewById(R.id.diamond1);
        diamond2 = (TextView) findViewById(R.id.diamond2);
        diamond3 = (TextView) findViewById(R.id.diamond3);
        diamond4 = (TextView) findViewById(R.id.diamond4);
        diamond5 = (TextView) findViewById(R.id.diamond5);
        diamond6 = (TextView) findViewById(R.id.diamond6);
//        rb_paytype_wechat.setCompoundDrawables(payDrawableWX, null,select_nomal,null);
//        rb_paytype_alipay.setCompoundDrawables(payDrawableALI, null,select_nomal,null);
//        rb_paytype_ywb.setCompoundDrawables(payDrawableTW, null,select_nomal,null);
        diamond1.setCompoundDrawables(diamondDrawable, null,null,null);
        diamond2.setCompoundDrawables(diamondDrawable, null,null,null);
        diamond3.setCompoundDrawables(diamondDrawable, null,null,null);
        diamond4.setCompoundDrawables(diamondDrawable, null,null,null);
        diamond5.setCompoundDrawables(diamondDrawable, null,null,null);
        diamond6.setCompoundDrawables(diamondDrawable, null,null,null);
        diamond_num.setText(diamond);
        initData();
        click();

    }

    private void initData() {
        String url = "https://y.tuwan.com/m/Diamond/paylist?format=json&type=android";
        OkManager.getInstance().getStrings(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result = response.body().string();
                            Gson gson = new Gson();
                            Diamond diamond = gson.fromJson(result, Diamond.class);
                            List<Diamond.DataBean> bean = diamond.getData();
                            for (int i = 0; i < bean.size(); i++) {
                                diamond1.setText(bean.get(0).getDiamond() + "");
                                diamond2.setText(bean.get(1).getDiamond() + "");
                                diamond3.setText(bean.get(2).getDiamond() + "");
                                diamond4.setText(bean.get(3).getDiamond() + "");
                                diamond5.setText(bean.get(4).getDiamond() + "");
                                diamond6.setText(bean.get(5).getDiamond() + "");
                                dia_mon1.setText("/￥" + bean.get(0).getMoney() + "");
                                dia_mon2.setText("/￥" + bean.get(1).getMoney() + "");
                                dia_mon3.setText("/￥" + bean.get(2).getMoney() + "");
                                dia_mon4.setText("/￥" + bean.get(3).getMoney() + "");
                                dia_mon5.setText("/￥" + bean.get(4).getMoney() + "");
                                dia_mon6.setText("/￥" + bean.get(5).getMoney() + "");
                            }
                        } catch (Exception e) {
                            ToastUtils.getInstance().showToast("正在反馈，请稍后...");
                        }
                    }
                });
            }
        });
    }
    private void click() {
        backss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        zh_jilu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiamondActivity.this, WechatRecordActivity.class);
                startActivity(intent);
            }
        });
        my_linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_linear1.setBackgroundResource(R.drawable.selector_chongzhi_background1);
                my_linear2.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear3.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear4.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear5.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear6.setBackgroundResource(R.drawable.selector_chongzhi_background);
                number = dia_mon1.getText().toString();
                numbers = number.substring(2);
                need_pays.setText("需支付" + numbers + "元");
            }
        });
        my_linear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_linear1.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear2.setBackgroundResource(R.drawable.selector_chongzhi_background1);
                my_linear3.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear4.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear5.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear6.setBackgroundResource(R.drawable.selector_chongzhi_background);
                number = dia_mon2.getText().toString();
                numbers = number.substring(2);
                need_pays.setText("需支付" + numbers + "元");
            }
        });
        my_linear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_linear1.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear2.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear3.setBackgroundResource(R.drawable.selector_chongzhi_background1);
                my_linear4.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear5.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear6.setBackgroundResource(R.drawable.selector_chongzhi_background);
                number = dia_mon3.getText().toString();
                numbers = number.substring(2);
                need_pays.setText("需支付" + numbers + "元");
            }
        });
        my_linear4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_linear1.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear2.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear3.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear4.setBackgroundResource(R.drawable.selector_chongzhi_background1);
                my_linear5.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear6.setBackgroundResource(R.drawable.selector_chongzhi_background);
                number = dia_mon4.getText().toString();
                numbers = number.substring(2);
                need_pays.setText("需支付" + numbers + "元");
            }
        });
        my_linear5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_linear1.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear2.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear3.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear4.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear5.setBackgroundResource(R.drawable.selector_chongzhi_background1);
                my_linear6.setBackgroundResource(R.drawable.selector_chongzhi_background);
                number = dia_mon5.getText().toString();
                numbers = number.substring(2);
                need_pays.setText("需支付" + numbers + "元");
            }
        });
        my_linear6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_linear1.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear2.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear3.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear4.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear5.setBackgroundResource(R.drawable.selector_chongzhi_background);
                my_linear6.setBackgroundResource(R.drawable.selector_chongzhi_background1);
                number = dia_mon6.getText().toString();
                numbers = number.substring(2);
                need_pays.setText("需支付" + numbers + "元");
            }
        });
//        if(PreventDoubleClickUtil.noDoubleClick())
            need_pays.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    v.setEnabled(false);
                    // 确实支付
                    if (rb_paytype_ywb.isChecked()) {
                        //余额支付
                        balancePay();
                    } else if (rb_paytype_alipay.isChecked()) {
                        //支付宝支付

                        serviceAlipay();
                    } else if (rb_paytype_wechat.isChecked()) {
                        //微信支付
                        weChatPay();
                    }
                }
            });
    }
    private void weChatPay() {
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .wxpay("json", numbers, 2003, 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<AlipayRecharge>() {
                    @Override
                    public void onNext(@NonNull AlipayRecharge result) {
                        super.onNext(result);
                        need_pays.setEnabled(true);
                        if (result.getError() == 0) {
                            paywx(result.getData());
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                    }
                });
    }
    /**
     * 微信支付错误检测 提示语
     */
    private static final String WX_PAY_ERRMSG_1 = "您没有安装微信...";
    private static final String WX_PAY_ERRMSG_2 = "当前版本不支持支付功能...";
    private boolean canPay;
    private void paywx(AlipayRecharge.DataBean data) {
//调起支付
        IWXAPI mIWXAPI = WXAPIFactory.createWXAPI(DiamondActivity.this, data.getAppid());
        mIWXAPI.registerApp(data.getAppid());
        canPay = true;
        if (!mIWXAPI.isWXAppInstalled()) {
            ToastUtils.getInstance().showToast(WX_PAY_ERRMSG_1);
            canPay = false;
        } else if (!mIWXAPI.isWXAppSupportAPI()) {
            ToastUtils.getInstance().showToast(WX_PAY_ERRMSG_2);
            canPay = false;
        }
        try {
            if (canPay) {
                //一下所有的参数上面均获取到了
                PayReq req = new PayReq();
                req.appId = data.getAppid();
                req.partnerId = data.getPartnerid();
                req.prepayId = data.getPrepayid();
                req.nonceStr = data.getNoncestr();
                req.timeStamp = data.getTimestamp() + "";
                req.packageValue = data.getPackageX();
                req.sign = data.getSign();
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                Log.e("微信支付调起结果", "paywx: " + data.getAppid() + "-----" + data.getPartnerid() + "-----" + data.getPrepayid() + "-----" + data.getNoncestr() + "-----" + +data.getTimestamp() + "-----" + data.getPackageX() + "-----" + data.getSign());
                //mIWXAPI.registerApp(Constants.APP_ID);
                mIWXAPI.sendReq(req);
                Log.e( "微信返回: ", req.checkArgs()+"");
            }
        } catch (Exception e) {
            Log.e("PAY_GET", "异常：" + e.getMessage());
        }
    }
    private void balancePay() {
        ToastUtils.getInstance().showToast("余额支付");
        need_pays.setEnabled(true);
        final String balancePay = "https://y.tuwan.com/m/Diamond/recharge?format=json&money=" + Integer.parseInt(numbers);
        OkManager.getInstance().getString(balancePay, cookie, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                need_pays.setEnabled(true);
                Log.e("余额钻石充值失败原因 ", e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    Log.e("余额钻石充值成功原因 ", result);
                        Gson gson = new Gson();
                        Code code = gson.fromJson(result, Code.class);
                    if (code.getError() == 0) {
//                        Intent intent = new Intent(DiamondActivity.this, RechargeActivity.class);
//                        intent.putExtra("money", numbers + "");
//                        startActivity(intent);
                            onPaySuccess();
                        } else if (code.getError() == 2) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.getInstance().showToast("余额不足，请重新选择支付方式");
                                }
                            });
                        }
                } catch (Exception e) {
                    Log.e("余额钻石充值成功失败原因: ", e.toString());
                }
            }
        });
    }
    private void serviceAlipay() {
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .aliapppay("json", numbers, 2003, 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<AlipayBean>() {
                    @Override
                    public void onNext(@NonNull AlipayBean result) {
                        super.onNext(result);
                        need_pays.setEnabled(true);
                        if (result.getError() == 0) {
                            payV2(result.data);
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                    }
                });
    }
    private static final int SDK_PAY_FLAG = 1;
    /**
     * 支付宝支付业务
     */
    public void payV2(final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(DiamondActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        //Toast.makeText(DiamondActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        onPaySuccess();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(DiamondActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };
    /**
     * 支付成功时的逻辑
     */
    private void onPaySuccess() {
//        RechargeActivity.show(BalanceActivity.this);
        Intent intent = new Intent(DiamondActivity.this, RechargeActivity.class);
        intent.putExtra("money", numbers + "");
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }
}