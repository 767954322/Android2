package com.tuwan.yuewan.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.tuwan.common.utils.DensityUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.uikit.common.media.picker.loader.PickerImageLoader;
import com.tuwan.yuewan.nim.uikit.common.media.picker.model.AlbumInfo;
import com.tuwan.yuewan.nim.uikit.common.media.picker.model.PhotoInfo;
import com.tuwan.yuewan.nim.uikit.common.media.picker.util.ThumbnailsUtil;
import com.tuwan.yuewan.utils.AppPhotoUtil;
import com.tuwan.yuewan.utils.EmotionKeyboard;

import java.util.ArrayList;
import java.util.List;


public class AppMessagePhotoAdapter extends RecyclerView.Adapter<AppMessagePhotoAdapter.PhotoViewHolder> {


    private Activity mContext;
    private List<PhotoInfo> mList = new ArrayList<>();

    private final Drawable drawableNormal;
    private final Drawable drawableSelected;
    private final AppPhotoUtil mAppPhotoUtil;

    public AppMessagePhotoAdapter(Activity activity,AppPhotoUtil utl) {
        this.mContext = activity;

        drawableNormal = activity.getResources().getDrawable(R.drawable.ic_message_pic_item_normal);
        drawableSelected = activity.getResources().getDrawable(R.drawable.ic_message_pic_item_selected);

        mAppPhotoUtil = utl;
    }

    public void setData(List<AlbumInfo> bean) {
        mList.clear();
        //清除所有数据
        if (bean == null) {
            notifyDataSetChanged();
            return;
        }
        for (AlbumInfo albumInfo : bean) {
            mList.addAll(albumInfo.getList());
        }

        notifyDataSetChanged();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        PhotoInfo photoInfo = mList.get(position);

        if (photoInfo.isChoose()) {
            holder.ivCb.setImageDrawable(drawableSelected);
        } else {
            holder.ivCb.setImageDrawable(drawableNormal);
        }

        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (photoInfo.realWidth == -1) {
            int keyBoardHeightS = EmotionKeyboard.getKeyBoardHeightS();
            //除了recyclerview以外的高度
            int height = keyBoardHeightS - DensityUtils.dp2px(mContext, (40 + 0.5f + 0.5f));
            int width = (int) ((height / (float) photoInfo.heigh) * photoInfo.width);
            photoInfo.realWidth = width;
            layoutParams.width = width;
        } else {
            layoutParams.width = photoInfo.realWidth;
        }

        String thumbPath = ThumbnailsUtil.getThumbnailWithImageID(photoInfo.getImageId(), photoInfo.getFilePath());
        PickerImageLoader.display(thumbPath, photoInfo.getAbsolutePath(), holder.iv, R.drawable.nim_image_default);

        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPhotoSelectClickListener.onPhotoSingleClick(mList, position);
            }
        });

        holder.ivCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoInfo photo = mList.get(position);
                if (photo.isChoose()) {
                    photo.setChoose(false);

                    holder.ivCb.setImageDrawable(drawableNormal);
                } else {
                    if (mAppPhotoUtil.getSelectedList().size() >= mAppPhotoUtil.mutiSelectLimitSize) {
                        Toast.makeText(mContext, String.format(mContext.getResources().getString(  R.string.picker_image_exceed_max_image_select), mAppPhotoUtil.mutiSelectLimitSize), Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        photo.setChoose(true);
                        holder.ivCb.setImageDrawable(drawableSelected);

                    }
                }
                onPhotoSelectClickListener.onPhotoSelectClick(photo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    class PhotoViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv;
        private ImageView ivCb;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv_message_item_pic);
            ivCb = (ImageView) itemView.findViewById(R.id.iv_message_item_pic_cb);
        }
    }

    OnPhotoSelectClickListener onPhotoSelectClickListener;

    public interface OnPhotoSelectClickListener {
        void onPhotoSelectClick(PhotoInfo selectPhoto);

        void onPhotoSingleClick(List<PhotoInfo> photos, int position);
    }

    public void setOnOnPhotoSelectClickListener(OnPhotoSelectClickListener listener) {
        this.onPhotoSelectClickListener = listener;
    }


}
