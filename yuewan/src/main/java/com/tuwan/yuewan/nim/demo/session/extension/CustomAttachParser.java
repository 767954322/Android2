package com.tuwan.yuewan.nim.demo.session.extension;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;

/**
 * @author zhangjie
 */
public class CustomAttachParser implements MsgAttachmentParser {


    private static final String KEY_TYPE = "type";

    @Override
    public MsgAttachment parse(String json) {
        CustomAttachment attachment = null;
        try {
            JSONObject object = JSON.parseObject(json);
            int type = object.getInteger(KEY_TYPE);
            switch (type) {
                case CustomAttachmentType.ORDER:
                    attachment = new OrderAttachment();
                    break;
                case CustomAttachmentType.SYSTEM_PUSH:
//                    return new SystemPushAttachment();
                    attachment = new SystemPushAttachment();
                    break;
                case CustomAttachmentType.TICKET:
                    attachment = new TicketAttachment();
                    break;
                case CustomAttachmentType.RTS:
                    attachment = new RTSAttachment();
                    break;
                case CustomAttachmentType.Red:
                    attachment = new RedAttachment();
                    break;
                case CustomAttachmentType.VOICE_GIFT:
                    attachment = new VoiceGiftAttachment();
                    break;
                case CustomAttachmentType.VOICE_DIAMOND:
                    attachment = new VoiceDiamondAttachment();
                    break;
                case CustomAttachmentType.GIFT:
                    attachment = new GiftAttachment();
                    break;
                case CustomAttachmentType.NOTICE:
                    attachment = new NoticeAttachment();
                    break;
                default:
//                    attachment = new DefaultCustomAttachment();
                    break;
            }

            if (attachment != null && type != 8) {
                attachment.fromJson(object);
            }
        } catch (Exception e) {

        }

        return attachment;
    }

    public static String packData(int type, JSONObject data) {
        data.put(KEY_TYPE, type);
        return data.toJSONString();
    }
}
