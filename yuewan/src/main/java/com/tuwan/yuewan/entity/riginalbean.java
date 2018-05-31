package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/6.
 */

public class riginalbean {


    /**
     * teacherID : 107833
     * speech : https://img3.tuwandata.com//uploads/play/mp3file/20180212/15184404493253.mp3
     * speech_durtion : 1
     * desc : &amp;lt;p&amp;gt;来下单123123123&amp;lt;/p&amp;gt;&amp;lt;p&amp;gt;&amp;lt;br/&amp;gt;&amp;lt;/p&amp;gt;
     * grading : 8
     * dtid : 20008
     * arcrank : 0
     * timestamp : 1493189996
     * randimg : https://img3.tuwandata.com/uploads/play/201802/107833-12090049.jpeg
     * meetgraing :
     * platform :
     * tstext : 请上传您的段位截图（需包含个人ID）,并填写 王者荣耀相关的描述
     * level : ["无段位","倔强黄铜","秩序白银","荣耀黄金","尊贵铂金","永恒钻石","最强王者","荣耀王者","至尊星耀"]
     * exampleicon : http://res.tuwan.com/templet/play/images/wzry-s1.jpg
     */

    private String teacherID;
    private String speech;
    private String speech_durtion;
    private String desc;
    private int grading;
    private String dtid;
    private String arcrank;
    private String timestamp;
    private String randimg;
    private String meetgraing;
    private String platform;
    private String tstext;
    private String exampleicon;
    private List<String> level;

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public String getSpeech() {
        return speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }

    public String getSpeech_durtion() {
        return speech_durtion;
    }

    public void setSpeech_durtion(String speech_durtion) {
        this.speech_durtion = speech_durtion;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getGrading() {
        return grading;
    }

    public void setGrading(int grading) {
        this.grading = grading;
    }

    public String getDtid() {
        return dtid;
    }

    public void setDtid(String dtid) {
        this.dtid = dtid;
    }

    public String getArcrank() {
        return arcrank;
    }

    public void setArcrank(String arcrank) {
        this.arcrank = arcrank;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRandimg() {
        return randimg;
    }

    public void setRandimg(String randimg) {
        this.randimg = randimg;
    }

    public String getMeetgraing() {
        return meetgraing;
    }

    public void setMeetgraing(String meetgraing) {
        this.meetgraing = meetgraing;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getTstext() {
        return tstext;
    }

    public void setTstext(String tstext) {
        this.tstext = tstext;
    }

    public String getExampleicon() {
        return exampleicon;
    }

    public void setExampleicon(String exampleicon) {
        this.exampleicon = exampleicon;
    }

    public List<String> getLevel() {
        return level;
    }

    public void setLevel(List<String> level) {
        this.level = level;
    }
}
