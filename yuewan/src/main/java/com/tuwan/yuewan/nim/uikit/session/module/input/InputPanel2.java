package com.tuwan.yuewan.nim.uikit.session.module.input;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.netease.nimlib.sdk.media.record.AudioRecorder;
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback;
import com.netease.nimlib.sdk.media.record.RecordType;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.utils.LogUtil;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.Code;
import com.tuwan.yuewan.entity.GiftListBean;
import com.tuwan.yuewan.nim.demo.session.extension.GiftAttachment;
import com.tuwan.yuewan.nim.uikit.common.media.picker.PickImageHelper;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.tuwan.yuewan.nim.uikit.common.util.string.StringUtil;
import com.tuwan.yuewan.nim.uikit.session.SessionCustomization;
import com.tuwan.yuewan.nim.uikit.session.actions.BaseAction;
import com.tuwan.yuewan.nim.uikit.session.actions.ImageAction;
import com.tuwan.yuewan.nim.uikit.session.actions.PickImageAction;
import com.tuwan.yuewan.nim.uikit.session.emoji.EmojiAdapter;
import com.tuwan.yuewan.nim.uikit.session.emoji.IEmoticonSelectedListener;
import com.tuwan.yuewan.nim.uikit.session.emoji.MoonUtil;
import com.tuwan.yuewan.nim.uikit.session.helper.SendImageHelper;
import com.tuwan.yuewan.nim.uikit.session.module.Container;
import com.tuwan.yuewan.ui.activity.DiamondActivity;
import com.tuwan.yuewan.ui.widget.MessageBtmView;
import com.tuwan.yuewan.utils.AppPhotoUtil;
import com.tuwan.yuewan.utils.EmotionKeyboard;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.Urls;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.functions.Action1;


/**
 * 底部文本编辑，语音等模块
 * {@link EmojiAdapter}具体的表情填充在这里
 * 拍照具体使用了这两个帮助类的代码{@link PickImageAction}{@link PickImageHelper}
 */
public class InputPanel2 implements EmotionKeyboard.IShowContentListener, IEmoticonSelectedListener, IAudioRecordCallback {

    Container mContainer;
    List<BaseAction> mActionList;

    MessageBtmView mMessageBtmView;

    ImageAction imageAction = new ImageAction();
    private AppPhotoUtil mAppPhotoUtil;
     Context context;
    public InputPanel2(Container container, MessageBtmView messageBtmView, List<BaseAction> actionList,Context context) {
        this.mContainer = container;
        this.mMessageBtmView = messageBtmView;
        this.mActionList = actionList;
        this.context = context;
        imageAction.setContainer(mContainer);
        init();

    }

    private void init() {
        //初始化控件
        mMessageBtmView.initData(mContainer.activity, mContainer.activity.findViewById(R.id.message_activity_list_view_container));

        mAppPhotoUtil = AppPhotoUtil.createInstance(mContainer.activity);
        mAppPhotoUtil.startImageScanTask();
        mMessageBtmView.initChild(mContainer.activity, mContainer.account, mAppPhotoUtil);
        mMessageBtmView.initGiftNumberBar();

        mAppPhotoUtilIndex = mAppPhotoUtil.getIndex();


        mMessageBtmView.mEmotionKeyboard.setShowContentListener(this);
        mMessageBtmView.mChildEmojy.show(this);

        initTextEdit();
        initEvent();
    }

    private int mAppPhotoUtilIndex;


    private void initEvent() {
        mMessageBtmView.mChildAudio.setOnTouchListener(touchListener);

        //常规顶部 发送按钮
        RxView.clicks(mMessageBtmView.mBtnSend)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onTextMessageSendButtonPressed();
                    }
                });
        //礼物顶部 发送
        RxView.clicks(mMessageBtmView.mGiftNumberSend)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mMessageBtmView.mChildGift.setGiftNumber(Integer.valueOf(mMessageBtmView.mEtGiftNumberBar.getText().toString()));
                        processSendGiftClick();

                        mMessageBtmView.hidenGiftTop();
                    }
                });

        //顶部操作栏 拍摄
        RxView.clicks(mMessageBtmView.mIvCamera)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mMessageBtmView.hiden();
                        imageAction.showCamera();
                    }
                });


        //childepic中的相册
        RxView.clicks(mMessageBtmView.mChildpic.mPic)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mAppPhotoUtil.cleanSelected();
                        mMessageBtmView.mChildpic.cleanSelected();

                        imageAction.showPic();
                    }
                });

        //childepic中的拍摄
        RxView.clicks(mMessageBtmView.mChildpic.mCamare)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        imageAction.showCamera();
                    }
                });

        //childgift中底部的发送
        RxView.clicks(mMessageBtmView.mChildGift.mTvWidgetGiftSend)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        processSendGiftClick();

                    }
                });

        //childpic中底部的发送
        RxView.clicks(mMessageBtmView.mChildpic.mTvSendImg)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        imageAction.sendImage(mAppPhotoUtil.isOrig, new SendImageHelper.Callback() {
                            @Override
                            public void sendImage(File file, boolean isOrig) {
                                mAppPhotoUtil.cleanSelected();
                                mMessageBtmView.mChildpic.cleanSelected();

                                imageAction.onPicked(file);
                            }
                        });

                    }
                });

    }


    private HashMap<String, String> map;

    public void processSendGiftClick() {
        GiftListBean.DataBean giftSelected = mMessageBtmView.mChildGift.getGiftSelected();
        if (giftSelected == null) {
            ToastUtils.getInstance().showToast("请先选择礼物");
            return;
        }

        int giftNumber = mMessageBtmView.mChildGift.getGiftNumber();

        GiftAttachment giftAttachment = new GiftAttachment();
        giftAttachment.setNumber(giftNumber);
        giftAttachment.setCharm_score(giftSelected.charm_score);
        giftAttachment.setId(giftSelected.id);
        giftAttachment.setIntro(giftSelected.intro);
        giftAttachment.setPic(giftSelected.pic);
        giftAttachment.setPrice(giftSelected.price);
        giftAttachment.setTitle(giftSelected.title);
        final IMMessage customMessage = MessageBuilder.createCustomMessage(mContainer.account, SessionTypeEnum.P2P, giftAttachment);

        map = new HashMap<String, String>();
        map.put("format", "json");
        map.put("id", giftSelected.id + "");
        map.put("num", giftNumber + "");
        map.put("uid", mContainer.getAccount() + "");
        map.put("anonymous", 0 + "");
        map.put("source", 4 + "");
        final GiftListBean.DataBean songBean = mMessageBtmView.mChildGift.getGiftSelected();
//得到所选的礼物
        //得到赠送的数量
        final int giftNum = mMessageBtmView.mChildGift.getGiftNumber();
        OkManager.getInstance().getSendGift(mContainer.activity, Urls.SEND_GIFT, map, true, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Log.e("发送礼物", "失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String result = response.body().string();
                    Log.e("发送礼物", "成功" + result.toString());
                    Gson gson = new Gson();
                    Code code = gson.fromJson(result, Code.class);
                    final int error = code.getError();
                    mContainer.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (error == 0) {
//                                mMessageBtmView.initChild(mContainer.activity, mContainer.account, mAppPhotoUtil);
//                                mMessageBtmView.mChildGift.invalidate();
                                ToastUtils.getInstance().showToast("赠送成功");
//                                ToastUtils.getInstance().showToast("lalallal5555");



                                if (songBean == null) {
                                    ToastUtils.getInstance().showToast("请选择礼物");
                                    return;
                                }
//                                Log.e("eeeeeeeeeeee",giftNum+"");
//                                Log.e("eeeeeeeeeeee",giftNumber+"");
//                                Log.e("eeeeeeeeeeee",songBean.diamond+"");
                                mMessageBtmView.mChildGift.setYuDiamond(mMessageBtmView.mChildGift.getTotlDiamond() - giftNum * songBean.diamond);
                                mMessageBtmView.hiden();

                                //11
                                mContainer.proxy.sendMessage(customMessage);
                                mMessageBtmView.mContentContainer.removeAllViews();
                            } else if (error == -1) {
                                ToastUtils.getInstance().showToast("未登录");
                            } else if (error == -2) {
                                ToastUtils.getInstance().showToast("钻石不足");
                                mMessageBtmView.hiden();
                                View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout3, null);
                                final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                                Button dialog_cancel = (Button) view.findViewById(R.id.dialog_cancel);
                                Button dialog_msg = (Button) view.findViewById(R.id.dialog_ok);
                                dialog.setView(view);
                                final AlertDialog show = dialog.show();
                                dialog_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        context.startActivity(new Intent(context, DiamondActivity.class));
                                        show.dismiss();
                                    }
                                });

                                dialog_msg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        show.dismiss();
                                    }
                                });



                            } else if (error == 1) {
                                ToastUtils.getInstance().showToast("参数错误");
                            } else if (error == 2) {
                                ToastUtils.getInstance().showToast("礼物不存在");
                            } else if (error == 3) {
                                ToastUtils.getInstance().showToast("不能给自己赠送");
                            } else if (error == 9) {
                                ToastUtils.getInstance().showToast("未知错误，请重试");
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e("错误原因: ", e.toString());
                }
            }
        });
    }

    public boolean collapse() {
        if (mMessageBtmView.isGiftTop()) {
            //如果正在输入礼物个数，就消耗该事件
            return true;
        }

        if (isButtomShow()) {
            //如果底部softinput或者面板是展示状态，就消耗该事件
            mMessageBtmView.hiden();

            return true;
        }
        return false;
    }


    public void reload(Container container, SessionCustomization customization) {
        this.mContainer = container;
    }


    public int getEditSelectionStart() {
        return mMessageBtmView.mEt.getSelectionStart();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.e("onActivityResult================= resultCode=" + (resultCode == Activity.RESULT_OK));

        if (resultCode != Activity.RESULT_OK) {
            //即使不是resultok也要更新数据的变更
            if (requestCode == AppPhotoUtil.PICKER_IMAGE_PREVIEW) {
                mMessageBtmView.mChildpic.onResultUpdate();
                return;
            }
            return;
        }
        //点击底部图片打开的预览页的result
        if (requestCode == AppPhotoUtil.PICKER_IMAGE_PREVIEW || requestCode == AppPhotoUtil.REQUEST_PIC) {
            imageAction.sendImage(mAppPhotoUtil.isOrig, new SendImageHelper.Callback() {
                @Override
                public void sendImage(File file, boolean isOrig) {
                    mAppPhotoUtil.cleanSelected();
                    mMessageBtmView.mChildpic.cleanSelected();

                    imageAction.onPicked(file);
                }
            });
            return;
        } else if (requestCode == AppPhotoUtil.REQUEST_CAMERA || requestCode == AppPhotoUtil.PREVIEW_IMAGE_FROM_CAMERA) {
            //拍摄其实是先打开系统拍照返回这里。又打开预览
            //所以是两个requestCode
            imageAction.onActivityResult(requestCode & 0xff, resultCode, data);
            return;
        }

    }

    @Override
    public void showContent(View view) {
        boolean needPannelScroll = !isButtomShow();
        if (view == mMessageBtmView.mIvAudio) {
            mMessageBtmView.showAudio();
        } else if (view == mMessageBtmView.mIvPic) {
            mMessageBtmView.showPic();
            mMessageBtmView.mChildpic.show();
        } else if (view == mMessageBtmView.mIvGift) {
            mMessageBtmView.showGift();
        } else if (view == mMessageBtmView.mIvEmojy) {
            mMessageBtmView.showEmojy();
            mMessageBtmView.mChildEmojy.show(this);
        }

        if (needPannelScroll) {
            mContainer.proxy.onInputPanelExpand();
        }
    }

    @Override
    public void showSoftInput(boolean isShow) {
        boolean needPannelScroll = !isButtomShow();
        LogUtil.e("showSoftInput:" + isShow + "  needPannelScroll:" + needPannelScroll);

        if (isShow) {
            mMessageBtmView.onSoftInputShow();
            if (needPannelScroll) {
                mContainer.proxy.onInputPanelExpand();
            }
        }

    }

    /**
     * 底部内容及keyboard是否显示
     */
    private boolean isButtomShow() {
        return mMessageBtmView.mContentContainer.isShown() || mMessageBtmView.mEmotionKeyboard.isSoftInputShown();
    }

    // 发送文本消息
    private void onTextMessageSendButtonPressed() {
        String text = mMessageBtmView.mEt.getText().toString();
        IMMessage textMessage = createTextMessage(text);

        if (mContainer.proxy.sendMessage(textMessage)) {
            mMessageBtmView.mEt.setText("");
        }
    }

    protected IMMessage createTextMessage(String text) {
        return MessageBuilder.createTextMessage(mContainer.account, mContainer.sessionType, text);
    }


    private void initTextEdit() {
        mMessageBtmView.mEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mMessageBtmView.mEt.addTextChangedListener(new TextWatcher() {
            private int start;
            private int count;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                this.start = start;
                this.count = count;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkSendButtonEnable(mMessageBtmView.mEt);
                MoonUtil.replaceEmoticons(mContainer.activity, s, start, count);

                int editEnd = mMessageBtmView.mEt.getSelectionEnd();
                mMessageBtmView.mEt.removeTextChangedListener(this);
                while (StringUtil.counterChars(s.toString()) > 5000 && editEnd > 0) {
                    s.delete(editEnd - 1, editEnd);
                    editEnd--;
                }
                mMessageBtmView.mEt.setSelection(editEnd);
                mMessageBtmView.mEt.addTextChangedListener(this);

            }
        });
    }

    /**
     * 显示发送或更多
     *
     * @param editText
     */
    private void checkSendButtonEnable(EditText editText) {
        String textMessage = editText.getText().toString();
        if (!TextUtils.isEmpty(textMessage)) {
            mMessageBtmView.mBtnSend.setVisibility(View.VISIBLE);
        } else {
            mMessageBtmView.mBtnSend.setVisibility(View.GONE);
        }
    }

    @Override
    public void hidenContent() {
        mMessageBtmView.hiden();
    }

    @Override
    public void onEmojiSelected(String key) {
        Editable mEditable = mMessageBtmView.mEt.getText();
        if (key.equals("/DEL")) {
            mMessageBtmView.mEt.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        } else {
            int start = mMessageBtmView.mEt.getSelectionStart();
            int end = mMessageBtmView.mEt.getSelectionEnd();
            start = (start < 0 ? 0 : start);
            end = (start < 0 ? 0 : end);
            mEditable.replace(start, end, key);
        }
    }

    /**
     * 录音相关 start
     ***************************************************************************************************************************************************************************/
    private long mStartTimeMillis;
    private long mEndTimeMillis;

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                LogUtil.e("ACTION_DOWN");
                mStartTimeMillis = SystemClock.currentThreadTimeMillis();
                if (!isInIvAudio(mMessageBtmView.mChildAudio.mIvAudio, event)) {
                    return false;
                }

                touched = true;
                initAudioRecord();
                onStartAudioRecord();
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
                LogUtil.e("ACTION_UP");
                touched = false;

                mEndTimeMillis = SystemClock.currentThreadTimeMillis();

                LogUtil.e("mStartTimeMillis:" + mStartTimeMillis + "  endTimeMillis:" + mEndTimeMillis);

                if ((mEndTimeMillis - mStartTimeMillis) < 10) {
                    onErrorAudioRecord();
                } else {
                    onEndAudioRecord(!isInIvAudio(mMessageBtmView.mChildAudio.mIvAudio, event));
                }

            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                LogUtil.e("ACTION_MOVE");
                touched = true;
                cancelAudioRecord(isInIvAudio(mMessageBtmView.mChildAudio.mIvAudio, event));
            }
            return true;
        }
    };
    private boolean started = false;
    private boolean touched = false; // 是否按着
    private boolean cancelled = false;


    // 语音
    protected AudioRecorder audioMessageHelper;

    /**
     * @return false表示在圆内，true表示在圆外
     */
    private boolean isInIvAudio(View view, MotionEvent event) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        RectF rectF = new RectF(location[0], location[1], location[0] + view.getWidth(), location[1] + view.getHeight());

        float x = event.getRawX(); // 获取相对于屏幕左上角的 x 坐标值
        float y = event.getRawY(); // 获取相对于屏幕左上角的 y 坐标值

        return rectF.contains(x, y);
    }

    /**
     * 初始化AudioRecord
     */
    private void initAudioRecord() {
        if (audioMessageHelper == null) {
            audioMessageHelper = new AudioRecorder(mContainer.activity, RecordType.AAC, AudioRecorder.DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND, this);
        }
    }

    /**
     * 开始语音录制
     */
    private void onStartAudioRecord() {
        mContainer.activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        audioMessageHelper.startRecord();
        cancelled = false;
    }

    /**
     * 结束语音录制
     *
     * @param cancel
     */
    private void onEndAudioRecord(boolean cancel) {
        started = false;
        mContainer.activity.getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        audioMessageHelper.completeRecord(cancel);
        stopAudioRecordAnim();
    }

    /**
     * 结束语音录制
     */
    private void onErrorAudioRecord() {
        mMessageBtmView.mChildAudio.setOnTouchListener(null);
        mMessageBtmView.showAudioTopError();

        Observable.timer(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<Long>() {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        started = false;
                        mContainer.activity.getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                        audioMessageHelper.completeRecord(true);

                    }
                });

        Observable.timer(1800, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<Long>() {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        mMessageBtmView.mChildAudio.setOnTouchListener(touchListener);
                        mMessageBtmView.toggleAudioTop();
                    }
                });
    }

    /**
     * 取消语音录制
     *
     * @param cancel
     */
    private void cancelAudioRecord(boolean cancel) {
        // reject
        if (!started) {
            return;
        }
        // no change
        if (cancelled == cancel) {
            return;
        }

        cancelled = cancel;
        updateTimerTip(cancel);
    }

    /**
     * 正在进行语音录制和取消语音录制，界面展示
     *
     * @param cancel
     */
    private void updateTimerTip(boolean cancel) {
        mMessageBtmView.updateAudioCancel(cancel);
    }

    /**
     * 开始语音录制动画
     */
    private void playAudioRecordAnim() {
        mMessageBtmView.toggleAudioTop();
    }

    /**
     * 结束语音录制动画
     */
    private void stopAudioRecordAnim() {
        mMessageBtmView.toggleAudioTop();
    }

    public boolean isRecording() {
        return audioMessageHelper != null && audioMessageHelper.isRecording();
    }


    @Override
    public void onRecordReady() {

    }

    @Override
    public void onRecordStart(File file, RecordType recordType) {
        started = true;
        if (!touched) {
            return;
        }
        updateTimerTip(false); // 初始化语音动画状态
        playAudioRecordAnim();
    }

    @Override
    public void onRecordSuccess(File audioFile, long audioLength, RecordType recordType) {
        IMMessage audioMessage = MessageBuilder.createAudioMessage(mContainer.account, mContainer.sessionType, audioFile, audioLength);
        mContainer.proxy.sendMessage(audioMessage);
    }

    @Override
    public void onRecordFail() {
        if (started) {
            Toast.makeText(mContainer.activity, R.string.recording_error, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRecordCancel() {

    }

    @Override
    public void onRecordReachedMaxTime(final int maxTime) {
        stopAudioRecordAnim();
        EasyAlertDialogHelper.createOkCancelDiolag(mContainer.activity, "", mContainer.activity.getString(R.string.recording_max_time), false, new EasyAlertDialogHelper.OnDialogActionListener() {
            @Override
            public void doCancelAction() {
            }

            @Override
            public void doOkAction() {
                audioMessageHelper.handleEndRecord(true, maxTime);
            }
        }).show();
    }

    public void onDestroy() {
        // release
        if (audioMessageHelper != null) {
            audioMessageHelper.destroyAudioRecorder();
        }

        mMessageBtmView.mChildpic.onDestory();

        AppPhotoUtil.onDestory(mAppPhotoUtilIndex);

    }


    public void onPause() {
        // 停止录音
        if (audioMessageHelper != null) {
            onEndAudioRecord(true);
        }
    }

    /*****************************************************************************************************************************************************************************
     * 录音相关 end
     **/


}
