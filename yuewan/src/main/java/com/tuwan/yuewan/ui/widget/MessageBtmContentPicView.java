package com.tuwan.yuewan.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.common.utils.LogUtil;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.AppMessagePhotoAdapter;
import com.tuwan.yuewan.nim.uikit.common.media.picker.activity.PickerAlbumPreviewActivity;
import com.tuwan.yuewan.nim.uikit.common.media.picker.model.PhotoInfo;
import com.tuwan.yuewan.nim.uikit.common.media.picker.util.PickerUtil;
import com.tuwan.yuewan.utils.AppPhotoUtil;
import com.tuwan.yuewan.utils.AppUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by zhangjie on 2017/10/30.
 */
public class MessageBtmContentPicView extends LinearLayout implements AppMessagePhotoAdapter.OnPhotoSelectClickListener {

    private final Activity mActivity;
    private RecyclerView mRecyclerView;
    private TextView mTvOriginalImg;//每发送一次，及每次MessageBtmContentPicView被收起，那么mRbOriginalImg的选中状态都要去掉
    public TextView mTvSendImg;

    public View mCamare;//拍摄
    public View mPic;//相册

    private AppPhotoUtil mAppPhotoUtil;
    private AppMessagePhotoAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    public MessageBtmContentPicView(Activity activity,AppPhotoUtil util) {
        super(activity);
        this.mActivity = activity;
        this.mAppPhotoUtil = util;
        init(activity);

        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new AppMessagePhotoAdapter(mActivity,mAppPhotoUtil);
        mAdapter.setOnOnPhotoSelectClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        View.inflate(context, R.layout.widget_message_pic, this);
        setBackgroundColor(0xffffffff);

        assignViews();

        mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mRecyclerView.setNestedScrollingEnabled(false);

        final ViewTreeObserver vto = mCamare.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mCamare.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                ViewGroup.LayoutParams layoutParams = mCamare.getLayoutParams();
                layoutParams.width = mCamare.getMeasuredHeight();

                ViewGroup.LayoutParams layoutParams2 = mPic.getLayoutParams();
                layoutParams2.width = mCamare.getMeasuredHeight();
            }
        });

        RxView.clicks(mTvOriginalImg)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mAppPhotoUtil.isOrig = !mAppPhotoUtil.isOrig;
                        updateOrigTv();
                    }
                });
    }



    private void cleanOrig(){
        mAppPhotoUtil.isOrig = false;
        updateOrigTv();
    }

    private void updateOrigTv() {
        if(mAppPhotoUtil.isOrig){
            long totalSize = 0;
            for (int i = 0; i < mAppPhotoUtil.getSelectedList().size(); i++) {
                PhotoInfo pi = mAppPhotoUtil.getSelectedList().get(i);
                totalSize += pi.getSize();
            }
            mTvOriginalImg.setText(String.format(mActivity.getResources().getString(R.string.picker_image_preview_original_select), PickerUtil.getFileSizeString(totalSize)));
            AppUtils.setDrawableLeft(mTvOriginalImg,getResources().getDrawable(R.drawable.ic_message_pic_btm_selected));

        }else{
            AppUtils.setDrawableLeft(mTvOriginalImg,getResources().getDrawable(R.drawable.ic_message_pic_btm_normal));
            mTvOriginalImg.setText("原图");
        }
    }

    private void assignViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mTvOriginalImg = (TextView) findViewById(R.id.rb_original_img);
        mTvSendImg = (TextView) findViewById(R.id.tv_send_img);
        mCamare = findViewById(R.id.fl_message_item_camera);
        mPic = findViewById(R.id.fl_message_item_pic);
    }


    public void show() {
        mAdapter.setData(mAppPhotoUtil.getList());
    }

    public void hiden() {
        mAdapter.setData(null);
    }
    public void onDestory(){
        mAppPhotoUtil = null;
    }

    @Override
    public void onPhotoSelectClick(PhotoInfo selectPhoto) {
        if(selectPhoto.isChoose()){
            mAppPhotoUtil.selectedPhoto(selectPhoto);
        }else{
            mAppPhotoUtil.removePhoto(selectPhoto);
        }
        // update bottom bar
        updateBottomBar();

        updateOrigTv();
    }

    private void updateBottomBar() {
        if(mAppPhotoUtil.getSelectedList().size()>0){
            mTvSendImg.setEnabled(true);
            mTvSendImg.setBackgroundResource(R.drawable.shape_bg_button_reply);
            mTvSendImg.setTextColor(0xff333333);
        }else{
            onSelectedEmpty();
            cleanOrig();
        }
    }

    private void onSelectedEmpty() {
        mTvSendImg.setEnabled(false);
        mTvSendImg.setBackgroundResource(R.drawable.shape_button_send_unenable);
        mTvSendImg.setTextColor(0xffffffff);
    }


    @Override
    public void onPhotoSingleClick(List<PhotoInfo> photos, int position) {
        PickerAlbumPreviewActivity.start(mActivity, position,-1);
    }

    public void cleanSelected(){
        //范围刷新
        mAdapter.notifyItemRangeChanged(mLinearLayoutManager.findFirstVisibleItemPosition(),mLinearLayoutManager.findLastVisibleItemPosition());

        onSelectedEmpty();
        cleanOrig();
    }

    public void onResultUpdate(){
        int start = Math.max(0,mLinearLayoutManager.findFirstVisibleItemPosition()-1);
        int end = Math.min(mAdapter.getItemCount()-1,mLinearLayoutManager.findLastVisibleItemPosition()+1);
        LogUtil.e("start:"+start+"  end:"+end);
//        //范围刷新
        mAdapter.notifyItemRangeChanged(start,end);
//        mAdapter.notifyDataSetChanged();

        updateBottomBar();
        updateOrigTv();
    }


}
