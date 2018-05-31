package com.tuwan.yuewan.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.VideoInfo;
import com.tuwan.yuewan.entity.VideoInfos;
import com.tuwan.yuewan.entity.VideoPlayEntity;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.utils.Code;
import com.tuwan.yuewan.utils.CommTools;
import com.tuwan.yuewan.utils.JumpUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public class VideoListActivity extends AppCompatActivity {

    private ListView mListView;
    private TextView noData,iv_titlebar_more;
    private ImageView iv_titlebar_back;
    private Button btnBack;
    ArrayList<VideoInfos> vList;
    private Intent lastIntent;
    private Handler handler=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        iv_titlebar_more = (TextView) findViewById(R.id.iv_titlebar_more);
        mListView = (ListView) findViewById(R.id.lv_video);
        noData = (TextView) findViewById(R.id.tv_nodata);
//        btnBack = (Button) findViewById(R.id.btn_back);

        lastIntent = getIntent();
                initData();

        mListView.setOnItemClickListener(new ItemClick());
        iv_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_titlebar_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);// 创建一个请求视频的意图
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);// 设置视频的质量，值为0-1，
//                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);// 设置视频的录制长度，s为单位
//                intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 10 * 1024 * 1024L);// 设置视频文件大小，字节为单位
                startActivityForResult(intent, Code.VIDEO_RECORD_REQUEST);// 设置请求码，在onActivityResult()方法中接收结果
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            Uri uri = data.getData();
            if (uri == null) {
                return;
            } else {
                Cursor c = getContentResolver().query(uri,
                        new String[]{MediaStore.MediaColumns.DATA},
                        null, null, null);
                if (c != null && c.moveToFirst()) {
                    SharedPreferences sp = getSharedPreferences("File", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("path", c.getString(0));
                    editor.commit();
                    finish();
                }
            }
        }
    }

    private void initData() {
        SharedPreferences sp = getSharedPreferences("File", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("path", "");
        editor.commit();
        vList = new ArrayList<VideoInfos>();
        String[] mediaColumns = new String[]{MediaStore.MediaColumns.DATA,
                BaseColumns._ID, MediaStore.MediaColumns.TITLE, MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.Video.VideoColumns.DURATION, MediaStore.MediaColumns.SIZE};
        final Cursor cursor = getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns,
                null, null, null);
        DialogMaker.showProgressDialog(VideoListActivity.this, null, getString(R.string.logining), true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
//                if (loginRequest != null) {
//                    loginRequest.abort();
//                    onLoginDone();
//                }
            }
        }).setCanceledOnTouchOutside(false);
        //开一条子线程加载网络数据
        Runnable runnable=new Runnable()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(2000);
                    //xmlwebData解析网络中xml中的数据
//                                        persons=XmlwebData.getData(path);
                    //发送消息，并把persons结合对象传递过去
                    if (cursor.moveToFirst()) {
                        do {
                            VideoInfos info = new VideoInfos();
                            info.setFilePath(cursor.getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
                            info.setMimeType(cursor.getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)));
                            info.setTitle(cursor.getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE)));
                            info.setTime(CommTools.LongToHms(cursor.getInt(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION))));
                            info.setSize(CommTools.LongToPoint(cursor
                                    .getLong(cursor
                                            .getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE))));
                            int id = cursor.getInt(cursor
                                    .getColumnIndexOrThrow(BaseColumns._ID));
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inDither = false;
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                            info.setB(MediaStore.Video.Thumbnails.getThumbnail(getContentResolver(), id,
                                    MediaStore.Images.Thumbnails.MICRO_KIND, options));
                            vList.add(info);

                        } while (cursor.moveToNext());
                    }
                    handler.sendMessage(handler.obtainMessage(0, vList));
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        };

                     new Thread(runnable).start();
                     //handler与线程之间的通信及数据处理
                    handler=new Handler()
                    {
                            public void handleMessage(Message msg)
                             {
                                     if(msg.what==0)
                                     {
                                             //msg.obj是获取handler发送信息传来的数据
//                                             @SuppressWarnings("unchecked")
//                                             ArrayList<VideoInfos> person=(ArrayList<VideoInfos>) msg.obj;
                                             //给ListView绑定数据
                                         DialogMaker.dismissProgressDialog();
                                         if (vList.size() == 0) {
                                             mListView.setVisibility(View.GONE);
                                             noData.setVisibility(View.VISIBLE);
                                         } else {
                                             VideoListAdapter adapter = new VideoListAdapter(VideoListActivity.this);
                                             mListView.setAdapter(adapter);
                                         }
                                         }
                                 }
                         };


//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

    }
    private class ItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mListView.getItemAtPosition(position);
            String filePath = vList.get(position).getFilePath();
            SharedPreferences sp = getSharedPreferences("File", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("path", filePath);
            editor.commit();
            finish();
        }

    }

    class VideoListAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public VideoListAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return vList.size();
        }

        @Override
        public Object getItem(int p) {
            return vList.get(p);
        }

        @Override
        public long getItemId(int p) {
            return p;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_video_list, null);
                holder.vImage = (ImageView) convertView.findViewById(R.id.video_img);
                holder.vTitle = (TextView) convertView.findViewById(R.id.video_title);
                holder.vSize = (TextView) convertView.findViewById(R.id.video_size);
                holder.vTime = (TextView) convertView.findViewById(R.id.video_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.vImage.setImageBitmap(vList.get(position).getB());
            holder.vTitle.setText(vList.get(position).getTitle()); // + "." + (videoList.get(position).getMimeType()).substring(6))
            holder.vSize.setText(vList.get(position).getSize());
            holder.vTime.setText(vList.get(position).getTime());

            holder.vImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String bpath = "file://" + vList.get(position).getFilePath();
                    //创建临时图片
//                    File photoOutputFile = SDPath.getFile("temp.jpg", SDPath.PHOTO_FILE_STR);
//                    Uri photoOutputUri = FileProvider.getUriForFile(
//                            this,
//                            .getPackageName() + ".fileprovider",
//                            photoOutputFile);

//                    intent.setDataAndType(Uri.parse(bpath), "video/*");
//                    startActivity(intent);
                    JumpUtils.goToVideoPlayer(VideoListActivity.this, holder.vImage, new VideoPlayEntity(bpath,"" , vList.get(position).getTitle()));
                }
            });
            return convertView;
        }

        class ViewHolder {
            ImageView vImage;
            TextView vTitle;
            TextView vSize;
            TextView vTime;
        }
    }

}
