package com.tuwan.yuewan.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tuwan.common.LibraryApplication;
import com.tuwan.yuewan.R;

/**
 * @author zhangjie
 * @date 2017/10/30s
 * 参考 inputPanel中的 audioRecordBtn
 */
public class MessageBtmContentAudioView extends FrameLayout {

    public ImageView mIvAudio;
    public ImageView mIvAudioNormal;
    private Drawable initBg;
    private Drawable initIc;
    private Drawable errorBg;
    private Drawable delBg;

    private int status = -1;

    public MessageBtmContentAudioView(Context context) {
        super(context);
        init(context);

    }

    private void init(Context context) {
        setBackgroundColor(0xffffffff);
        View.inflate(context, R.layout.widget_message_audio, this);

        mIvAudio = (ImageView) findViewById(R.id.iv_audio);
        mIvAudioNormal = (ImageView) findViewById(R.id.iv_audio_scale);

        initBg = LibraryApplication.getInstance().getResources().getDrawable(R.drawable.shape_audio_bg_init);
        initIc = LibraryApplication.getInstance().getResources().getDrawable(R.drawable.ic_audio_init);
        errorBg = LibraryApplication.getInstance().getResources().getDrawable(R.drawable.shape_audio_bg_error);
        delBg = LibraryApplication.getInstance().getResources().getDrawable(R.drawable.ic_audio_del);
    }

    Animator animate = AnimatorInflater.loadAnimator(LibraryApplication.getInstance().getApplicationContext(), R.animator.anim_scale_message);

    public void setIvInit() {
        if (status == 0) {
            return;
        }
        mIvAudioNormal.setVisibility(View.GONE);
        animate.end();
        status = 0;

        mIvAudio.setBackground(initBg);
        mIvAudio.setImageDrawable(initIc);

    }

    public void setIvError() {
        if (status == 1) {
            return;
        }
        mIvAudioNormal.setVisibility(View.GONE);
        animate.end();
        status = 1;

        mIvAudio.setBackground(errorBg);
        mIvAudio.setImageBitmap(null);
    }

    public void setIvDel() {
        if (status == 2) {
            return;
        }
        mIvAudioNormal.setVisibility(View.GONE);
        animate.end();

        status = 2;
        mIvAudio.setBackground(delBg);
        mIvAudio.setImageBitmap(null);
    }

    public void setIvNormal() {
        if (status == 3) {
            return;
        }
        status = 3;
        mIvAudioNormal.setVisibility(View.VISIBLE);

        animate.setTarget(mIvAudioNormal);
        animate.start();
    }


}
