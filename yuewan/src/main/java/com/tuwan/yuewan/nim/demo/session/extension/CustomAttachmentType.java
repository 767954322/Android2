package com.tuwan.yuewan.nim.demo.session.extension;

import com.tuwan.yuewan.nim.demo.session.viewholder.MsgViewHolderOrder;
import com.tuwan.yuewan.nim.uikit.session.viewholder.MsgViewHolderBase;

/**
 * @author zhangjie
 * 与布局的对应{@link MsgViewHolderBase}
 */
public interface CustomAttachmentType {
    // 多端统一
    /**
     * 订单消息 {@link MsgViewHolderOrder}{@link OrderAttachment}
     */
    int ORDER = 1;

    /**
     * 系统通知 系统通知
     */
    int SYSTEM_PUSH = 2;

    /**
     * 优惠券
     */
    int TICKET = 3;

    int RTS = 4;
    int VOICE_GIFT = 5;
    int VOICE_DIAMOND = 6;

    /**
     * 红包
     */
    int Red = 7;

    /**
     * 闪约
     */
    int Dating = 8;
    /**
     * 礼物
     */
    int GIFT = 11;
    int NOTICE = 12;

    int RedPacket = 60;
    int OpenedRedPacket = 61;
}
