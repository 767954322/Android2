package com.tuwan.yuewan.utils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by apple on 2017/7/31.
 */

public class OkHttpManager {

    //声明静态的私有的本类对象
    private static OkHttpManager manager;
    private final OkHttpClient client;
    public static Type type;

    //私有化构造方法


    private OkHttpManager() {
        client = new OkHttpClient.Builder().build();
    }

    //创建静态的公共的返回本类对象的带锁的单例方法
    public static OkHttpManager getInstance(Type types) {
        type = types;
        if (manager == null) {
            synchronized (OkHttpManager.class) {
                manager = new OkHttpManager();
            }
        }
        return manager;
    }


    //暴露一个方法给外部调用，同时在该方法中返回结果
    public <T> void getDataFromNet(final String cookie, String url, Map<String, String> map, final CallBacks<T> callBacks) {
        Request request = new Request.Builder().url(url).addHeader("Cookie", cookie).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        callBacks.fail(e.getMessage());
                    }
                }).start();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //注意看这块的代码
                Gson gson = new Gson();
                //String 字符串转变为一个bean对象  中间也需要一个转换器  TypeAdapter  在这里面这个type相当于.class
                TypeAdapter<T> adapter = (TypeAdapter<T>) gson.getAdapter(TypeToken.get(type));
                final T t = adapter.fromJson(response.body().string());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        callBacks.suc(t);
                    }
                }).start();
            }
        });
    }
}
