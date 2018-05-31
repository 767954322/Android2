package com.tuwan.yuewan.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tuwan.common.LibraryApplication;
import com.tuwan.common.utils.LogUtil;
import com.tuwan.common.utils.RxTimerUtil;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.framework.IGiftViewListener;
import com.tuwan.yuewan.nim.uikit.session.emoji.EmoticonPickerView;
import com.tuwan.yuewan.ui.activity.TeacherServiceDetialActivity;
import com.tuwan.yuewan.utils.AppPhotoUtil;
import com.tuwan.yuewan.utils.EmotionKeyboard;

import java.util.ArrayList;


/**
 * Created by zhangjie on 2017/10/30.
 */
public class MessageBtmView extends LinearLayout implements IGiftViewListener {

    //顶部3个父控件
    private View mTopContainer;
    private View mAudioContainer;
    public View mGiftNumberContainer;
    //顶部常规子控件
    public EditText mEt;
    public View mBtnSend;
    //顶部语音输入子控件
    private TextView mTvAudio;
    private ProgressBar mPb;
    //顶部礼物数量输入子控件
    public EditText mEtGiftNumberBar;
    public View mGiftNumberSend;
    //顶部操作栏5个按钮
    public ImageView mIvAudio;
    public ImageView mIvPic;
    public ImageView mIvCamera;
    public ImageView mIvGift;
    public ImageView mIvEmojy;

    //底部父控件
    public FrameLayout mContentContainer;
    //底部4个内容页
    public MessageBtmContentAudioView mChildAudio;
    public MessageBtmContentPicView mChildpic;
    public MessageBtmContentGiftView mChildGift;
    public EmoticonPickerView mChildEmojy;

    private ArrayList<View> mViews;
    private Drawable mAudioNormal;
    private Drawable mAudioSelected;
    private Drawable mPicNormal;
    private Drawable mPicSelected;
    private Drawable mGiftNormal;
    private Drawable mGiftSelected;
    private Drawable mEmojyNormal;
    private Drawable mEmojySelected;

    //表情面板 帮助类
    public EmotionKeyboard mEmotionKeyboard;

    private RxTimerUtil mRxTimerUtil;
    private Activity mActivity;

    public MessageBtmView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MessageBtmView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public MessageBtmView(TeacherServiceDetialActivity teacherServiceDetialActivity) {
        super(teacherServiceDetialActivity);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);
        View.inflate(context, R.layout.widget_message_btm, this);

        assignViews();

        initDrawable();

        mRxTimerUtil = new RxTimerUtil();
    }

    //最下方的一排对话或添加照片按钮
    private void initDrawable() {
        mAudioNormal = LibraryApplication.getInstance().getResources().getDrawable(R.drawable.ic_message_audio_nomal);
        mAudioSelected = LibraryApplication.getInstance().getResources().getDrawable(R.drawable.ic_message_audio_selected);
        mPicNormal = LibraryApplication.getInstance().getResources().getDrawable(R.drawable.ic_message_pic_normal);
        mPicSelected = LibraryApplication.getInstance().getResources().getDrawable(R.drawable.ic_message_pic_selected);
        mGiftNormal = LibraryApplication.getInstance().getResources().getDrawable(R.drawable.ic_message_gift_normal);
        mGiftSelected = LibraryApplication.getInstance().getResources().getDrawable(R.drawable.ic_message_gift_selected);
        mEmojyNormal = LibraryApplication.getInstance().getResources().getDrawable(R.drawable.ic_message_emojy_normal);
        mEmojySelected = LibraryApplication.getInstance().getResources().getDrawable(R.drawable.ic_message_emojy_selected);
    }

    private void assignViews() {
        mEt = (EditText) findViewById(R.id.bar_edit_text);
        mAudioContainer = findViewById(R.id.fl_message_btm_audio);
        mTvAudio = (TextView) findViewById(R.id.tv_message_btm_audio);
        mPb = (ProgressBar) findViewById(R.id.pb_message_btm_audio);
        mTopContainer = findViewById(R.id.include_emotion_view);
        mBtnSend = findViewById(R.id.bar_btn_send);
        mIvAudio = (ImageView) findViewById(R.id.iv_audio);
        mIvPic = (ImageView) findViewById(R.id.iv_pic);
        mIvCamera = (ImageView) findViewById(R.id.iv_camera);
        mIvGift = (ImageView) findViewById(R.id.iv_gift);
        mIvEmojy = (ImageView) findViewById(R.id.iv_emojy);

        //输入礼物数量
        mGiftNumberContainer = findViewById(R.id.ll_gift_number_bar);
        mEtGiftNumberBar = (EditText) findViewById(R.id.et_gift_number_bar);
        mGiftNumberSend = findViewById(R.id.btn_gift_number_bar_send);

        mContentContainer = (FrameLayout) findViewById(R.id.fl_emotionview_layout);

        mViews = new ArrayList<>();
        mViews.add(mIvAudio);
        mViews.add(mIvPic);
        mViews.add(mIvGift);
        mViews.add(mIvEmojy);
    }

    public void initData(Activity activity, View contentView) {
        this.mActivity = activity;
        mEmotionKeyboard = EmotionKeyboard.with(activity)
                .setEmotionView(mContentContainer)//绑定表情面板
                .bindToContent(contentView)//绑定内容view
                .bindToEditText(mEt)//判断绑定那种EditView
                .bindToEmotionButton(mViews, 3)//绑定表情按钮
                .build();
    }


    public void initChild(Activity activity, String account, AppPhotoUtil util) {

        mChildAudio = new MessageBtmContentAudioView(activity);
        mIvAudio.setVisibility(View.VISIBLE);
        mIvAudio.setTag(mChildAudio);

        mChildpic = new MessageBtmContentPicView(activity, util);
        mIvPic.setVisibility(View.VISIBLE);
        mIvPic.setTag(mChildpic);

        mIvCamera.setVisibility(View.VISIBLE);

        mChildGift = new MessageBtmContentGiftView(activity);
        if (account.equals("tuwan_order")) {
            mChildGift.setData(333);
        } else if (account.equals("tuwan_system")) {
            mChildGift.setData(444);
        } else if (account.equals("tuwan_voice")) {
            mChildGift.setData(555);
        } else if(account.equals("tuwan_dating")){
//            mChildGift.setData(Integer.parseInt(account));
        }else {
            mChildGift.setData(Integer.parseInt(account));
        }
        mChildGift.setGiftListener(this);
        mIvGift.setVisibility(View.VISIBLE);
        mIvGift.setTag(mChildGift);

        mChildEmojy = new EmoticonPickerView(activity);
        mIvEmojy.setVisibility(View.VISIBLE);
        mIvEmojy.setTag(mChildEmojy);
    }

    /**
     * 隐藏底部action
     */
    public void hiden() {
        mIsAudio = false;
        removeContentViews();

        mIvAudio.setImageDrawable(mAudioNormal);
        mIvPic.setImageDrawable(mPicNormal);
        mIvGift.setImageDrawable(mGiftNormal);
        mIvEmojy.setImageDrawable(mEmojyNormal);
        mEt.clearFocus();

        mEmotionKeyboard.interceptBackPress();
    }

    public void onSoftInputShow() {
        mIsAudio = false;
        removeContentViews();

        mIvAudio.setImageDrawable(mAudioNormal);
        mIvPic.setImageDrawable(mPicNormal);
        mIvGift.setImageDrawable(mGiftNormal);
        mIvEmojy.setImageDrawable(mEmojyNormal);
    }


    public void showEmojy() {
        mIsAudio = false;
        removeContentViews();
        mContentContainer.addView(mChildEmojy);

        mIvAudio.setImageDrawable(mAudioNormal);
        mIvPic.setImageDrawable(mPicNormal);
        mIvGift.setImageDrawable(mGiftNormal);
        mIvEmojy.setImageDrawable(mEmojySelected);

        mEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
    }

    public void showAudio() {
        mIsAudio = true;
        removeContentViews();
        mContentContainer.addView(mChildAudio);

        mIvAudio.setImageDrawable(mAudioSelected);
        mIvPic.setImageDrawable(mPicNormal);
        mIvGift.setImageDrawable(mGiftNormal);
        mIvEmojy.setImageDrawable(mEmojyNormal);
    }

    public void showPic() {
        mIsAudio = false;
        removeContentViews();
        mContentContainer.addView(mChildpic);

        mIvAudio.setImageDrawable(mAudioNormal);
        mIvPic.setImageDrawable(mPicSelected);
        mIvGift.setImageDrawable(mGiftNormal);
        mIvEmojy.setImageDrawable(mEmojyNormal);
    }

    public void showGift() {
        mIsAudio = false;
        removeContentViews();
        mContentContainer.addView(mChildGift);

        mChildGift.onHiden();
        mIvAudio.setImageDrawable(mAudioNormal);
        mIvPic.setImageDrawable(mPicNormal);
        mIvGift.setImageDrawable(mGiftSelected);
        mIvEmojy.setImageDrawable(mEmojyNormal);
    }

    public void removeContentViews() {
        int childCount = mContentContainer.getChildCount();
        if (childCount > 0) {
            if (mContentContainer.getChildAt(0) instanceof MessageBtmContentPicView) {
                mChildpic.hiden();
            }
        }
        mContentContainer.removeAllViews();
    }

    private boolean mIsGift = false;

    /**
     * 是否正在显示礼物输入框
     */
    public boolean isGiftTop() {
        return mIsGift;
    }

    public void hidenGiftTop() {
        mIsGift = false;
        mRxTimerUtil.cancel();

        mChildGift.onHiden();
        mGiftNumberContainer.setVisibility(View.GONE);
        mTopContainer.setVisibility(View.VISIBLE);
        hiden();
    }

    public void showGiftTop() {
        mIsGift = true;
        mGiftNumberContainer.setVisibility(View.VISIBLE);
        mTopContainer.setVisibility(View.GONE);
        mEmotionKeyboard.interceptBackPress();

        final InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mEtGiftNumberBar.post(new Runnable() {
            @Override
            public void run() {
                mEtGiftNumberBar.requestFocus();
                mEtGiftNumberBar.requestFocusFromTouch();

                imm.showSoftInput(mEtGiftNumberBar, 0);

                //每过500毫米就轮训keyboard是否显示
                mRxTimerUtil.interval(500, mRxNextGift);
            }
        });
    }

    RxTimerUtil.IRxNext mRxNextGift = new RxTimerUtil.IRxNext() {
        @Override
        public void doNext(long number) {
            if (!mEmotionKeyboard.isSoftInputShown()) {
                hidenGiftTop();
            }
        }
    };

    private boolean mIsNormal = true;
    private boolean mIsAudio = false;

    public void updateAudioCancel(boolean isNormal) {
        mIsNormal = isNormal;

        if (!mRxTimerUtil.isRunning()) {
            mRxTimerUtil.interval(200, mRxNextAudio);
        }
    }


    RxTimerUtil.IRxNext mRxNextAudio = new RxTimerUtil.IRxNext() {
        @Override
        public void doNext(long number) {
            int n = (int) (number / 5) + 1;
            String nStr = n > 9 ? n + "" : "0" + n;

            LogUtil.e("n:" + n + " nStr:" + nStr);
            if (mIsNormal) {
                mTvAudio.setTextColor(0xFF999999);
                mTvAudio.setText(getSpanStirng(nStr));

                mChildAudio.setIvNormal();
            } else {
                mTvAudio.setTextColor(0xFFFF4949);
                mTvAudio.setText("松开取消（" + nStr + "″）");
                mChildAudio.setIvDel();
            }
            mPb.setProgress(n);
        }
    };
    ForegroundColorSpan mSpan = new ForegroundColorSpan(0xfff98200);

    private CharSequence getSpanStirng(String contentTime) {
        SpannableString spannableString = new SpannableString("上划取消（" + contentTime + "″）");
        spannableString.setSpan(mSpan, 4, 4 + contentTime.length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    //视频录制
    public void showAudioTopError() {
        mRxTimerUtil.cancel();

        mTvAudio.setText("录制时间过短");
        mTvAudio.setTextColor(0xFFFD9770);

        if (mAudioContainer.getVisibility() == View.GONE) {
            //展示tv，隐藏Action和et
            mAudioContainer.setVisibility(View.VISIBLE);
            animateToViewUp.setTarget(mAudioContainer);
            animateToViewUp.start();

            animateToViewDown.setTarget(mTopContainer);
            animateToViewDown.start();
        }
        mChildAudio.setIvError();
    }

    Animator animateToViewUp = AnimatorInflater.loadAnimator(LibraryApplication.getInstance().getApplicationContext(), R.animator.anim_translate_up_message);
    Animator animateToViewDown = AnimatorInflater.loadAnimator(LibraryApplication.getInstance().getApplicationContext(), R.animator.anim_translate_down_message);

    /**
     * 切换顶部内容及tv的展示状态
     */
    public void toggleAudioTop() {
        if (!mIsAudio) {
            return;
        }
        if (mAudioContainer.getVisibility() == View.GONE) {
            //展示tv，隐藏Action和et
            mAudioContainer.setVisibility(View.VISIBLE);

            animateToViewUp.setTarget(mAudioContainer);
            animateToViewUp.start();

            animateToViewDown.setTarget(mTopContainer);
            animateToViewDown.start();

        } else {
            //展示Action和et，隐藏tv
            mAudioContainer.setVisibility(View.GONE);

            animateToViewUp.setTarget(mTopContainer);
            animateToViewUp.start();

            animateToViewDown.setTarget(mAudioContainer);
            animateToViewDown.start();

            mTvAudio.setText(getSpanStirng("00"));
            mTvAudio.setTextColor(0xFF999999);
            mChildAudio.setIvInit();

            mRxTimerUtil.cancel();
        }
        mIsNormal = true;
        mPb.setProgress(0);
    }

    @Override
    public void onShowGiftEditText() {
        showGiftTop();
    }

    public void initGiftNumberBar() {
        mEtGiftNumberBar.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        mEtGiftNumberBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String textMessage = mEtGiftNumberBar.getText().toString();
                if (!TextUtils.isEmpty(textMessage)) {
                    mGiftNumberSend.setVisibility(View.VISIBLE);
                } else {
                    mGiftNumberSend.setVisibility(View.GONE);
                }
            }
        });
    }


}
