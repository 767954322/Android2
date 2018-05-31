package com.tuwan.yuewan.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjie on 2017/11/1.
 */

public class AppPhotoUtilManager {

    public static List<AppPhotoUtil> list = new ArrayList<>();

    public static AppPhotoUtil getAppPhotoUtil(){
        return list.get(list.size()-1);
    }







}
