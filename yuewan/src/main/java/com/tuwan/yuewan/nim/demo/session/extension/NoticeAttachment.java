package com.tuwan.yuewan.nim.demo.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujianghua on 2015/7/8.
 */
public class NoticeAttachment extends CustomAttachment {

    public int id;//id
    public String litpic;//导图
    public String title;//标题
    public String url;//跳转地址
    public int pubdate;//发布时间，时间戳
    public String body;//内容


    public NoticeAttachment() {
        super(CustomAttachmentType.NOTICE);
    }

    @Override
    protected void parseData(JSONObject data) {
        id = data.getIntValue("id");
        title = data.getString("title");
        litpic = data.getString("litpic");
        url = data.getString("url");
        body = data.getString("body");
        pubdate = data.getIntValue("pubdate");
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("id", id);
        data.put("title", title);
        data.put("litpic", litpic);
        data.put("url", url);
        data.put("body", body);
        data.put("pubdate", pubdate);
        return data;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPubdate(int pubdate) {
        this.pubdate = pubdate;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
