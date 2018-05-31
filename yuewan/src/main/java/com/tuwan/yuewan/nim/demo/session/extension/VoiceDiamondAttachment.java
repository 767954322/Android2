package com.tuwan.yuewan.nim.demo.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author volin
 * @Create 2018.1.19
 */
public class VoiceDiamondAttachment extends CustomAttachment {
    public int diamond;

    public VoiceDiamondAttachment() {
        super(CustomAttachmentType.VOICE_DIAMOND);
    }

    @Override
    protected void parseData(JSONObject data) {
        diamond = data.getInteger("diamond");
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("diamond", diamond);
        return data;
    }


}
