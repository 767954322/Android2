package com.tuwan.yuewan.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.MainServiceAdapter;
import com.tuwan.yuewan.adapter.ServerAdapter;
import com.tuwan.yuewan.entity.Code;
import com.tuwan.yuewan.entity.teacher.Servers;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.Urls;
import com.umeng.socialize.utils.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 我的服务
 */
public class MyServiceActivity extends BaseActivity {

    private ImageView serv_back;
    private TextView serv_title;
    private TextView serv_save;
    private View linlay;
    private GridAdapter ga;
    private TextView tv_main;
    private RelativeLayout main;
    private ListView service_list;
    private String name;
    private int id;
    private List<Map> maps;
    private String id1;
    OkHttpClient client = new OkHttpClient();
    private String names;
    private String cookie;
    List<Servers.DataBean> list = new ArrayList<>();
    private WindowManager wm;
    private boolean hasChecked = false;
    private int width;
    private int height;
    private ArrayList<Person> al = new ArrayList<>();
    private String sid;
    private Jsons jsons = new Jsons();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private HashMap<String, Object> map;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 200) {
                tv_main.setText(names);
            }
        }
    };
    private ServerAdapter serverAdapter;
    private int price;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_service;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));
        SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
        cookie = preferences.getString("Cookie", null);
        initView();
    }

    private void initView() {
        wm = getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        serv_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        serv_title = (TextView) findViewById(R.id.tv_titlebar_title);
        serv_title.setText("我的服务");
        serv_save = (TextView) findViewById(R.id.tv_titlebar_title_save);
        serv_save.setText("保存");
        linlay = (View) findViewById(R.id.linlay);
        tv_main = (TextView) findViewById(R.id.tv_main);
        main = (RelativeLayout) findViewById(R.id.main);
        service_list = (ListView) findViewById(R.id.service_list);
        initData();
        serv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        serverAdapter = new ServerAdapter(list, MyServiceActivity.this, linlay, wm, cookie);
        service_list.setAdapter(serverAdapter);
        serverAdapter.setOnItemClickleteners(new ServerAdapter.OnItemClickleteners() {
            @Override
            public void onClicks(int pos, int po) {
                jsons.getService().get(pos).setSwitchX(po);
            }

            @Override
            public void onDJ(int pos, int po) {
                initJDOK(pos);
            }
        });
        setMain();
        serv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPostJson(1);
            }
        });
    }

    //价格选择
    private void initJDOK(final int pos) {
        View view = View.inflate(MyServiceActivity.this, R.layout.my_pop_money, null);
        WindowManager wm = getWindowManager();
        final int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        final PopupWindow window = new PopupWindow(view, width, height);

//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.setFocusable(true);

//        window.showAsDropDown(mTitle, 0, 0);
        window.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        final GridView my_pop_grid = (GridView) view.findViewById(R.id.my_pop_grid);
        final TextView pop_actual = (TextView) view.findViewById(R.id.pop_actual);
        final LinearLayout button = (LinearLayout) view.findViewById(R.id.button);
//        pop_actual.setText(list.get(pos).getTprice());


        Button money_sore = (Button) view.findViewById(R.id.money_sore);
        if (list.get(pos).getPrices().size() >= 1) {
            ga = new GridAdapter(list.get(pos).getPrices(), MyServiceActivity.this);
            my_pop_grid.setAdapter(ga);
        }
        final List<Servers.DataBean.PricesBean> prices = list.get(pos).getPrices();
        my_pop_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               TextView tv= (TextView) view.findViewById(R.id.bjzl_grid_text);

                if(prices.get(position).getSelect() == 1){
                    ga.setSelected(position);
                    ga.notifyDataSetChanged();

                    pop_actual.setText(prices.get(position).getPrice() + "");
                    price = prices.get(position).getPrice();

                }else {
                    Toast.makeText(MyServiceActivity.this, "接单满"+prices.get(position).getNum()+"开启", Toast.LENGTH_SHORT).show();

                }


            }
        });


        money_sore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(pos).setTprice(price);
//                handler.sendEmptyMessage(202);
                serverAdapter.notifyDataSetChanged();
                if (pop_actual.getText().equals("")) {

                    Toast.makeText(MyServiceActivity.this, "请选择服务单价", Toast.LENGTH_SHORT).show();
                } else {

                    jsons.getService().get(pos).setPrice(price + "");
                    window.dismiss();
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
    }

    //设置主服务
    private void setMain() {
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(MyServiceActivity.this, R.layout.pop_service, null);

                final PopupWindow window = new PopupWindow(view, width, height);
                // 设置PopupWindow的背景
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // 设置PopupWindow是否能响应外部点击事件
                window.setOutsideTouchable(true);
                window.showAsDropDown(linlay, 0, 0);
                ImageView popup_close = (ImageView) view.findViewById(R.id.popup_close3);
                TextView service_sure = (TextView) view.findViewById(R.id.service_sure);
                ListView pop_list = (ListView) view.findViewById(R.id.pop_list);
                popup_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        window.dismiss();
                    }
                });
                final MainServiceAdapter adapter = new MainServiceAdapter(al, MyServiceActivity.this);
                pop_list.setAdapter(adapter);
                pop_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    int currentNum = 1;

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for (Person person : al) {
                            person.setBoo(false);
                        }
                        if (currentNum == 1) {
                            al.get(position).setBoo(true);
                            sid = al.get(position).getId();
                            names = al.get(position).getName();
                            currentNum = position;
                        } else if (currentNum == position) {
                            for (Person person : al) {
                                person.setBoo(false);
                            }
                            currentNum = 1;
                        } else if (currentNum != position) {
                            for (Person person : al) {
                                person.setBoo(false);
                            }
                            al.get(position).setBoo(true);
                            sid = al.get(position).getId();
                            names = al.get(position).getName();
                            currentNum = position;
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
                service_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        jsons.setMainid(sid);
                        initPostJson(0);
                        window.dismiss();
                    }
                });
            }
        });
    }

    //设置主服务后发送
    private void initPostJson(final int i) {
        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        Gson gson = new Gson();
        String s = gson.toJson(jsons);
        map = new HashMap<>();
        map.put("format", "json");
        map.put("data", s);
        OkManager.getInstance().postJson(MyServiceActivity.this, Urls.TEACHER_SERVICE_SEETING, map, true, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onLoginDone();
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                onLoginDone();
//                Log.e("eeeeeeeee",response+"");
                final String ss = response.body().string();
//                Log.e("eeeeeeeee",ss+"");
                handler.sendEmptyMessage(200);
                if (i == 1) {
                    MyServiceActivity.this.finish();
                }
                MyServiceActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson1 = new Gson();
                        Code code = gson1.fromJson(ss,Code.class);
                        if(code.getError()==0){
                            Toast.makeText(MyServiceActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            MyServiceActivity.this.finish();

                        }else if(code.getError()==-1){
                            Toast.makeText(MyServiceActivity.this, "您还没有登录", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MyServiceActivity.this, "您的参数有误", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }
        });
    }

    private ArrayList<ServiceBean> sblist = new ArrayList<>();

    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }

    private void initData() {
        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        final Request request = new Request.Builder()
                .url("https://y.tuwan.com/m/Teacher/getServices?format=json")
                .addHeader("Cookie", cookie)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Log.e("导师我的服务据错误原因：", e.toString());
                onLoginDone();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onLoginDone();
                String result = response.body().string();
//                Log.e("导师我的服务返回数据", result);
                Gson gson = new Gson();
                Servers servers = gson.fromJson(result, Servers.class);
                final List<Servers.DataBean> been = servers.getData();
                list.addAll(been);
                maps = new ArrayList<Map>();
                for (int i = 0; i < been.size(); i++) {
                    final Servers.DataBean dataBean = been.get(i);
                    name = dataBean.getName();
                    id = dataBean.getId();
                    ServiceBean sb = new ServiceBean();
                    sb.setId(id);
                    sb.setPrice(dataBean.getPrices() + "");
                    sb.setSwitchX(dataBean.getSwitchX());
                    sblist.add(sb);
                    Person person = new Person();
                    person.setName(dataBean.getName());
                    person.setIcon(dataBean.getIcon());
                    person.setId(dataBean.getId() + "");
                    person.setPrices(dataBean.getPrices() + "");
                    al.add(person);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dataBean.getMain() == 1) {
                                String s = dataBean.getName();
//                                Log.e("是否是主服务 ", s);
                                tv_main.setVisibility(View.VISIBLE);
                                tv_main.setText(s);
                            }
                        }
                    });
                }
                jsons.setService(sblist);
            }
        });
    }

    //--------------------------------------------------实体类----------------------------------------
    public class Person {
        private String name;
        private String icon;
        private String id;
        private String prices;
        private boolean boo;

        public Person() {

        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", icon='" + icon + '\'' +
                    ", id='" + id + '\'' +
                    ", prices='" + prices + '\'' +
                    ", boo=" + boo +
                    '}';
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPrices() {
            return prices;
        }

        public void setPrices(String prices) {
            this.prices = prices;
        }

        public boolean isBoo() {
            return boo;
        }

        public void setBoo(boolean boo) {
            this.boo = boo;
        }
    }

    public class Jsons {

        /**
         * mainid : 14
         * service : [{"id":14,"price":45,"switch":1},{"id":14,"price":45,"switch":1}]
         */

        private String mainid;
        private List<ServiceBean> service;

        public String getMainid() {
            return mainid;
        }

        public void setMainid(String mainid) {
            this.mainid = mainid;
        }

        public List<ServiceBean> getService() {
            return service;
        }

        public void setService(List<ServiceBean> service) {
            this.service = service;
        }

        @Override
        public String toString() {
            return "Jsons{" +
                    "mainid='" + mainid + '\'' +
                    ", service=" + service +
                    '}';
        }
    }

    public class ServiceBean {

        /**
         * id : 14
         * price : 45
         * switch : 1
         */

        private int id;
        private String price;
        @SerializedName("switch")
        private int switchX;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getSwitchX() {
            return switchX;
        }

        public void setSwitchX(int switchX) {
            this.switchX = switchX;
        }

        @Override
        public String toString() {
            return "ServiceBean{" +
                    "id=" + id +
                    ", price='" + price + '\'' +
                    ", switchX=" + switchX +
                    '}';
        }
    }

    class GridAdapter extends BaseAdapter {
        private Context conte;
        private List<Servers.DataBean.PricesBean> li;

        private int selected;
        public GridAdapter(List<Servers.DataBean.PricesBean> tprice, Context context) {
            this.li = tprice;
            this.conte = context;
        }
        public void setSelected(int position){
            this.selected = position;
        }
        @Override
        public int getCount() {
            return li.size();
        }

        @Override
        public Object getItem(int position) {
            return li.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHold vh;
            if (convertView == null) {
                vh = new ViewHold();
                convertView = LayoutInflater.from(conte).inflate(R.layout.bjzl_grid_item2, null);
                vh.tv = (TextView) convertView.findViewById(R.id.bjzl_grid_text);
                vh.layout__ = (LinearLayout) findViewById(R.id.bjzl_grid_linear);
                convertView.setTag(vh);

            } else {
                vh = (ViewHold) convertView.getTag();
            }
//            if (li.get(position).setPrice()) {

            vh.tv.setText(li.get(position).getPrice() + "元");

            if(li.get(position).getSelect()==1){
                vh.tv.setBackgroundResource(R.drawable.shape_text_bg);
                vh.tv.setTextColor(Color.rgb(102,102,102));
                if (position==selected) {
                    vh.tv.setBackgroundResource(R.drawable.selector_chongzhi_background1);
                    vh.tv.setTextColor(Color.rgb(170, 99, 8));
                }else {
                    vh.tv.setBackgroundResource(R.drawable.shape_text_bg);
                    vh.tv.setTextColor(Color.rgb(102,102,102));
                }
            } else {
                vh.tv.setBackgroundResource(R.drawable.my_service_bg);
                vh.tv.setTextColor(Color.rgb(102,102,102));

            }

            return convertView;
        }


    }

    class ViewHold {
        TextView tv;
        LinearLayout layout__;
    }
}
