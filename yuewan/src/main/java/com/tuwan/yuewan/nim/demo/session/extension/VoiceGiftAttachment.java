package com.tuwan.yuewan.nim.demo.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author volin
 * @Create 2018.1.19
 */
public class VoiceGiftAttachment extends CustomAttachment {
    public String teacher_name;
    public String user_name;
    public String user_avatar;
    public String gift_title;
    public int gift_num;
    public String gift_pic;
    public int send;

    public VoiceGiftAttachment() {
        super(CustomAttachmentType.VOICE_DIAMOND);
    }

    @Override
    protected void parseData(JSONObject data) {
        teacher_name = data.getString("teacher_name");
        user_name = data.getString("user_name");
        user_avatar = data.getString("user_avatar");
        gift_title = data.getString("gift_title");
        gift_num = data.getInteger("gift_num");
        gift_pic = data.getString("gift_pic");
        send = data.getInteger("send");
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("teacher_name", teacher_name);
        data.put("user_name", user_name);
        data.put("user_avatar", user_avatar);
        data.put("gift_title", gift_title);
        data.put("gift_num", gift_num);
        data.put("gift_pic", gift_pic);
        data.put("send", send);
        return data;
    }


}
