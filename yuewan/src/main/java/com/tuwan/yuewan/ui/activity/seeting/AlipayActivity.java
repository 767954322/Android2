package com.tuwan.yuewan.ui.activity.seeting;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.Code;
import com.tuwan.yuewan.entity.Codes;
import com.tuwan.yuewan.entity.teacher.PhoneNumber;
import com.tuwan.yuewan.ui.widget.ClearEditText;
import com.tuwan.yuewan.utils.CallBacks;
import com.tuwan.yuewan.utils.OkHttpManager;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.Urls;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.functions.Action1;

public class AlipayActivity extends AppCompatActivity {

    private ImageView iv_alipay_back;
    private ClearEditText alipay_accent;
    private ClearEditText alipay_name;
    private TextView alipay_phone;
    private ClearEditText alipay_ma;
    private Button alipay_btn_get;
    private Button alipay_btn_sure;
    private ClearEditText alipay_yinhang;
    private ClearEditText alipay_huhang;
    private TextView alipay_slt;
    private String getnumber = "https://user.tuwan.com/api/method/userextinfo";
    private String getCode = "https://api.tuwan.com/playteach/?data=getcode&format=jsonp&paytype=2";
    private String sure = "https://api.tuwan.com/playteach/?data=bindpaytype&format=json";
//    https://api.tuwan.com/playteach/?data=bindpaytype&format=json
    private String tel;
    private String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay);
        initView();
    }

    private void initView() {
        SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
        cookie = preferences.getString("Cookie", null);
        alipay_yinhang = (ClearEditText) findViewById(R.id.alipay_yinhang);
        alipay_huhang = (ClearEditText) findViewById(R.id.alipay_huhang);
        iv_alipay_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        alipay_accent = (ClearEditText) findViewById(R.id.alipay_accent);
        alipay_name = (ClearEditText) findViewById(R.id.alipay_name);
        alipay_phone = (TextView) findViewById(R.id.alipay_phone);
        alipay_ma = (ClearEditText) findViewById(R.id.alipay_ma);
        alipay_btn_get = (Button) findViewById(R.id.alipay_btn_get);
        alipay_btn_sure = (Button) findViewById(R.id.alipay_btn_sure);
        alipay_slt = (TextView) findViewById(R.id.alipay_slt);
        alipay_slt.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        OkManager.getInstance().getString(getnumber, cookie, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("获取手机号失败原因", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                PhoneNumber phoneNumber = gson.fromJson(result, PhoneNumber.class);
                tel = phoneNumber.getData().getTel();
                try {
                    if (!TextUtils.isEmpty(tel)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alipay_phone.setText(tel);
                                Log.e("获取手机号返回结果: ", tel);
                            }
                        });
                    } else {

                    }
                } catch (Exception e) {
                    Log.e("获取手机号返回失败原因", e.toString());
                }

            }
        });
        initData();
    }

    private void initData() {
        //退出页面
        RxView.clicks(iv_alipay_back)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });

        //确认绑定
        RxView.clicks(alipay_btn_sure)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        submit();
                    }
                });

        //支付宝示例图
        RxView.clicks(alipay_slt)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                    }
                });

        //获取验证码
        RxView.clicks(alipay_btn_get)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e("网址: ", Urls.GETCODE);
                        OkHttpManager.getInstance(Codes.class).getDataFromNet(cookie, getCode, null, new CallBacks<Codes>() {
                            @Override
                            public void suc(Codes codes) {
                                int code = codes.getCode();
                                Log.e("获取验证码返回结果: ", code + "");
                                if (code == 0) {
                                    ToastUtils.getInstance().showToast("请注意接收短信");
                                }
                                if (code == -2) {
                                    ToastUtils.getInstance().showToast("1分钟之内只能发一次");
                                }
                            }

                            @Override
                            public void fail(String str) {
                                Log.e("获取验证码失败原因", str);
                            }
                        });
                    }
                });
    }


    private void submit() {
        // validate
        String accent = alipay_accent.getText().toString().trim();
        if (TextUtils.isEmpty(accent)) {
            Toast.makeText(this, "请输入支付宝账号", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = alipay_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "校验真实姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        String ma = alipay_ma.getText().toString().trim();
        if (TextUtils.isEmpty(ma)) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        String yinhang = alipay_yinhang.getText().toString().trim();
        if (TextUtils.isEmpty(yinhang)) {
            Toast.makeText(this, "请输入银行卡类型", Toast.LENGTH_SHORT).show();
            return;
        }

        String huhang = alipay_huhang.getText().toString().trim();
        if (TextUtils.isEmpty(huhang)) {
            Toast.makeText(this, "请输入开户行", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("paytype", "3");
        builder.add("bank_id", accent);
        builder.add("bank_name", name);
        builder.add("bank_from", yinhang);
        builder.add("bank_area", huhang);
        builder.add("checkcode", ma);
        RequestBody formBody = builder.build();




        final Request request = new Request.Builder()
                .url(sure)
                .addHeader("Cookie", cookie)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("绑定失败原因", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("yzhshahshhdhas",request+"");
                String result = response.body().string();
                Gson gson = new Gson();
               final Codes codes = gson.fromJson(result, Codes.class);
                Log.e("yzhshahshhdhas",result+"");


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (codes.getCode() == 0) {
                            ToastUtils.getInstance().showToast("绑定成功");
                            finish();
                        }else {
                            ToastUtils.getInstance().showToast("请检查你的信息是否有误");
                        }
                    }
                });
            }
        });
    }
}
