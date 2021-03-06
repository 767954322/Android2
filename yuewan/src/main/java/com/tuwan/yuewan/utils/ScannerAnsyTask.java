package com.tuwan.yuewan.utils;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.tuwan.yuewan.entity.VideoInfo;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/2.
 */

public class ScannerAnsyTask extends AsyncTask<Void,Integer,List<VideoInfo>> {
    private List<VideoInfo> videoInfos=new ArrayList<VideoInfo>();
    @Override
    public List<VideoInfo> doInBackground(Void... params) {
        videoInfos=getVideoFile(videoInfos, Environment.getExternalStorageDirectory());
        videoInfos=filterVideo(videoInfos);
        Log.e("tga","最后的大小"+videoInfos.get(8).getDisplayName());
        Log.e("tga","最后的大小"+videoInfos.get(8).getPath());
        Log.e("tga","最后的大小"+videoInfos.size());
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
                        Log.e("tga","name"+video.getPath());
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

    /**1M=10240 b,小于10m的过滤掉
     * 过滤视频文件
     * @param videoInfos
     * @return
     */
    private List<VideoInfo> filterVideo(List<VideoInfo> videoInfos){
        List<VideoInfo> newVideos=new ArrayList<VideoInfo>();
        for(VideoInfo videoInfo:videoInfos){
            File f=new File(videoInfo.getPath());
            if(f.exists()&&f.isFile()&&f.length()>10240){
                newVideos.add(videoInfo);
                Log.e("TGA","文件大小"+f.length());
            }else {
                Log.e("TGA","文件太小或者不存在");
            }
        }
        return newVideos;
    }
}
