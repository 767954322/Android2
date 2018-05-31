package com.tuwan.yuewan.nim.demo.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujianghua on 2015/7/8.
 */
public class GiftAttachment extends CustomAttachment {

    public int number;//礼物数量

    public int id;
    public String title;//礼物标题
    public String pic;//图片
    public String intro;//描述
    public int price;//价格（分）
    public int charm_score;//用户贡献分

    public GiftAttachment() {
        super(CustomAttachmentType.GIFT);
    }

    @Override
    protected void parseData(JSONObject data) {
        number = data.getIntValue("number");
        id = data.getIntValue("id");
        title = data.getString("title");
        pic = data.getString("pic");
        intro = data.getString("intro");
        price = data.getIntValue("price");
        charm_score = data.getIntValue("charm_score");
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("number", number);
        data.put("id", id);
        data.put("title", title);
        data.put("pic", pic);
        data.put("intro", intro);
        data.put("price", price);
        data.put("charm_score", charm_score);
        return data;
    }


    public void setNumber(int number) {
        this.number = number;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCharm_score(int charm_score) {
        this.charm_score = charm_score;
    }
}
