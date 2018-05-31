package com.tuwan.yuewan.ui.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.flyco.systembar.SystemBarHelper;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.FollowAdapter;
import com.tuwan.yuewan.ui.activity.FollowActivity;
import com.tuwan.yuewan.utils.CompareSort;

import java.util.Collections;

public class CancelOrderActivity extends BaseActivity implements View.OnClickListener {


    private ImageView fanhui;
    private RelativeLayout cancel_title;
    private RelativeLayout cancel_1;
    private RelativeLayout cancel_2;
    private RelativeLayout cancel_3;
    private RelativeLayout cancel_4;
    private RelativeLayout cancel_5;
    private EditText cancel_et;
    private Button cancel_bt;
    String ss = "大神未响应";
    private ImageView cancel_img1;
    private ImageView cancel_img2;
    private ImageView cancel_img3;
    private ImageView cancel_img4;
    private ImageView cancel_img5;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_cancel_order;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.cancel_title));
        initView();
    }

    private void initView() {
        fanhui = (ImageView) findViewById(R.id.fanhui);
        fanhui.setOnClickListener(this);
        cancel_title = (RelativeLayout) findViewById(R.id.cancel_title);
        cancel_title.setOnClickListener(this);
        cancel_1 = (RelativeLayout) findViewById(R.id.cancel_1);
        cancel_1.setOnClickListener(this);
        cancel_2 = (RelativeLayout) findViewById(R.id.cancel_2);
        cancel_2.setOnClickListener(this);
        cancel_3 = (RelativeLayout) findViewById(R.id.cancel_3);
        cancel_3.setOnClickListener(this);
        cancel_4 = (RelativeLayout) findViewById(R.id.cancel_4);
        cancel_4.setOnClickListener(this);
        cancel_5 = (RelativeLayout) findViewById(R.id.cancel_5);
        cancel_5.setOnClickListener(this);
        cancel_et = (EditText) findViewById(R.id.cancel_et);
        cancel_bt = (Button) findViewById(R.id.cancel_bt);
        cancel_bt.setOnClickListener(this);
        cancel_img1 = (ImageView) findViewById(R.id.cancel_img1);
        cancel_img2 = (ImageView) findViewById(R.id.cancel_img2);
        cancel_img3 = (ImageView) findViewById(R.id.cancel_img3);
        cancel_img4 = (ImageView) findViewById(R.id.cancel_img4);
        cancel_img5 = (ImageView) findViewById(R.id.cancel_img5);

        cancel_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0){
//                    img_follow_name.setVisibility(View.VISIBLE);
                    cancel_img1.setVisibility(View.GONE);
                    cancel_img2.setVisibility(View.GONE);
                    cancel_img3.setVisibility(View.GONE);
                    cancel_img4.setVisibility(View.GONE);
                    cancel_img5.setVisibility(View.GONE);
                    ss = cancel_et.getText().toString().trim();
                }else {
                    ss = "";
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancel_1) {
            ss = "大神未响应";
            cancel_img1.setVisibility(View.VISIBLE);
            cancel_img2.setVisibility(View.GONE);
            cancel_img3.setVisibility(View.GONE);
            cancel_img4.setVisibility(View.GONE);
            cancel_img5.setVisibility(View.GONE);
        } else if (i == R.id.cancel_2) {
            ss = "大神要求取消订单";
            cancel_img1.setVisibility(View.GONE);
            cancel_img2.setVisibility(View.VISIBLE);
            cancel_img3.setVisibility(View.GONE);
            cancel_img4.setVisibility(View.GONE);
            cancel_img5.setVisibility(View.GONE);
        } else if (i == R.id.cancel_3) {
            ss = "订单信息填写有误";
            cancel_img1.setVisibility(View.GONE);
            cancel_img2.setVisibility(View.GONE);
            cancel_img3.setVisibility(View.VISIBLE);
            cancel_img4.setVisibility(View.GONE);
            cancel_img5.setVisibility(View.GONE);
        } else if (i == R.id.cancel_4) {
            ss = "我临时有事";
            cancel_img1.setVisibility(View.GONE);
            cancel_img2.setVisibility(View.GONE);
            cancel_img3.setVisibility(View.GONE);
            cancel_img4.setVisibility(View.VISIBLE);
            cancel_img5.setVisibility(View.GONE);
        } else if (i == R.id.cancel_5) {
            ss = "尝试性操作";
            cancel_img1.setVisibility(View.GONE);
            cancel_img2.setVisibility(View.GONE);
            cancel_img3.setVisibility(View.GONE);
            cancel_img4.setVisibility(View.GONE);
            cancel_img5.setVisibility(View.VISIBLE);
        }else if (i == R.id.cancel_bt) {
            submit();
        } else if (i == R.id.fanhui) {
            finish();
        }
//            ss = cancel_et.getText().toString().trim();
//            cancel_img1.setVisibility(View.GONE);
//            cancel_img2.setVisibility(View.GONE);
//            cancel_img3.setVisibility(View.GONE);
//            cancel_img4.setVisibility(View.GONE);
//            cancel_img5.setVisibility(View.GONE);
//            cancel_et.setVisibility(View.VISIBLE);
    }

    private void submit() {
        // validate
//        String et = cancel_et.getText().toString().trim();
//        if (TextUtils.isEmpty(et)) {
//            Toast.makeText(this, "请说明你的原因...", Toast.LENGTH_SHORT).show();
//            return;
//        }
        if (!ss.equals("")) {
            Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        bundle.putString("cause", et);
//        intent.putExtras(bundle);
            intent.putExtra("cause", ss);
            CancelOrderActivity.this.setResult(2221, intent);
            finish();
        }else {
            ToastUtils.getInstance().showToast("请选择一个原因");
        }


    }
}
