package com.tuwan.yuewan.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.tuwan.common.config.Url;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.ui.dialog.CustomDialogManager;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.R2;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.entity.AlipayBean;
import com.tuwan.yuewan.entity.ErrorCodeBean;
import com.tuwan.yuewan.entity.PayEntity;
import com.tuwan.yuewan.entity.PayResult;
import com.tuwan.yuewan.entity.pay.AlipayRecharge;
import com.tuwan.yuewan.entity.pay.PayInfo;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.widget.CountDownView;
import com.tuwan.yuewan.utils.OkManager;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import rx.functions.Action1;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


/**
 * Created by zhangjie on 2017/10/23.
 * 因为这里没有单独请求并展示用的接口，所以这是不完整的页面，先不抽取
 * <p>
 * 支付页及之前页面与付款的逻辑
 */
public class PayActivity extends BaseActivity {

    /**
     * 支付逻辑开启页面以及支付成功时关闭页面
     */
    public static final int REQUEST_CODE_PAY = 358;

    @BindView(R2.id.iv_titlebar_back)
    ImageView mIvTitlebarBack;
    @BindView(R2.id.tv_titlebar_title)
    TextView mTvTitlebarTitle;
    @BindView(R2.id.rl_titlebar)
    RelativeLayout mRlTitlebar;

//    @BindView(R2.id.countdown)
//    CountDownView mCountdown;
    @BindView(R2.id.iv_pay_avart)
    ImageView mIvPayAvart;
    @BindView(R2.id.tv_pay_nikenam)
    TextView mTvPayNikenam;
    @BindView(R2.id.tv_pay_servicename)
    TextView mTvPayServicename;
    @BindView(R2.id.tv_pay_money_number)
    TextView mTvPayMoneyNumber;
    @BindView(R2.id.tv_pay_money)
    TextView mTvPayMoney;

    @BindView(R2.id.rg_pay_type)
    RadioGroup mRgPayType;
    @BindView(R2.id.rb_paytype_wechat)
    RadioButton mRbPaytypeWechat;
    @BindView(R2.id.rb_paytype_alipay)
    RadioButton mRbPaytypeAlipay;
    @BindView(R2.id.rb_paytype_ywb)
    RadioButton mRbPaytypeYwb;

    @BindView(R2.id.tv_pay_confrim)
    TextView mTvPayConfrim;
    //    @BindView(R2.id.second)
//    TextView second;
//
//    @BindView(R2.id.minute)
    TextView minute;
    private PayEntity mEntity;
    private String cookie;
    private int mTime;
    private String tradeno;

    private float timeprice;
    private float price;
    private float sile;
    private int teacherid;
    private YApp mapp;
    private String name = "";

    public static void show(PayEntity entity, Activity activity) {
        Intent intent = new Intent(activity, PayActivity.class);
        intent.putExtra("entity", entity);
        activity.startActivityForResult(intent, REQUEST_CODE_PAY);
    }

    public static void action(String tradeno, Activity activity) {
        Intent intent = new Intent(activity, PayActivity.class);
        intent.putExtra("tradeno", tradeno);
        activity.startActivityForResult(intent, REQUEST_CODE_PAY);
    }

//    public static void actionResult(Activity activity) {
//        Intent intent = new Intent(activity, PaySuccessActivity.class);
//        activity.startActivity(intent);
//    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_pay;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        mapp = (YApp) getApplication();
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));
        SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
        cookie = preferences.getString("Cookie", null);
        tradeno = getIntent().getStringExtra("tradeno");
        String url = Url.payDetail + "?format=json&orderid=" + tradeno;
//        String url = Url.payDetail + "?format=json&orderid=" + "";

        DialogMaker.showProgressDialog(PayActivity.this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        OkManager okManager = OkManager.getInstance();
        okManager.getAsync(this, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onLoginDone();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                onLoginDone();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Gson gson = new Gson();
                            PayInfo payInfo = gson.fromJson(result, PayInfo.class);
                            if (payInfo.getError() != 0 ){
                                ToastUtils.getInstance().showToast("请求错误 请返回重试");
                                return;
                            }
                            timeprice = payInfo.getData().getTimeprice();
                            price = payInfo.getData().getPrice();
                            sile = payInfo.getData().getSile();
                            teacherid = payInfo.getData().getTeacherid();

                            Glide.with(PayActivity.this)
                                    .load(payInfo.getData().getAvatar())
                                    .apply(bitmapTransform(new RoundedCornersTransformation(100, 0, RoundedCornersTransformation.CornerType.ALL)))
                                    .into(mIvPayAvart);

                            mapp.setMoney(price + "");
                            mapp.setSile(sile + "");
                            mapp.setTeacherid(teacherid + "");
                            mapp.setTimeprice(timeprice + "");
                            mapp.setTradeno(tradeno + "");
                            name = payInfo.getData().getService();
                            mTvPayNikenam.setText(payInfo.getData().getNickname());
                            mTvPayServicename.setText(payInfo.getData().getService());
                            mTvPayMoneyNumber.setText(payInfo.getData().getTimeprice() + " * " + payInfo.getData().getHours() + payInfo.getData().getUnit());
                            mTvPayMoney.setText("¥ " + payInfo.getData().getPrice());
                            mTvPayConfrim.setText("确认支付  ¥" + payInfo.getData().getPrice());
                            mTvPayConfrim.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    v.setEnabled(false);
                                    // 确实支付
                                    if (mRbPaytypeYwb.isChecked()) {
                                        //约玩币支付
                                        ServiceBanlacePay();

                                    } else if (mRbPaytypeAlipay.isChecked()) {
                                        //支付宝支付
                                        serviceAlipay();
                                    } else if (mRbPaytypeWechat.isChecked()) {
                                        //微信支付
                                        serviceWechat();
                                    }
                                }
                            });

//                            int letTime = payInfo.getData().getTime();
//                            if (letTime >= 600) {
//                                letTime = 599;
//                            }
//                            mCountdown.startTime(letTime,PayActivity.this);
                            AbsoluteSizeSpan span = new AbsoluteSizeSpan(10, true);
                            SpannableString spannableString = new SpannableString("约玩币 " + "（余额" + payInfo.getData().getCurrency() + "）");
                            spannableString.setSpan(span, 4, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                            ForegroundColorSpan spanColor = new ForegroundColorSpan(0xff999999);
                            spannableString.setSpan(spanColor, 4, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                            mRbPaytypeYwb.setText(spannableString);
                        } catch (Exception e) {

                        }
                    }
                });
            }
        }, true);
    }
    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }
//    public static void Pop() {
//        View view = View.inflate(PayActivity.this, R.layout.cancel_pop, null);
//        WindowManager wm = getWindowManager();
//        final int width = wm.getDefaultDisplay().getWidth();
//        int height = wm.getDefaultDisplay().getHeight();
//        final PopupWindow window = new PopupWindow(view, width, height);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        window.setOutsideTouchable(true);
////        window.showAsDropDown(mTitle, 0, 0);
//        window.showAtLocation(view, Gravity.CENTER, width, height + 500);
//        ImageView popup_close3 = (ImageView) view.findViewById(R.id.popup_close3);
//        Button pop_know2 = (Button) view.findViewById(R.id.pop_know2);
//        TextView kq = (TextView) view.findViewById(R.id.pop_kq);
//        TextView js = (TextView) view.findViewById(R.id.pop_js);
//        kq.setText("订单取消");
//        js.setVisibility(View.GONE);
//        pop_know2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                window.dismiss();
//                PayActivity.this.finish();
//            }
//        });
//        popup_close3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                window.dismiss();
//                PayActivity.this.finish();
//            }
//        });
//    }

    private void serviceWechat() {
        String aid = tradeno;
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .wxpay("json", aid, 2000, 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<AlipayRecharge>() {
                    @Override
                    public void onNext(@NonNull AlipayRecharge result) {
                        super.onNext(result);
                        mTvPayConfrim.setEnabled(true);
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
        IWXAPI mIWXAPI = WXAPIFactory.createWXAPI(PayActivity.this, "wx6cd4c28b58e8737f");
//        mIWXAPI.registerApp(data.getAppid());
//        mIWXAPI.handleIntent(getIntent(), this);

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
                req.extData = "app data";
                mIWXAPI.registerApp(data.getAppid());
                mIWXAPI.sendReq(req);
                mapp.setSecene(1);
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                mIWXAPI.registerApp(Constants.APP_ID);
                finish();
            } else {
            }

        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mCountdown != null) {
//            mCountdown.onDestroy();
//        }
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();

        //SystemBarHelper.setHeightAndPadding(this, mRlTitlebar);
        mTvTitlebarTitle.setText("在线支付");
        RxView.clicks(mIvTitlebarBack)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        showLeaveDialog();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        showLeaveDialog();
    }

    private void showLeaveDialog() {
        final CustomDialogManager.CustomDialog customDialog = CustomDialogManager.getInstance().getDialog(this, R.layout.dialog_pay_leave).setSizeOnDP(290, 320);

        customDialog.findViewById(R.id.iv_dialog_cancel).setOnClickListener(new View.OnClickListener() {
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

        customDialog.findViewById(R.id.tv_dialog_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                finish();
            }
        });

        customDialog.show();
    }


    /**
     * 支付成功时的逻辑
     */
    private void onPaySuccess() {

//        Intent intent = new Intent(PayActivity.this, PaySuccessActivity.class);
//        intent.putExtra("name",name);
//        intent.putExtra("timeprice", timeprice + "");
//        intent.putExtra("price", price + "");
//        intent.putExtra("sile", sile + "");
//        intent.putExtra("tradeno", tradeno + "");
//        intent.putExtra("teacherid", teacherid + "");
//        PaySuccessActivity.show(PayActivity.this);
//        startActivity(intent);
        Intent intent = new Intent(PayActivity.this, OrderDetailsActivity.class);
        intent.putExtra("tradeno", tradeno);
        intent.putExtra("payType","1");
        startActivity(intent);
        finish();
        setResult(RESULT_OK);
        finish();
    }

    //余额支付----------------------
    private void ServiceBanlacePay() {
        String url = "https://y.tuwan.com/m/order/PayBalance?format=json&source=4&code=" + tradeno;

        OkManager.getInstance().getString(url, cookie, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("余额支付错误原因: ", e.toString());
                mTvPayConfrim.setEnabled(true);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String result = response.body().string();
                try {
                    Gson gson = new Gson();
                    final ErrorCodeBean codeBean = gson.fromJson(result, ErrorCodeBean.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvPayConfrim.setEnabled(true);
                            // -1001 未登录， -1002 余额不足， -1 支付失败， 1支付成功
                            if (codeBean.code == -1002) {
//                                ToastUtils.getInstance().showToast("余额不足，请选择其他方式支付");
//                                ToastUtils.getInstance().showToast("余额不足");
                                initBanlacePay();
                            } else if (codeBean.code == -1) {
                                ToastUtils.getInstance().showToast("支付失败");
                            } else if (codeBean.code == 1) {
                                onPaySuccess();
                            }
                        }
                    });
                } catch (Exception e) {
                }
            }
        });
    }

    //余额不足弹窗
    private void initBanlacePay() {
        Dialog dialog = new AlertDialog.Builder(PayActivity.this)
                .setTitle("余额不足").setMessage("是否去充值?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setPositiveButton("去支付", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PayActivity.this, BalanceActivity.class);
//                        intent.putExtra("currency", currency);
                        startActivity(intent);
                    }
                }).create();
        dialog.show();

    }

    //-------------------------------余额支付结束


    //支付宝支付----------------------
    private void serviceAlipay() {
        String aid = tradeno;
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .aliapppay("json", aid, 2000, 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<AlipayBean>() {
                    @Override
                    public void onNext(@NonNull AlipayBean result) {
                        super.onNext(result);
                        mTvPayConfrim.setEnabled(true);
                        payV2(result.data);
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
                PayTask alipay = new PayTask(PayActivity.this);
                if(orderInfo!=null){
                    Map<String, String> result = alipay.payV2(orderInfo, true);

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);

                }

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
//                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        onPaySuccess();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) and run LayoutCreator again
    }


}
