package com.tuwan.yuewan.nim.demo.avchat;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.demo.avchat.activity.AVChatExitCode;
import com.tuwan.yuewan.nim.demo.avchat.constant.CallStateEnum;
import com.tuwan.yuewan.nim.demo.avchat.widgets.ToggleListener;
import com.tuwan.yuewan.nim.demo.session.extension.VoiceGiftAttachment;
import com.tuwan.yuewan.nim.uikit.cache.NimUserInfoCache;
import com.tuwan.yuewan.nim.uikit.common.ui.imageview.HeadImageView;

/**
 * 音频管理器， 音频界面初始化和管理
 * Created by hzxuwen on 2015/4/24.
 */
public class AVChatAudio implements View.OnClickListener, ToggleListener {
    // constant
    private static final int[] NETWORK_GRADE_DRAWABLE = new int[]{R.drawable.network_grade_0, R.drawable.network_grade_1, R.drawable.network_grade_2, R.drawable.network_grade_3};
    private static final int[] NETWORK_GRADE_LABEL = new int[]{R.string.avchat_network_grade_0, R.string.avchat_network_grade_1, R.string.avchat_network_grade_2, R.string.avchat_network_grade_3};

    private enum STATE {CALLING, CALLED}

    private enum ROLE {USER, TEACHER}

    private Context context;
    // view
    private View rootView;

    // data
    private AVChatUI manager;
    private AVChatUIListener listener;

    // state
    private boolean init = false;

    private LinearLayout mCallingImage;
    private HeadImageView mCallingHead;
    private TextView mCallingNickname;
    private TextView mCallingText;

    private RelativeLayout mCalledImage;
    private HeadImageView mCalledHead;
    private TextView mCalledNickname;
    private TextView mCalledAge;
    private ImageView mCalledDiamondIcon;
    private TextView mCalledDiamond;
    private Chronometer mCalledTime;
    private TextView mCalledText;

    private LinearLayout mCalledButton;
    private ImageView mCalledButtonHansfree;
    private ImageView mCalledButtonMute;
    private ImageView mCalledButtonGift;

    private RelativeLayout mCallButton;
    private LinearLayout mCalledButtonContent;
    private ImageView mCalledButtonRefuse;
    private ImageView mCalledButtonRecept;
    private LinearLayout mCallingButtonContent;
    private ImageView mCallingButtonRefuse;
    private TextView mCallingButtonRefuseText;
    private ImageView mAvchatLittlescr;

    //礼物
    private RelativeLayout mGift;
    private HeadImageView mGiftHead;
    private TextView mGiftTitle;
    private TextView mGiftUsername;
    private ImageView mGiftImage;
    private TextView mGiftNum;

    //top tips
    private RelativeLayout mTopTipsBg;
    private TextView mTopTipsText;
    private TextView mIncomeTips;

    private int mDiamond = 0;
    private boolean mUser = true; //是否是用户
    private int mMinuteDiamond = 6; //每分钟钻石
    private int mPrice = 0;
    private CountDownTimer mTimer;
    private boolean isTopTips = false;
    private AVChatAudio.STATE mState = STATE.CALLING;

    public AVChatAudio(Context context, View root, AVChatUIListener listener, AVChatUI manager) {
        this.context = context;
        this.rootView = root;
        this.listener = listener;
        this.manager = manager;

        findViews();
    }

    /**
     * 音视频状态变化及界面刷新
     *
     * @param state
     */
    public void onCallStateChange(CallStateEnum state) {
        switch (state) {
            case OUTGOING_AUDIO_CALLING: //拨打出的免费通话
                switchCallState(STATE.CALLING, ROLE.USER);
                setCallingUserInfo();
                mUser = true;

                setCallingText("正在等待对方接受邀请...");
                break;

            case INCOMING_AUDIO_CALLING://免费通话请求
                switchCallState(STATE.CALLING, ROLE.TEACHER);
                setCallingUserInfo();
                mUser = false;

                setCallingText("邀请您进行语音聊天...");
                break;

            case AUDIO:
                setCalledUserinfo();
                setTime(true);
                switchCallState(STATE.CALLED, mUser ? ROLE.USER : ROLE.TEACHER);

                if (!mUser) {
                    listener.callStart();
                    listener.callUserInfo();
                } else {
                    //计算时长
                    countDownTimer();
                }
                break;

            case AUDIO_CONNECTING:
                switchCallState(STATE.CALLING, ROLE.USER);
                setCallingUserInfo();
                setCallingText("连接中，请稍候...");
                break;
            default:
                break;
        }
    }

    public void setCallingText(String text) {
        mCallingText.setText(text);
    }

    /**
     * 界面初始化
     */
    private void findViews() {
        if (init || rootView == null) {
            return;
        }

        mCallingImage = (LinearLayout) rootView.findViewById(R.id.avchat_calling_image);
        mCallingHead = (HeadImageView) rootView.findViewById(R.id.avchat_calling_head);
        mCallingNickname = (TextView) rootView.findViewById(R.id.avchat_calling_nickname);
        mCallingText = (TextView) rootView.findViewById(R.id.avchat_calling_text);

        mCalledImage = (RelativeLayout) rootView.findViewById(R.id.avchat_called_image);
        mCalledHead = (HeadImageView) rootView.findViewById(R.id.avchat_called_head);
        mCalledNickname = (TextView) rootView.findViewById(R.id.avchat_called_nickname);
        mCalledAge = (TextView) rootView.findViewById(R.id.avchat_called_age);
        mCalledDiamondIcon = (ImageView) rootView.findViewById(R.id.avchat_called_diamond_icon);
        mCalledDiamond = (TextView) rootView.findViewById(R.id.avchat_called_diamond);
        mCalledTime = (Chronometer) rootView.findViewById(R.id.avchat_called_time);
        mCalledText = (TextView) rootView.findViewById(R.id.avchat_called_text);
        mAvchatLittlescr = (ImageView) rootView.findViewById(R.id.avchat_littlescr);

        mCalledButton = (LinearLayout) rootView.findViewById(R.id.avchat_called_button);
        mCalledButtonHansfree = (ImageView) rootView.findViewById(R.id.avchat_hansfree);
        mCalledButtonMute = (ImageView) rootView.findViewById(R.id.avchat_mute);
        mCalledButtonGift = (ImageView) rootView.findViewById(R.id.avchat_gift_button);

        mCallButton = (RelativeLayout) rootView.findViewById(R.id.avchat_call_button);
        mCalledButtonContent = (LinearLayout) rootView.findViewById(R.id.avchat_button_called_content);
        mCalledButtonRefuse = (ImageView) rootView.findViewById(R.id.avchat_button_called_refuse);
        mCalledButtonRecept = (ImageView) rootView.findViewById(R.id.avchat_button_called_recept);
        mCallingButtonContent = (LinearLayout) rootView.findViewById(R.id.avchat_button_calling_content);
        mCallingButtonRefuse = (ImageView) rootView.findViewById(R.id.avchat_button_calling_refuse);
        mCallingButtonRefuseText = (TextView) rootView.findViewById(R.id.avchat_button_calling_refuse_text);

        //礼物
        mGift = (RelativeLayout) rootView.findViewById(R.id.avchat_gift_tips);
        mGiftHead = (HeadImageView) rootView.findViewById(R.id.avchat_gift_head);
        mGiftTitle = (TextView) rootView.findViewById(R.id.avchat_gift_title);
        mGiftUsername = (TextView) rootView.findViewById(R.id.avchat_gift_username);
        mGiftImage = (ImageView) rootView.findViewById(R.id.avchat_gift_image);
        mGiftNum = (TextView) rootView.findViewById(R.id.avchat_gift_num);

        mTopTipsBg = (RelativeLayout) rootView.findViewById(R.id.avchat_top_tips_bg);
        mTopTipsText = (TextView) rootView.findViewById(R.id.avchat_top_tips_text);
        mIncomeTips = (TextView) rootView.findViewById(R.id.avchat_income_tips);

        mCalledButtonRefuse.setOnClickListener(this);
        mCallingButtonRefuse.setOnClickListener(this);
        mCalledButtonRecept.setOnClickListener(this);
        mCalledButtonHansfree.setOnClickListener(this);
        mCalledButtonMute.setOnClickListener(this);
        mCalledButtonGift.setOnClickListener(this);
        mAvchatLittlescr.setOnClickListener(this);

        init = true;
    }

    /**
     * 设置用户信息
     */
    private void setCallingUserInfo() {
        String account = manager.getAccount();
        mCallingHead.loadBuddyAvatar(account);
        mCallingNickname.setText(NimUserInfoCache.getInstance().getUserDisplayName(account));
    }

    private void setCalledUserinfo() {
        String account = manager.getAccount();
        mCalledHead.loadBuddyAvatar(account);
        mCalledNickname.setText(NimUserInfoCache.getInstance().getUserDisplayName(account));
    }

    /**
     * 切换通话状态页面
     *
     * @param state 状态 0拨打 1通话中
     * @param role 角色 0用户 1通话中
     */
    private void switchCallState(STATE state, ROLE role) {
        mGift.setVisibility(View.GONE);
        mState = state;

        if (state == STATE.CALLING) {
            mCallingImage.setVisibility(View.VISIBLE);
            mCallButton.setVisibility(View.VISIBLE);

            mCalledImage.setVisibility(View.GONE);
            mCalledButton.setVisibility(View.GONE);

            if (role == ROLE.USER) {
                mCalledButtonContent.setVisibility(View.GONE);
                mCallingButtonContent.setVisibility(View.VISIBLE);
                mCallingButtonRefuseText.setText("取消");
            } else {
                mCalledButtonContent.setVisibility(View.VISIBLE);
                mCallingButtonContent.setVisibility(View.GONE);
            }
        } else {
            if (role == ROLE.TEACHER) {
                mCalledDiamondIcon.setVisibility(View.GONE);
                mCalledDiamond.setVisibility(View.GONE);
            } else {
                mCalledDiamondIcon.setVisibility(View.VISIBLE);
                mCalledDiamond.setVisibility(View.VISIBLE);
            }

            mCallingImage.setVisibility(View.GONE);
            mCallButton.setVisibility(View.VISIBLE);
            mCallingButtonContent.setVisibility(View.VISIBLE);

            mCalledImage.setVisibility(View.VISIBLE);
            mCalledButton.setVisibility(View.VISIBLE);
            mCallingButtonRefuseText.setText("挂断");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.avchat_button_calling_refuse) {
            listener.onHangUp();
        } else if (v.getId() == R.id.avchat_button_called_refuse) {
            listener.onRefuse();

            if (mState == STATE.CALLED && !mUser) {
                listener.callEnd();
            }
        } else if (v.getId() == R.id.avchat_button_called_recept) {
            listener.onReceive();
        } else if (v.getId() == R.id.avchat_mute) {
            listener.toggleMute();
        } else if (v.getId() == R.id.avchat_hansfree) {
            listener.toggleSpeaker();
        } else if (v.getId() == R.id.avchat_gift_button) {
            listener.showGiftDialog();
        } else if(v.getId() == R.id.avchat_littlescr) {
            listener.startVideoService();
        }

    }

    /**
     * 设置通话时间显示
     *
     * @param visible
     */
    private void setTime(boolean visible) {
        mCalledTime.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (visible) {
            mCalledTime.setBase(manager.getTimeBase());
            mCalledTime.start();
        }
    }

    public void closeSession(int exitCode) {
        if (init) {
            mCalledTime.stop();
        }
    }

    public void setMute(boolean open) {
        mCalledButtonMute.setBackgroundResource(open ? R.drawable.icon_call_mute_normol : R.drawable.icon_call_mute_chosen);
    }

    public void setHansfree(boolean open) {
        mCalledButtonHansfree.setBackgroundResource(open ? R.drawable.icon_call_hansfree_chosen : R.drawable.icon_call_hansfree_normal);
    }

    /**
     * 设置礼物
     *
     * @param voiceGiftAttachment
     */
    public void setGift(VoiceGiftAttachment voiceGiftAttachment) {
        mGift.setVisibility(View.VISIBLE);
        mGiftHead.loadAvatar(voiceGiftAttachment.user_avatar);
        Glide.with(context)
                .load(voiceGiftAttachment.gift_pic)
                .into(mGiftImage);
        mGiftNum.setText("X" + voiceGiftAttachment.gift_num);

        if (voiceGiftAttachment.send == 1) {
            mGiftUsername.setText(voiceGiftAttachment.user_name);
            mGiftTitle.setText("送给" + voiceGiftAttachment.teacher_name + voiceGiftAttachment.gift_title);
        } else {
            mGiftUsername.setText(voiceGiftAttachment.teacher_name);
            mGiftTitle.setText("收到" + voiceGiftAttachment.user_name + voiceGiftAttachment.gift_title);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mGift.setVisibility(View.GONE);
            }
        }, 5000);
    }

    public void showTopTips() {
        if (!mUser) {
            return;
        }

        mTopTipsBg.setVisibility(View.VISIBLE);
        mTopTipsText.setText("通话时间不足3分钟，请补充钻石");
        isTopTips = true;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTopTipsBg.setVisibility(View.GONE);
            }
        }, 5000);
    }

    public void showIncomeTips(int price) {
        mIncomeTips.setVisibility(View.VISIBLE);
        mIncomeTips.setText("我的收入："+ price +"钻石/分钟");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIncomeTips.setVisibility(View.GONE);
            }
        }, 5000);
    }

    /**
     * 设置用户信息
     */
    public void setUserInfo(String sex, int age) {
        String sexStr = "";
        int sexColor = R.drawable.avchat_age_bg;
        if ("男".equals(sex) || "1".equals(sex)) {
            sexStr += "♂";
            sexColor = R.drawable.avchat_age_bg_man;
        } else if("女".equals(sex) || "2".equals(sex)) {
            sexStr += "♀";
            sexColor = R.drawable.avchat_age_bg;
        }

        if (age >= 0) {
            sexStr += age;
        }

        if ("".equals(sexStr)) {
            mCalledAge.setVisibility(View.GONE);
        } else {
            mCalledAge.setVisibility(View.VISIBLE);
            mCalledAge.setText(sexStr);
            mCalledAge.setBackground(context.getResources().getDrawable(sexColor));

        }

    }

    /**
     * 设置钻石
     */
    public void setDiamond(int diamond) {
        mDiamond = diamond;
        mCalledDiamond.setText(diamond +"钻石");
    }

    /**
     * 设置价格
     * @param price
     */
    public void setPrice(int price) {
        mPrice = price;
    }

    /**
     * 计算时长
     */
    public void countDownTimer() {
        if (mDiamond < mPrice) {
            if (mUser) {
                listener.onHangUp(AVChatExitCode.DIAMOND_NO);
            } else {
                listener.onHangUp(AVChatExitCode.HANGUP);
            }
            listener.callEnd();
        }

        if ((float)mDiamond/mPrice <= 3.0f && !isTopTips) {
            showTopTips();
        }

        if (mTimer != null) {
            mTimer.cancel();
        }

        final long total = (long) mDiamond * 2/mPrice * 30000;
        mTimer = new CountDownTimer(total, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished/1000;
                if (time%30 == 0 || (total/1000-time) == 6) {
                    mDiamond -= mPrice / 2;
                    if ((float)mDiamond / mPrice <= 3.0f && !isTopTips) {
                        showTopTips();
                    }
                    mCalledDiamond.setText(mDiamond + "钻石");
                }

            }

            @Override
            public void onFinish() {
                if (mUser) {
                    listener.onHangUp(AVChatExitCode.DIAMOND_NO);
                } else {
                    listener.onHangUp(AVChatExitCode.HANGUP);
                }
                listener.callEnd();
            }
        };
        mTimer.start();
    }

    public String getTime() {
        return mCalledTime.getText().toString();
    }

    /******************************* toggle listener *************************/
    @Override
    public void toggleOn(View v) {
        onClick(v);
    }

    @Override
    public void toggleOff(View v) {
        onClick(v);
    }

    @Override
    public void toggleDisable(View v) {

    }
}
