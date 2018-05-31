package com.tuwan.yuewan.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangjie on 2017/10/20.
 */

public class UserInfoBean implements Parcelable {


    public String avatar;//导师头像
    public String nickname;//导师昵称
    public int sex;//性别  1 男，2 女
    public int age;//年龄
    public String randimg;//大图
    public String tag;//图标上的标签，有则显示，无则不显示
    public int videocheck;//真人认证 1 认证，0未认证，不显示认证图标
    public int Attention;//是否关注，1关注，-1 未关注

    public UserInfoBean(String avatar, String nickname, int sex, int age, String randimg, String tag, int videocheck, int attention) {
        this.avatar = avatar;
        this.nickname = nickname;
        this.sex = sex;
        this.age = age;
        this.randimg = randimg;
        this.tag = tag;
        this.videocheck = videocheck;
        this.Attention = attention;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.avatar);
        dest.writeString(this.nickname);
        dest.writeInt(this.sex);
        dest.writeInt(this.age);
        dest.writeString(this.randimg);
        dest.writeString(this.tag);
        dest.writeInt(this.videocheck);
        dest.writeInt(this.Attention);
    }

    protected UserInfoBean(Parcel in) {
        this.avatar = in.readString();
        this.nickname = in.readString();
        this.sex = in.readInt();
        this.age = in.readInt();
        this.randimg = in.readString();
        this.tag = in.readString();
        this.videocheck = in.readInt();
        this.Attention = in.readInt();
    }

    public static final Creator<UserInfoBean> CREATOR = new Creator<UserInfoBean>() {
        @Override
        public UserInfoBean createFromParcel(Parcel source) {
            return new UserInfoBean(source);
        }

        @Override
        public UserInfoBean[] newArray(int size) {
            return new UserInfoBean[size];
        }
    };
}
