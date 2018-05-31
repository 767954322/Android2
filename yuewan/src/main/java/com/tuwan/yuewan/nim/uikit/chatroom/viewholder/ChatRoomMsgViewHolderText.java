package com.tuwan.yuewan.nim.uikit.chatroom.viewholder;

import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.uikit.NimUIKit;
import com.tuwan.yuewan.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.tuwan.yuewan.nim.uikit.common.util.sys.ScreenUtil;
import com.tuwan.yuewan.nim.uikit.session.emoji.MoonUtil;

/**
 * Created by hzxuwen on 2016/1/18.
 */
public class ChatRoomMsgViewHolderText extends ChatRoomMsgViewHolderBaseText {
    public ChatRoomMsgViewHolderText(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected boolean isShowBubble() {
        return false;
    }

    @Override
    protected boolean isShowHeadImage() {
        return false;
    }

    @Override
    public void setNameTextView() {
        nameContainer.setPadding(ScreenUtil.dip2px(6), 0, 0, 0);
        ChatRoomViewHolderHelper.setNameTextView(message, nameTextView, nameIconView, context);
    }

    @Override
    protected void bindContentView() {
        TextView bodyTextView = findViewById(R.id.nim_message_item_text_body);
        bodyTextView.setTextColor(Color.BLACK);
        layoutDirection();
        MoonUtil.identifyFaceExpression(NimUIKit.getContext(), bodyTextView, getDisplayText(), ImageSpan.ALIGN_BOTTOM);
        bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        bodyTextView.setOnLongClickListener(longClickListener);
    }

    private void layoutDirection() {
        TextView bodyTextView = findViewById(R.id.nim_message_item_text_body);
        bodyTextView.setPadding(ScreenUtil.dip2px(6), 0, 0, 0);
    }
}
