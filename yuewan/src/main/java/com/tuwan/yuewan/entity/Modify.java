package com.tuwan.yuewan.entity;

/**
 * Created by Administrator on 2017/12/18.
 */

public class Modify {
    String oldpassword;
    String newpassword;

    public Modify(String oldpassword, String newpassword) {
        this.oldpassword = oldpassword;
        this.newpassword = newpassword;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    @Override
    public String toString() {
        return "{\"oldpassword\":" + "\"" + oldpassword + "\"" +
                ",\"newpassword\":" + "\"" + newpassword + "\"" +
                "}";
    }
}
