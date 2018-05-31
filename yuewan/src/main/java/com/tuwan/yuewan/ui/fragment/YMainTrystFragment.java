package com.tuwan.yuewan.ui.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.dialog.picker.DataPickerDialog;
import com.google.gson.Gson;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.AppFragmentPageAdapter;
import com.tuwan.yuewan.adapter.MainNewGridAdapter;
import com.tuwan.yuewan.adapter.MainTrystGridAdapter;
import com.tuwan.yuewan.entity.Dan;
import com.tuwan.yuewan.entity.DatingOrder;
import com.tuwan.yuewan.entity.NewActivity;
import com.tuwan.yuewan.entity.NewAppIndex;
import com.tuwan.yuewan.entity.TrystRemarks;
import com.tuwan.yuewan.entity.TrystServices;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.activity.MainRankingListActivity;
import com.tuwan.yuewan.ui.activity.SearchActivity;
import com.tuwan.yuewan.ui.activity.TrystActivity;
import com.tuwan.yuewan.view.GridViewForScrollView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 首页
 */
public class YMainTrystFragment extends BaseFragment implements View.OnClickListener{

    private LinearLayout lly_tryst_service;
    private TextView tv_tryst_service;
    private TextView tv_tryst_unit;
    private TextView tv_tryst_sex_all, tv_tryst_sex_grid, tv_tryst_sex_boy;
    private LinearLayout lly_tryst_reduce, lly_tryst_add, lly_tryst_remarks, lly_tryst_grading;
    private Button bt_tryst_submit;
    private TextView tv_tryst_number;
    private View v_tryst_grading;
    private GridViewForScrollView gv_remarks;
    private GridViewForScrollView gv_grading;
    private TrystServices trystServices = new TrystServices();
    private int dtid = 0;
    private String serviceName = "";
    private int sex = 0;
    private int number = 1;
    private List<TrystRemarks> remarksList = new ArrayList<TrystRemarks>();
    private List<TrystRemarks> gradingList = new ArrayList<TrystRemarks>();
    private String remarkid = "";
    private int gradingid = 0;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_main_tryst;
    }
    @Override
    protected void setUpView() {
//        mainToobar = (RelativeLayout) getContentView().findViewById(R.id.main_toobar);
        lly_tryst_service = (LinearLayout) getContentView().findViewById(R.id.lly_tryst_service);
        tv_tryst_service = (TextView) getContentView().findViewById(R.id.tv_tryst_service);
        tv_tryst_unit = (TextView) getContentView().findViewById(R.id.tv_tryst_unit);
        tv_tryst_sex_all = (TextView) getContentView().findViewById(R.id.tv_tryst_sex_all);
        tv_tryst_sex_grid = (TextView) getContentView().findViewById(R.id.tv_tryst_sex_grid);
        tv_tryst_sex_boy = (TextView) getContentView().findViewById(R.id.tv_tryst_sex_boy);
        lly_tryst_reduce = (LinearLayout) getContentView().findViewById(R.id.lly_tryst_reduce);
        lly_tryst_add = (LinearLayout) getContentView().findViewById(R.id.lly_tryst_add);
        lly_tryst_remarks = (LinearLayout) getContentView().findViewById(R.id.lly_tryst_remarks);
        lly_tryst_grading = (LinearLayout) getContentView().findViewById(R.id.lly_tryst_grading);
        tv_tryst_number = (TextView) getContentView().findViewById(R.id.tv_tryst_number);
        gv_remarks = (GridViewForScrollView) getContentView().findViewById(R.id.gv_remarks);
        gv_grading = (GridViewForScrollView) getContentView().findViewById(R.id.gv_grading);
        v_tryst_grading = getContentView().findViewById(R.id.v_tryst_grading);
        bt_tryst_submit = (Button) getContentView().findViewById(R.id.bt_tryst_submit);
        lly_tryst_service.setOnClickListener(this);
        tv_tryst_sex_all.setOnClickListener(this);
        tv_tryst_sex_grid.setOnClickListener(this);
        tv_tryst_sex_boy.setOnClickListener(this);
        lly_tryst_reduce.setOnClickListener(this);
        lly_tryst_add.setOnClickListener(this);
        bt_tryst_submit.setOnClickListener(this);
        setData();
//        setRemarks();
    }


    @Override
    protected void setUpData() {
//        mContentFragment = new YMainContentNewFragment();
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, mContentFragment).commitAllowingStateLoss();
    }

    private void showGrading(List<TrystRemarks> list){
        gradingList.clear();
        gradingList.addAll(list);
        for (int i = 0; i < gradingList.size(); i++) {
            if (gradingList.get(i).getName().trim().equals("全部")){
                gradingList.get(i).setType(true);
            }else {
                gradingList.get(i).setType(false);
            }
        }
        final MainTrystGridAdapter gradingAdapter = new MainTrystGridAdapter(getActivity());
        gradingAdapter.setData(gradingList,0);
        gv_grading.setAdapter(gradingAdapter);
        gv_grading.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < gradingList.size(); i++) {
                    if (i == position){
                        gradingList.get(i).setType(true);
                    }else {
                        gradingList.get(i).setType(false);
                    }
                }
                gradingAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showRemarks(List<TrystRemarks> list){
        remarksList.clear();
        remarksList.addAll(list);
        for (int i = 0; i < remarksList.size(); i++) {
            remarksList.get(i).setType(false);
        }
        final MainTrystGridAdapter mainNewGridAdapter = new MainTrystGridAdapter(getActivity());
        mainNewGridAdapter.setData(remarksList,0);
        gv_remarks.setAdapter(mainNewGridAdapter);
        gv_remarks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (remarksList.get(position).isType()) {
                    remarksList.get(position).setType(false);
                }else {
                    remarksList.get(position).setType(true);
                }
                mainNewGridAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.lly_tryst_service) {
            showDialog();
        }else if (i == R.id.tv_tryst_sex_all){
            initSexView();
            tv_tryst_sex_all.setBackgroundResource(R.drawable.text_biankuang_2);
            tv_tryst_sex_all.setTextColor(Color.rgb(255, 198, 2));
            sex = 0;
        }else if (i == R.id.tv_tryst_sex_grid){
            initSexView();
            tv_tryst_sex_grid.setBackgroundResource(R.drawable.text_biankuang_2);
            tv_tryst_sex_grid.setTextColor(Color.rgb(255, 198, 2));
            sex = 2;
        }else if (i == R.id.tv_tryst_sex_boy){
            initSexView();
            tv_tryst_sex_boy.setBackgroundResource(R.drawable.text_biankuang_2);
            tv_tryst_sex_boy.setTextColor(Color.rgb(255, 198, 2));
            sex = 1;
        }else if(i == R.id.lly_tryst_reduce){
            if (number > 1){
                number = number - 1;
                tv_tryst_number.setText("" + number);
            }
        }else if (i == R.id.lly_tryst_add){
            number = number + 1;
            tv_tryst_number.setText("" + number);
        }else if(i == R.id.bt_tryst_submit) {
            if (dtid != 0) {
                remarkid = "";
                gradingid = 0;
                for (int j = 0; j < remarksList.size(); j++) {
                    if (remarksList.get(j).isType()) {
                        remarkid = remarkid + "," + remarksList.get(j).getId();
                    }
                }
                for (int j = 0; j < gradingList.size(); j++) {
                    if (gradingList.get(j).isType()) {
                        gradingid = gradingList.get(j).getId();
                    }
                }
                DialogMaker.showProgressDialog(getActivity(), null, "", true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                }).setCanceledOnTouchOutside(false);
                if (gradingList.size() > 0) {
                    ServiceFactory.getShortCacheInstance()
                            .createService(YService.class)
                            .mDatingOrder("json", dtid, sex, number, remarkid, gradingid)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new CommonObserver<DatingOrder>() {
                                @Override
                                public void onNext(@NonNull final DatingOrder result) {
                                    super.onNext(result);
                                    onLoginDone();
                                    if (result.getError() == 0) {
                                        Intent intent = new Intent();
                                        intent.putExtra("id", result.getData().getId());
                                        intent.putExtra("dtId", dtid);
                                        intent.putExtra("number", number);
                                        intent.setClass(getActivity(), TrystActivity.class);
                                        startActivity(intent);
                                    } else {
                                        ToastUtils.getInstance().showToast(result.getError_msg());
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    super.onError(e);
                                    onLoginDone();
                                }
                            });
                }else {
                    ServiceFactory.getShortCacheInstance()
                            .createService(YService.class)
                            .mDatingOrders("json", dtid, sex, number, remarkid)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new CommonObserver<DatingOrder>() {
                                @Override
                                public void onNext(@NonNull final DatingOrder result) {
                                    super.onNext(result);
                                    onLoginDone();
                                    if (result.getError() == 0) {
                                        Intent intent = new Intent();
                                        intent.putExtra("id", result.getData().getId());
                                        intent.putExtra("dtId", dtid);
                                        intent.putExtra("number", number);
                                        intent.setClass(getActivity(), TrystActivity.class);
                                        startActivity(intent);
                                    } else {
                                        ToastUtils.getInstance().showToast(result.getError_msg());
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    super.onError(e);
                                    onLoginDone();
                                }
                            });
                }
            }else {
                ToastUtils.getInstance().showToast("请选择服务");
            }
        }
    }

    private void setData(){
        DialogMaker.showProgressDialog(getActivity(), null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        ServiceFactory.getShortCacheInstance()
                .createService(YService.class)
                .mServices("json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<TrystServices>() {
                    @Override
                    public void onNext(@NonNull TrystServices result) {
                        super.onNext(result);
                        onLoginDone();
                        trystServices = result;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
//                        showError(e);
                        onLoginDone();
                    }
                });
    }

    private final void showDialog() {
        final DataPickerDialog.Builder builder = new DataPickerDialog.Builder(getActivity());
        final List<String> data = new ArrayList<String>();
        for (int i = 0; i < trystServices.getData().size(); i++) {
            data.add(trystServices.getData().get(i).getName());
        }
        DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(0).setTitle("选择服务")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        serviceName = itemValue;
                        for (int i = 0; i < trystServices.getData().size(); i++) {
                            if (serviceName.equals(trystServices.getData().get(i).getName())){
                                dtid = trystServices.getData().get(i).getDtid();
                                tv_tryst_unit.setText("(" + trystServices.getData().get(i).getUnit() + ")");
                                if (trystServices.getData().get(i).getRemarks().size() > 0){
                                    lly_tryst_remarks.setVisibility(View.VISIBLE);
                                    showRemarks(trystServices.getData().get(i).getRemarks());
                                }else {
                                    lly_tryst_remarks.setVisibility(View.GONE);
                                }
                                if (trystServices.getData().get(i).getGrading().size() > 0){
                                    lly_tryst_grading.setVisibility(View.VISIBLE);
                                    v_tryst_grading.setVisibility(View.VISIBLE);
                                    showGrading(trystServices.getData().get(i).getGrading());
                                }else {
                                    lly_tryst_grading.setVisibility(View.GONE);
                                    v_tryst_grading.setVisibility(View.GONE);
                                }
                            }
                        }
                        tv_tryst_service.setText(itemValue);
                    }
                }).create(0);

        dialog.show();
    }

    private void initSexView(){
        tv_tryst_sex_all.setBackgroundResource(R.drawable.text_biankuang);
        tv_tryst_sex_grid.setBackgroundResource(R.drawable.text_biankuang);
        tv_tryst_sex_boy.setBackgroundResource(R.drawable.text_biankuang);
        tv_tryst_sex_all.setTextColor(Color.rgb(102, 102, 102));
        tv_tryst_sex_grid.setTextColor(Color.rgb(102, 102, 102));
        tv_tryst_sex_boy.setTextColor(Color.rgb(102, 102, 102));
    }

    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }
}