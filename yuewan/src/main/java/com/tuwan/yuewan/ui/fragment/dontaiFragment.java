package com.tuwan.yuewan.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.dongtaiAdapter;
import com.tuwan.yuewan.entity.Codes;
import com.tuwan.yuewan.entity.dongtaibean;
import com.tuwan.yuewan.ui.activity.TeacherMainInfoActivity;
import com.tuwan.yuewan.ui.activity.dongtaiActivity;
import com.tuwan.yuewan.utils.OkManager;
import com.umeng.socialize.utils.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class dontaiFragment extends Fragment implements View.OnClickListener {
    private LinearLayout layout;
    private int teacherid;
    private RecyclerView recyclerView;
    private dongtaiAdapter Adapter;
    private int id;
    private Button button_jiazai;
    private dongtaibean dongtaibean;
    private int i=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dontai, container, false);
        layout = (LinearLayout) view.findViewById(R.id.layout);
        button_jiazai = (Button) view.findViewById(R.id.button_jiazai);
        TeacherMainInfoActivity activity = (TeacherMainInfoActivity) getActivity();
        teacherid = activity.getTeacherid();
        layout.setOnClickListener(this);
        button_jiazai.setOnClickListener(this);
        button_jiazai.setVisibility(View.GONE);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        select();



    }

    @Override
    public void onClick(View v) {
        int i1 = v.getId();
        if (i1 == R.id.layout) {
            Intent intent = new Intent(getActivity(), dongtaiActivity.class);
            intent.putExtra("teacherid", teacherid);
            getActivity().startActivity(intent);
        }
//        else if(i1 == R.id.button_jiazai){
//            final OkManager okManager = OkManager.getInstance();
//            i++;
//            String url="https://y.tuwan.com/m/Content/getDynamicListApi&format=json&teacherid="+teacherid+"&page="+i+"";
//            okManager.getAsync(getActivity(), url, new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    final String resule = response.body().string();
//
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            final Gson gson = new Gson();
//                            dongtaibean =  gson.fromJson(resule,dongtaibean.class);
//
//                            Adapter = new dongtaiAdapter(getActivity(),dongtaibean);
//                            recyclerView.removeAllViews();
//                            Adapter.notifyItemRangeChanged(0,3);
//                            recyclerView.setAdapter(Adapter);
//                            delete();
//                            Adapter.notifyItemRangeChanged(0,3);
//
//                        }
//                    });
//
//                }
//            },true);
//
//
//        }
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

    }

    private void select(){
        final OkManager okManager = OkManager.getInstance();
        String url="https://y.tuwan.com/m/Content/getDynamicListApi&format=json&teacherid="+teacherid+"&page="+i+"";
        okManager.getAsync(getActivity(), url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resule = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                        final Gson gson = new Gson();

                        dongtaibean =  gson.fromJson(resule,dongtaibean.class);
                        }catch (Exception e){

                            Log.e("错误分析",e+"");
                        }
                        Adapter = new dongtaiAdapter(getActivity(),dongtaibean);
                        recyclerView.setAdapter(Adapter);
                        delete();
                    }
                });

            }
        },true);

    }

    private void delete(){



                        Adapter.setOnRecyclerViewListener(new dongtaiAdapter.OnRecyclerViewListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                id=dongtaibean.getData().get(position).getId();


                                String url="https://api.tuwan.com/playteach/?type=play&data=dynamicdel&format=json&id="+id+"";

                                OkManager okmanger = OkManager.getInstance();
                                okmanger.getAsync(getActivity(), url, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        final String reult = response.body().string();

                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                Gson gson1  = new Gson();
                                                Codes codes =  gson1.fromJson(reult,Codes.class);
                                                if(codes.getCode()==1){
                                                    select();
                                                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();

                                                    Adapter.notifyItemRangeChanged(0,3);
                                                }


                                            }
                                        });

                                    }
                                },true);



                            }
                        });

        }








    @Override
    public void onResume() {
        super.onResume();
//        Adapter.notifyDataSetChanged();
        select();
    }
}
