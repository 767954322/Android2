package com.tuwan.yuewan.ui.fragment.order;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.AllAdapter;
import com.tuwan.yuewan.entity.AllOrderBean;
import com.tuwan.yuewan.ui.activity.OrderDetailsActivity;
import com.tuwan.yuewan.utils.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverFragment extends Fragment {

    private ListView over_list;
    private AllAdapter allAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_over, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        over_list = (ListView) view.findViewById(R.id.over_list);
        order();

    }



    private void order() {
        final OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("format", "json")
                .add("status", 3 + "")
                .add("page", 1 + "");
        RequestBody formBody = builder.build();
        SharedPreferences preferences = getActivity().getSharedPreferences("infos", Context.MODE_PRIVATE);
        String cookie = preferences.getString("Cookie", null);

        final Request request = new Request.Builder()
                .url("https://y.tuwan.com/m/Order/getOrderApi")
                .addHeader("Cookie", cookie)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("获取订单失败原因", e.toString());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String result = response.body().string();
                Log.e("返回数据_订单_已完成: ", result);
                LogUtils.showLogCompletion(result,200,"返回数据_订单_全部: ");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Gson gson = new Gson();
                            AllOrderBean allOrderBean = gson.fromJson(result, AllOrderBean.class);
                            final List<AllOrderBean.DataBean> list = new ArrayList();
                            list.addAll(allOrderBean.getData());
                            allAdapter = new AllAdapter(list, getActivity());
                            over_list.setAdapter(allAdapter);
                            over_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
                                    String tradeno = list.get(position).getTradeno();
                                    intent.putExtra("tradeno", tradeno);
                                    startActivity(intent);
                                }
                            });
                        } catch (Exception e) {
                            Log.e(": ", e.toString());
                        }
                    }
                });
            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();
        allAdapter.notifyDataSetChanged();

    }
}
