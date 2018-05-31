package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by zhangjie on 2017/10/10.
 */

public class IndexNewBean {

    //轮播图
    public List<BannerlistBean> bannerlist;
    //订制菜单
    public List<MainNavBean> nav;
    //推荐数据
    public List<MainPersonCardBean> data;
    public IndexActivity activity;

    public int total;//数据总数
    public int page;//当前页
    public int totalPage;//总页数

    @Override
    public String toString() {
        return "IndexNewBean{" +
                "bannerlist=" + bannerlist +
                ", nav=" + nav +
                ", data=" + data +
                ", total=" + total +
                ", page=" + page +
                ", totalPage=" + totalPage +
                '}';
    }

    public static class BannerlistBean {
        /**
         * id : 368907
         * litpic : http://www.tuwan.com/uploads/1710/20/807-1G020205443521.jpg
         * title : 点点约玩：周星大作战
         * url : http://y.tuwan.com/weekstar/
         * pubdate : 1508503931
         */

        public int id;//id
        public String litpic;//图片
        public String title;//标题
        public String url;//链接，有则显示网页内容，无则不处理
        public int pubdate;//发布时间，时间戳

        @Override
        public String toString() {
            return "BannerlistBean{" +
                    "id=" + id +
                    ", litpic='" + litpic + '\'' +
                    ", title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    ", pubdate=" + pubdate +
                    '}';
        }
    }

    public static class IndexActivity {
        /**
         * id : 368907
         * litpic : http://www.tuwan.com/uploads/1710/20/807-1G020205443521.jpg
         * title : 点点约玩：周星大作战
         * url : http://y.tuwan.com/weekstar/
         * pubdate : 1508503931
         */

        public String image;//图片
        public String url;//链接，有则显示网页内容，无则不处理

        @Override
        public String toString() {
            return "IndexActivity{" +
                    "image='" + image + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }


}
