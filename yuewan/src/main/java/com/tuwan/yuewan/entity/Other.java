package com.tuwan.yuewan.entity;

/**
 * Created by TUWAN on 2017/11/18.
 */

public class Other {
    String openid;
    String token;
    String name;
    String iconurl;
    String appname;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    @Override
    public String toString() {
        return "{\"openid\":" + "\""+openid +"\""+
                ",\"token\":" + "\""+token +"\""+
                ",\"name\":" + "\""+name +"\""+
                ",\"iconurl\":" + "\""+iconurl +"\""+
                ",\"appname\":" + "\""+appname +"\""+
                "}";
    }
}
