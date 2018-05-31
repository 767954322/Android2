package com.tuwan.yuewan.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.gson.Gson;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.addinfobean;
import com.tuwan.yuewan.utils.OkManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity {
    private List<addinfobean.DataBean.CityBean> listcity = new ArrayList<>();
    private List<addinfobean.DataBean.ProvinceBean> listprovince = new ArrayList<>();
    private ArrayList optionsItems = new ArrayList();
    private ArrayList options2Items = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void onClick(View view) {
        for (int i = 0; i < listcity.size(); i++) {
            optionsItems.add(listcity.get(i).getArea());

        }
        for (int i = 0; i < listprovince.size(); i++) {
            options2Items.add(listprovince.get(i).getArea());

        }

        String url = "https://y.tuwan.com/m/User/appExtInfo&format=json";
        OkManager okManager = OkManager.getInstance();
        okManager.getAsync(Main2Activity.this, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.e("eeeeeeeeee",response+"");
                final String result = response.body().string();
//                Log.e("eeeeeeeeee",result+"");
                Main2Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Gson gson = new Gson();
                        addinfobean addinfobean = gson.fromJson(result, com.tuwan.yuewan.entity.addinfobean.class);
                        if (addinfobean.getError() == 0) {

                            listcity = addinfobean.getData().getCity();
                            listprovince = addinfobean.getData().getProvince();
                        }

                    }
                });


            }
        }, true);

        CityPicker cityPicker = new CityPicker.Builder(Main2Activity.this)
                .textSize(14)
                .title("地址选择")
                .titleBackgroundColor("#FFFFFF")
                .confirTextColor("#696969")
                .cancelTextColor("#696969")
                .province("江苏省")
                .city("常州市")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)

                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();
    }
}
