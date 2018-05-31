package com.tuwan.common.net;

import com.tuwan.common.net.converter.MyGsonConverterFactory;
import com.tuwan.common.utils.OkHttpProvider;

import java.lang.reflect.Field;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 获取ServiceFactory对象的方法请慎重对待，否则将会导致错误
 */
public class ServiceFactory {

    //    private final Gson mGsonDateFormat;
    private OkHttpClient mOkHttpClient;


    public ServiceFactory() {
//        mGsonDateFormat = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd hh:mm:ss")
//                .create();
        mOkHttpClient = OkHttpProvider.getDefaultOkHttpClient();
    }

    private static ServiceFactory INSTANCE;

    /**
     * 没有网络的时候将读取缓存
     */
    public static ServiceFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServiceFactory();
        }
        INSTANCE.mOkHttpClient = OkHttpProvider.getDefaultOkHttpClient();
        return INSTANCE;
    }

    /**
     * 没有网络时读取缓存，wifi状态下
     */
    public static ServiceFactory getShortCacheInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServiceFactory();
        }
        INSTANCE.mOkHttpClient = OkHttpProvider.getShortCahceOkHttpClient();
        return INSTANCE;
    }
    public static ServiceFactory getLongCacheInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServiceFactory();
        }
        INSTANCE.mOkHttpClient = OkHttpProvider.getLongCacheOkHttpClient();
        return INSTANCE;
    }

    public static ServiceFactory getNoCacheInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServiceFactory();
        }
        INSTANCE.mOkHttpClient = OkHttpProvider.getNoCacheOkHttpClient();
        return INSTANCE;
    }

//    /**
//     * 只读取缓存的数据
//     */
//    public static ServiceFactory getReadCacheInstance() {
//        if (INSTANCE == null) {
//            INSTANCE = new ServiceFactory();
//        }
//        INSTANCE.mOkHttpClient = OkHttpProvider.getReadCacheOkHttpClient();
//        return INSTANCE;
//    }

    public <S> S createService(Class<S> serviceClass) {
        String baseUrl = "";
        try {
            Field field1 = serviceClass.getField("BASE_URL");
            baseUrl = (String) field1.get(serviceClass);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.getMessage();
            e.printStackTrace();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mOkHttpClient)
                .addConverterFactory(MyGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(serviceClass);
    }


}
