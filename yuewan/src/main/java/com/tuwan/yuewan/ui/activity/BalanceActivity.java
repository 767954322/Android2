package com.tuwan.yuewan.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.flyco.systembar.SystemBarHelper;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tuwan.common.LibraryApplication;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.entity.AlipayBean;
import com.tuwan.yuewan.entity.PayResult;
import com.tuwan.yuewan.entity.pay.AlipayRecharge;

import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.activity.order.RechargeActivity;
import com.tuwan.yuewan.utils.SharedPreferencesHelper;
import android.widget.LinearLayout;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class BalanceActivity extends BaseActivity {

    private TextView zh_jilu;
    private TextView wushi;
    private TextView yibai;
    private TextView wubai;
    private TextView yiqian;
    private LinearLayout layout1,layout2;
    private TextView sanqian;
    private TextView liuliuliuliu;
    private EditText custom_chongzhi;
    private ImageView wechat_choose;
    private TextView textView3;
    private ImageView alipay_choose;
    private String number = "50元";
    private TextView need_pay;
    private int flag = 1;
    private ImageView ic_arrow_back_black;
    private String cookie;
    private AlipayRecharge.DataBean data;
    private TextView another;
    private LinearLayout lly_custom_chongzhi;
    private double currency;
    private int viewType = 0;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_balance;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.me_yue_title));
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        currency = intent.getDoubleExtra("currency", (double) 0);
        SharedPreferencesHelper helper = new SharedPreferencesHelper(BalanceActivity.this);
        cookie = (String) helper.get("Cookie", null);
        zh_jilu = (TextView) findViewById(R.id.zh_jilu);
        wushi = (TextView) findViewById(R.id.wushi);
        yibai = (TextView) findViewById(R.id.yibai);
        wubai = (TextView) findViewById(R.id.wubai);
        yiqian = (TextView) findViewById(R.id.yiqian);
        sanqian = (TextView) findViewById(R.id.sanqian);
        liuliuliuliu = (TextView) findViewById(R.id.liuliuliuliu);
        custom_chongzhi = (EditText) findViewById(R.id.custom_chongzhi);
        wechat_choose = (ImageView) findViewById(R.id.wechat_choose);
        alipay_choose = (ImageView) findViewById(R.id.alipay_choose);
        need_pay = (TextView) findViewById(R.id.need_pay);
        ic_arrow_back_black = (ImageView) findViewById(R.id.ic_arrow_back_black);
        another = (TextView) findViewById(R.id.another);
        layout1= (LinearLayout) findViewById(R.id.layout1);
        layout2= (LinearLayout) findViewById(R.id.layout2);
        lly_custom_chongzhi = (LinearLayout) findViewById(R.id.lly_custom_chongzhi);

        ViewTreeObserver vto = wushi.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (viewType == 0) {
                    int marWidth = getLocation(wushi);
                    LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, DensityUtils.dp2px(LibraryApplication.getInstance(), 40));
                    layout.setMargins(marWidth, 0, 0, 0);  //设置间距
                    lly_custom_chongzhi.setLayoutParams(layout);
                    lly_custom_chongzhi.setVisibility(View.VISIBLE);
                    viewType = 1;
                }
            }
        });
        another.setText("￥ " + currency + "");
        click();

    }

    // 获取控件间距
    public int getLocation(View v) {
        int loc = 0;
        loc = v.getLeft();

        return loc;
    }

    private void click() {
        ic_arrow_back_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        zh_jilu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BalanceActivity.this, WechatRecordActivity.class);
                startActivity(intent);
            }
        });
        wushi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wushi.setBackgroundResource(R.drawable.selector_chongzhi_background1);
                yibai.setBackgroundResource(R.drawable.selector_chongzhi_background);
                wubai.setBackgroundResource(R.drawable.selector_chongzhi_background);
                yiqian.setBackgroundResource(R.drawable.selector_chongzhi_background);
                sanqian.setBackgroundResource(R.drawable.selector_chongzhi_background);
                custom_chongzhi.setBackgroundResource(R.drawable.selector_chongzhi_background);
                liuliuliuliu.setBackgroundResource(R.drawable.selector_chongzhi_background);
                number = wushi.getText().toString();
                need_pay.setText("需支付" + number);
            }
        });
        yibai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wushi.setBackgroundResource(R.drawable.selector_chongzhi_background);
                yibai.setBackgroundResource(R.drawable.selector_chongzhi_background1);
                wubai.setBackgroundResource(R.drawable.selector_chongzhi_background);
                yiqian.setBackgroundResource(R.drawable.selector_chongzhi_background);
                sanqian.setBackgroundResource(R.drawable.selector_chongzhi_background);
                liuliuliuliu.setBackgroundResource(R.drawable.selector_chongzhi_background);
                custom_chongzhi.setBackgroundResource(R.drawable.selector_chongzhi_background);
                number = yibai.getText().toString();
                need_pay.setText("需支付" + number);
            }
        });
        wubai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wushi.setBackgroundResource(R.drawable.selector_chongzhi_background);
                yibai.setBackgroundResource(R.drawable.selector_chongzhi_background);
                wubai.setBackgroundResource(R.drawable.selector_chongzhi_background1);
                yiqian.setBackgroundResource(R.drawable.selector_chongzhi_background);
                sanqian.setBackgroundResource(R.drawable.selector_chongzhi_background);
                liuliuliuliu.setBackgroundResource(R.drawable.selector_chongzhi_background);
                custom_chongzhi.setBackgroundResource(R.drawable.selector_chongzhi_background);
                number = wubai.getText().toString();
                need_pay.setText("需支付" + number);
            }
        });
        yiqian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wushi.setBackgroundResource(R.drawable.selector_chongzhi_background);
                yibai.setBackgroundResource(R.drawable.selector_chongzhi_background);
                wubai.setBackgroundResource(R.drawable.selector_chongzhi_background);
                yiqian.setBackgroundResource(R.drawable.selector_chongzhi_background1);
                sanqian.setBackgroundResource(R.drawable.selector_chongzhi_background);
                liuliuliuliu.setBackgroundResource(R.drawable.selector_chongzhi_background);
                custom_chongzhi.setBackgroundResource(R.drawable.selector_chongzhi_background);
                number = yiqian.getText().toString();
                need_pay.setText("需支付" + number);
            }
        });
        sanqian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wushi.setBackgroundResource(R.drawable.selector_chongzhi_background);
                yibai.setBackgroundResource(R.drawable.selector_chongzhi_background);
                wubai.setBackgroundResource(R.drawable.selector_chongzhi_background);
                yiqian.setBackgroundResource(R.drawable.selector_chongzhi_background);
                sanqian.setBackgroundResource(R.drawable.selector_chongzhi_background1);
                liuliuliuliu.setBackgroundResource(R.drawable.selector_chongzhi_background);
                custom_chongzhi.setBackgroundResource(R.drawable.selector_chongzhi_background);
                number = sanqian.getText().toString();
                need_pay.setText("需支付" + number);

            }
        });
        liuliuliuliu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wushi.setBackgroundResource(R.drawable.selector_chongzhi_background);
                yibai.setBackgroundResource(R.drawable.selector_chongzhi_background);
                wubai.setBackgroundResource(R.drawable.selector_chongzhi_background);
                yiqian.setBackgroundResource(R.drawable.selector_chongzhi_background);
                sanqian.setBackgroundResource(R.drawable.selector_chongzhi_background);
                liuliuliuliu.setBackgroundResource(R.drawable.selector_chongzhi_background1);
                custom_chongzhi.setBackgroundResource(R.drawable.selector_chongzhi_background);
                number = liuliuliuliu.getText().toString();
                need_pay.setText("需支付" + number);

            }
        });
        custom_chongzhi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                wushi.setBackgroundResource(R.drawable.selector_chongzhi_background);
                yibai.setBackgroundResource(R.drawable.selector_chongzhi_background);
                wubai.setBackgroundResource(R.drawable.selector_chongzhi_background);
                yiqian.setBackgroundResource(R.drawable.selector_chongzhi_background);
                sanqian.setBackgroundResource(R.drawable.selector_chongzhi_background);
                liuliuliuliu.setBackgroundResource(R.drawable.selector_chongzhi_background);
            }
        });
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wechat_choose.setImageResource(R.drawable.pay_choose2x);
                alipay_choose.setImageResource(R.drawable.pay_normal2x);
                flag = 1;
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alipay_choose.setImageResource(R.drawable.pay_choose2x);
                wechat_choose.setImageResource(R.drawable.pay_normal2x);
                flag = 2;
            }
        });
        custom_chongzhi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pay();
            }

            @Override
            public void afterTextChanged(Editable s) {
                number = s.toString() +"元";
                need_pay.setText("需支付" + number);
                pay();
            }
        });
        pay();
    }

    private void pay() {
        need_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String moneyStr = number.replace("元", "");
                final int money;
                if (!moneyStr.isEmpty()) {
                    money = Integer.parseInt(moneyStr);
                } else {
                    money = 0;
                }

                if (money < 10) {
                    ToastUtils.getInstance().showToast("最少充值10元");
                } else if (money >= 10) {
                    v.setEnabled(false);
                    final View view = v;
                    if (flag == 1) {
                        ServiceFactory.getNoCacheInstance()
                                .createService(YService.class)
                                .wxpay("json", money +"", 2001, 4)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CommonObserver<AlipayRecharge>() {
                                    @Override
                                    public void onNext(@NonNull AlipayRecharge result) {
                                        super.onNext(result);
                                        view.setEnabled(true);
                                        if (result.getError() == 0) {
                                            paywx(result.getData());
                                        }
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        super.onError(e);
                                    }
                                });
                    } else if (flag == 2) {
                        ServiceFactory.getNoCacheInstance()
                                .createService(YService.class)
                                .aliapppay("json", money +"", 2001, 4)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CommonObserver<AlipayBean>() {
                                    @Override
                                    public void onNext(@NonNull AlipayBean result) {
                                        super.onNext(result);
                                        view.setEnabled(true);
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
                }

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
        IWXAPI mIWXAPI = WXAPIFactory.createWXAPI(BalanceActivity.this, data.getAppid());
        mIWXAPI.registerApp(data.getAppid());
        canPay = true;
        if (!mIWXAPI.isWXAppInstalled()) {
            ToastUtils.getInstance().showToast(WX_PAY_ERRMSG_1);
            canPay = false;
        } else if (!mIWXAPI.isWXAppSupportAPI()) {
            ToastUtils.getInstance().showToast(WX_PAY_ERRMSG_2);
            canPay = false;
        }
        YApp mapp = (YApp) getApplication();
        mapp.setMoney(number+"");
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
//                 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                Log.e("微信支付调起结果", "paywx: " + data.getAppid() + "-----" + data.getPartnerid() + "-----" + data.getPrepayid() + "-----" + data.getNoncestr() + "-----" + +data.getTimestamp() + "-----" + data.getPackageX() + "-----" + data.getSign());
//                mIWXAPI.registerApp(Constants.APP_ID);
                mapp.setSecene(2);
                mIWXAPI.sendReq(req);
                Log.e("微信返回: ", req.checkArgs() + "");
                finish();
            }

        } catch (Exception e) {
            Log.e("PAY_GET", "异常：" + e.getMessage());
        }
    }

    private static final int SDK_PAY_FLAG = 1;

    /**
     * 支付宝支付业务
     *
     * @param orderInfo
     */
    public void payV2(final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(BalanceActivity.this);
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
                        Toast.makeText(BalanceActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        onPaySuccess();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(BalanceActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
        String b = number;
        b = b.replace("元", "");
        Intent intent = new Intent(BalanceActivity.this, RechargeActivity.class);
        intent.putExtra("money", b);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }
}
