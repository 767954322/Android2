package com.tuwan.yuewan.entity;

/**
 * Created by Administrator on 2018/2/2.
 */

public class VideoInfo {
    String displayName;
    String path;

    public VideoInfo() {

    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "displayName='" + displayName + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    public VideoInfo(String displayName, String path) {
        this.displayName = displayName;
        this.path = path;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
