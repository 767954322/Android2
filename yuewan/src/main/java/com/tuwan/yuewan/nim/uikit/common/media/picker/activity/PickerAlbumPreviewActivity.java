package com.tuwan.yuewan.nim.uikit.common.media.picker.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.uikit.common.media.picker.adapter.PickerPreviewPagerAdapter;
import com.tuwan.yuewan.nim.uikit.common.media.picker.model.PhotoInfo;
import com.tuwan.yuewan.nim.uikit.common.media.picker.util.PickerUtil;
import com.tuwan.yuewan.nim.uikit.common.ui.imageview.BaseZoomableImageView;
import com.tuwan.yuewan.nim.uikit.common.util.media.BitmapDecoder;
import com.tuwan.yuewan.nim.uikit.common.util.media.ImageUtil;
import com.tuwan.yuewan.nim.uikit.session.constant.Extras;
import com.tuwan.yuewan.utils.AppPhotoUtil;

import java.util.List;


/**
 * 图片viewpager展示的页面
 * 有三种展示情况，通过EXTRA_SHOW_ALL_PHOTO数据来区分
 * EXTRA_SHOW_ALL_PHOTO==-1时表示展示所有的图片
 * EXTRA_SHOW_ALL_PHOTO==-2时表示展示所有选中的图片
 * EXTRA_SHOW_ALL_PHOTO为其他值时表示展示对应文件夹下的图片
 *
 * @author zhangjie
 */
public class PickerAlbumPreviewActivity extends BaseActivity implements OnClickListener, OnPageChangeListener {

    private TextView mTitle;
    private AppPhotoUtil mAppPhotoUtil;
    private List<PhotoInfo> photoLists;


    public static void start(Activity activity, int position,int albmPosition) {
        Intent intent = new Intent(activity, PickerAlbumPreviewActivity.class);
        intent.putExtra(Extras.EXTRA_PREVIEW_CURRENT_POS, position);
        intent.putExtra(Extras.EXTRA_SHOW_ALL_PHOTO, albmPosition);
        activity.startActivityForResult(intent, AppPhotoUtil.PICKER_IMAGE_PREVIEW);
    }

    private ViewPager imageViewPager;

    private PickerPreviewPagerAdapter imageViewPagerAdapter;
    private int firstDisplayImageIndex = 0;
    private int mAlbmPosition ;

    private int currentPosition = -1;
    private boolean isSupportOriginal = true;

    private BaseZoomableImageView currentImageView;

    private int tempIndex = -1;

    @SuppressWarnings("unused")
    private LinearLayout previewOperationBar;

    private ImageButton originalImage;

    private TextView originalImageSizeTip;
    private TextView previewSendBtn;
    private ImageView previewSelectBtn;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_picker_image_preview_activity;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        mAppPhotoUtil = AppPhotoUtil.getInstance();
        firstDisplayImageIndex = getIntent().getIntExtra(Extras.EXTRA_PREVIEW_CURRENT_POS, 0);
        mAlbmPosition = getIntent().getIntExtra(Extras.EXTRA_SHOW_ALL_PHOTO, -1);

        initUI();
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();

        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));

        findViewById(R.id.iv_titlebar_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initUI() {
        if(mAlbmPosition==-1){
            photoLists = mAppPhotoUtil.getPhotoInfoList();
        }else if(mAlbmPosition==-2) {
            photoLists = mAppPhotoUtil.getSelectedList();
        }else{
            photoLists = mAppPhotoUtil.getList().get(mAlbmPosition).getList();
        }

        mTitle = (TextView) findViewById(R.id.tv_titlebar_title);

        previewSelectBtn = (ImageView) findViewById(R.id.iv_titlebar_more);
        previewSelectBtn.setOnClickListener(this);

        previewOperationBar = (LinearLayout) findViewById(R.id.picker_image_preview_operator_bar);
        originalImage = (ImageButton) findViewById(R.id.picker_image_preview_orignal_image);
        originalImageSizeTip = (TextView) findViewById(R.id.picker_image_preview_orignal_image_tip);
        originalImage.setOnClickListener(this);
        originalImageSizeTip.setOnClickListener(this);

        if (!isSupportOriginal) {
            originalImage.setVisibility(View.GONE);
            originalImageSizeTip.setVisibility(View.GONE);
        }
        previewSendBtn = (TextView) findViewById(R.id.picker_image_preview_send);
        previewSendBtn.setOnClickListener(this);
        updateSelectBtnStatus();
        updateOriImageSizeTip();

        imageViewPager = (ViewPager) findViewById(R.id.picker_image_preview_viewpager);
        imageViewPager.setOnPageChangeListener(this);
        imageViewPager.setOffscreenPageLimit(2);
        imageViewPagerAdapter = new PickerPreviewPagerAdapter(this, photoLists, getLayoutInflater(), imageViewPager.getLayoutParams().width, imageViewPager.getLayoutParams().height, this);
        imageViewPager.setAdapter(imageViewPagerAdapter);

        setTitleIndex(firstDisplayImageIndex);
        updateTitleSelect(firstDisplayImageIndex);
        imageViewPager.setCurrentItem(firstDisplayImageIndex);

    }


    private void updateTitleSelect(int index) {
        if (photoLists == null || index >= photoLists.size()) {
            return;
        }
        PhotoInfo photo = photoLists.get(index);
        if (photo.isChoose()) {
            previewSelectBtn.setImageResource(R.drawable.ic_message_pic_item_selected);
        } else {
            previewSelectBtn.setImageResource(R.drawable.ic_message_pic_item_normal);
        }
    }

    private void setTitleIndex(int index) {
        if (photoLists.size() <= 0) {
            mTitle.setText("");
        } else {
            index++;
            mTitle.setText(index + "/" + photoLists.size());
        }
    }

    public void updateCurrentImageView(final int position) {
        if (photoLists == null || (position > 0 && position >= photoLists.size())) {
            return;
        }

        if (currentPosition == position) {
            return;
        } else {
            currentPosition = position;
        }

        LinearLayout currentLayout = (LinearLayout) imageViewPager.findViewWithTag(position);
        if (currentLayout == null) {
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateCurrentImageView(position);
                }
            }, 300);
            return;
        }
        currentImageView = (BaseZoomableImageView) currentLayout.findViewById(R.id.imageView);
        currentImageView.setViewPager(imageViewPager);

        setImageView(photoLists.get(position));
    }

    public void setImageView(PhotoInfo info) {
        if (info == null) {
            return;
        }

        if (info.getAbsolutePath() == null) {
            return;
        }

        Bitmap bitmap = BitmapDecoder.decodeSampledForDisplay(info.getAbsolutePath());
        if (bitmap == null) {
            currentImageView.setImageBitmap(ImageUtil.getDefaultBitmapWhenGetFail());
            Toast.makeText(this, R.string.picker_image_error, Toast.LENGTH_LONG).show();
        } else {
            try {
                bitmap = ImageUtil.rotateBitmapInNeeded(info.getAbsolutePath(), bitmap);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            currentImageView.setImageBitmap(bitmap);
        }
    }

    private void restoreList() {
        if (tempIndex != -1) {
            imageViewPager.setAdapter(imageViewPagerAdapter);
            setTitleIndex(tempIndex);
            imageViewPager.setCurrentItem(tempIndex);
            tempIndex = -1;
        }
    }

    private void updateSelectBtnStatus() {
        int selectSize = mAppPhotoUtil.getSelectedList().size();
        if (selectSize > 0) {
            previewSendBtn.setEnabled(true);
            previewSendBtn.setBackgroundResource(R.drawable.shape_bg_button_reply);
            previewSendBtn.setText(String.format(this.getResources().getString(R.string.picker_image_send_select), selectSize));
        } else {
            previewSendBtn.setEnabled(false);
            previewSendBtn.setBackgroundResource(R.drawable.shape_button_send_unenable);
            previewSendBtn.setText(R.string.picker_image_send);
        }
    }

    private void updatePreviewSelectBtnStatus(PhotoInfo info) {
        if (info.isChoose()) {
            previewSelectBtn.setImageResource(R.drawable.ic_message_pic_item_selected);
        } else {
            previewSelectBtn.setImageResource(R.drawable.ic_message_pic_item_normal);
        }
    }

    private void updateOriImageSizeTip() {
        if (mAppPhotoUtil.isOrig) {
            long totalSize = 0;
            for (int i = 0; i < mAppPhotoUtil.getSelectedList().size(); i++) {
                PhotoInfo pi = mAppPhotoUtil.getSelectedList().get(i);
                totalSize += pi.getSize();
            }
            originalImageSizeTip.setText(String.format(this.getResources().getString(R.string.picker_image_preview_original_select), PickerUtil.getFileSizeString(totalSize)));
            originalImage.setImageResource(R.drawable.ic_message_pic_btm_selected);
        } else {
            originalImageSizeTip.setText("原图");
            originalImage.setImageResource(R.drawable.ic_message_pic_btm_normal);
        }
    }


    @Override
    public void onResume() {
        // restore the data source
        restoreList();

        super.onResume();
    }

    @Override
    public void onPause() {
        // save the data source and recycle all bitmaps
        imageViewPager.setAdapter(null);
        tempIndex = currentPosition;
        currentPosition = -1;

        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_titlebar_more) {

            PhotoInfo photoInfo = photoLists.get(currentPosition);

            boolean choose = photoInfo.isChoose();
            if (choose) {
                mAppPhotoUtil.removePhoto(photoInfo);
            } else {
                if (!mAppPhotoUtil.selectedPhoto(photoInfo)) {
                    Toast.makeText(this, String.format(getResources().getString(R.string.picker_image_exceed_max_image_select), mAppPhotoUtil.mutiSelectLimitSize), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            updatePreviewSelectBtnStatus(photoInfo);
            updateSelectBtnStatus();

            // 如果一个都没有选择，则自动取消原图发送
            if (mAppPhotoUtil.getSelectedList().size() == 0 && mAppPhotoUtil.isOrig) {
                mAppPhotoUtil.isOrig = false;
            }
            updateOriImageSizeTip();

        } else if (v.getId() == R.id.picker_image_preview_send) {
            if (mAppPhotoUtil.getSelectedList() != null && mAppPhotoUtil.getSelectedList().size() == 0) { // 没有选择，点击发送则发送当前图片
                PhotoInfo current = photoLists.get(currentPosition);
                mAppPhotoUtil.selectedPhoto(current);
            }
            setResult(RESULT_OK);
            finish();

        } else if (v.getId() == R.id.picker_image_preview_orignal_image||v.getId() == R.id.picker_image_preview_orignal_image_tip) {
            mAppPhotoUtil.isOrig = !mAppPhotoUtil.isOrig;
            updateOriImageSizeTip();
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
        setTitleIndex(arg0);
        updateTitleSelect(arg0);
    }
}
