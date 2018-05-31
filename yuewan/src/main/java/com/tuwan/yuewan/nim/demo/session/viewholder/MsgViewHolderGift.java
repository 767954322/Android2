package com.tuwan.yuewan.nim.demo.session.viewholder;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.demo.session.extension.GiftAttachment;
import com.tuwan.yuewan.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.tuwan.yuewan.nim.uikit.session.viewholder.MsgViewHolderBase;

/**
 * Created by zhoujianghua on 2015/8/7.
 */
public class MsgViewHolderGift extends MsgViewHolderBase {

    private ImageView mIvSessionGift;
    private TextView mTvSessionGiftTitle;
    private TextView mTvSessionGiftSub;

    public MsgViewHolderGift(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_gift;
    }

    @Override
    protected void inflateContentView() {
        assignViews();
    }

    @Override
    protected void bindContentView() {
        GiftAttachment attachment = (GiftAttachment) message.getAttachment();
        if (attachment == null) {
            return;
        }
        int s = Integer.parseInt(message.getFromAccount());
        int ss = Integer.parseInt(message.getSessionId());
        if (s == ss) {

            mTvSessionGiftTitle.setText(attachment.number + "个" + attachment.title);
            mTvSessionGiftSub.setText("魅力值：+" + attachment.charm_score * attachment.number);
            Glide.with(context)
                    .asBitmap()
                    .load(attachment.pic)
                    .into(mIvSessionGift);
        } else {
            mTvSessionGiftTitle.setText(attachment.number + "个" + attachment.title);
            mTvSessionGiftSub.setText("贡献值：+" + attachment.charm_score * attachment.number);
            Glide.with(context)
                    .asBitmap()
                    .load(attachment.pic)
                    .into(mIvSessionGift);
        }
    }

    private void assignViews() {
        mIvSessionGift = (ImageView) findViewById(R.id.iv_session_gift);
        mTvSessionGiftTitle = (TextView) findViewById(R.id.tv_session_gift_title);
        mTvSessionGiftSub = (TextView) findViewById(R.id.tv_session_gift_sub);
    }


}
