package com.tuwan.yuewan.nim.demo.main.viewholder;

import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.demo.main.helper.MessageHelper;
import com.tuwan.yuewan.nim.uikit.common.adapter.TViewHolder;
import com.tuwan.yuewan.nim.uikit.common.ui.imageview.HeadImageView;
import com.tuwan.yuewan.nim.uikit.common.util.sys.ScreenUtil;
import com.tuwan.yuewan.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;

public class CustomNotificationViewHolder extends TViewHolder {

    private HeadImageView imgHead;

    private TextView lblNickname;

    private TextView lblMessage;

    private TextView lblDatetime;

    private CustomNotification notification;

    private View bottomLine;

    @Override
    public void refresh(Object item) {
        notification = (CustomNotification) item;

        updateBackground();

        loadPortrait();

        updateNickLabel(MessageHelper.getName(notification.getFromAccount(), notification.getSessionType()));

        updateMsgLabel();
    }

    private void updateBackground() {
        bottomLine.setVisibility(isLastItem() ? View.VISIBLE : View.GONE);
        view.setBackgroundResource(R.drawable.list_item_bg_selecter);
    }

    protected void loadPortrait() {
        // 设置头像
        if (notification.getSessionType() == SessionTypeEnum.P2P) {
            imgHead.loadBuddyAvatar(notification.getFromAccount());
        } else if (notification.getSessionType() == SessionTypeEnum.Team) {
            imgHead.setImageResource(R.drawable.nim_avatar_group);
        }
    }

    private void updateMsgLabel() {
        JSONObject jsonObj = JSONObject.parseObject(notification.getContent());
        String id = jsonObj.getString("id");
        String content;
        if (id != null && id.equals("1")) {
            content = "正在输入...";
        } else {
            content = jsonObj.getString("content");
        }
        lblMessage.setText(content);

        String timeString = TimeUtil.getTimeShowString(notification.getTime(), true);
        lblDatetime.setText(timeString);
    }

    protected void updateNickLabel(String nick) {
        int labelWidth = ScreenUtil.screenWidth;
        labelWidth -= ScreenUtil.dip2px(50 + 70); // 减去固定的头像和时间宽度

        if (labelWidth > 0) {
            lblNickname.setMaxWidth(labelWidth);
        }

        lblNickname.setText(nick);
    }
    @Override
    protected int getResId() {
        return R.layout.item_custom_notification;
    }
    @Override
    public void inflate() {
        this.imgHead = (HeadImageView) view.findViewById(R.id.img_head);
        this.lblNickname = (TextView) view.findViewById(R.id.tv_nick_name);
        this.lblMessage = (TextView) view.findViewById(R.id.tv_message);
        this.lblDatetime = (TextView) view.findViewById(R.id.tv_date_time);
        this.bottomLine = view.findViewById(R.id.bottom_line);
    }
}
