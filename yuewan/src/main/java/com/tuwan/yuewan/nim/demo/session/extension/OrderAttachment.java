package com.tuwan.yuewan.nim.demo.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public class OrderAttachment extends CustomAttachment {

    public String title;//标题
    public String name;//名字
    public String service;//服务
    public String otime;//预约时间
    public String orderid;//订单ID
    public String avatar;//图片

    public OrderAttachment() {
        super(CustomAttachmentType.ORDER);
    }

    @Override
    protected void parseData(JSONObject data) {
        title = data.getString("title");
        name = data.getString("name");
        service = data.getString("service");
        otime = data.getString("otime");
        orderid = data.getString("orderid");
        avatar = data.getString("avatar");
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("title", title);
        data.put("name", name);
        data.put("service", service);
        data.put("otime", otime);
        data.put("orderid", orderid);
        data.put("avatar", avatar);
        return data;
    }


}
