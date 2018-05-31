package com.tuwan.yuewan.nim.uikit.common.media.picker.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.uikit.common.media.picker.adapter.PickerPhotoAdapter;
import com.tuwan.yuewan.nim.uikit.common.media.picker.model.PhotoInfo;
import com.tuwan.yuewan.nim.uikit.session.constant.Extras;
import com.tuwan.yuewan.utils.AppPhotoUtil;

import java.io.Serializable;
import java.util.List;

public class PickerImageFragment extends BaseFragment implements OnItemClickListener {

	private AppPhotoUtil mAppPhotoUtil;

	public interface OnPhotoSelectClickListener {
		void onPhotoSelectClick(PhotoInfo selectPhoto);
		void onPhotoSingleClick(List<PhotoInfo> photos, int position);
	}
		
	private GridView pickerImageGridView;

	private OnPhotoSelectClickListener onPhotoSelectClickListener;
	
	private List<PhotoInfo> photoList;
	
	private PickerPhotoAdapter photoAdapter;

	public PickerImageFragment() {
		this.setContainerId(R.id.picker_photos_fragment);
	}
		
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (onPhotoSelectClickListener == null) {
			onPhotoSelectClickListener = (OnPhotoSelectClickListener) activity;
		}
	}

	@Override
	protected int setLayoutResourceID() {
		return R.layout.nim_picker_images_fragment;
	}

	@Override
	protected void setUpView() {
		mAppPhotoUtil = AppPhotoUtil.getInstance();
		proceedExtra();
		findViews();
	}

	@Override
	protected void setUpData() {

	}


	private void proceedExtra() {
		Bundle bundle = getArguments();
		photoList = getPhotos(bundle);
	}
	
	private void findViews() {		
		pickerImageGridView = (GridView) getContentView().findViewById(R.id.picker_images_gridview);
		photoAdapter = new PickerPhotoAdapter(getActivity(), photoList, pickerImageGridView, mAppPhotoUtil);
		pickerImageGridView.setAdapter(photoAdapter);
		pickerImageGridView.setOnItemClickListener(this);
	}
	
	public void resetFragment(List<PhotoInfo> list){
		pickerImageGridView.setAdapter(null);		

		photoList = list;
		photoAdapter = new PickerPhotoAdapter(getActivity(), photoList, pickerImageGridView, mAppPhotoUtil);
		pickerImageGridView.setAdapter(photoAdapter);
	}
	
	public void updateGridview(){
		photoAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		onPhotoSelectClickListener.onPhotoSingleClick(photoList, position);
	}
	
	public List<PhotoInfo> getPhotos(Bundle bundle) {
		return toPhotos(bundle.getSerializable(Extras.EXTRA_PHOTO_LISTS));
	}
	
	@SuppressWarnings("unchecked")
	private List<PhotoInfo> toPhotos(Serializable sPhotos) {
		if (sPhotos != null && sPhotos instanceof List<?>) {
			return (List<PhotoInfo>) sPhotos;
		}
		return null;
	}
}
