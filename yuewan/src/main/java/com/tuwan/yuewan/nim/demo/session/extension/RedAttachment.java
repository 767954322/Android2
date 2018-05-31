package com.tuwan.yuewan.nim.demo.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujianghua on 2015/7/8.
 */
public class RedAttachment extends CustomAttachment {

    public int type;//type
    public String icon;//icon
    public String title;//标题
    public String url;//跳转地址
    public String times;//发布时间，时间戳
    public String intro;//内容


    public RedAttachment() {
        super(CustomAttachmentType.NOTICE);
    }

    @Override
    protected void parseData(JSONObject data) {
        type = data.getIntValue("type");
        title = data.getString("title");
        times = data.getString("times");
        url = data.getString("url");
        intro = data.getString("intro");
        icon = data.getString("icon");
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("type", type);
        data.put("title", title);
        data.put("intro", intro);
        data.put("url", url);
        data.put("times", times);
        data.put("icon", icon);
        return data;
    }

    @Override
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
