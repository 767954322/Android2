package com.tuwan.yuewan.nim.demo.session.viewholder;

import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.demo.session.extension.DatingAttachment;
import com.tuwan.yuewan.nim.demo.session.extension.SystemPushAttachment;
import com.tuwan.yuewan.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.tuwan.yuewan.nim.uikit.session.viewholder.MsgViewHolderBase;

/**
 * Created by zhoujianghua on 2015/8/7.
 */
public class MsgViewHolderDating extends MsgViewHolderBase {

    DatingAttachment attachment;
    private TextView mTv;


    public MsgViewHolderDating(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_system_push;
    }

    @Override
    protected void inflateContentView() {
        mTv = (TextView) view.findViewById(R.id.tv_seesion_item_system_push);

//
    }

    @Override
    protected void bindContentView() {
        if (message.getAttachment() == null) {
            return;
        }
        attachment = (DatingAttachment)message.getAttachment();
//        mTv.setText(attachment.id);
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

}
