package com.tuwan.yuewan.ui.activity;


import android.content.Intent;

import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;

import android.widget.RelativeLayout;


import com.bumptech.glide.Glide;

import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * 提交服务
 */
public class ApplyImageActivity extends BaseActivity {

    private RelativeLayout rlyApply;
    private PhotoView imgApply;
    private String imgUrl = "";

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_apply_img;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        if (intent.hasExtra("imgUrl")) {
            imgUrl = intent.getStringExtra("imgUrl");
        }
//        LinearLayout applyone = (LinearLayout) findViewById(R.id.apply_one);
//        textView5 = (TextView) findViewById(R.id.textView5);
        rlyApply = (RelativeLayout) findViewById(R.id.rly_apply);
        imgApply = (PhotoView) findViewById(R.id.img_apply);
        Glide.with(this).load(imgUrl).into(imgApply);
        imgApply.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish();
            }
        });
        rlyApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }





}
