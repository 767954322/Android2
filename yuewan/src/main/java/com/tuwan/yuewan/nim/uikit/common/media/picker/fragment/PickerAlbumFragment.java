package com.tuwan.yuewan.nim.uikit.common.media.picker.fragment;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.uikit.common.media.picker.adapter.PickerAlbumAdapter;
import com.tuwan.yuewan.nim.uikit.common.media.picker.model.AlbumInfo;
import com.tuwan.yuewan.utils.AppPhotoUtil;


public class PickerAlbumFragment extends BaseFragment implements OnItemClickListener {

    public interface OnAlbumItemClickListener {
        void OnAlbumItemClick(AlbumInfo info);
    }

    private OnAlbumItemClickListener onAlbumItemClickListener;

    private LinearLayout loadingLay;
    private TextView loadingTips;
    private TextView loadingEmpty;
    private ListView albumListView;


    private PickerAlbumAdapter albumAdapter;

    public PickerAlbumFragment() {
        this.setContainerId(R.id.picker_album_fragment);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (onAlbumItemClickListener == null) {
            onAlbumItemClickListener = (OnAlbumItemClickListener) activity;
        }
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.nim_picker_image_folder_activity;
    }

    @Override
    protected void setUpView() {
        findViews();
    }

    @Override
    protected void setUpData() {
        if(AppPhotoUtil.getInstance().getList().size()>0){
            albumAdapter = new PickerAlbumAdapter(getActivity(), AppPhotoUtil.getInstance().getList());
            albumListView.setAdapter(albumAdapter);

            loadingLay.setVisibility(View.GONE);
        }else{
            albumListView.setVisibility(View.GONE);
            loadingTips.setVisibility(View.GONE);
        }
    }

    private void findViews() {
        loadingLay = (LinearLayout) getContentView().findViewById(R.id.picker_image_folder_loading);
        loadingTips = (TextView) getContentView().findViewById(R.id.picker_image_folder_loading_tips);
        loadingEmpty = (TextView) getContentView().findViewById(R.id.picker_image_folder_loading_empty);

        albumListView = (ListView) getContentView().findViewById(R.id.picker_image_folder_listView);
        albumListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        onAlbumItemClickListener.OnAlbumItemClick(AppPhotoUtil.getInstance().getList().get(position));
    }


}
