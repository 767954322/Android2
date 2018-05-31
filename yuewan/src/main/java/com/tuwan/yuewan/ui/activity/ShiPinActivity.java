package com.tuwan.yuewan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.VideoInfo;
import com.tuwan.yuewan.ui.video.CustomRecordActivity;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class ShiPinActivity extends BaseActivity implements View.OnClickListener {
    public static final int RECORD_CUSTOM_VIDEO = 2;
    private ImageView xc_fanhui;
    private TextView xc_baocun;
    private static GridView xc_grid;
    private static VideoInfo xcb;
    private static ArrayList<VideoInfo> al = new ArrayList<>();
    private static XiangCeAdapter xca;
    private String ss;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_job;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                initShiPin();
                xca.notifyDataSetChanged();
            }
        });
        th.start();
    }

    private void initData() {
        xcb = new VideoInfo();
        xcb.setDisplayName("12");
        al.add(xcb);
        xca = new XiangCeAdapter(ShiPinActivity.this, al);
        xc_grid.setAdapter(xca);
//        ScannerAnsyTasks ansyTask = new ScannerAnsyTasks();
//        ansyTask.execute();
    }


    private void initView() {
        xc_fanhui = (ImageView) findViewById(R.id.iv_titlebar_back);
        xc_fanhui.setOnClickListener(this);
        xc_baocun = (TextView) findViewById(R.id.xc_baocun);
        xc_baocun.setOnClickListener(this);
        xc_grid = (GridView) findViewById(R.id.xc_grid);
        al.clear();
        xc_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(ShiPinActivity.this, "点击了" + position, Toast.LENGTH_SHORT).show();
                if (position == 0) {
                    initXiangJi();
                } else {
                    ss = al.get(position).getPath().toString();
                }
            }
        });
    }

    private void initXiangJi() {
        Intent intent = new Intent(this, CustomRecordActivity.class);
        startActivityForResult(intent, RECORD_CUSTOM_VIDEO);
    }

    private void initShiPin() {
        Cursor cursor = getApplication().getApplicationContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        try {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)); // 显示名称
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)); // 路径
                xcb = new VideoInfo();
                xcb.setDisplayName(title);
                xcb.setPath(path);
                al.add(xcb);
                xca.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.xc_baocun) {
            initBaoCun();
        } else if (i == R.id.iv_titlebar_back) {
            finish();
        }
    }

    private void initBaoCun() {
        Intent intent = new Intent(ShiPinActivity.this, EditdataActivity.class);
        intent.putExtra("files", ss);
        setResult(123, intent);
        finish();
//        Toast.makeText(this, "ss=" + ss, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == RECORD_CUSTOM_VIDEO) {
            String filePath = data.getStringExtra("videoPath");
            ss = filePath;
            initBaoCun();
//            mVideoView.setVideoPath(filePath);
//            mVideoView.start();
        }

    }

    //----------------------------------------适配器---------------------------------
    static class XiangCeAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<VideoInfo> list;

        public XiangCeAdapter(ShiPinActivity jobActivity, ArrayList<VideoInfo> al) {
            this.context = jobActivity;
            this.list = al;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.xiangce_item, null);
                vh.img = (ImageView) convertView.findViewById(R.id.xc_item_img);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            if (list.get(position).getDisplayName().equals("12")) {
                Glide.with(context).load(R.drawable.video_icon).thumbnail(0.3f).into(vh.img);
            } else {
                Log.e("视频地址展示: ", list.get(position).getPath().toString());
                vh.img.setImageBitmap(getVideoThumbnail(list.get(position).getPath().toString(), 100, 100,
                        MediaStore.Images.Thumbnails.MICRO_KIND));
            }
            return convertView;
        }

        /**
         * 获取视频的缩略图
         * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
         * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
         *
         * @param videoPath 视频的路径
         * @param width     指定输出视频缩略图的宽度
         * @param height    指定输出视频缩略图的高度
         * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
         *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
         * @return 指定大小的视频缩略图
         */
        private Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                         int kind) {
            Bitmap bitmap = null;
            // 获取视频的缩略图
            bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
//            System.out.println("w" + bitmap.getWidth());
//            System.out.println("h" + bitmap.getHeight());
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            return bitmap;
        }

        class ViewHolder {
            private ImageView img;
        }
    }

    public class ScannerAnsyTasks extends AsyncTask<Void, Integer, List<VideoInfo>> {
        private List<VideoInfo> videoInfos = new ArrayList<VideoInfo>();

        @Override
        public List<VideoInfo> doInBackground(Void... params) {
            videoInfos = getVideoFile(videoInfos, Environment.getExternalStorageDirectory());
            videoInfos = filterVideo(videoInfos);
            Log.e("tga", "最后的大小" + videoInfos.get(8).getDisplayName());
            Log.e("tga", "最后的大小" + videoInfos.get(8).getPath());
            Log.e("tga", "最后的大小" + videoInfos.size());
            al.addAll(videoInfos);
            return videoInfos;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<VideoInfo> videoInfos) {
            super.onPostExecute(videoInfos);
        }

        /**
         * 获取视频文件
         *
         * @param list
         * @param file
         * @return
         */
        private List<VideoInfo> getVideoFile(final List<VideoInfo> list, File file) {

            file.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {

                    String name = file.getName();

                    int i = name.indexOf('.');
                    if (i != -1) {
                        name = name.substring(i);
                        if (name.equalsIgnoreCase(".mp4")
                                || name.equalsIgnoreCase(".3gp")
                                || name.equalsIgnoreCase(".wmv")
                                || name.equalsIgnoreCase(".ts")
                                || name.equalsIgnoreCase(".rmvb")
                                || name.equalsIgnoreCase(".mov")
                                || name.equalsIgnoreCase(".m4v")
                                || name.equalsIgnoreCase(".avi")
                                || name.equalsIgnoreCase(".m3u8")
                                || name.equalsIgnoreCase(".3gpp")
                                || name.equalsIgnoreCase(".3gpp2")
                                || name.equalsIgnoreCase(".mkv")
                                || name.equalsIgnoreCase(".flv")
                                || name.equalsIgnoreCase(".divx")
                                || name.equalsIgnoreCase(".f4v")
                                || name.equalsIgnoreCase(".rm")
                                || name.equalsIgnoreCase(".asf")
                                || name.equalsIgnoreCase(".ram")
                                || name.equalsIgnoreCase(".mpg")
                                || name.equalsIgnoreCase(".v8")
                                || name.equalsIgnoreCase(".swf")
                                || name.equalsIgnoreCase(".m2v")
                                || name.equalsIgnoreCase(".asx")
                                || name.equalsIgnoreCase(".ra")
                                || name.equalsIgnoreCase(".ndivx")
                                || name.equalsIgnoreCase(".xvid")) {
                            VideoInfo video = new VideoInfo();
                            file.getUsableSpace();
                            video.setDisplayName(file.getName());
                            video.setPath(file.getAbsolutePath());
                            Log.e("tga", "name" + video.getPath());
                            list.add(video);
                            return true;
                        }
                        //判断是不是目录
                    } else if (file.isDirectory()) {
                        getVideoFile(list, file);
                    }
                    return false;
                }
            });

            return list;
        }

        /**
         * 1M=10240 b,小于10m的过滤掉
         * 过滤视频文件
         *
         * @param videoInfos
         * @return
         */
        private List<VideoInfo> filterVideo(List<VideoInfo> videoInfos) {
            List<VideoInfo> newVideos = new ArrayList<VideoInfo>();
            for (VideoInfo videoInfo : videoInfos) {
                File f = new File(videoInfo.getPath());
                if (f.exists() && f.isFile() && f.length() > 10240) {
                    newVideos.add(videoInfo);
                    Log.e("TGA", "文件大小" + f.length());
                } else {
                    Log.e("TGA", "文件太小或者不存在");
                }
            }
            return newVideos;
        }
    }

}
