package com.tuwan.yuewan.ui.fragment.record.gift;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.SendGiftAdapter;
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
 * Created by Administrator on 2018/1/18.
 */

public class SendGiftFragment extends Fragment {

    private ListView all_record;
    private HashMap<String, String> map;
    private int page = 1;
    private SendGiftAdapter adapter;
    private int totalPage;
    private ArrayList<SendGiftBean.DataBean> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_record, null);
        all_record = (ListView) view.findViewById(R.id.all_record);
        adapter = new SendGiftAdapter(getActivity(), list);
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

    private void initData() {
        map = new HashMap<>();
        map.put("format", "json");
        map.put("mode", "app");
        map.put("page", page + "");
        map.put("step", "15");
        OkManager.getInstance().getSendGift(getActivity(), Urls.SENDGIFT, map, true, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("发送的礼物错误信息", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("发送的礼物信息", "全部：" + result.toString());
                Gson gson = new Gson();
                try {
                    SendGiftBean fromJson = gson.fromJson(result, SendGiftBean.class);
                    list.addAll(fromJson.getData());
                    handler.sendEmptyMessage(200);
                } catch (Exception e) {
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

    //-----------------------------------------实体类-----------------------------------------
    public class SendGiftBean {
        /**
         * error : 0
         * page : 1
         * totalPage : 2
         * data : [{"num":1,"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","title":"香吻","name":"苏木槿","create_time":"2018-01-12 14:27:20","uid":107833,"teacherid":472143,"charm":1},{"num":1,"pic":"http://img3.tuwandata.com/uploads/play/2132001515566355.png","title":"胡萝卜","name":"Leo","create_time":"2018-01-11 15:37:54","uid":107833,"teacherid":1068754,"charm":10},{"num":2,"pic":"http://img3.tuwandata.com/uploads/play/2132001515566355.png","title":"胡萝卜","name":"李莫愁zZ","create_time":"2018-01-11 15:27:27","uid":107833,"teacherid":206460,"charm":20},{"num":2,"pic":"http://img3.tuwandata.com/uploads/play/2132001515566355.png","title":"胡萝卜","name":"芊骨","create_time":"2018-01-11 15:26:59","uid":107833,"teacherid":1199899,"charm":20},{"num":1,"pic":"http://img3.tuwandata.com/uploads/play/2132001515566355.png","title":"胡萝卜","name":"二丫","create_time":"2018-01-11 15:25:57","uid":107833,"teacherid":1013228,"charm":10},{"num":1,"pic":"http://img3.tuwandata.com/uploads/play/2132001515566355.png","title":"胡萝卜","name":"C叉叉","create_time":"2018-01-11 15:25:24","uid":107833,"teacherid":450623,"charm":10},{"num":1,"pic":"http://img3.tuwandata.com/uploads/play/2132001515566355.png","title":"胡萝卜","name":"軟","create_time":"2018-01-11 15:25:09","uid":107833,"teacherid":1192559,"charm":10},{"num":1,"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","title":"香吻","name":"橙皮酱","create_time":"2018-01-10 13:49:34","uid":107833,"teacherid":451374,"charm":1},{"num":1,"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","title":"香吻","name":"橙皮酱","create_time":"2018-01-10 13:49:33","uid":107833,"teacherid":451374,"charm":1},{"num":1,"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","title":"香吻","name":"橙皮酱","create_time":"2018-01-10 13:49:31","uid":107833,"teacherid":451374,"charm":1},{"num":1,"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","title":"香吻","name":"宝贝小花花","create_time":"2018-01-09 21:21:50","uid":107833,"teacherid":421817,"charm":1},{"num":1,"pic":"http://img3.tuwandata.com/uploads/play/1427371503976282.png","title":"黄瓜","name":"真心","create_time":"2017-12-22 18:06:35","uid":107833,"teacherid":233568,"charm":10},{"num":1,"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","title":"香吻","name":"真心","create_time":"2017-12-22 18:06:26","uid":107833,"teacherid":233568,"charm":1},{"num":1,"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","title":"香吻","name":"烟烟","create_time":"2017-12-22 18:02:37","uid":107833,"teacherid":405978,"charm":1},{"num":1,"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","title":"香吻","name":"媛宝宝","create_time":"2017-12-22 18:00:09","uid":107833,"teacherid":354307,"charm":1}]
         */

        private int error;
        private int page;
        private int totalPage;
        private List<DataBean> data;

        public int getError() {
            return error;
        }

        public void setError(int error) {
            this.error = error;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public class DataBean {
            /**
             * num : 1
             * pic : http://img3.tuwandata.com/uploads/play/1629711503976254.png
             * title : 香吻
             * name : 苏木槿
             * create_time : 2018-01-12 14:27:20
             * uid : 107833
             * teacherid : 472143
             * charm : 1
             */

            private int num;
            private String pic;
            private String title;
            private String name;
            private String create_time;
            private int uid;
            private int teacherid;
            private int charm;

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getTeacherid() {
                return teacherid;
            }

            public void setTeacherid(int teacherid) {
                this.teacherid = teacherid;
            }

            public int getCharm() {
                return charm;
            }

            public void setCharm(int charm) {
                this.charm = charm;
            }
        }
    }
}
