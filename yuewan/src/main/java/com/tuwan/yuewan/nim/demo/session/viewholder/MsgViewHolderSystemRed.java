package com.tuwan.yuewan.nim.demo.session.viewholder;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.demo.session.extension.RedAttachment;
import com.tuwan.yuewan.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.tuwan.yuewan.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.tuwan.yuewan.ui.activity.RedWebActivity;
import com.tuwan.yuewan.ui.activity.YMainActivity;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by zhoujianghua on 2015/8/7.
 */
public class MsgViewHolderSystemRed extends MsgViewHolderBase {

    RedAttachment attachment;

    private RelativeLayout rlySessionRed;
    private ImageView ivSessionRedIcon;
    private TextView tvSessionRedTitle;
    private TextView tvSessionRedName;
    private TextView tvSessionRedTime;


    public MsgViewHolderSystemRed(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_red;
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
        attachment = (RedAttachment) message.getAttachment();

        Glide.with(context)
                .asBitmap()
                .load(attachment.icon)
                .into(ivSessionRedIcon);

        tvSessionRedTitle.setText(attachment.getTitle());
        tvSessionRedName.setText(attachment.getIntro());
        tvSessionRedTime.setText(attachment.getTimes());

        RxView.clicks(rlySessionRed)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(context, RedWebActivity.class);
                        intent.putExtra("url",attachment.url);
                        if (YMainActivity.sInstance != null) {
                            YMainActivity.sInstance.startActivity(intent);
                        }
                    }
                });

    }


    private void assignViews() {
        rlySessionRed = (RelativeLayout) view.findViewById(R.id.rly_tv_session_red);
        ivSessionRedIcon = (ImageView) view.findViewById(R.id.iv_session_red_icon);
        tvSessionRedTitle = (TextView) view.findViewById(R.id.tv_session_red_title);
        tvSessionRedName = (TextView) view.findViewById(R.id.tv_session_red_name);
        tvSessionRedTime = (TextView) view.findViewById(R.id.tv_session_red_time);

//        int width = LibraryApplication.SCREEN_WIDTH - DensityUtils.dp2px(LibraryApplication.getInstance(), 50);
//        int height = width * 340 / 650;
//
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivSessionNoticeIcon.getLayoutParams();
//        layoutParams.width = width;
//        layoutParams.height = height;

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
