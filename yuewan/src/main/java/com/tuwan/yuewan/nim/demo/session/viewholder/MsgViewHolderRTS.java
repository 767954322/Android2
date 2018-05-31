package com.tuwan.yuewan.nim.demo.session.viewholder;

import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.demo.session.extension.RTSAttachment;
import com.tuwan.yuewan.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.tuwan.yuewan.nim.uikit.session.viewholder.MsgViewHolderBase;

public class MsgViewHolderRTS extends MsgViewHolderBase {

    private TextView textView;

    public MsgViewHolderRTS(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_rts;
    }

    @Override
    protected void inflateContentView() {
        textView = (TextView) view.findViewById(R.id.rts_text);
    }

    @Override
    protected void bindContentView() {
        RTSAttachment attachment = (RTSAttachment) message.getAttachment();
        textView.setText(attachment.getContent());
    }

    @Override
    protected int leftBackground() {
        return R.drawable.nim_message_left_white_bg;
    }

    @Override
    protected int rightBackground() {
        return R.drawable.nim_message_right_blue_bg;
    }
}

