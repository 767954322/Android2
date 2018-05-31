package com.tuwan.yuewan.ui.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.evnetbean.XiangCeBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JobActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView xc_fanhui;
    private TextView xc_baocun;
    private static GridView xc_grid;
    private static List<String> result;
    private static XiangCeBean xcb;
    private static ArrayList<XiangCeBean> al = new ArrayList<>();
    private static XiangCeAdapter xca;
    private static int REQUEST_CAMERA_2 = 202;
    private String mFilePath;
    private String ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        initView();
        initData();
    }

    private void initData() {
        getSystemPhotoList(JobActivity.this);

    }

    public List<String> getSystemPhotoList(final Context context) {
        result = new ArrayList<String>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null || cursor.getCount() <= 0) {  // 没有图片
            // return null;
        } else {
            while (cursor.moveToNext()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String path = cursor.getString(index); // 文件地址
                File file = new File(path);
                if (file.exists()) {
                    result.add(path);
//                    Log.e("---------", path);
                }
//                Log.e("=====", result.toString());
            }
        }
//        Glide.with(context).load(result.get(0).toString()).into(img);
        xcb = new XiangCeBean();
        xcb.setImg("12");
        al.add(xcb);
        for (int i = 0; i < result.size(); i++) {
            File file = new File(result.get(i));
            String fname = file.getName();
            xcb = new XiangCeBean();
            xcb.setDz(result.get(i));
            xcb.setName(fname);
            xcb.setImg(result.get(i));
            al.add(xcb);
        }
//        xca.notifyDataSetChanged();
        xca = new XiangCeAdapter(JobActivity.this, al);
        xc_grid.setAdapter(xca);
        return result;
    }

    private void initView() {
        // 获取SD卡路径
        mFilePath = Environment.getExternalStorageDirectory().getPath();
        mFilePath = mFilePath + "/" + "temp.png";// 指定路径
        xc_fanhui = (ImageView) findViewById(R.id.iv_titlebar_back);
        xc_fanhui.setOnClickListener(this);
        xc_baocun = (TextView) findViewById(R.id.xc_baocun);
        xc_baocun.setOnClickListener(this);
        xc_grid = (GridView) findViewById(R.id.xc_grid);
        al.clear();
        xc_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(JobActivity.this, "点击了"+position, Toast.LENGTH_SHORT).show();
                if (position == 0) {
                    initXiangJi();
                } else {
                    ss = al.get(position).getImg().toString();
                }
            }
        });
    }

    private void initXiangJi() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
        Uri photoUri = Uri.fromFile(new File(mFilePath)); // 传递路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);// 更改系统默认存储路径
        startActivityForResult(intent, REQUEST_CAMERA_2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回数据
            if (requestCode == REQUEST_CAMERA_2) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(mFilePath); // 根据路径获取数据
//                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
//                    ivShowPicture.setImageBitmap(bitmap);// 显示图片
                    ss = mFilePath;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();// 关闭流
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        finish();
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
        Intent intent = new Intent(JobActivity.this, EditdataActivity.class);
        intent.putExtra("url", ss);
        setResult(7, intent);
        finish();

    }


    static class XiangCeAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<XiangCeBean> list;

        public XiangCeAdapter(JobActivity jobActivity, ArrayList<XiangCeBean> al) {
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
            if (list.get(position).getImg().equals("12")) {
                Glide.with(context).load(R.drawable.ic_message_camera).into(vh.img);
            } else {
                Glide.with(context).load(list.get(position).getImg()).into(vh.img);
            }
            return convertView;
        }

        class ViewHolder {
            private ImageView img;
        }
    }

}
