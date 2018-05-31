package com.tuwan.yuewan.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.RedAdapter;
import com.tuwan.yuewan.adapter.RedSelectAdapter;
import com.tuwan.yuewan.entity.RedEnvelopes;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.service.YService;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;

public class RedSelectActivity extends BaseActivity {

    private TextView tv_im_title;
    private LinearLayout lly_select_no;
    private LinearLayout lly_my_no;
    private LinearLayout lly_my_red_history;
    private ImageView img_red_check_no;
    private ImageView iv_titlebar_back;
    private ListView mListview;
    private TextView tv_my_red_more;
    private RedSelectAdapter mAdapter;
    private ArrayList<RedEnvelopes.RedEnvelopsData> redList = new ArrayList<RedEnvelopes.RedEnvelopsData>();
    private String gameid;
    private String price;
    private int ucid;
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_red;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {

        initView();
        initData();
    }

    public static void show(Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), RedSelectActivity.class);
        fragment.startActivity(intent);
    }

    private void initView() {
        Intent intent = getIntent();
        gameid = intent.getStringExtra("gameid");
        price = intent.getStringExtra("price");
        ucid = intent.getIntExtra("ucid",-1);
        tv_im_title = (TextView) findViewById(R.id.tv_im_title);
        lly_select_no = (LinearLayout) findViewById(R.id.lly_select_no);
        lly_my_no = (LinearLayout) findViewById(R.id.lly_my_no);
        lly_my_red_history = (LinearLayout) findViewById(R.id.lly_my_red_history);
        img_red_check_no = (ImageView) findViewById(R.id.img_red_check_no);
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        mListview = (ListView) findViewById(R.id.lv_my_red);
        tv_my_red_more = (TextView) findViewById(R.id.tv_my_red_more);
        tv_im_title.setText("使用红包");
        lly_select_no.setVisibility(View.VISIBLE);
        lly_my_red_history.setVisibility(View.GONE);
        lly_select_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < redList.size(); i++) {
                    redList.get(i).setSelect(false);
                }
                mAdapter.notifyDataSetChanged();
                img_red_check_no.setImageResource(R.drawable.pay_choose);
                Intent intent = new Intent();
                intent.putExtra("type","0");
                setResult(1001,intent);
                finish();
            }
        });
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i < redList.size(); i++) {
                    if (i == position){
                        redList.get(i).setSelect(true);
                    }else {
                        redList.get(i).setSelect(false);
                    }
                }
                img_red_check_no.setImageResource(R.drawable.pay_normal3x);
                mAdapter.notifyDataSetChanged();
                Intent intent = getIntent();
                intent.putExtra("type","1");
                intent.putExtra("ucid",redList.get(position).getUcid());
                intent.putExtra("price", redList.get(position).getPrice());
                setResult(1001,intent);
                finish();
            }
        });
    }

    private void initData() {
        //        退出页面
        RxView.clicks(iv_titlebar_back)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
        RxView.clicks(tv_my_red_more)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent();
                        intent.setClass(RedSelectActivity.this,MyRedHistoryActivity.class);
                        startActivity(intent);
                    }
                });

        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .mRedList("json",gameid,price)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<RedEnvelopes>(){
                    @Override
                    public void onNext(RedEnvelopes redEnvelopes) {
                        super.onNext(redEnvelopes);
                        redList = new ArrayList<RedEnvelopes.RedEnvelopsData>();
                        double price = 0;
                        for (int i = 0; i < redEnvelopes.getData().size(); i++) {
//                            if (redEnvelopes.getData().get(i).getExpire() != 1&&redEnvelopes.getData().get(i).getUsed() != 1) {
                                RedEnvelopes.RedEnvelopsData data = new RedEnvelopes.RedEnvelopsData();
                                data.setDesc(redEnvelopes.getData().get(i).getDesc());
                                data.setEdate(redEnvelopes.getData().get(i).getEdate());
                                data.setExpire(redEnvelopes.getData().get(i).getExpire());
                                data.setPrice(redEnvelopes.getData().get(i).getPrice());
                                data.setSdate(redEnvelopes.getData().get(i).getSdate());
                                data.setTitle(redEnvelopes.getData().get(i).getTitle());
                                data.setUcid(redEnvelopes.getData().get(i).getUcid());
                                data.setUsed(redEnvelopes.getData().get(i).getUsed());
                                if(redEnvelopes.getData().get(i).getUcid() == ucid){
                                    data.setSelect(true);
                                }else {
                                    data.setSelect(false);
                                }
                                redList.add(data);
                        }
                        if (ucid == -1){
                            img_red_check_no.setImageResource(R.drawable.pay_choose);
                        }
                        mAdapter = new RedSelectAdapter(RedSelectActivity.this);
                        mAdapter.setData(redList);
                        mListview.setAdapter(mAdapter);
                        setListViewHeightBasedOnChildren(mListview);
                        if (redList.size() < 1){
                            lly_my_no.setVisibility(View.VISIBLE);
                        }else {
                            lly_my_no.setVisibility(View.GONE);
                        }
                        onLoginDone();
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

    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除

        listView.setLayoutParams(params);
    }
}
