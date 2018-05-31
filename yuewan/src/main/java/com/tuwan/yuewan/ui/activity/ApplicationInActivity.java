package com.tuwan.yuewan.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.AppliItem;
import com.tuwan.yuewan.adapter.AppliItem2;
import com.tuwan.yuewan.adapter.ApplicationInAdapter;
import com.tuwan.yuewan.entity.InBean;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.service.YService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * 申请入驻
 */
public class ApplicationInActivity extends BaseActivity {

    private ImageView backs;
    private TextView in_help;
    private TextView in_title_ruzhu;
    private ListView in_recycler1;
    private ListView in_recycler2;
    private ListView in_recycler3;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_application_in;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        RelativeLayout toobar = (RelativeLayout) findViewById(R.id.application_toobar);
        //SystemBarHelper.setHeightAndPadding(ApplicationInActivity.this, toobar);

        backs = (ImageView) findViewById(R.id.iv_titlebar_back);
        in_help = (TextView) findViewById(R.id.in_help);
        in_recycler1 = (ListView) findViewById(R.id.in_recycler1);
        in_recycler2 = (ListView) findViewById(R.id.in_recycler2);
        in_recycler3 = (ListView) findViewById(R.id.in_recycler3);
        in_title_ruzhu = (TextView) findViewById(R.id.in_title_ruzhu);
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        in_help.setVisibility(View.GONE);
        in_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        in_title_ruzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplicationInActivity.this, RedWebActivity.class);
                intent.putExtra("url", "https://wx.tuwan.com/events/app/mentorcode");
                startActivity(intent);
            }
        });
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();

    }

    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }

    private void initData() {
        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .in()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<InBean>() {
                    @Override
                    public void onNext(@NonNull InBean result) {
                        super.onNext(result);
                        onLoginDone();
                        try {
                            final List<InBean.LinegBean> linegBeen = new ArrayList<InBean.LinegBean>();
                            final List<InBean.OnlinegBean> onlinegBeen = new ArrayList<InBean.OnlinegBean>();
                            final List<InBean.OnlinepBean> onlinepBeen = new ArrayList<>();

                            List<InBean.LinegBean> listBeen3 = result.getLineg();
                            final List<InBean.OnlinegBean> listBeen1 = result.getOnlineg();
                            List<InBean.OnlinepBean> listBeen2 = result.getOnlinep();

//                            Log.e("返回的数据_申请入驻", listBeen3.get(0).getGameicon());
                            linegBeen.addAll(listBeen3);
                            onlinegBeen.addAll(listBeen1);
                            onlinepBeen.addAll(listBeen2);
                            ApplicationInAdapter inAdapter = new ApplicationInAdapter(ApplicationInActivity.this, linegBeen);
                            AppliItem inAdapter2 = new AppliItem(ApplicationInActivity.this, onlinegBeen);
                            AppliItem2 inAdapter3 = new AppliItem2(ApplicationInActivity.this, onlinepBeen);
                            in_recycler1.setAdapter(inAdapter2);
                            in_recycler2.setAdapter(inAdapter3);
                            in_recycler3.setAdapter(inAdapter);
                            setListViewHeightBasedOnChildren(in_recycler1);
                            setListViewHeightBasedOnChildren(in_recycler2);
                            setListViewHeightBasedOnChildren(in_recycler3);
                            in_recycler1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    int state = onlinegBeen.get(position).getState();
                                    if (state == 0) {
                                        Intent intent = new Intent(ApplicationInActivity.this, ApplyForActivity.class);
                                        intent.putExtra("title", onlinegBeen.get(position).getTitle());
                                        intent.putExtra("dtid", onlinegBeen.get(position).getDtid());
                                        intent.putExtra("imgUrl", onlinegBeen.get(position).getGameicon());
                                        intent.putExtra("state", state);
                                        intent.putExtra("one", 1);
                                        startActivity(intent);
                                    }
                                    if (state == -1) {
                                        Intent intent = new Intent(ApplicationInActivity.this, StateActivity.class);
                                        intent.putExtra("states", state);
                                        startActivity(intent);
                                    }
                                    if(state == 1){
                                        Intent intent = new Intent(ApplicationInActivity.this, ApplyForActivity.class);
                                        intent.putExtra("title", onlinegBeen.get(position).getTitle());
                                        intent.putExtra("dtid", onlinegBeen.get(position).getDtid());
                                        intent.putExtra("imgUrl", onlinegBeen.get(position).getGameicon());
                                        intent.putExtra("state", state);
                                        intent.putExtra("one", 1);
                                        startActivity(intent);
                                    }
                                }
                            });
                            in_recycler2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    int state = onlinepBeen.get(position).getState();
                                    if (state == 0) {
                                        Intent intent = new Intent(ApplicationInActivity.this, ApplyForActivity.class);
                                        intent.putExtra("title", onlinepBeen.get(position).getTitle());
                                        intent.putExtra("dtid", onlinepBeen.get(position).getDtid());
                                        intent.putExtra("imgUrl", onlinepBeen.get(position).getGameicon());
                                        intent.putExtra("state", state);
                                        intent.putExtra("one", 2);
                                        startActivity(intent);
                                    }
                                    if (state == -1) {
                                        Intent intent = new Intent(ApplicationInActivity.this, StateActivity.class);
                                        intent.putExtra("states", state);
                                        startActivity(intent);
                                    }
                                    if(state == 1){
                                        Intent intent = new Intent(ApplicationInActivity.this, ApplyForActivity.class);
                                        intent.putExtra("title", onlinepBeen.get(position).getTitle());
                                        intent.putExtra("dtid", onlinepBeen.get(position).getDtid());
                                        intent.putExtra("imgUrl", onlinepBeen.get(position).getGameicon());
                                        intent.putExtra("state", state);
                                        intent.putExtra("one", 2);
                                        startActivity(intent);
                                    }
                                }
                            });
                            in_recycler3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    int state = linegBeen.get(position).getState();
                                    if (state == 0) {
                                        Intent intent = new Intent(ApplicationInActivity.this, ApplyForActivity.class);
                                        intent.putExtra("title", linegBeen.get(position).getTitle());
                                        intent.putExtra("dtid", linegBeen.get(position).getDtid());
                                        intent.putExtra("imgUrl", linegBeen.get(position).getGameicon());
                                        intent.putExtra("state", state);
                                        intent.putExtra("one", 3);
                                        startActivity(intent);
                                    }
                                    if (state == -1) {
                                        Intent intent = new Intent(ApplicationInActivity.this, StateActivity.class);
                                        intent.putExtra("states", state);
                                        startActivity(intent);
                                    }
                                    if(state == 1){
                                        Intent intent = new Intent(ApplicationInActivity.this, ApplyForActivity.class);
                                        intent.putExtra("title", linegBeen.get(position).getTitle());
                                        intent.putExtra("dtid", linegBeen.get(position).getDtid());
                                        intent.putExtra("imgUrl", linegBeen.get(position).getGameicon());
                                        intent.putExtra("state", state);
                                        intent.putExtra("one", 3);
                                        startActivity(intent);
                                    }

                                }
                            });
                        } catch (Exception e) {
                            System.out.println("错误原因：" + e.toString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        onLoginDone();
                    }

                });
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
