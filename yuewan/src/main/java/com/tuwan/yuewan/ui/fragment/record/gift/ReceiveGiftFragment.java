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
import com.tuwan.yuewan.adapter.ReceiveGiftAdapter;
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

public class ReceiveGiftFragment extends Fragment {

    private ListView all_record;
    private HashMap<String, String> map;
    private int page = 1;
    private ReceiveGiftAdapter adapter;
    private int totalPage;
    private ArrayList<ReceiveGiftBean.DataBean> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_record, null);
        all_record = (ListView) view.findViewById(R.id.all_record);
        adapter = new ReceiveGiftAdapter(getActivity(), list);
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
        OkManager.getInstance().getSendGift(getActivity(), Urls.RECEIVEGIFT, map, true, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("收到礼物错误信息", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("收到礼物信息", "全部：" + result.toString());
                Gson gson = new Gson();
                try {
                    ReceiveGiftBean fromJson = gson.fromJson(result, ReceiveGiftBean.class);
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
    public class ReceiveGiftBean {
        /**
         * data : [{"charm":1,"create_time":"2018-01-18 12:04:45","name":"carle23","num":1,"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","teacherid":107833,"title":"香吻","uid":228564},{"charm":1,"create_time":"2018-01-18 11:41:54","name":"carle23","num":1,"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","teacherid":107833,"title":"香吻","uid":228564},{"charm":1,"create_time":"2018-01-16 10:18:04","name":"dsfa","num":1,"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","teacherid":107833,"title":"香吻","uid":136438},{"charm":1,"create_time":"2018-01-16 10:17:40","name":"dsfa","num":1,"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","teacherid":107833,"title":"香吻","uid":136438},{"charm":1,"create_time":"2018-01-16 10:17:39","name":"dsfa","num":1,"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","teacherid":107833,"title":"香吻","uid":136438},{"charm":1,"create_time":"2018-01-16 10:17:36","name":"dsfa","num":1,"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","teacherid":107833,"title":"香吻","uid":136438},{"charm":1,"create_time":"2018-01-16 10:17:35","name":"dsfa","num":1,"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","teacherid":107833,"title":"香吻","uid":136438},{"charm":1,"create_time":"2018-01-16 10:17:19","name":"dsfa","num":1,"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","teacherid":107833,"title":"香吻","uid":136438},{"charm":1,"create_time":"2018-01-16 10:17:15","name":"dsfa","num":1,"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","teacherid":107833,"title":"香吻","uid":136438},{"charm":1,"create_time":"2018-01-16 10:17:14","name":"dsfa","num":1,"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","teacherid":107833,"title":"香吻","uid":136438},{"charm":1,"create_time":"2018-01-16 10:17:13","name":"dsfa","num":1,"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","teacherid":107833,"title":"香吻","uid":136438},{"charm":10,"create_time":"2018-01-11 14:46:38","name":"Asdy","num":1,"pic":"http://img3.tuwandata.com/uploads/play/2132001515566355.png","teacherid":107833,"title":"胡萝卜","uid":129440},{"charm":10,"create_time":"2018-01-11 14:46:30","name":"Asdy","num":1,"pic":"http://img3.tuwandata.com/uploads/play/2132001515566355.png","teacherid":107833,"title":"胡萝卜","uid":129440},{"charm":10,"create_time":"2018-01-11 14:46:06","name":"Asdy","num":1,"pic":"http://img3.tuwandata.com/uploads/play/2132001515566355.png","teacherid":107833,"title":"胡萝卜","uid":129440},{"charm":10,"create_time":"2018-01-11 14:45:48","name":"Asdy","num":1,"pic":"http://img3.tuwandata.com/uploads/play/2132001515566355.png","teacherid":107833,"title":"胡萝卜","uid":129440}]
         * error : 0
         * page : 1
         * totalPage : 10
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
             * charm : 1
             * create_time : 2018-01-18 12:04:45
             * name : carle23
             * num : 1
             * pic : http://img3.tuwandata.com/uploads/play/1629711503976254.png
             * teacherid : 107833
             * title : 香吻
             * uid : 228564
             */

            private int charm;
            private String create_time;
            private String name;
            private int num;
            private String pic;
            private int teacherid;
            private String title;
            private int uid;

            public int getCharm() {
                return charm;
            }

            public void setCharm(int charm) {
                this.charm = charm;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

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

            public int getTeacherid() {
                return teacherid;
            }

            public void setTeacherid(int teacherid) {
                this.teacherid = teacherid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }
        }
    }


}
