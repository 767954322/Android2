package com.tuwan.yuewan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.GiftlistAdapter;
import com.tuwan.yuewan.entity.sgiftbean;
import com.tuwan.yuewan.utils.OkManager;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by zhangjie on 2017/10/18.
 */

public class TeacherGiftActivity extends BaseActivity {
    private TextView tv_titlebar_title;
    private RecyclerView recyclerView;
    private GiftlistAdapter giftlistAdapter;
    public String url;
    private ImageView iv_titlebar_more,iv_titlebar_back;
    public static void show(String teacherid, Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), TeacherGiftActivity.class);
        intent.putExtra("teacherid", teacherid);
        fragment.startActivity(intent);
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_teacher_gift;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
//        SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));
            initView();
            tv_titlebar_title.setText("收到礼物");

            wl();

    }

    private void initView() {
        tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        iv_titlebar_more = (ImageView) findViewById(R.id.iv_titlebar_more);
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        iv_titlebar_more.setVisibility(View.GONE);
        iv_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//请求网络
    private void wl(){
        Intent intent =getIntent();
        String uid = intent.getStringExtra("uid");
        Log.e("yzsydhauhdsjka",uid+"");
        url = "https://y.tuwan.com/m/Teacher/giftUser&format=json&teacherid="+uid+"&type=-1&mode=papp";

        OkManager okManager = OkManager.getInstance();

        okManager.getAsync(TeacherGiftActivity.this, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();

                Log.e("yzsydhauhdsjka",result+"");
                TeacherGiftActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Gson gson = new Gson();
                        sgiftbean sgiftbean =  gson.fromJson(result,sgiftbean.class);


                            giftlistAdapter = new GiftlistAdapter(TeacherGiftActivity.this,sgiftbean.getGift());
                            recyclerView.setAdapter(giftlistAdapter);
                        }

                });
            }
        },true);
    }
}
