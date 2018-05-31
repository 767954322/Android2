package com.tuwan.yuewan.nim.demo.avchat;

/**
 * 音视频界面操作
 */
public interface AVChatUIListener {
    void onHangUp();
    void onRefuse();
    void onReceive();
    void toggleMute();
    void toggleSpeaker();
    void toggleRecord();
    void videoSwitchAudio();
    void audioSwitchVideo();
    void switchCamera();
    void closeCamera();
    void showGiftDialog();
    void callStart();
    void callEnd();
    void onHangUp(int code);
    void callUserInfo();
    void startVideoService();
}
