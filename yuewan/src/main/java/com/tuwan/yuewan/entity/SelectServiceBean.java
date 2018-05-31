package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/1.
 */

public class SelectServiceBean {
    private String teacherId;
    private String speech;
    private String desc;
    private String grading;
    private String dtid;
    private String arcrank;
    private String timestamp;
    private String randimg;
    private String meetgraing;
    private String platform;
    private String tstext;
    private String exampleicon;
    private String stext;
    private String sbutton;
    private String stype;
    private String sval;
    private String ptext;
    private List<String> level;

    @Override
    public String toString() {
        return "SelectServiceBean{" +
                "teacherId='" + teacherId + '\'' +
                ", speech='" + speech + '\'' +
                ", desc='" + desc + '\'' +
                ", grading='" + grading + '\'' +
                ", dtid='" + dtid + '\'' +
                ", arcrank='" + arcrank + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", randimg='" + randimg + '\'' +
                ", meetgraing='" + meetgraing + '\'' +
                ", platform='" + platform + '\'' +
                ", tstext='" + tstext + '\'' +
                ", exampleicon='" + exampleicon + '\'' +
                ", level=" + level +
                '}';
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getSpeech() {
        return speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGrading() {
        return grading;
    }

    public void setGrading(String grading) {
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

    public String getStext() {
        return stext;
    }

    public void setStext(String stext) {
        this.stext = stext;
    }

    public String getSbutton() {
        return sbutton;
    }

    public void setSbutton(String sbutton) {
        this.sbutton = sbutton;
    }

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public String getSval() {
        return sval;
    }

    public void setSval(String sval) {
        this.sval = sval;
    }

    public String getPtext() {
        return ptext;
    }

    public void setPtext(String ptext) {
        this.ptext = ptext;
    }
}
