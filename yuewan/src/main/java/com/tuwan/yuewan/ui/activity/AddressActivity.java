package com.tuwan.yuewan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.ui.widget.ClearEditText;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

public class AddressActivity extends AppCompatActivity {

    private ImageView iv_titlebar_back;
    private TextView adress_bc;
    private ClearEditText ages_adress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initView();
    }

    private void initView() {
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        adress_bc = (TextView) findViewById(R.id.adress_bc);
        ages_adress = (ClearEditText) findViewById(R.id.ages_adress);
        RxView.clicks(iv_titlebar_back)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
        //保存
        RxView.clicks(adress_bc)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        submit();
                    }
                });
    }

    private void submit() {
        // validate
        String adress = ages_adress.getText().toString().trim();
        if (TextUtils.isEmpty(adress)) {
            Toast.makeText(this, "请输入详细的地址", Toast.LENGTH_SHORT).show();
            return;
        }else {
            Intent intent=new Intent(AddressActivity.this,EditdataActivity.class);
            intent.putExtra("adress",adress);
            setResult(3,intent);
            finish();
        }


    }
}
