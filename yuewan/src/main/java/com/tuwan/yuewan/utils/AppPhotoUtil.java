package com.tuwan.yuewan.utils;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.tuwan.yuewan.nim.uikit.common.media.dao.MediaDAO;
import com.tuwan.yuewan.nim.uikit.common.media.picker.model.AlbumInfo;
import com.tuwan.yuewan.nim.uikit.common.media.picker.model.PhotoInfo;
import com.tuwan.yuewan.nim.uikit.common.media.picker.util.ThumbnailsUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhangjie on 2017/11/1.
 */

public class AppPhotoUtil {

    public static final String FILE_PREFIX = "file://";
    private List<AlbumInfo> albumInfolist = new ArrayList<AlbumInfo>();
    private List<PhotoInfo> mSelectedPhotoList = new ArrayList<PhotoInfo>();

    public static final int REQUEST_PIC = 101;
    public static final int REQUEST_CAMERA = 102;
    public final static int PICKER_IMAGE_PREVIEW = 105;
    public final static int PREVIEW_IMAGE_FROM_CAMERA = 106;

    /**
     * 最大选择数量
     */
    public int mutiSelectLimitSize = 9;

    /**
     * 为初始化时的activity
     */
    private Activity mActivity;

    public boolean isOrig = false;

    /**
     * 创建一个AppPhotoUtil
     */
    public static AppPhotoUtil createInstance(Activity activity){
        AppPhotoUtil appPhotoUtil = new AppPhotoUtil(activity);
        AppPhotoUtilManager.list.add(appPhotoUtil);
        return appPhotoUtil;
    }

    public static AppPhotoUtil getInstance(){
        return AppPhotoUtilManager.getAppPhotoUtil();
    }

    public static void onDestory(int index){
        if (index < AppPhotoUtilManager.list.size()) {
            AppPhotoUtilManager.list.remove(index);
        }
    }
    public int getIndex(){
        return AppPhotoUtilManager.list.indexOf(this);
    }



    public void startImageScanTask() {
        new ImageScanAsyncTask().execute();
    }

    private AppPhotoUtil(Activity activity) {
        this.mActivity = activity;
    }

    private Activity getActivity(){
        return mActivity;
    }

    public List<AlbumInfo> getList(){
        return albumInfolist;
    }
    public List<PhotoInfo> getPhotoInfoList(){
        ArrayList<PhotoInfo> photoInfos = new ArrayList<>();
        for (AlbumInfo albumInfo : albumInfolist) {
            photoInfos.addAll(albumInfo.getList());
        }
        return photoInfos;
    }

    public List<PhotoInfo> getSelectedList(){
        return mSelectedPhotoList;
    }

    /**
     * @return true添加选中成功 false数量超标
     */
    public boolean selectedPhoto(PhotoInfo info){
        if (mSelectedPhotoList.size()>=mutiSelectLimitSize){
            return false;
        }
        mSelectedPhotoList.add(info);
        info.setChoose(true);
        return true;
    }
    public boolean removePhoto(PhotoInfo info){
        mSelectedPhotoList.remove(info);
        info.setChoose(false);
        return true;
    }
    public void cleanSelected(){
        for (PhotoInfo photoInfo : mSelectedPhotoList) {
            photoInfo.setChoose(false);
        }
        mSelectedPhotoList.clear();
    }


    /********************
     * 获取相册图片
     */
    private class ImageScanAsyncTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected Object doInBackground(Void... params) {
            getAllMediaThumbnails();
            getAllMediaPhotos();

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (getActivity() != null && albumInfolist != null) {
                //获得到了所有的缩略图
            }
        }
    }

    private void getAllMediaThumbnails() {
        ThumbnailsUtil.clear();
        Cursor cursorThumb = null;
        try {
            cursorThumb = MediaDAO.getAllMediaThumbnails(getActivity());
            if (cursorThumb != null && cursorThumb.moveToFirst()) {
                int imageID;
                String imagePath;
                do {
                    imageID = cursorThumb.getInt(cursorThumb.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID));
                    imagePath = cursorThumb.getString(cursorThumb.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
                    ThumbnailsUtil.put(imageID, FILE_PREFIX + imagePath);
                } while (cursorThumb.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cursorThumb != null) {
                    cursorThumb.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private void getAllMediaPhotos() {
        if (albumInfolist == null) {
            albumInfolist = new ArrayList<AlbumInfo>();
        } else {
            albumInfolist.clear();
        }

        Cursor cursorPhotos = null;
        try {
            cursorPhotos = MediaDAO.getAllMediaPhotos(getActivity());
            HashMap<String, AlbumInfo> hash = new HashMap<String, AlbumInfo>();
            AlbumInfo albumInfo = null;
            PhotoInfo photoInfo = null;

            if (cursorPhotos != null && cursorPhotos.moveToFirst()) {
                do {
                    int index = 0;
                    int _id = cursorPhotos.getInt(cursorPhotos.getColumnIndex(MediaStore.Images.Media._ID));
                    String path = cursorPhotos.getString(cursorPhotos.getColumnIndex(MediaStore.Images.Media.DATA));
                    String album = cursorPhotos.getString(cursorPhotos.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    long size = cursorPhotos.getLong(cursorPhotos.getColumnIndex(MediaStore.Images.Media.SIZE));
                    int width = cursorPhotos.getInt(cursorPhotos.getColumnIndex(MediaStore.Images.Media.WIDTH));
                    int heigh = cursorPhotos.getInt(cursorPhotos.getColumnIndex(MediaStore.Images.Media.HEIGHT));

                    if (!isValidImageFile(path)) {
                        Log.d("PICKER", "it is not a vaild path:" + path);
                        continue;
                    }

                    List<PhotoInfo> photoList = new ArrayList<PhotoInfo>();
                    photoInfo = new PhotoInfo();
                    if (hash.containsKey(album)) {
                        albumInfo = hash.remove(album);
                        if (albumInfolist.contains(albumInfo)) {
                            index = albumInfolist.indexOf(albumInfo);
                        }
                        photoInfo.setImageId(_id);
                        photoInfo.setFilePath(FILE_PREFIX + path);
                        photoInfo.setAbsolutePath(path);
                        photoInfo.setSize(size);
                        photoInfo.heigh = heigh;
                        photoInfo.width = width;

                        albumInfo.getList().add(photoInfo);
                        albumInfolist.set(index, albumInfo);
                        hash.put(album, albumInfo);
                    } else {
                        albumInfo = new AlbumInfo();
                        photoList.clear();
                        photoInfo.setImageId(_id);
                        photoInfo.setFilePath(FILE_PREFIX + path);
                        photoInfo.setAbsolutePath(path);
                        photoInfo.setSize(size);
                        photoList.add(photoInfo);
                        photoInfo.heigh = heigh;
                        photoInfo.width = width;

                        albumInfo.setImageId(_id);
                        albumInfo.setFilePath(FILE_PREFIX + path);
                        albumInfo.setAbsolutePath(path);
                        albumInfo.setAlbumName(album);
                        albumInfo.setList(photoList);
                        albumInfolist.add(albumInfo);
                        hash.put(album, albumInfo);
                    }
                } while (cursorPhotos.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cursorPhotos != null) {
                    cursorPhotos.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private boolean isValidImageFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }

        File imageFile = new File(filePath);
        if (imageFile.exists()) {
            return true;
        }
        return false;
    }
    /**
     * 获取相册图片
     *******************/



}
