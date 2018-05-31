package com.tuwan.yuewan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.tuwan.common.ui.activity.base.WebBaseActivity;

/**
 * Created by zhangjie on 2017/10/12.
 */
public class WebActivity extends WebBaseActivity {

    public final static String STRING_EXTRA_URL = "StringExtraUrl";


    @Override
    protected String initUrl() {
        return getIntent().getStringExtra(STRING_EXTRA_URL);
    }


    @Override
    protected void customInit(Bundle savedInstanceState) {

    }




}
