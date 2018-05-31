package com.tuwan.common.utils;

import android.text.TextUtils;

import com.jakewharton.rxbinding.internal.Preconditions;
import com.tuwan.common.LibraryApplication;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


public class OkHttpProvider {

    private final static long DEFAULT_TIMEOUT = 10;

    public static OkHttpClient getDefaultOkHttpClient() {
        return getOkHttpClient(new CacheControlInterceptor());
    }
    public static OkHttpClient getLongCacheOkHttpClient() {
        return getOkHttpClient(new LongCacheControlInterceptor());
    }
    public static OkHttpClient getShortCahceOkHttpClient() {
        return getOkHttpClient(new ShortCacheControlInterceptor());
    }

    public static OkHttpClient getNoCacheOkHttpClient() {
        return getOkHttpClient(new FromNetWorkControlInterceptor());
    }

//    public static OkHttpClient getReadCacheOkHttpClient() {
//        return getOkHttpClient(new CacheOnlyInterceptor());
//    }

    private static OkHttpClient getOkHttpClient(Interceptor cacheControl) {
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        //设置超时时间
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        //设置缓存
        File httpCacheDirectory = new File(LibraryApplication.getInstance().getCacheDir(), "OkHttpCache");
        httpClientBuilder.cache(new Cache(httpCacheDirectory, 100 * 1024 * 1024));
        //设置拦截器
        httpClientBuilder.addInterceptor(cacheControl);
        httpClientBuilder.addNetworkInterceptor(cacheControl);
        httpClientBuilder.addInterceptor(new UserAgentInterceptor("Android Device"));

        if (LibConstants.mDebug) {
            //日志显示级别
            HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
            //新建log拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    LogUtil.e("HttpLoggingInterceptor:" + message);
                }
            });
            loggingInterceptor.setLevel(level);
            //定制O
            //OkHttp进行添加拦截器loggingInterceptor
            httpClientBuilder.addInterceptor(loggingInterceptor);
        }


        return httpClientBuilder.build();
    }


    /**
     * Cache-Control：请求：no-cache（不要缓存的实体，要求现在从WEB服务器去取）
     max-age：（只接受 Age 值小于 max-age 值，并且没有过期的对象） 秒
     max-stale：（可以接受过去的对象，但是过期时间必须小于 max-stale 值） 秒
     min-fresh：（接受其新鲜生命期大于其当前 Age 跟 min-fresh 值之和的缓存对象）
     响应：public(可以用 Cached 内容回应任何用户)
     private（只能用缓存内容回应先前请求该内容的那个用户）
     no-cache（可以缓存，但是只有在跟WEB服务器验证了其有效后，才能返回给客户端）
     max-age：（本响应包含的对象的过期时间）
     ALL: no-store（不允许缓存）
     */
    private static class CacheControlInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtil.isConnected(LibraryApplication.getInstance())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response response = chain.proceed(request);

            if (NetworkUtil.isConnected(LibraryApplication.getInstance())) {
                int maxAge = 60 * 30;//默认缓存半小时
                String cacheControl = request.cacheControl().toString();
                if (TextUtils.isEmpty(cacheControl)) {
                    cacheControl = "public, max-age=" + maxAge;
                }
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", cacheControl)
                        .build();

            } else {
                int maxStale = 60 * 60 * 24 * 30;
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    }

    private static class LongCacheControlInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtil.isConnected(LibraryApplication.getInstance())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response response = chain.proceed(request);

            if (NetworkUtil.isConnected(LibraryApplication.getInstance())) {
                int maxAge = 60 * 60 * 24 * 30;//默认缓存30天
                String cacheControl = request.cacheControl().toString();
                if (TextUtils.isEmpty(cacheControl)) {
                    cacheControl = "public, max-age=" + maxAge;
                }
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", cacheControl)
                        .build();

            } else {
                int maxStale = 60 * 60 * 24 * 30;
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    }

    private static class ShortCacheControlInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtil.isConnected(LibraryApplication.getInstance())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }else if(NetworkUtil.isWifiConnected(LibraryApplication.getInstance())){
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
            }

            Response response = chain.proceed(request);

            if (NetworkUtil.isWifiConnected(LibraryApplication.getInstance())){
                int maxAge = 5;
                String cacheControl = request.cacheControl().toString();
                if (TextUtils.isEmpty(cacheControl)) {
                    cacheControl = "public, max-age=" + maxAge;
                }
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", cacheControl)
                        .build();
            }else if (NetworkUtil.isConnected(LibraryApplication.getInstance())) {
                int maxAge = 60 * 1;
                String cacheControl = request.cacheControl().toString();
                if (TextUtils.isEmpty(cacheControl)) {
                    cacheControl = "public, max-age=" + maxAge;
                }
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", cacheControl)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 30;
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    }

    private static class FromNetWorkControlInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build();

            Response response = chain.proceed(request);
            return response;
        }
    }

//    /**
//     * 只读缓存，没有缓存会执行onError
//     */
//    private static class CacheOnlyInterceptor implements Interceptor {
//
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Request request = chain.request();
//            request = request.newBuilder()
//                    .cacheControl(CacheControl.FORCE_CACHE)
//                    .build();
//
//            Response originalResponse = chain.proceed(request);
//
//            int maxStale = 60 * 60 * 24 * 30;
//            return originalResponse.newBuilder()
//                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                    .removeHeader("Pragma")
//                    .build();
//        }
//    }

    private static class UserAgentInterceptor implements Interceptor {
        private static final String USER_AGENT_HEADER_NAME = "User-Agent";
        private final String userAgentHeaderValue;

        UserAgentInterceptor(String userAgentHeaderValue) {
            this.userAgentHeaderValue = Preconditions.checkNotNull(userAgentHeaderValue, "userAgentHeaderValue = null");
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request originalRequest = chain.request();
            Request.Builder builder = originalRequest.newBuilder()
                    .removeHeader(USER_AGENT_HEADER_NAME)
                    .addHeader(USER_AGENT_HEADER_NAME, userAgentHeaderValue);
            final Request requestWithUserAgent = LibraryApplication.getInstance().addOkHttpAddHeader(builder)
                    .build();
            return chain.proceed(requestWithUserAgent);
        }
    }

}
