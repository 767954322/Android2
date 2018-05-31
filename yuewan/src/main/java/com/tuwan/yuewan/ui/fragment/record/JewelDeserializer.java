package com.tuwan.yuewan.ui.fragment.record;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tuwan.yuewan.entity.Record;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2018/1/2.
 */

public class JewelDeserializer implements JsonDeserializer<Record.DataBean> {


    @Override
    public Record.DataBean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Record.DataBean rdb = new Record.DataBean();
        if (json.isJsonObject()) {
            JsonObject object = json.getAsJsonObject();
            rdb.setDate(object.get("date").getAsString());
            rdb.setMode(object.get("mode").getAsInt());
            rdb.setPrice(object.get("price").getAsString());
            rdb.setRemark(object.get("remark").getAsString());
            rdb.setTitle(object.get("title").getAsString());
        } else {
            String string = json.getAsString();
            rdb = new Gson().fromJson(string, Record.DataBean.class);
        }
        return rdb;
    }
}
