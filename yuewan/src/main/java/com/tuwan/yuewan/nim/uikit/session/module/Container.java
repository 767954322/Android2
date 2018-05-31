package com.tuwan.yuewan.nim.uikit.session.module;

import android.app.Activity;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

/**
 * Created by zhoujianghua on 2015/7/6.
 */
public class Container {
    public final Activity activity;
    public final String account;
    public final SessionTypeEnum sessionType;
    public final ModuleProxy proxy;

    public Container(Activity activity, String account, SessionTypeEnum sessionType, ModuleProxy proxy) {
        this.activity = activity;
        this.account = account;
        this.sessionType = sessionType;
        this.proxy = proxy;
    }

    public Activity getActivity(ModuleProxy proxy) {
        return activity;
    }

    public String getAccount() {
        return account;
    }

    public SessionTypeEnum getSessionType() {
        return sessionType;
    }

    public ModuleProxy getProxy() {
        return proxy;
    }

    @Override
    public String toString() {
        return "Container{" +
                "activity=" + activity +
                ", account='" + account + '\'' +
                ", sessionType=" + sessionType +
                ", proxy=" + proxy +
                '}';
    }
}
