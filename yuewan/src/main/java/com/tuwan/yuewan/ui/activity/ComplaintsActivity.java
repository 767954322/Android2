package com.tuwan.yuewan.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.Code;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.functions.Action1;

public class ComplaintsActivity extends BaseActivity {

    //    private ImageView iv_titlebar_back2;
    private EditText complains_et;
    private Button complains_btn;
    private String cookie;
    private ImageView iv_titlebar_back;
    private TextView tv_titlebar_title;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_complaints;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));
        initView();
    }

    private void initView() {
        SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
        cookie = preferences.getString("Cookie", null);
//        iv_titlebar_back2 = (ImageView) findViewById(R.id.iv_titlebar_back2);
        complains_et = (EditText) findViewById(R.id.complains_et);
        complains_btn = (Button) findViewById(R.id.complains_btn);
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        initData();
    }

    private void initData() {

        RxView.clicks(complains_et)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        complains_et.setCursorVisible(true);
                    }
                });
        //确定吐槽
        RxView.clicks(complains_btn)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        submit();
//                        Toast.makeText(ComplaintsActivity.this, "，反馈成功，已经收到来自您的建议，", Toast.LENGTH_SHORT).show();
                        initPopup();
//                        finish();
                    }
                });

        RxView.clicks(iv_titlebar_back).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });

        tv_titlebar_title.setText("投诉建议");
    }


    private void submit() {

        String et = complains_et.getText().toString().trim();
        if (TextUtils.isEmpty(et)) {
            Toast.makeText(this, "请尽情的吐槽吧", Toast.LENGTH_SHORT).show();
            return;
        } else {
            OkHttpClient client = new OkHttpClient();
            String url = "https://y.tuwan.com/m/Compliant/add" + "?format=json" + "&content=" + et;
            Request request = new Request.Builder()
                    .addHeader("Cookie", cookie)
                    .url(url)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.e("投诉失败的原因", e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
//                    Log.e("投诉返回的数据: ", result);
                    Gson gson = new Gson();
                    Code code = gson.fromJson(result, Code.class);
                    try {
                        if (code.getError() == 0) {
                            ToastUtils.getInstance().showToast("我们会将你的意见及时反馈");
                            finish();
                        } else if (code.getError() == 1) {
//                            ToastUtils.getInstance().showToast("请重新输入");
                        } else if (code.getError() == -1) {
//                            ToastUtils.getInstance().showToast("该用户未登录");
                        }
                    } catch (Exception e) {
//                        ToastUtils.getInstance().showToast(e.toString());
                    }
                }
            });
        }
    }

    //投诉成功弹出框
    private void initPopup() {
        View view = View.inflate(ComplaintsActivity.this, R.layout.order_details, null);
        WindowManager wm = getWindowManager();
        final int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        final PopupWindow window = new PopupWindow(view, width, height);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.showAsDropDown(iv_titlebar_back, 0, 0);
        ImageView close = (ImageView) view.findViewById(R.id.popup_close2);
        TextView tv1 = (TextView) view.findViewById(R.id.receiving_tv);
        TextView tv2 = (TextView) view.findViewById(R.id.receiving_tv2);
        TextView tv3 = (TextView) view.findViewById(R.id.receiving_tv3);
        tv1.setText("反馈成功，已经收到来自您的建议");
        tv2.setText("我们会将你的意见及时反馈");
        tv3.setText(" ");
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
                ComplaintsActivity.this.finish();
            }
        });
    }

}
