package com.tuwan.yuewan.ui.fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.tuwan.common.config.Url;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.AllsAdapter;
import com.tuwan.yuewan.entity.AllOrderBean;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.ui.activity.OrderDetailsActivity;
import com.tuwan.yuewan.ui.activity.RegisterDataActivity;
import com.tuwan.yuewan.ui.activity.YMainActivity;
import com.tuwan.yuewan.utils.OkManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderListFragment extends Fragment {

    //控件是否已经初始化
    private boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;
    public XRecyclerView lv_all;
    public View view1;
    private int refreshTime = 0;
    private int times = 0;
    private int status = 0;
    private int page = 1;
    private int totalPage = 0;
    private AllsAdapter allAdapter;
    private List<AllOrderBean.DataBean> list = new ArrayList();
    private Button bt;
    private LinearLayout ll;

    public void setMode(int mode) {
        this.status = mode;
        this.page = 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, null);
        isCreateView = true;
        initView(view);
        return view;
    }

    //此方法在控件初始化前调用，所以不能在此方法中直接操作控件会出现空指针
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser && isCreateView) {
//            lazyLoad();
//        }
//    }

//    private void lazyLoad() {
//        //如果没有加载过就加载，否则就不再加载了
//        if (!isLoadData) {
//            //加载数据操作
//            isLoadData = true;
//        }
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        //第一个fragment会调用
//        if (getUserVisibleHint())
//            lazyLoad();
//    }

    private void initView(View view) {
        lv_all = (XRecyclerView) view.findViewById(R.id.lv_all);
        view1 = (View) view.findViewById(R.id.views);
        bt = (Button) view.findViewById(R.id.order_list_bt);
        ll = (LinearLayout) view.findViewById(R.id.order_list_linear);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), YMainActivity.class));
                getActivity().finish();
            }
        });
        lv_all.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        allAdapter = new AllsAdapter(list, getActivity(), view1);
        lv_all.setAdapter(allAdapter);

        allAdapter.setClickCallBack(new AllsAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int pos) {
                SharedPreferences mySharedPreferences = getContext().getSharedPreferences("namess", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("names", list.get(pos).getNickname());
                editor.commit();
                Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
                String tradeno = list.get(pos).getTradeno();
                intent.putExtra("tradeno", tradeno);
                startActivity(intent);
            }

            @Override
            public void onRefresh() {
                order();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        allAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        order();
        setLoadListener();

    }

    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }

    private void order() {
        DialogMaker.showProgressDialog(getActivity(), null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        OkManager okManager = OkManager.getInstance();
        String url = Url.orderList + "?format=json&status=" + status + "&page=" + page;
        okManager.getAsync(OrderListFragment.this, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //Log.d("test2", e.getMessage());
                onLoginDone();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();

                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            AllOrderBean allOrderBean = gson.fromJson(result, AllOrderBean.class);
//                            isLoadData = true;
                            totalPage = allOrderBean.getCountPage();
                            page = allOrderBean.getPage();
                            list.addAll(allOrderBean.getData());
                            allAdapter.setNowTime(allOrderBean.getNowtime());
                            allAdapter.notifyDataSetChanged();
                            lv_all.reset();
                            if (list.size() <= 0) {
                                ll.setVisibility(View.VISIBLE);
                            } else {
                                ll.setVisibility(View.GONE);
                            }
                            onLoginDone();
                        }
                    });
                } catch (Exception e) {
                }
            }
        }, true);
    }

    private void setLoadListener() {
        lv_all.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                list.clear();
                allAdapter.notifyDataSetChanged();
                page = 1;

                if (allAdapter.downTimer != null) {
                    allAdapter.downTimer.cancel();
                }
                if (allAdapter.downTimer1 != null) {
                    allAdapter.downTimer1.cancel();
                }
                if (allAdapter.downTimer2 != null) {
                    allAdapter.downTimer2.cancel();
                }

                order();
//                allAdapter.notifyDataSetChanged();
                lv_all.refreshComplete();
            }

            @Override
            public void onLoadMore() {
//                int i = list.size();
                if (totalPage > page) {
                    Log.d("page:", totalPage + "," + page + "");
                    ++page;
                    order();
//                    lv_all.isLoadingData = false;
//                    lv_all.loadMoreComplete();
                } else {
                    lv_all.loadMoreComplete();
                    Toast.makeText(getContext(), "当前已经是最后一页", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
