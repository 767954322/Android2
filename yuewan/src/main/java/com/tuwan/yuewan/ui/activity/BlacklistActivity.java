package com.tuwan.yuewan.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.MenuAdapter;
import com.tuwan.yuewan.entity.backlistbean;
import com.tuwan.yuewan.utils.ChineseToEnglish;
import com.tuwan.yuewan.utils.CompareSort2;
import com.tuwan.yuewan.utils.OkManager;
import com.umeng.socialize.utils.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BlacklistActivity extends AppCompatActivity implements View.OnClickListener {
private String url,uids="";
private ImageView iv_titlebar_back;
private TextView number;
    private ArrayList<backlistbean.DataBean> users2 = new ArrayList<backlistbean.DataBean>();

private MenuAdapter menuAdapter;
    private RecyclerView swipeMenuRecyclerView;

    private List<String> mData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);
        initView();
        basklist();

    }


    private void basklist() {
        final List<String> blackList = NIMClient.getService(FriendService.class).getBlackList();

        for (int i = 0; i <blackList.size() ; i++) {
            if(i==blackList.size()-1){

                uids+=blackList.get(i);
            }else {
                uids+=blackList.get(i)+",";
            }
        }
        if(blackList.size()==0){

            number.setVisibility(View.GONE);

        }else {
            number.setVisibility(View.GONE);
            number.setText("已经拉黑"+blackList.size()+"名用户");
        }

        OkManager okManager = OkManager.getInstance();

        url="https://y.tuwan.com/m/User/getUserInfo&format=json&uids="+uids+"";

        okManager.getAsync(BlacklistActivity.this, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            final String rulse = response.body().string();
                BlacklistActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    try{

                        Gson gson = new Gson();
                        backlistbean backlistbean = gson.fromJson(rulse, com.tuwan.yuewan.entity.backlistbean.class);
                        Log.e("sjsjsjsj",backlistbean.toString()+"");


                        if(backlistbean.getError()==0){



                            List<com.tuwan.yuewan.entity.backlistbean.DataBean> data = backlistbean.getData();

                            for (int i = 0; i <data.size() ; i++) {
                                backlistbean.DataBean user = new backlistbean.DataBean();
                                user.setNickname(backlistbean.getData().get(i).getNickname());
                                user.setAge(backlistbean.getData().get(i).getAge());
                                user.setAvatar(backlistbean.getData().get(i).getAvatar());
                                user.setCity(backlistbean.getData().get(i).getCity());
                                user.setOnline(backlistbean.getData().get(i).getOnline());
                                user.setUid(backlistbean.getData().get(i).getUid());
                                user.setSex(backlistbean.getData().get(i).getSex());
                                user.setVip(backlistbean.getData().get(i).getVip());
                                user.setIcons(backlistbean.getData().get(i).getIcons());

                                String firstSpell = ChineseToEnglish.getFirstSpell(backlistbean.getData().get(i).getNickname());
//                                String substring = Cn2Spell.getPinYinFirstLetter(backlistbean.getData().get(i).getNickname()).toUpperCase();;
                                String substring = firstSpell.substring(0, 1).toUpperCase();
                                if(substring.matches("[A-Z]")){
//                                    data.get(i).setLetter(substring);
                                    user.setLetter(substring);
                                }else {
                                    user.setLetter("#");
                                }
//
                                users2.add(user);

//                                Log.e("user",user.toString()+"");
//                                Log.e("user",users2.size()+"");
                            }
//

                            Collections.sort(users2,new CompareSort2());
                            int width = swipeMenuRecyclerView.getWidth();
//                            menuAdapter.setData(users2);
                            menuAdapter = new MenuAdapter(BlacklistActivity.this,users2,width);
                            swipeMenuRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
                            swipeMenuRecyclerView.setAdapter(menuAdapter);
                            menuAdapter.setOnRecyclerViewListener(new MenuAdapter.OnRecyclerViewListener() {
                                @Override
                                public void onItemClick(View view, final int position) {

                                    NIMClient.getService(FriendService.class).removeFromBlackList(users2.get(position).getUid() + "").setCallback(new RequestCallback<Void>() {
                                        @Override
                                        public void onSuccess(Void param) {
                                            Toast.makeText(BlacklistActivity.this, "移除成功" , Toast.LENGTH_SHORT).show();
                                            users2.remove(position);
                                            menuAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailed(int code) {
                                            Toast.makeText(BlacklistActivity.this, "移除失败" , Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onException(Throwable exception) {

                                        }
                                    });
                                }
                            });



                        }
//                    }catch (Exception e){
//
//                    }

                }
            });



            }
        },true);



    }

    private void initView() {
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        iv_titlebar_back.setOnClickListener(this);
        swipeMenuRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        number = (TextView) findViewById(R.id.number);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_titlebar_back){
            finish();
        }

    }

}
