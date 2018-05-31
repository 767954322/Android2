package com.tuwan.yuewan.ui.fragment.record;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.OrderPagerAdapters;
import com.tuwan.yuewan.entity.Record;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.Urls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopUPRecordFragment extends Fragment {
    private LinearLayout layout_jl;
    private ListView all_record;
    private HashMap<String, String> map;
    private int page = 1;
    private List<Record.DataBean> data = new ArrayList<>();
    private OrderPagerAdapters adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_record, null);
        all_record = (ListView) view.findViewById(R.id.all_record);
        layout_jl = (LinearLayout) view.findViewById(R.id.layout_jl);
        adapter = new OrderPagerAdapters(getActivity(), data);
        all_record.setAdapter(adapter);
        initData();
        initView();
        return view;
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 200) {
                adapter.notifyDataSetChanged();
            }
        }
    };
    private int totalPage;

    private void initData() {
        map = new HashMap<>();
        map.put("format", "json");
        map.put("type", 1 + "");
        map.put("page", page + "");
        OkManager.getInstance().getSendGift(getActivity(), Urls.MY_RECORD, map, true, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("记录错误原因 ", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
//                Log.e("交易记录返回数据", "全部："+result.toString());
                try {



                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Record record = JSON.parseObject(result, Record.class);
                        if(record.getTotal()==0){


                            layout_jl.setVisibility(View.VISIBLE);
                        }else {

                            data.addAll(record.getData());
                            totalPage = record.getTotalPage();

                            handler.sendEmptyMessage(200);
                        }

                    }
                });
                }catch (Exception e){

                }
            }
        });
    }

    private void initView() {
        all_record.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
//                    Log.d("ListView", "##### 滚动到顶部 #####");
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
//                    Log.e("ListView", "##### 滚动到底部 ######");
                    if (totalPage <= page) {
                        Toast.makeText(getContext(), "当前已经是最后一页", Toast.LENGTH_SHORT).show();
                    } else {
                        page++;
                        initData();
                    }
                }
            }
        });
    }



}
