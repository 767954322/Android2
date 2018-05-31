package com.tuwan.yuewan.nim.demo.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujianghua on 2015/7/8.
 */
public class SystemPushAttachment extends CustomAttachment {

    public int type;
    public String content;//内容
    public int time;


    public SystemPushAttachment() {
        super(CustomAttachmentType.SYSTEM_PUSH);
    }

    @Override
    protected void parseData(JSONObject data) {
        content = data.getString("content");
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("content", content);
        return data;
    }
}
