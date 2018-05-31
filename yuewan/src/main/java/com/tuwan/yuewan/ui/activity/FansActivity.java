package com.tuwan.yuewan.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.FollowAdapter;
import com.tuwan.yuewan.entity.Follow;
import com.tuwan.yuewan.entity.FollowBean;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.utils.Cn2Spell;
import com.tuwan.yuewan.utils.CompareSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;

public class FansActivity extends BaseActivity {

    private TextView tv_im_title;
    private LinearLayout lly_fans;
    private EditText et_follow_search;
    private ImageView iv_titlebar_back;
    private ImageView img_follow_name;
    private TextView tv_follow_fans;
    private TextView tv_default;
    private ListView mListview;
    private FollowAdapter mAdapter;
    private ArrayList<FollowBean> users;
    private ArrayList<FollowBean> searchUsers;
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_follow;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {

        initView();
        initData();
    }

    public static void show(Activity activity) {
        Intent intent = new Intent(activity, FansActivity.class);
        activity.startActivity(intent);
    }

    private void initView() {

        tv_im_title = (TextView) findViewById(R.id.tv_im_title);
        et_follow_search = (EditText) findViewById(R.id.et_follow_search);
        img_follow_name = (ImageView) findViewById(R.id.img_follow_name);
        tv_follow_fans = (TextView) findViewById(R.id.tv_follow_fans);
        tv_default = (TextView) findViewById(R.id.tv_default);
        mListview = (ListView) findViewById(R.id.listview);
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        lly_fans = (LinearLayout) findViewById(R.id.lly_fans);
    }

    private void initData() {
        tv_im_title.setText("粉丝");
        searchUsers = new ArrayList<>();
        //        退出页面
        RxView.clicks(iv_titlebar_back)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
        RxView.clicks(img_follow_name)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        et_follow_search.setText("");
                    }
                });
        et_follow_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

                    String keytag = et_follow_search.getText().toString().trim();

                    if (TextUtils.isEmpty(keytag)) {
                        Toast.makeText(FansActivity.this, "请输入昵称", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    searchUsers.clear();
                    for (int i = 0; i < users.size(); i++) {
                        if (users.get(i).getNickname().toLowerCase().contains(keytag.toLowerCase())){
                            searchUsers.add(users.get(i));
                        }
                    }
                    if (searchUsers.size() > 0){
                        //排序
                        Collections.sort(searchUsers, new CompareSort());

                        //设置数据
                        mAdapter = new FollowAdapter(FansActivity.this);
                        mAdapter.setData(searchUsers);
                        mListview.setAdapter(mAdapter);
                        setListViewHeightBasedOnChildren(mListview);
                    }else {
                        Toast.makeText(FansActivity.this, "该昵称不存在 请重新输入", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }
                return false;
            }
        });

        et_follow_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0){
                    img_follow_name.setVisibility(View.VISIBLE);
                }else {
                    img_follow_name.setVisibility(View.GONE);
                    //排序
                    Collections.sort(users, new CompareSort());
                    //设置数据
                    mAdapter = new FollowAdapter(FansActivity.this);
                    mAdapter.setData(users);
                    mListview.setAdapter(mAdapter);
                    setListViewHeightBasedOnChildren(mListview);
                }
            }
        });
        DialogMaker.showProgressDialog(FansActivity.this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .fansList_attention("json", 1, "1000")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<Follow>(){
                    @Override
                    public void onNext(Follow follow) {
                        super.onNext(follow);
                        Log.d("follow",follow.page + "");
                        users = new ArrayList<>();
                        for (int i = 0; i < follow.data.size(); i++) {
                            FollowBean user = new FollowBean();
                            user.setNickname(follow.data.get(i).getNickname());
                            user.setAge(follow.data.get(i).getAge());
                            user.setAvatar(follow.data.get(i).getAvatar());
                            user.setCity(follow.data.get(i).getCity());
                            user.setInitial(follow.data.get(i).getInitial());
                            user.setOnline(follow.data.get(i).getOnline());
                            user.setUid(follow.data.get(i).getUid());
                            user.setSex(follow.data.get(i).getSex());
                            user.setVip(follow.data.get(i).getVip());
                            user.setIcons(follow.data.get(i).getIcons());

//                            String firstSpell = ChineseToEnglish.getFirstSpell(follow.data.get(i).getNickname());
//                            String substring = firstSpell.substring(0, 1).toUpperCase();

                            String substring = Cn2Spell.getPinYinFirstLetter(follow.data.get(i).getNickname()).toUpperCase();
                            if(substring.matches("[A-Z]")){
                                user.setLetter(substring);
                            }else {
                                user.setLetter("#");
                            }
                            users.add(user);
                        }

                        //排序
                        Collections.sort(users, new CompareSort());

                        //设置数据
                        mAdapter = new FollowAdapter(FansActivity.this);
                        mAdapter.setData(users);
                        mListview.setAdapter(mAdapter);
                        setListViewHeightBasedOnChildren(mListview);
                        onLoginDone();
                        if (users.size() < 1){
                            tv_default.setVisibility(View.VISIBLE);
                        }else {
                            tv_default.setVisibility(View.GONE);
                        }
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
