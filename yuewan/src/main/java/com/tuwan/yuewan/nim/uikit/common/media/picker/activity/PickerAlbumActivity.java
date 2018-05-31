package com.tuwan.yuewan.nim.uikit.common.media.picker.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.uikit.common.media.picker.fragment.PickerAlbumFragment;
import com.tuwan.yuewan.nim.uikit.common.media.picker.fragment.PickerAlbumFragment.OnAlbumItemClickListener;
import com.tuwan.yuewan.nim.uikit.common.media.picker.fragment.PickerImageFragment;
import com.tuwan.yuewan.nim.uikit.common.media.picker.fragment.PickerImageFragment.OnPhotoSelectClickListener;
import com.tuwan.yuewan.nim.uikit.common.media.picker.loader.PickerImageLoader;
import com.tuwan.yuewan.nim.uikit.common.media.picker.model.AlbumInfo;
import com.tuwan.yuewan.nim.uikit.common.media.picker.model.PhotoInfo;
import com.tuwan.yuewan.nim.uikit.common.media.picker.model.PickerContract;
import com.tuwan.yuewan.nim.uikit.session.constant.Extras;
import com.tuwan.yuewan.utils.AppPhotoUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Inner image picker, no longer use third-part application
 */
public class PickerAlbumActivity extends BaseActivity implements OnAlbumItemClickListener, OnPhotoSelectClickListener, OnClickListener {

    private FrameLayout pickerAlbumLayout;
    private FrameLayout pickerPhotosLayout;

    private PickerAlbumFragment photoFolderFragment;
    private PickerImageFragment photoFragment;

    private View pickerBottomBar;

    private TextView pickerPreview;
    private TextView pickerSend;

    private boolean isSendOriginalImage;
    private boolean isAlbumPage;

    private AppPhotoUtil mAppPhotoUtil;
    private TextView mTitle;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_picker_album_activity;
    }


    @Override
    protected void customInit(Bundle savedInstanceState) {
        mAppPhotoUtil = AppPhotoUtil.getInstance();

        initUI();

        updateSelectBtnStatus();
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));

        mTitle.setText(getResources().getString(R.string.picker_image_folder));

        RxView.clicks(findViewById(R.id.iv_titlebar_back))
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        onBackPressed();
                    }
                });
    }


    private void initUI() {
        mTitle = (TextView) findViewById(R.id.tv_titlebar_title);

        // bottom bar
        pickerBottomBar = findViewById(R.id.picker_bottombar);
        pickerBottomBar.setVisibility(View.VISIBLE);
        pickerPreview = (TextView) findViewById(R.id.picker_bottombar_preview);
        pickerPreview.setOnClickListener(this);
        pickerSend = (TextView) findViewById(R.id.picker_bottombar_select);
        pickerSend.setOnClickListener(this);

        // fragment
        pickerAlbumLayout = (FrameLayout) findViewById(R.id.picker_album_fragment);
        pickerPhotosLayout = (FrameLayout) findViewById(R.id.picker_photos_fragment);
        photoFolderFragment = new PickerAlbumFragment();
        switchContent(photoFolderFragment);

        isAlbumPage = true;
    }

    @Override
    public void OnAlbumItemClick(AlbumInfo info) {
        // check photo list if has already be choose
        List<PhotoInfo> photoList = info.getList();
        if (photoList == null) {
            return;
        }

        // switch to photo fragment
        pickerAlbumLayout.setVisibility(View.GONE);
        pickerPhotosLayout.setVisibility(View.VISIBLE);
        if (photoFragment == null) {
            photoFragment = new PickerImageFragment();
            photoFragment.setArguments(makeDataBundle(photoList, true, mAppPhotoUtil.mutiSelectLimitSize));
            switchContent(photoFragment);
        } else {
            photoFragment.resetFragment(photoList);
        }
        // update title
        mTitle.setText(info.getAlbumName());
        isAlbumPage = false;
    }

    public Bundle makeDataBundle(List<PhotoInfo> photos, boolean mutiMode, int mutiSelectLimitSize) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Extras.EXTRA_PHOTO_LISTS, new ArrayList<PhotoInfo>(photos));
        bundle.putBoolean(Extras.EXTRA_MUTI_SELECT_MODE, mutiMode);
        bundle.putInt(Extras.EXTRA_MUTI_SELECT_SIZE_LIMIT, mutiSelectLimitSize);

        return bundle;
    }

    @Override
    public void onPhotoSingleClick(List<PhotoInfo> photos, int position) {
        int albmPosition = -1;
        for (AlbumInfo albumInfo : mAppPhotoUtil.getList()) {
            if (albumInfo.getList() == photos) {
                albmPosition = mAppPhotoUtil.getList().indexOf(albumInfo);
                break;
            }
        }

        PickerAlbumPreviewActivity.start(this, position, albmPosition);
    }

    @Override
    public void onPhotoSelectClick(PhotoInfo selectPhoto) {
        // check
        boolean isChoose = selectPhoto.isChoose();
        if (isChoose) {
            mAppPhotoUtil.selectedPhoto(selectPhoto);
        } else {
            mAppPhotoUtil.removePhoto(selectPhoto);
        }
        // update bottom bar
        updateSelectBtnStatus();
    }


    private void updateSelectBtnStatus() {
        int selectSize = mAppPhotoUtil.getSelectedList().size();
        if (selectSize > 0) {
//            pickerPreview.setEnabled(true);
//            pickerPreview.setTextColor(0xff666666);

            pickerPreview.setVisibility(View.VISIBLE);

            pickerSend.setEnabled(true);
            pickerSend.setText(String.format(this.getResources().getString(R.string.picker_image_send_select), selectSize));
            pickerSend.setBackgroundResource(R.drawable.shape_bg_button_reply);
        } else {
//            pickerPreview.setEnabled(false);
//            pickerPreview.setTextColor(0xff999999);

            pickerPreview.setVisibility(View.GONE);

            pickerSend.setEnabled(false);
            pickerSend.setText(R.string.picker_image_send);
            pickerSend.setBackgroundResource(R.drawable.shape_button_send_unenable);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.picker_bottombar_preview) {
            PickerAlbumPreviewActivity.start(this, 0, -2);
        } else if (v.getId() == R.id.picker_bottombar_select) {
            setResult(RESULT_OK, PickerContract.makeDataIntent(mAppPhotoUtil.getSelectedList(), isSendOriginalImage));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (isAlbumPage) {
            finish();
        } else {
            backToAlbumPage();
        }
    }

    private void backToAlbumPage() {
        mTitle.setText(R.string.picker_image_folder);
        isAlbumPage = true;
        pickerAlbumLayout.setVisibility(View.VISIBLE);
        pickerPhotosLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppPhotoUtil.PICKER_IMAGE_PREVIEW) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }else{
                updateSelectBtnStatus();
                photoFragment.updateGridview();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        PickerImageLoader.initCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PickerImageLoader.clearCache();

        mAppPhotoUtil.cleanSelected();
    }
}
