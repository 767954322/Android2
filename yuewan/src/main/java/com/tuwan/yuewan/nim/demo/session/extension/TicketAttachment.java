package com.tuwan.yuewan.nim.demo.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujianghua on 2015/7/8.
 */
public class TicketAttachment extends CustomAttachment {


    public String title;//标题
    public String icon;//服务图标
    public String name;//服务名
    public String intro;//介绍
    public String money;//价格
    public String unit;//单位
    public String use;//使用说明

    public TicketAttachment() {
        super(CustomAttachmentType.TICKET);
    }

    @Override
    protected void parseData(JSONObject data) {
        title = data.getString("title");
        icon = data.getString("icon");
        name = data.getString("name");
        intro = data.getString("intro");
        money = data.getString("money");
        unit = data.getString("unit");
        use = data.getString("use");
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("title", title);
        data.put("icon", icon);
        data.put("name", name);
        data.put("intro", intro);
        data.put("money", money);
        data.put("unit", unit);
        data.put("use", use);
        return data;
    }


}
