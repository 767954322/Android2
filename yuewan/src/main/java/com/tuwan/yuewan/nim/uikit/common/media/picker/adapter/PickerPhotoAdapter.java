package com.tuwan.yuewan.nim.uikit.common.media.picker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.uikit.common.media.picker.fragment.PickerImageFragment.OnPhotoSelectClickListener;
import com.tuwan.yuewan.nim.uikit.common.media.picker.loader.PickerImageLoader;
import com.tuwan.yuewan.nim.uikit.common.media.picker.model.PhotoInfo;
import com.tuwan.yuewan.nim.uikit.common.media.picker.util.ThumbnailsUtil;
import com.tuwan.yuewan.nim.uikit.common.util.sys.ScreenUtil;
import com.tuwan.yuewan.utils.AppPhotoUtil;

import java.util.List;

public class PickerPhotoAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<PhotoInfo> list;
    private GridView gridView;
    private int width = ScreenUtil.screenWidth / 4;


    private OnPhotoSelectClickListener onPhotoSelectClickListener;
    private final AppPhotoUtil mAppPhotoUtil;

    public PickerPhotoAdapter(Context context, List<PhotoInfo> list, GridView gridView, AppPhotoUtil appPhotoUtil) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.list = list;
        this.gridView = gridView;

        if (onPhotoSelectClickListener == null) {
            onPhotoSelectClickListener = (OnPhotoSelectClickListener) context;
        }

        mAppPhotoUtil = appPhotoUtil;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.nim_picker_photo_grid_item, null);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.picker_photo_grid_item_img);
            viewHolder.select = (ImageView) convertView.findViewById(R.id.picker_photo_grid_item_select);
            viewHolder.selectHotPot = (RelativeLayout) convertView.findViewById(R.id.picker_photo_grid_item_select_hotpot);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mAppPhotoUtil.mutiSelectLimitSize > 1) {
            viewHolder.selectHotPot.setVisibility(View.VISIBLE);
        } else {
            viewHolder.selectHotPot.setVisibility(View.GONE);
        }

        LayoutParams hotpotLayoutParams = viewHolder.selectHotPot.getLayoutParams();
        hotpotLayoutParams.width = width / 2;
        hotpotLayoutParams.height = width / 2;
        viewHolder.selectHotPot.setLayoutParams(hotpotLayoutParams);
        viewHolder.selectHotPot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoInfo photoInfo = list.get(position);
                if (photoInfo.isChoose()) {
                    photoInfo.setChoose(false);

                    viewHolder.select.setImageResource(R.drawable.ic_message_pic_item_normal);
                } else {

                    if (mAppPhotoUtil.getSelectedList().size() >= mAppPhotoUtil.mutiSelectLimitSize) {
                        Toast.makeText(mContext, String.format(mContext.getResources().getString(
                                R.string.picker_image_exceed_max_image_select), mAppPhotoUtil.mutiSelectLimitSize), Toast.LENGTH_SHORT).show();
                    } else {
                        photoInfo.setChoose(true);
                        viewHolder.select.setImageResource(R.drawable.ic_message_pic_item_selected);
                    }


                }
                onPhotoSelectClickListener.onPhotoSelectClick(photoInfo);

            }
        });

        if (list.get(position).isChoose()) {
            viewHolder.select.setImageResource(R.drawable.ic_message_pic_item_selected);
        } else {
            viewHolder.select.setImageResource(R.drawable.ic_message_pic_item_normal);
        }

        LayoutParams imageLayoutParams = viewHolder.image.getLayoutParams();
        imageLayoutParams.width = width;
        imageLayoutParams.height = width;
        viewHolder.image.setLayoutParams(imageLayoutParams);

        final PhotoInfo photoInfo = list.get(position);
        if (photoInfo != null) {
            String thumbPath = ThumbnailsUtil.getThumbnailWithImageID(photoInfo.getImageId(), photoInfo.getFilePath());
            PickerImageLoader.display(thumbPath, photoInfo.getAbsolutePath(), viewHolder.image, R.drawable.nim_image_default);
        }

        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
        public ImageView select;
        public RelativeLayout selectHotPot;
    }
}

