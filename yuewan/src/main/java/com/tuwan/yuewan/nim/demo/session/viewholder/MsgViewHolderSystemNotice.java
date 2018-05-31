package com.tuwan.yuewan.nim.demo.session.viewholder;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.common.LibraryApplication;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.demo.session.extension.NoticeAttachment;
import com.tuwan.yuewan.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.tuwan.yuewan.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.tuwan.yuewan.ui.activity.RedWebActivity;
import com.tuwan.yuewan.ui.activity.YMainActivity;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by zhoujianghua on 2015/8/7.
 */
public class MsgViewHolderSystemNotice extends MsgViewHolderBase {

    NoticeAttachment attachment;

    private ImageView ivSessionNoticeIcon;
    private TextView tvSessionNoticeTitle;
    private TextView tvSessionNoticeDesc;
    private TextView tvSessionNoticeClick;


    public MsgViewHolderSystemNotice(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_system_notice;
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
        attachment = (NoticeAttachment) message.getAttachment();

        if (TextUtils.isEmpty(attachment.litpic)) {
            ivSessionNoticeIcon.setVisibility(View.GONE);
        }else {
            ivSessionNoticeIcon.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .asBitmap()
                    .load(attachment.litpic)
                    .into(ivSessionNoticeIcon);
        }

        if (TextUtils.isEmpty(attachment.title)){
            tvSessionNoticeTitle.setVisibility(View.GONE);
        }else {
            tvSessionNoticeTitle.setVisibility(View.VISIBLE);
        }

        tvSessionNoticeTitle.setText(attachment.title);

        if (TextUtils.isEmpty(attachment.body)) {
            tvSessionNoticeDesc.setVisibility(View.GONE);
        } else {
            tvSessionNoticeDesc.setText(Html.fromHtml(attachment.body));
            tvSessionNoticeDesc.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(attachment.url)){
            tvSessionNoticeClick.setVisibility(View.GONE);
        }else {
            tvSessionNoticeClick.setVisibility(View.VISIBLE);
        }

        RxView.clicks(tvSessionNoticeClick)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(context, RedWebActivity.class);
                        intent.putExtra("url",attachment.url);
                        YMainActivity.sInstance.startActivity(intent);
                    }
                });

    }


    private void assignViews() {
        ivSessionNoticeIcon = (ImageView) view.findViewById(R.id.iv_session_notice_icon);
        tvSessionNoticeTitle = (TextView) view.findViewById(R.id.tv_session_notice_title);
        tvSessionNoticeDesc = (TextView) view.findViewById(R.id.tv_session_notice_desc);
        tvSessionNoticeClick = (TextView) view.findViewById(R.id.tv_session_notice_click);

        int width = LibraryApplication.SCREEN_WIDTH - DensityUtils.dp2px(LibraryApplication.getInstance(), 50);
        int height = width * 340 / 650;

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivSessionNoticeIcon.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;

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
