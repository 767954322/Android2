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

public class HeightActivity extends AppCompatActivity {

    private ImageView iv_titlebar_back;
    private TextView name_bc;
    private ClearEditText weight_nc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);
        initView();
    }

    private void initView() {
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        name_bc = (TextView) findViewById(R.id.name_bc);
        weight_nc = (ClearEditText) findViewById(R.id.weight_nc);
        RxView.clicks(iv_titlebar_back)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
        //保存
        RxView.clicks(name_bc)
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
        String nc = weight_nc.getText().toString().trim();
        if (TextUtils.isEmpty(nc)) {
            Toast.makeText(this, "请输入你的身高", Toast.LENGTH_SHORT).show();
            return;
        }else {
            Intent intent=new Intent(HeightActivity.this,EditdataActivity.class);
            intent.putExtra("height",nc);
            setResult(5,intent);
            finish();
        }




    }
}
