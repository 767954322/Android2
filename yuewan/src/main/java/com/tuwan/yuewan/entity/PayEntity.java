package com.tuwan.yuewan.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangjie on 2017/10/23.
 */

public class PayEntity implements Parcelable {

    /**
     * 订单号
     */
    public String tradeno;

    /**
     * 价格,单价
     */
    public int money;
    /**
     * 数量
     */
    public int number;
    /**
     * 服务名
     */
    public String title;
    /**
     * 约玩币余额
     */
    public double ywbBalance;

    public int id;//服务ID
    public int teacherID;//导师ID

    public String avatar;
    public String nickname;
    public int age;
    public int sex;

    public String typeflag;//单位

    public PayEntity(String tradeno, int money, int number,String typeflag, MakeOrderRecentOrderBean bean,int gamePosition) {
        this.tradeno = tradeno;
        this.money = money;
        this.number = number;
        this.typeflag = typeflag;
        this.title = bean.gamelist.get(gamePosition).title;
        this.ywbBalance = bean.money;
        this.id = bean.teachinfo.id;
        this.teacherID = bean.teachinfo.teacherID;
        this.avatar = bean.teachinfo.avatar;
        this.nickname = bean.teachinfo.nickname;
        this.age = bean.teachinfo.age;
        this.sex = bean.teachinfo.sex;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tradeno);
        dest.writeInt(this.money);
        dest.writeInt(this.number);
        dest.writeString(this.title);
        dest.writeDouble(this.ywbBalance);
        dest.writeInt(this.id);
        dest.writeInt(this.teacherID);
        dest.writeString(this.avatar);
        dest.writeString(this.nickname);
        dest.writeInt(this.age);
        dest.writeInt(this.sex);
        dest.writeString(this.typeflag);
    }

    protected PayEntity(Parcel in) {
        this.tradeno = in.readString();
        this.money = in.readInt();
        this.number = in.readInt();
        this.title = in.readString();
        this.ywbBalance = in.readDouble();
        this.id = in.readInt();
        this.teacherID = in.readInt();
        this.avatar = in.readString();
        this.nickname = in.readString();
        this.age = in.readInt();
        this.sex = in.readInt();
        this.typeflag = in.readString();
    }

    public static final Creator<PayEntity> CREATOR = new Creator<PayEntity>() {
        @Override
        public PayEntity createFromParcel(Parcel source) {
            return new PayEntity(source);
        }

        @Override
        public PayEntity[] newArray(int size) {
            return new PayEntity[size];
        }
    };
}
