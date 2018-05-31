package com.tuwan.yuewan.ui.activity.teacher;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.Withdrawalsbean;
import com.tuwan.yuewan.entity.Withdrawalsbean2;
import com.tuwan.yuewan.ui.activity.Alipay2Activity;
import com.tuwan.yuewan.ui.activity.RedWebActivity;
import com.tuwan.yuewan.ui.activity.WechatRecordActivity;
import com.tuwan.yuewan.ui.widget.ClearEditText;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.SharedPreferencesHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.functions.Action1;

public class WithdrawalsActivity extends AppCompatActivity {

    private ImageView iv_titlebar_back;
    private TextView with_type;
    private TextView with_account;
    private ClearEditText with_et_money;
    private TextView with_prompt;
    private TextView with_balance;
    private Button with_sure;
    private double money;
    private Withdrawalsbean withdrawalsbean;
    private TextView with_help;
    private TextView record;
    private ImageView img_protocol;
    private TextView tv_protocol;
    private Button button;
    private   int trim2;
    private Button pop_know;
    private TextView tixian__;
    private TextView pop_js,pop_kq;
    private ImageView popup_icon,popup_close2;
    private String protocolType = "0";
    private String PROTOCOL_URL = "https://y.tuwan.com/home/withdraw_protocol/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals);
        initView();


       submitt();





    }

    @Override
    protected void onResume() {
        super.onResume();
//        String trim = with_et_money.getText().toString().trim();
//        int trim2;
//        if("".equals(trim)){
//            trim2=0;
//
//        }else {
//            trim2= Integer.parseInt(trim);
//
//        }
//        Log.e("eeeeeeeeeeeeeeeeee",trim2+"");
//        if(money>trim2){
//            if(trim2>=100){
//                with_sure.setBackgroundColor(Color.parseColor("#FFC71A"));
//
//            }
//        }else {
//
//
//        }
    }

    private void initView() {
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        with_type = (TextView) findViewById(R.id.with_type);
        record = (TextView) findViewById(R.id.record);
        with_account = (TextView) findViewById(R.id.with_account);
        with_et_money = (ClearEditText) findViewById(R.id.with_et_money);
        with_prompt = (TextView) findViewById(R.id.with_prompt);
        with_balance = (TextView) findViewById(R.id.with_balance);
        img_protocol = (ImageView) findViewById(R.id.img_protocol);
        tv_protocol = (TextView) findViewById(R.id.tv_protocol);
        with_sure = (Button) findViewById(R.id.with_sure);
        with_help = (TextView) findViewById(R.id.with_help);
        button = (Button) findViewById(R.id.button_bangding);
        tixian__ = (TextView) findViewById(R.id.tixian__);
        initData();

    }

    private void initData() {
        RxView.clicks(iv_titlebar_back)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });

        RxView.clicks(record)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(WithdrawalsActivity.this, WechatRecordActivity.class));
                    }
                });
        RxView.clicks(img_protocol)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        startActivity(new Intent(WithdrawalsActivity.this, WechatRecordActivity.class));
                        if (protocolType.equals("0")){
                            img_protocol.setImageResource(R.drawable.pay_normal3x);
                            protocolType = "1";
                            with_sure.setOnClickListener(null);
                            with_sure.setBackgroundColor(Color.rgb(198,198,198));
                        }else {
                            img_protocol.setImageResource(R.drawable.pay_choose3x);
                            protocolType = "0";
                            submitt();
                        }
                    }
                });
        RxView.clicks(tv_protocol)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(WithdrawalsActivity.this, RedWebActivity.class);
                        intent.putExtra("url", PROTOCOL_URL);
                        startActivity(intent);
                    }
                });
        wangluo();

    }

    private void wangluo() {
        SharedPreferencesHelper helper=new SharedPreferencesHelper(WithdrawalsActivity.this);
        String  cookie = (String) helper.get("Cookie", null);
        OkManager okmanger = OkManager.getInstance();
        String url="https://y.tuwan.com/m/User/getDrawData&format=json";

        okmanger.getString(url, cookie, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
//                Log.e("eeeeeeeeeeeeeeeee",response+"");
//                Log.e("eeeeeeeeeeeeeeeee",result+"");
                WithdrawalsActivity.this.runOnUiThread
                        (new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                withdrawalsbean = JSONObject.parseObject(result,Withdrawalsbean.class);
                                if(withdrawalsbean.getError()==0){

                                    money = withdrawalsbean.getData().getMoney();

                                    if(withdrawalsbean.getData().getAccount().equals("")){


                                        with_account.setText("暂时没有绑定任何提现账户");
                                        with_type.setText("");
                                        with_balance.setText("余额：￥"+money);
                                        with_sure.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Toast.makeText(WithdrawalsActivity.this, "暂时没有绑定任何银行卡，请先去绑定再进行体现", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                        button.setVisibility(View.VISIBLE);
                                        button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent= new Intent(WithdrawalsActivity.this, Alipay2Activity.class);
                                                startActivity(intent);
                                            }
                                        });



                                    }else {
                                        with_type.setText("提现到"+withdrawalsbean.getData().getType());
                                    with_account.setText("   "+withdrawalsbean.getData().getAccount());
                                    with_balance.setText("余额：￥"+money);
                                        button.setVisibility(View.GONE);
//                                        submit();
                                        submitt();

                                    }

                                }else {

                                    Toast.makeText(WithdrawalsActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                                }



                            }
                        });
            }
        });




    }


    private void submit() {
        // validate
        String money = with_et_money.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
    private void submitt(){




                with_sure.setBackgroundColor(Color.parseColor("#FFC71A"));
                with_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String trim = with_et_money.getText().toString().trim();

                        if ("".equals(trim)) {
                            trim2 = 0;

                        } else {
                            try {
                                 trim2 = Integer.parseInt(trim);

                            }catch (Exception e){

                            }

                        }
                        if (money > trim2) {
                            if (trim2 >= 100) {


                                with();

                            }else {
                                Toast.makeText(WithdrawalsActivity.this, "输入金额有误，请重新输入", Toast.LENGTH_SHORT).show();
                                with_et_money.setText("");
                            }
                        }else {

                            Toast.makeText(WithdrawalsActivity.this, "账户目前没有这么多余额", Toast.LENGTH_SHORT).show();
                            with_et_money.setText("");
                        }


                    }  });

        }







    private void with(){

        OkManager okmanger = OkManager.getInstance();
        String money2 = with_et_money.getText().toString().trim();
        String url="https://y.tuwan.com/m/User/withdraw?money="+money2+"&format=json";
        okmanger.getAsync(WithdrawalsActivity.this, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();

                WithdrawalsActivity.this.runOnUiThread
                        (new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                Withdrawalsbean2 withdrawalsbean2 = gson.fromJson(result, Withdrawalsbean2.class);
                                if(withdrawalsbean2.getError()==0)
                                {
                                    View view = View.inflate(WithdrawalsActivity.this, R.layout.pop_tixian, null);
                                    WindowManager wm = getWindowManager();
                                    final int width = wm.getDefaultDisplay().getWidth();
                                    int height = wm.getDefaultDisplay().getHeight();
                                    final PopupWindow window = new PopupWindow(view,width,height);
                                    // 设置PopupWindow的背景
                                    if (isFinishing()){
                                        return;
                                    }
                                    window.showAtLocation(view, Gravity.CENTER,width,height/2);
                                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    // 设置PopupWindow是否能响应外部点击事件
                                    window.setOutsideTouchable(true);

                                    pop_know = (Button) view.findViewById(R.id.pop_know);
                                    pop_js = (TextView) view.findViewById(R.id.pop_js);
                                    pop_kq = (TextView) view.findViewById(R.id.pop_kq);
                                    popup_icon = (ImageView) view.findViewById(R.id.popup_icon);
                                    popup_close2 = (ImageView) view.findViewById(R.id.popup_close2);
                                    pop_kq.setText("恭喜提现成功");
                                    pop_js.setText("预计"+withdrawalsbean2.getData().getDate()+"到账");

                                    RxView.clicks(popup_close2)
                                            .throttleFirst(1, TimeUnit.SECONDS)
                                            .subscribe(new Action1<Void>() {
                                                @Override
                                                public void call(Void aVoid) {
                                                    window.dismiss();
                                                    finish();
                                                }
                                            });
                                    RxView.clicks(pop_know)
                                            .throttleFirst(1, TimeUnit.SECONDS)
                                            .subscribe(new Action1<Void>() {
                                                @Override
                                                public void call(Void aVoid) {
                                                    window.dismiss();
                                                    finish();
                                                }
                                            });


//                                    Toast.makeText(WithdrawalsActivity.this, "提现成功，预计到账时间"+withdrawalsbean2.getData().getDate(), Toast.LENGTH_SHORT).show();

                                }else if(withdrawalsbean2.getError()==2) {
                                    Toast.makeText(WithdrawalsActivity.this, "余额不足，即提现金额大于余额", Toast.LENGTH_SHORT).show();

                                }else if(withdrawalsbean2.getError()==1){
                                    Toast.makeText(WithdrawalsActivity.this, "金额格式错误", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
            }
        },true);

    }


}
