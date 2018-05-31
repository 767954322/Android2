package com.tuwan.yuewan.ui.activity.seeting;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.bankbean;
import com.tuwan.yuewan.nim.uikit.NimUIKit;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.ui.activity.Alipay2Activity;
import com.tuwan.yuewan.ui.activity.SeetingActivity;
import com.tuwan.yuewan.utils.OkManager;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.functions.Action1;

public class BindingActivity extends BaseActivity implements View.OnClickListener{
    private String types;
    private String uid;
    private String name;
    private String gender;
    private String iconurl;
    private String openid;
    private String accesstoken;
    private String token;
    private String refreshToken;
    private Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 999) {
                initIntent();
            } else if (msg.what == 888) {
                initCommet();
            } else if (msg.what == 777) {
                onLoginDone();
                initVview();
                Toast.makeText(BindingActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private boolean look = false;
    private ImageView iv_titlebar_back;
    private TextView bind_phone;
    private TextView bind_phone_number;
    private TextView bind_tw;
    private TextView bind_tw_state;
    private TextView bind_qq;
    private TextView bind_qq_state;
    private TextView bind_wx;
    private TextView bind_wx_state;
    private TextView bind_wb;
    private TextView bind_wb_state;
    private TextView bind_zfb;
    private LinearLayout bind_phones;
    private String tel;
    private TextView bank_yi;
    private ImageView more;
    private AbortableFuture<LoginInfo> loginRequest;
    private int isteacher;
    private LinearLayout aa;
    private Code code;
    private HashMap<String, String> map;
    private Gson gson;
    private String appname;
    private String bind = "";

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_binding;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        isteacher = getIntent().getIntExtra("isteacher", 0);
        bind = getIntent().getStringExtra("bind");
//        RelativeLayout toobar = (RelativeLayout) findViewById(R.id.binding_toobar);
        //SystemBarHelper.setHeightAndPadding(BindingActivity.this, toobar);
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        bind_phone = (TextView) findViewById(R.id.bind_phone);
        bind_phone_number = (TextView) findViewById(R.id.bind_phone_number);
        bind_phones = (LinearLayout) findViewById(R.id.bind_phones);
//        bind_tw = (TextView) findViewById(R.id.bind_tw);
//        bind_tw_state = (TextView) findViewById(R.id.bind_tw_state);
        bind_qq = (TextView) findViewById(R.id.bind_qq);
        bank_yi = (TextView) findViewById(R.id.bank_yi);
        bind_qq_state = (TextView) findViewById(R.id.bind_qq_state);
        bind_wx = (TextView) findViewById(R.id.bind_wx);
        more = (ImageView) findViewById(R.id.more);
        bind_wx_state = (TextView) findViewById(R.id.bind_wx_state);
//        bind_wb = (TextView) findViewById(R.id.bind_wb);
//        bind_wb_state = (TextView) findViewById(R.id.bind_wb_state);
        bind_zfb = (TextView) findViewById(R.id.bind_zfb);
        aa = (LinearLayout) findViewById(R.id.binding_alipay);
        if (isteacher == 1) {
            aa.setVisibility(View.VISIBLE);
            bank_yi.setVisibility(View.VISIBLE);
        } else {
            aa.setVisibility(View.GONE);
        }
        initVview();
        initData();

    }

    private void initVview() {
        OkManager okManager = OkManager.getInstance();
        String url = "https://y.tuwan.com/m/User/getBindList&format=json";
        okManager.getAsync(BindingActivity.this, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String reslue = response.body().string();
                BindingActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        bankbean bankbean = gson.fromJson(reslue, com.tuwan.yuewan.entity.bankbean.class);
                        if (bankbean.getError() == 0) {
                            if (bankbean.getData().getBank() == 1) {
                                more.setVisibility(View.GONE);
                                bank_yi.setVisibility(View.VISIBLE);
                                bank_yi.setText("解绑");
                                RxView.clicks(bank_yi)
                                        .throttleFirst(1, TimeUnit.SECONDS)
                                        .subscribe(new Action1<Void>() {
                                            @Override
                                            public void call(Void aVoid) {
                                                dialo();
                                            }
                                        });
                            } else {
                                bank_yi.setText("绑定");
                                more.setVisibility(View.GONE);
                                bank_yi.setVisibility(View.VISIBLE);
                            }
                            if (bankbean.getData().getQq() == 1) {
                                bind_qq_state.setText("解绑");
                                bind_qq_state.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialo();
                                    }
                                });
                            } else {
                                bind_qq_state.setText("绑定");
                            }
                            if (bankbean.getData().getWx() == 1) {
                                bind_wx_state.setText("解绑");
                                bind_wx_state.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialo();
                                    }
                                });
                            } else {
                                bind_wx_state.setText("绑定");
                            }
                            if (bankbean.getData().getPhone().equals("") && bankbean.getData().getPhone().toString() == null && bankbean.getData().getPhone().isEmpty()) {
                                bind_phone_number.setText(bankbean.getData().getPhone().toString());
//                                bind_wx_state.setEnabled(false);
                                bind_phone_number.setText("");
                            } else {
                                bind_phone_number.setText(bankbean.getData().getPhone().toString());
                            }
                            bind_phones.setOnClickListener(BindingActivity.this);
                        } else {
                            Toast.makeText(BindingActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }, true);
    }

    private void initData() {
        //退出页面
        RxView.clicks(iv_titlebar_back)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
        //银行卡绑定
        RxView.clicks(bank_yi)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        if (TextUtils.isEmpty(tel)) {
////                            startActivity(new Intent(BindingActivity.this, PhoneBindActivity.class));
//                            Toast.makeText(BindingActivity.this, "您还没有绑定手机号,请先绑定手机号", Toast.LENGTH_SHORT).show();
//                        } else if (!TextUtils.isEmpty(tel)) {
                        if (bind.equals("1")) {
                            startActivity(new Intent(BindingActivity.this, Alipay2Activity.class));
                        }else {
                            showBind();
                        }
//                            startActivity(new Intent(BindingActivity.this, AlipayActivity.class));
//                        }
                    }
                });
        //手机绑定
//        RxView.clicks(bind_phones)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        if (TextUtils.isEmpty(tel)) {
//                            startActivity(new Intent(BindingActivity.this, PhoneBindActivity.class));
//                            finish();
//                        } else {
//                            showDiaglog();
//                        }
//                    }
//                });
        //手机绑定
//        RxView.clicks(bind_phone_number)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        if (TextUtils.isEmpty(tel)) {
//                            startActivity(new Intent(BindingActivity.this, PhoneBindActivity.class));
//                        }
//                        else{
//                            showDiaglog();
//                        }
//                    }
//                });

//        //兔玩账号绑定
//        RxView.clicks(bind_tw)
//                .throttleFirst(1,TimeUnit.SECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//
//                    }
//                });

//QQ绑定
        RxView.clicks(bind_qq_state)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        UMShareAPI.get(BindingActivity.this).getPlatformInfo(BindingActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                    }
                });

        //微信绑定
        RxView.clicks(bind_wx_state)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        UMShareAPI.get(BindingActivity.this).getPlatformInfo(BindingActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                    }
                });

//        //微博绑定
//        RxView.clicks(bind_wb_state)
//                .throttleFirst(1,TimeUnit.SECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//
//                    }
//                });

    }

    private void showDiaglog() {
        new AlertDialog.Builder(BindingActivity.this)
                .setTitle("联系客服")
                .setMessage("更改手机绑定请联系客服")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NimUIKit.startP2PSession(BindingActivity.this, 107714 + "");
                    }
                })
                .create().show();
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, final Map<String, String> data) {
            if (isFinishing()){
                return;
            }
            DialogMaker.showProgressDialog(BindingActivity.this, null, getString(R.string.logining), true, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (loginRequest != null) {
                        loginRequest.abort();
                        onLoginDone();
                    }
                }
            }).setCanceledOnTouchOutside(false);

            if (platform == SHARE_MEDIA.WEIXIN) {
                uid = data.get("unionid");
                name = data.get("name");
                gender = data.get("gender");
                iconurl = data.get("profile_image_url");
                openid = data.get("unionid");
                accesstoken = data.get("accessToken");
                token = data.get("access_token");
                appname = "weixin";
            } else if (platform == SHARE_MEDIA.QQ) {
                uid = data.get("uid");
                name = data.get("name");
                gender = data.get("gender");
                iconurl = data.get("profile_image_url");
                openid = data.get("openid");
                accesstoken = data.get("accessToken");
                token = data.get("access_token");
                appname = "qq";
//                appname = "mobileqq";
            }
            hand.sendEmptyMessage(999);
//            initIntent();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            onLoginDone();
            Toast.makeText(BindingActivity.this, "绑定失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            onLoginDone();
            Toast.makeText(BindingActivity.this, "绑定取消", Toast.LENGTH_SHORT).show();
        }
    };

    private void onLoginDone() {
        loginRequest = null;
        DialogMaker.dismissProgressDialog();
    }

    private void dialo() {
        View view = LayoutInflater.from(BindingActivity.this).inflate(R.layout.dialog_layout4, null);
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(BindingActivity.this);
        Button dialog_cancel = (Button) view.findViewById(R.id.dialog_cancel);
        Button dialog_msg = (Button) view.findViewById(R.id.dialog_ok);
        builder1.setView(view);
        final AlertDialog show = builder1.show();
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences mySharedPreferences = getSharedPreferences("namess", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("names", "我的客服");
                editor.commit();
                NimUIKit.startP2PSession(BindingActivity.this, 107714 + "");
                show.dismiss();
                finish();
            }
        });

        dialog_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
            }
        });
    }

    private void showBind() {
        View view = LayoutInflater.from(BindingActivity.this).inflate(R.layout.dialog_layout4, null);
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(BindingActivity.this);
        Button dialog_cancel = (Button) view.findViewById(R.id.dialog_cancel);
        TextView dialog_msg = (TextView) view.findViewById(R.id.dialog_msg);
        Button dialog_ok = (Button) view.findViewById(R.id.dialog_ok);
        dialog_msg.setText("请先进行身份认证");
        builder1.setView(view);
        final AlertDialog show = builder1.show();
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BindingActivity.this, AuthenticationActivity.class));
                finish();
            }
        });

        dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
            }
        });
    }

    private void initIntent() {
        SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
        String cookie = preferences.getString("Cookie", null);
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();

        OkHttpClient client = okBuilder.build();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("openid", openid)
                .add("token", accesstoken)
                .add("name", name)
                .add("iconurl", iconurl)
                .add("appname", appname)
                .add("type", "chkuser");
        RequestBody formBody = builder.build();
        final Request request = new Request.Builder()
                .url("https://open.tuwan.com/api/umeng/callback.ashx")
                .post(formBody).addHeader("Cookie", cookie)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onLoginDone();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                gson = new Gson();
                code = gson.fromJson(result, Code.class);
                hand.sendEmptyMessage(888);
//                initCommet();
            }
        });
    }

    //提交绑定
    private void initCommet() {
        map = new HashMap<>();
        map.put("token", code.getData().getToken());
        map.put("type", "binduser");
        map.put("avatar", code.getData().getAvatar());
        String url = "https://user.tuwan.com/bind/bind.aspx?token=" + code.getData().getToken() + "&type=binduser&avatar=" + code.getData().getAvatar();
        OkManager.getInstance().getAsync(BindingActivity.this, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onLoginDone();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                gson = new Gson();
                try {
                    Code2 code2 = gson.fromJson(string, Code2.class);
                    if (code2.getCode() == 0) {
                        hand.sendEmptyMessage(777);
                    }
                } catch (Exception e) {
                }
            }
        }, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bind_phones) {
            if (bind_phone_number.getText().toString().trim().equals("")){
                startActivity(new Intent(BindingActivity.this, PhoneBindActivity.class));
                finish();
            }else {
                dialo();
            }
        }
    }

    //--------------------------------------实体类----------------------------------
    class Code {

        /**
         * code : 1
         * msg : exists
         * data : {"userid":1199285,"token":"7B9241526D69100E1D3FF36B029551485FC8467CB6E47AF4913178AE52424A1C48D183D47F2F44CF","app":"weixin","avatar":"http://uc.tuwan.com/images/noavatar_middle.gif"}
         */

        private int code;
        private String msg;
        private DataBean data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public class DataBean {
            /**
             * userid : 1199285
             * token : 7B9241526D69100E1D3FF36B029551485FC8467CB6E47AF4913178AE52424A1C48D183D47F2F44CF
             * app : weixin
             * avatar : http://uc.tuwan.com/images/noavatar_middle.gif
             */

            private int userid;
            private String token;
            private String app;
            private String avatar;

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public String getApp() {
                return app;
            }

            public void setApp(String app) {
                this.app = app;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }
    }

    class Code2 {

        /**
         * code : 0
         * msg : OK
         * data : {}
         */

        private int code;
        private String msg;
        private String data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
