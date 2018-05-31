package com.tuwan.yuewan.nim.demo.session.viewholder;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.demo.session.extension.OrderAttachment;
import com.tuwan.yuewan.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.tuwan.yuewan.nim.uikit.session.activity.P2PMessageActivity;
import com.tuwan.yuewan.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.tuwan.yuewan.ui.activity.OrderDetailsActivity;

/**
 * Created by hzliuxuanlin on 17/9/15.
 */
public class MsgViewHolderOrder extends MsgViewHolderBase {

    private OrderAttachment guessAttachment;

    private TextView mTvContactsItemOrder;
    private ImageView mIvContactsItemOrder;
    private TextView mIvContactsItemNikename;
    private TextView mIvContactsItemServicename;
    private TextView mIvContactsItemTime;
    private TextView mTvContactsItemDetial;
    private RelativeLayout relative;

    public MsgViewHolderOrder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.item_session_order;
    }

    @Override
    protected void inflateContentView() {
        assignViews();
    }


    @Override
    protected void bindContentView() {
        if (message.getAttachment() == null) {
            return;
        }
        guessAttachment = (OrderAttachment) message.getAttachment();
        Glide.with(context).load(guessAttachment.avatar).into(mIvContactsItemOrder);
        mTvContactsItemOrder.setText(guessAttachment.title);
        mIvContactsItemNikename.setText(guessAttachment.name);
        mIvContactsItemServicename.setText(guessAttachment.service);
        mIvContactsItemTime.setText(guessAttachment.otime);
        relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intents = new Intent(context, OrderDetailsActivity.class);
                intents.putExtra("tradeno", guessAttachment.orderid);
                context.startActivity(intents);
                P2PMessageActivity p2PMessageActivity = (P2PMessageActivity) context;
                p2PMessageActivity.finish();

            }
        });
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
        relative = (RelativeLayout) view.findViewById(R.id.iv_contacts_item_all);
        mTvContactsItemOrder = (TextView) view.findViewById(R.id.tv_contacts_item_order);
        mIvContactsItemOrder = (ImageView) view.findViewById(R.id.iv_contacts_item_order);
        mIvContactsItemNikename = (TextView) view.findViewById(R.id.iv_contacts_item_nikename);
        mIvContactsItemServicename = (TextView) view.findViewById(R.id.iv_contacts_item_servicename);
        mIvContactsItemTime = (TextView) view.findViewById(R.id.iv_contacts_item_time);
        mTvContactsItemDetial = (TextView) view.findViewById(R.id.tv_contacts_item_detial);
    }



}
