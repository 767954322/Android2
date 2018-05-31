package com.tuwan.yuewan.entity;

import com.tuwan.yuewan.ui.widget.MakeOrderTimePicker;

/**
 * Created by zhangjie on 2017/10/21.
 * 选择的日期（格式 YYYY-MM-DD） date
 * 选择的时间（格式 HH:ii） timestart
 * 小时数或局数 hours
 */

public class TimePikcerEntity {

    public @MakeOrderTimePicker.NODE_TYPE int type;

    /**
     * 日期 YYYY-MM-DD
     */
    public String date;
    /**
     * 时间 HH:ii
     */
    public String timestart;

    /**
     * 允许选择的小时数
     * 当值为24时就是全部都允许
     */
    public int hours;

    public TimePikcerEntity() {
        type = MakeOrderTimePicker.NODE_LIFELESS;
    }
    public TimePikcerEntity(int type) {
        this.type = type;
    }

    public TimePikcerEntity(int type, String date, String timestart, int hours) {
        this.type = type;
        this.date = date;
        this.timestart = timestart;
        this.hours = hours;
    }

    @Override
    public String toString() {
        return "TimePikcerEntity{" +
                "type=" + type +
                ", date='" + date + '\'' +
                ", timestart='" + timestart + '\'' +
                ", hours=" + hours +
                '}';
    }
}
