package com.tuwan.yuewan.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.FilterListener;
import com.tuwan.yuewan.adapter.SwitchAdapter;
import com.tuwan.yuewan.adapter.SwitchAdapter2;
import com.tuwan.yuewan.entity.ErrorBean;
import com.tuwan.yuewan.entity.Switch;
import com.tuwan.yuewan.entity.SwitchItem;

import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.widget.ClearEditText;
import com.tuwan.yuewan.utils.OkManager;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchActivity extends BaseActivity {

    private ClearEditText edit;
    private Button switchs;
    private ListView switch_list;
    private ListView switch_list2;
    private List<String> list = new ArrayList<>();
    private List<SwitchItem.DataBean> list2 = new ArrayList<>();
    boolean isFilter;
    private SwitchAdapter adapter = null;
    private List<String> data;
    private List<SwitchItem.DataBean> dataBeen;
    private LinearLayout mNoSearch;
    private String editString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        setData();// 给listView设置adapter
        setListeners();// 设置监听


    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
    }

    private void initView() {
        edit = (ClearEditText) findViewById(R.id.edit);
        switchs = (Button) findViewById(R.id.switchs);
        switch_list = (ListView) findViewById(R.id.switch_list);
        switch_list2 = (ListView) findViewById(R.id.switch_list2);
        mNoSearch = (LinearLayout) findViewById(R.id.noSearch);

    }

    private void submit() {
        // validate
        String editString = edit.getText().toString().trim();
        if (TextUtils.isEmpty(editString)) {
            Toast.makeText(this, "请输入导师姓名", Toast.LENGTH_SHORT).show();
            list2.clear();
            switch_list.setVisibility(View.GONE);
            switch_list2.setVisibility(View.GONE);
            return;
        }
    }

    /**
     * 数据初始化并设置adapter
     */
    private void setData() {
        initData();// 初始化数据

        // 这里创建adapter的时候，构造方法参数传了一个接口对象，这很关键，回调接口中的方法来实现对过滤后的数据的获取
        adapter = new SwitchAdapter(list, this, new FilterListener() {
            // 回调方法获取过滤后的数据
            public void getFilterData(List<String> list) {
                // 这里可以拿到过滤后数据，所以在这里可以对搜索后的数据进行操作
//                Log.e("TAG", "接口回调成功");
//                Log.e("TAG", list.toString());
                setItemClick(list);
            }
        });

    }

    /**
     * 给listView添加item的单击事件
     *
     * @param filter_lists 过滤后的数据集
     */
    protected void setItemClick(final List<String> filter_lists) {
        switch_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 点击对应的item时，弹出toast提示所点击的内容
//                   Toast.makeText(SearchActivity.this, filter_lists.get(position), Toast.LENGTH_SHORT).show();
                edit.setText(filter_lists.get(position));
                searchSubmit();

            }
        });
    }

    /**
     * 简单的list集合添加一些测试数据
     */
    private void initData() {
        String editString = edit.getText().toString().trim();
                DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                }).setCanceledOnTouchOutside(false);
                ServiceFactory.getShortCacheInstance()
                        .createService(YService.class)
                        .mPrompt("json",editString)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CommonObserver<Switch>() {
                            @Override
                            public void onNext(Switch errorBean) {
                                super.onNext(errorBean);
                                onLoginDone();
                                Switch aSwitch = errorBean;
                                data = aSwitch.getData();
                                list.clear();
                                list.addAll(data);
                                adapter.notifyDataSetChanged();
                                switch_list2.setVisibility(View.GONE);
                                switch_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        edit.setText(list.get(position));
                                    }
                                });
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                onLoginDone();
                            }
                        });

    }
    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }

    private void setListeners() {
        // 没有进行搜索的时候，也要添加对listView的item单击监听
        setItemClick(list);

        /**
         * 对编辑框添加文本改变监听，搜索的具体功能在这里实现
         * 很简单，文本该变的时候进行搜索。关键方法是重写的onTextChanged（）方法。
         */
        edit.addTextChangedListener(new TextWatcher() {
            /**
             * 编辑框内容改变的时候会执行该方法
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 如果adapter不为空的话就根据编辑框中的内容来过滤数据
                if (adapter != null) {
                    switch_list.setVisibility(View.VISIBLE);
                    switch_list2.setVisibility(View.GONE);
                    adapter.getFilter().filter(s);
                    if(!s.equals("")){
                        adapter.getText(s.toString());
                        switch_list.setAdapter(adapter);
                    }


                }else if (adapter == null) {
                    list2.clear();
                    switch_list.setVisibility(View.GONE);
                    switch_list2.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //执行搜索的代码
                    searchSubmit();
                }
                return false;
            }
        });

        switchs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //searchSubmit();
                finish();
            }
        });
    }

    private void searchSubmit(){
        mNoSearch.setVisibility(View.GONE);
        submit();
        list2.clear();
        switch_list.setVisibility(View.GONE);
        switch_list2.setVisibility(View.VISIBLE);
        editString = edit.getText().toString().trim();
        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        ServiceFactory.getShortCacheInstance()
                .createService(YService.class)
                .mSearchResult("json","app",editString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<SwitchItem>() {
                    @Override
                    public void onNext(SwitchItem errorBean) {
                        super.onNext(errorBean);
                        onLoginDone();
                        SwitchItem aSwitch = errorBean;
                        dataBeen = aSwitch.getData();
                        list2.clear();
                        list2.addAll(dataBeen);
                        SwitchAdapter2 adapter2 = new SwitchAdapter2(list2, editString,SearchActivity.this);
                        switch_list2.setAdapter(adapter2);
//                                    switch_list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                        @Override
//                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                                            ToastUtils.getInstance().showToast("6666666666666666666");
//                                            ToastUtils.getInstance().showToast(list2.get(position).getNickname());
//                                        }
//                                    });
//                            Log.e("搜索返回数据: ", result);
                        if(dataBeen.size() == 0){
                            mNoSearch.setVisibility(View.VISIBLE);
                        }
                        else{
                            mNoSearch.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        onLoginDone();
                    }
                });

    }


}
