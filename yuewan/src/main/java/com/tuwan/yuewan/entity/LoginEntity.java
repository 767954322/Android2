package com.tuwan.yuewan.entity;

/**
 * Created by zhangjie on 2017/11/13.
 */

public class LoginEntity {

    /**
     * 用户名     = "张杰#1872"
     */
    public String username ;
    /**
     * 密码       = "Zj7758258"
     */
    public String password ;
    /**
     * 写死mobile,无需验证码
     */
    public String platform="mobile";

    @Override
    public String toString() {
        return "{\"username\":" + "\""+username +"\""+
                ",\"password\":" + "\""+password +"\""+
                ",\"platform\":" + "\""+platform +"\""+
                "}";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
