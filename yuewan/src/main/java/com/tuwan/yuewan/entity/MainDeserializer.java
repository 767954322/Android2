package com.tuwan.yuewan.entity;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.tuwan.yuewan.entity.evnetbean.TeacherMainBean;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2018/1/2.
 */

public class MainDeserializer implements JsonDeserializer {
    @Override
    public TeacherMainBean.ServiceBean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        TeacherMainBean.ServiceBean service = new TeacherMainBean.ServiceBean();
        if (json.isJsonObject()) {
            //类型正确
            JsonObject jsonObject = json.getAsJsonObject();

            service.setIcon(jsonObject.get("icon").getAsString());
            service.setTitle(jsonObject.get("title").getAsString());
            service.setPrice(jsonObject.get("price").getAsInt());
            service.setUnit(jsonObject.get("unit").getAsString());
            service.setId(jsonObject.get("id").getAsInt());
            service.setGrading(jsonObject.get("grading").getAsString());
        }

        return service;
    }
}
