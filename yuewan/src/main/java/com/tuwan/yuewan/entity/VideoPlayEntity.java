package com.tuwan.yuewan.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangjie on 2017/10/17.
 */

public class VideoPlayEntity implements Parcelable {

    public String video;
    public String image;
    public String title;

    public VideoPlayEntity(String video, String image, String title) {
        this.video = video;
        this.image = image;
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.video);
        dest.writeString(this.image);
        dest.writeString(this.title);
    }


    protected VideoPlayEntity(Parcel in) {
        this.video = in.readString();
        this.image = in.readString();
        this.title = in.readString();
    }

    public static final Creator<VideoPlayEntity> CREATOR = new Creator<VideoPlayEntity>() {
        @Override
        public VideoPlayEntity createFromParcel(Parcel source) {
            return new VideoPlayEntity(source);
        }

        @Override
        public VideoPlayEntity[] newArray(int size) {
            return new VideoPlayEntity[size];
        }
    };
}
