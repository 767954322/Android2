package com.tuwan.yuewan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.tuwan.yuewan.ui.fragment.OrderListFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/12/13.
 */

public class OkManager {
    private OkHttpClient client;
    private MediaType MEDIA_TYPE_PNG = MediaType.parse("video/mp4");
    //private final String TAG = OkManager.class.getSimpleName();//获得类名
    private final String TAG = "GsonUtils";
    private String str;


    private OkManager() {
        client = new OkHttpClient();
        //设置连接超时时间为10s
        client.newBuilder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS);
    }

    //private volatile static OkManager okManager;
    //采用单例模式获取对象
   /* public static OkManager getInstance() {
        OkManager instance = null;
        if (null == okManager) {
            //同步锁
            synchronized (OkManager.class) {
                if (null == instance) {
                    instance = new OkManager();
                    okManager = instance;
                }
            }
        }
        return instance;
    }*/


    private static OkManager okManager;

    //创建 单例模式（OkHttp官方建议如此操作）
    public static OkManager getInstance() {
        if (null == okManager) {
            synchronized (OkManager.class) {
                okManager = new OkManager();
            }
        }
        return okManager;
    }

    /**
     * 同步请求，在android开发中不常用，因为会阻塞UI线程
     *
     * @param url
     * @return
     */
    public String syncGetByURL(String url) {
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (null != response && response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * 异步get请求
     */
    public void getAsync(Context context, String url, Callback callback, boolean setCookie) {
        Request.Builder builder = new Request.Builder();

        if (setCookie) {
            SharedPreferences preferences = context.getSharedPreferences("infos", Context.MODE_PRIVATE);
            String cookie = preferences.getString("Cookie", null);
            if (!cookie.isEmpty()) {
                builder = builder.addHeader("Cookie", cookie);
            }
        }
        Request request = builder.get().url(url).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 异步get请求
     */
    public void getAsync(OrderListFragment fragment, String url, Callback callback, boolean setCookie) {
        Request.Builder builder = new Request.Builder();

        if (setCookie) {
            SharedPreferences preferences = fragment.getContext().getSharedPreferences("infos", Context.MODE_PRIVATE);
            String cookie = preferences.getString("Cookie", null);
            if (!cookie.isEmpty()) {
                builder = builder.addHeader("Cookie", cookie);
            }
        }
        Request request = builder.get().url(url).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 异步get请求方式获取bitmap,通过接口回调获取
     *
     * @param url
     */
    public void getBitmap(String url) {
        final Request request = new Request.Builder().get().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure_IOException" + e.toString());
                if (null != okManagerBitmapListner) {
                    okManagerBitmapListner.onResponseFailure(call, e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse=");
                if (response != null && response.isSuccessful()) {
                    byte[] bytes = response.body().bytes();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    //方法二,可以获取字节流,然后转换成图片
                    //InputStream inputStream = response.body().byteStream();
                    //Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream);
                    if (null != okManagerBitmapListner) {
                        okManagerBitmapListner.onResponseBitmap(bitmap);
                    }
                }
            }
        });
    }

    /**
     * 异步get请求获取String类型的json数据
     *
     * @param url    请求地址
     * @param cookie app的cookie值添加在请求头里
     */

    public void getString(String url, String cookie) {

        final Request request = new Request.Builder().url(url).addHeader("Cookie", cookie).get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "IOException" + e.toString());
                if (null != okManagerStringListner) {
                    okManagerStringListner.onResponseFailure(call, e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null != response && response.isSuccessful()) {
                    ResponseBody body = response.body();
                    String string = body.string();
                    Log.d(TAG, "string=" + string);
                    if (null != okManagerStringListner) {
                        okManagerStringListner.onResponseString(string);
                    }
                }
            }
        });
    }

    public void getString(String url) {

        final Request request = new Request.Builder().url(url).get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "IOException" + e.toString());
                if (null != okManagerStringListner) {
                    okManagerStringListner.onResponseFailure(call, e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null != response && response.isSuccessful()) {
                    ResponseBody body = response.body();
                    String string = body.string();
                    Log.d(TAG, "string=" + string);
                    if (null != okManagerStringListner) {
                        okManagerStringListner.onResponseString(string);
                    }
                }
            }
        });
    }

    public void getString(String url, String cookie, Callback callback) {
        Request request = new Request.Builder().addHeader("Cookie", cookie).url(url).get().build();
        client.newCall(request).enqueue(callback);
    }

    public void getStrings(String url, Callback callback) {
        Request request = new Request.Builder().url(url).get().build();
        client.newCall(request).enqueue(callback);

    }

    //有参数有cookie的get请求
    public void getSendGift(Context context, String url, Map<String, String> map, boolean boo, Callback callback) {
        String strParams = url;
        if (map != null) {
            Iterator<String> iterator = map.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                strParams += "&" + key + "=" + map.get(key);
            }
        }
        Request.Builder builder = new Request.Builder();
        if (boo) {
            SharedPreferences preferences = context.getSharedPreferences("infos", Context.MODE_PRIVATE);
            String cookie = preferences.getString("Cookie", null);
            if (!cookie.isEmpty()) {
                builder = builder.addHeader("Cookie", cookie);
            }
        }
        Request request = builder.get().url(strParams).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * post表单请求提交,获取json字符串
     *
     * @param url
     * @param params
     */
    public void postComplexForm(String url, Map<String, Object> params, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("infos", Context.MODE_PRIVATE);
        String cookie = preferences.getString("Cookie", null);
        FormBody.Builder builder = new FormBody.Builder();//表单对象，包含以input开始的对象，以html表单为主
        //把map集合中的参数添加到FormBody表单中.
        if (null != params && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.add(entry.getKey(), (String) entry.getValue());
            }
            RequestBody requestBody = builder.build();//创建请求体
            Request request = new Request.Builder().url(url).addHeader("Cookie", cookie).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "IOException" + e.toString());
                    if (null != okManagerStringListner) {
                        okManagerStringListner.onResponseFailure(call, e);
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (null != response && response.isSuccessful()) {
                        String string = response.body().string();
                        Log.e(TAG + "***********", "string=" + string);
                        if (null != okManagerStringListner) {
                            okManagerStringListner.onResponseString(string);
                        }
                    }
                }
            });
        }
    }

    //post请求 绑定银行卡
    public void postBankCard(Context context, String url, Map<String, String> params, Boolean boo, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();//表单对象，包含以input开始的对象，以html表单为主
        //把map集合中的参数添加到FormBody表单中.
        if (null != params && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
            RequestBody requestBody = builder.build();//创建请求体
            if (boo) {
                SharedPreferences preferences = context.getSharedPreferences("infos", Context.MODE_PRIVATE);
                String cookie = preferences.getString("Cookie", null);
                if (!cookie.isEmpty()) {
//                    builde = builde.addHeader("Cookie", cookie);
                    Request request = new Request.Builder().url(url).addHeader("Cookie", cookie).post(requestBody).build();
                    client.newCall(request).enqueue(callback);
                }
            }
        }
    }

    //post请求
    public void postJson(Context context, String url, Map<String, Object> params, Boolean boo, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();//表单对象，包含以input开始的对象，以html表单为主
        //把map集合中的参数添加到FormBody表单中.
        if (null != params && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.add(entry.getKey(), (String) entry.getValue());
            }
            RequestBody requestBody = builder.build();//创建请求体
            if (boo) {
                SharedPreferences preferences = context.getSharedPreferences("infos", Context.MODE_PRIVATE);
                String cookie = preferences.getString("Cookie", null);
                if (!cookie.isEmpty()) {
//                    builde = builde.addHeader("Cookie", cookie);
                    Request request = new Request.Builder().url(url).addHeader("Cookie", cookie).post(requestBody).build();
                    client.newCall(request).enqueue(callback);
                }
            }
        }
    }

    /**
     * post请求
     *
     * @param url
     * @param params
     * @param cookie
     * @param callback
     */
    public void postQingQiu(String url, Map<String, Object> params, String cookie, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();//表单对象，包含以input开始的对象，以html表单为主
        //把map集合中的参数添加到FormBody表单中.
        if (null != params && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.add(entry.getKey(), (String) entry.getValue());
            }
            RequestBody requestBody = builder.build();//创建请求体
            Request request = new Request.Builder().url(url).addHeader("Cookie", cookie).post(requestBody).build();
            client.newCall(request).enqueue(callback);
        }
    }

    /**
     * 第三方登录知道注册
     *
     * @param url
     * @param params
     * @param callback
     */
    public void postRegister(String url, Map<String, String> params, Callback callback) {

        FormBody.Builder builder = new FormBody.Builder();//表单对象，包含以input开始的对象，以html表单为主
        //把map集合中的参数添加到FormBody表单中.
        if (null != params && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
            RequestBody requestBody = builder.build();//创建请求体
            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(callback);
        }
    }

    /**
     * post 视频上传
     *
     * @param url      上传的地址
     * @param files    文件的地址
     * @param cookie   app的cookie值添加在请求头里
     * @param callback
     */
    public void postCommitShiPin(String url, String files, String cookie, Callback callback) {
        File file = new File(files);
        String fname = file.getName();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_PNG, file);
        builder.addFormDataPart("video", fname, requestBody);

        Request request = new Request.Builder()
                .url(url).addHeader("Cookie", cookie)
                .post(builder.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * post普通参数上传
     *
     * @param url    上传的地址
     * @param cookie app的cookie值添加在请求头里
     * @param params 上传的参数
     */
    public void postCommitData(String url, String cookie, Map<String, Object> params, Callback callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (String key : params.keySet()) {
            Object object = params.get(key);
            builder.addFormDataPart(key, (String) object);
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url)
                .addHeader("Cookie", cookie).post(body).build();
        client.newCall(request).enqueue(callback);
    }





    /**
     * post上传图片       图片经过Base64转换
     *
     * @param url    上传的地址
     * @param file   文件的地址
     * @param cookie app的cookie值添加在请求头里
     */
    public void postCommitImg(String url, String file, String cookie, Callback callback) {
        Bitmap bitmap = BitmapFactory.decodeFile(file);
        String bitmapstr = bitmapToBase64(bitmap);
        RequestBody requestBody = null;
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("img", bitmapstr);
        requestBody = formBody.build();
        Request request = new Request.Builder()
                .url(url).addHeader("Cookie", cookie)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }





    /**
     * post上传图片       图片经过Base64转换
     *
     * @param url    上传的地址
     * @param file   文件的地址
     * @param cookie app的cookie值添加在请求头里
     */
    public void postCommitImgMore(String url,String name,String idcard, String file, String file2,   String cookie, Callback callback) {
        //正面
        Bitmap bitmap = BitmapFactory.decodeFile(file);
        String bitmapstr = bitmapToBase64(bitmap);
        //反面
        Bitmap bitmap2 = BitmapFactory.decodeFile(file2);
        String bitmapstr2 = bitmapToBase64(bitmap2);
        RequestBody requestBody = null;
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("file_text1", bitmapstr);
        formBody.add("file_text2", bitmapstr2);
        formBody.add("name", name);
        formBody.add("idcard", idcard);
        requestBody = formBody.build();
        Request request = new Request.Builder()
                .url(url).addHeader("Cookie", cookie)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }



    /**
     * post上传图片       图片经过Base64转换
     *
     * @param url    上传的地址
     * @param file   文件的地址
     * @param cookie app的cookie值添加在请求头里
     *               发布动态
     */
    public void postCommitImgmain(String url, String file, String name, String cookie, Callback callback) {

        Bitmap bitmap = BitmapFactory.decodeFile(file);
        String bitmapstr = bitmapToBase64(bitmap);

        RequestBody requestBody = null;
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("content", name);

        formBody.add("file_64", bitmapstr);
        requestBody = formBody.build();
        Request request = new Request.Builder()
                .url(url).addHeader("Cookie", cookie)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void postCommitjubao(String url, int teacherid, String name,String file, String cookie, Callback callback) {

        Bitmap bitmap = BitmapFactory.decodeFile(file);
        String bitmapstr = bitmapToBase64(bitmap);

        RequestBody requestBody = null;
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("teacherid", String.valueOf(teacherid));

        formBody.add("reson", name);
        formBody.add("images", bitmapstr);
        requestBody = formBody.build();
        Request request = new Request.Builder()
                .url(url).addHeader("Cookie", cookie)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = "data:image/jpg;base64," + Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Bitmap的接口
     */
    public interface OkManagerBitmapListner {
        void onResponseBitmap(Bitmap bitmap);

        void onResponseFailure(Call call, IOException e);
    }

    public OkManagerBitmapListner getOkManagerBitmapListner() {
        return okManagerBitmapListner;
    }

    public void setOkManagerBitmapListner(OkManagerBitmapListner okManagerListner) {
        this.okManagerBitmapListner = okManagerListner;
    }

    private OkManagerBitmapListner okManagerBitmapListner;


    /**
     * String的接口
     */
    public interface OkManagerStringListner {
        void onResponseString(String string);

        void onResponseFailure(Call call, IOException e);
    }

    private OkManagerStringListner okManagerStringListner;

    public OkManagerStringListner getOkManagerStringListner() {
        return okManagerStringListner;
    }

    public void setOkManagerStringListner(OkManagerStringListner okManagerStringListner) {
        this.okManagerStringListner = okManagerStringListner;
    }
}
