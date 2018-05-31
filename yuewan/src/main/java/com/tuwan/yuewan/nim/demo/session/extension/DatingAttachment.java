package com.tuwan.yuewan.nim.demo.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujianghua on 2015/7/8.
 */
public class DatingAttachment extends CustomAttachment {

    public int type;
    public String id;
    public String uid;
    public String nickname;
    public String avatar;
    public String sex;
    public String vip;
    public String age;
    public String hours;
    public String unit;
    public String price;
    public String sname;
    public String datestr;


    public DatingAttachment() {
        super(CustomAttachmentType.Dating);
    }

    @Override
    protected void parseData(JSONObject data) {
        id = data.getString("id");
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("id", id);
        return data;
    }
}
