package com.tuwan.yuewan.nim.demo.session.viewholder;

import android.widget.ImageView;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.demo.session.extension.TicketAttachment;
import com.tuwan.yuewan.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.tuwan.yuewan.nim.uikit.session.viewholder.MsgViewHolderBase;

/**
 * Created by zhoujianghua on 2015/8/7.
 */
public class MsgViewHolderTicket extends MsgViewHolderBase {

    private TextView mTvSessionTicketTitle;
    private ImageView mIvSessionTicketIcon;
    private TextView mTvSessionTicketName;
    private TextView mTvSessionTicketIntro;
    private TextView mTvSessionTicketTime;
    private TextView mTvSessionTicketMoney;
    private TextView mTvSessionTicketUnit;
    private TextView mTvSessionTicketUse;

    public MsgViewHolderTicket(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_ticket;
    }

    @Override
    protected void inflateContentView() {
        assignViews();
    }

    @Override
    protected void bindContentView() {
        TicketAttachment attachment = (TicketAttachment) message.getAttachment();
        if (attachment == null) {
            return;
        }
        mTvSessionTicketTitle.setText(attachment.title);
        mTvSessionTicketName.setText(attachment.name);
        mTvSessionTicketIntro.setText(attachment.intro);
        mTvSessionTicketTime.setText(attachment.title);
        mTvSessionTicketMoney.setText(attachment.money);
        mTvSessionTicketUnit.setText(attachment.unit);
        mTvSessionTicketUse.setText(attachment.use);

    }


    // 返回该消息是不是居中显示
    @Override
    protected boolean isMiddleItem() {
        return true;
    }

    // 是否显示头像，默认为显示
    @Override
    protected boolean isShowHeadImage() {
        return false;
    }

    // 是否显示气泡背景，默认为显示
    @Override
    protected boolean isShowBubble() {
        return false;
    }




    private void assignViews() {
        mTvSessionTicketTitle = (TextView) view.findViewById(R.id.tv_session_ticket_title);
        mIvSessionTicketIcon = (ImageView) view.findViewById(R.id.iv_session_ticket_icon);
        mTvSessionTicketName = (TextView) view.findViewById(R.id.tv_session_ticket_name);
        mTvSessionTicketIntro = (TextView) view.findViewById(R.id.tv_session_ticket_intro);
        mTvSessionTicketTime = (TextView) view.findViewById(R.id.tv_session_ticket_time);
        mTvSessionTicketMoney = (TextView) view.findViewById(R.id.tv_session_ticket_money);
        mTvSessionTicketUnit = (TextView) view.findViewById(R.id.tv_session_ticket_unit);
        mTvSessionTicketUse = (TextView) view.findViewById(R.id.tv_session_ticket_use);
    }





}
