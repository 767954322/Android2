package com.tuwan.yuewan.ui.activity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/22.
 */

public class teacherInfomybean {


    /**
     * error : 0
     * info : {"images":["http://ucavatar.tuwan.com/data/avatar/000/31/30/54_avatar_big2.jpg"],"video":"http://tuwancourseout.oss-cn-hangzhou.aliyuncs.com/teachvideo/eJtYxXysNF.mp4","videocheck":0,"online":1,"charmBaseScore":20000,"charmScore":"20787","charmIcon":2,"charmLevel":2,"nextTotal":35000,"nextcharmIcon":2,"nextcharmLevel":3,"devoteBaseScore":0,"devoteLevel":0,"devoteScore":800,"nextDevoteLevel":1,"nextDevoteScore":5000,"sex":2,"age":23,"distance":"深圳市","timestr":"9小时前","nickname":"Seven♚清歌晚 ","height":"165CM","constell":"双子座","work":"","organs":"眼睛、腰部","tags":"鼻子、耳朵","like":"去留随你","uid":"313054","vipUid":566667,"fansNum":66,"attNum":0,"weixin":"","Attention":-1,"weixinState":1}
     * devoterank : [{"uid":318158,"avatar":"http://ucavatar.tuwan.com/data/avatar/000/31/81/58_avatar_middle.jpg","name":"丨SEVEN丨"},{"uid":1072809,"avatar":"http://ucavatar.tuwan.com/data/avatar/001/07/28/09_avatar_middle.jpg","name":"阿七"},{"uid":402533,"avatar":"http://ucavatar.tuwan.com/data/avatar/000/40/25/33_avatar_middle.jpg","name":"蓝蔚"}]
     * givegift : [{"pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","num":"1661"},{"pic":"http://img3.tuwandata.com/uploads/play/1427371503976282.png","num":"100"},{"pic":"http://img3.tuwandata.com/uploads/play/1182661503976353.png","num":"56"}]
     * service : [{"sid":1551,"image":"http://res.tuwan.com/templet/play/images/app/icon_lolup3x.png","title":"线上LOL","total":914,"dtid":31540,"grading":"钻石","pricestr":"42/小时"},{"sid":3599,"image":"http://res.tuwan.com/templet/play/images/app/icon_pubg3x.png","title":"绝地求生","total":262,"dtid":20014,"grading":"新秀","pricestr":"40/小时"}]
     * dynamicshow : 1
     * dynamiclist : {"data":[{"id":9360,"uid":313054,"content":"我喜欢搜房然后安乐死","images":"http://img1.tuwandata.com/v2/thumb/jpg/MmM0ZSw1MDAsNTAwLDcsOCwxLC0xLE5PTkUsLCwxMDA=/u/www.tuwan.com/uploads/play/201712/313054-16013622.jpeg","time":"2017-12-16 01:36","imgurl":"http://www.tuwan.com/uploads/play/201712/313054-16013622.jpeg","imgWidth":500,"imgHeight":500,"getUp":0,"myUp":0},{"id":9359,"uid":313054,"content":"吃鸡不存在的","images":"http://img.tuwandata.com/v2/thumb/jpg/ZWFiNiw1MDAsMzc1LDcsOCwxLC0xLE5PTkUsLCwxMDA=/u/www.tuwan.com/uploads/play/201712/313054-16013424.jpeg","time":"2017-12-16 01:34","imgurl":"http://www.tuwan.com/uploads/play/201712/313054-16013424.jpeg","imgWidth":500,"imgHeight":375,"getUp":0,"myUp":0},{"id":7752,"uid":313054,"content":"还有什么是你离开的借口","images":"http://img1.tuwandata.com/v2/thumb/jpg/NWZlMSw1MDAsNTAwLDcsOCwxLC0xLE5PTkUsLCwxMDA=/u/www.tuwan.com/uploads/play/201711/313054-26112803.jpeg","time":"2017-11-26 23:28","imgurl":"http://www.tuwan.com/uploads/play/201711/313054-26112803.jpeg","imgWidth":500,"imgHeight":500,"getUp":0,"myUp":0},{"id":7751,"uid":313054,"content":"冬天快乐","images":"http://img1.tuwandata.com/v2/thumb/jpg/MmIwOCw1MDAsNTAwLDcsOCwxLC0xLE5PTkUsLCwxMDA=/u/www.tuwan.com/uploads/play/201711/313054-26112736.jpeg","time":"2017-11-26 23:27","imgurl":"http://www.tuwan.com/uploads/play/201711/313054-26112736.jpeg","imgWidth":500,"imgHeight":500,"getUp":0,"myUp":0}],"page":1,"CountPage":1}
     */

    private int error;
    private InfoBean info;
    private int dynamicshow;
    private DynamiclistBean dynamiclist;
    private List<DevoterankBean> devoterank;
    private List<GivegiftBean> givegift;
    private List<ServiceBean> service;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public int getDynamicshow() {
        return dynamicshow;
    }

    public void setDynamicshow(int dynamicshow) {
        this.dynamicshow = dynamicshow;
    }

    public DynamiclistBean getDynamiclist() {
        return dynamiclist;
    }

    public void setDynamiclist(DynamiclistBean dynamiclist) {
        this.dynamiclist = dynamiclist;
    }

    public List<DevoterankBean> getDevoterank() {
        return devoterank;
    }

    public void setDevoterank(List<DevoterankBean> devoterank) {
        this.devoterank = devoterank;
    }

    public List<GivegiftBean> getGivegift() {
        return givegift;
    }

    public void setGivegift(List<GivegiftBean> givegift) {
        this.givegift = givegift;
    }

    public List<ServiceBean> getService() {
        return service;
    }

    public void setService(List<ServiceBean> service) {
        this.service = service;
    }

    public static class InfoBean {
        /**
         * images : ["http://ucavatar.tuwan.com/data/avatar/000/31/30/54_avatar_big2.jpg"]
         * video : http://tuwancourseout.oss-cn-hangzhou.aliyuncs.com/teachvideo/eJtYxXysNF.mp4
         * videocheck : 0
         * online : 1
         * charmBaseScore : 20000
         * charmScore : 20787
         * charmIcon : 2
         * charmLevel : 2
         * nextTotal : 35000
         * nextcharmIcon : 2
         * nextcharmLevel : 3
         * devoteBaseScore : 0
         * devoteLevel : 0
         * devoteScore : 800
         * nextDevoteLevel : 1
         * nextDevoteScore : 5000
         * sex : 2
         * age : 23
         * distance : 深圳市
         * timestr : 9小时前
         * nickname : Seven♚清歌晚
         * height : 165CM
         * constell : 双子座
         * work :
         * organs : 眼睛、腰部
         * tags : 鼻子、耳朵
         * like : 去留随你
         * uid : 313054
         * vipUid : 566667
         * fansNum : 66
         * attNum : 0
         * weixin :
         * Attention : -1
         * weixinState : 1
         */

        private String video;
        private int videocheck;
        private int online;
        private int charmBaseScore;
        private int charmScore;
        private int charmIcon;
        private int charmLevel;
        private int nextTotal;
        private int nextcharmIcon;
        private int nextcharmLevel;
        private int devoteBaseScore;
        private int devoteLevel;
        private int devoteScore;
        private int nextDevoteLevel;
        private int nextDevoteScore;
        private int sex;
        private int age;
        private String distance;
        private String timestr;
        private String nickname;
        private String height;
        private String constell;
        private String work;
        private String organs;
        private String tags;
        private String like;
        private String uid;
        private int vipUid;
        private int viplevel;
        private int fansNum;
        private int attNum;
        private String weixin;
        private int Attention;
        private int weixinState;
        private List<String> images;

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public int getVideocheck() {
            return videocheck;
        }

        public void setVideocheck(int videocheck) {
            this.videocheck = videocheck;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public int getCharmBaseScore() {
            return charmBaseScore;
        }

        public void setCharmBaseScore(int charmBaseScore) {
            this.charmBaseScore = charmBaseScore;
        }

        public int getCharmScore() {
            return charmScore;
        }

        public void setCharmScore(int charmScore) {
            this.charmScore = charmScore;
        }

        public int getCharmIcon() {
            return charmIcon;
        }

        public void setCharmIcon(int charmIcon) {
            this.charmIcon = charmIcon;
        }

        public int getCharmLevel() {
            return charmLevel;
        }

        public void setCharmLevel(int charmLevel) {
            this.charmLevel = charmLevel;
        }

        public int getNextTotal() {
            return nextTotal;
        }

        public void setNextTotal(int nextTotal) {
            this.nextTotal = nextTotal;
        }

        public int getNextcharmIcon() {
            return nextcharmIcon;
        }

        public void setNextcharmIcon(int nextcharmIcon) {
            this.nextcharmIcon = nextcharmIcon;
        }

        public int getNextcharmLevel() {
            return nextcharmLevel;
        }

        public void setNextcharmLevel(int nextcharmLevel) {
            this.nextcharmLevel = nextcharmLevel;
        }

        public int getDevoteBaseScore() {
            return devoteBaseScore;
        }

        public void setDevoteBaseScore(int devoteBaseScore) {
            this.devoteBaseScore = devoteBaseScore;
        }

        public int getDevoteLevel() {
            return devoteLevel;
        }

        public void setDevoteLevel(int devoteLevel) {
            this.devoteLevel = devoteLevel;
        }

        public int getDevoteScore() {
            return devoteScore;
        }

        public void setDevoteScore(int devoteScore) {
            this.devoteScore = devoteScore;
        }

        public int getNextDevoteLevel() {
            return nextDevoteLevel;
        }

        public void setNextDevoteLevel(int nextDevoteLevel) {
            this.nextDevoteLevel = nextDevoteLevel;
        }

        public int getNextDevoteScore() {
            return nextDevoteScore;
        }

        public void setNextDevoteScore(int nextDevoteScore) {
            this.nextDevoteScore = nextDevoteScore;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getTimestr() {
            return timestr;
        }

        public void setTimestr(String timestr) {
            this.timestr = timestr;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getConstell() {
            return constell;
        }

        public void setConstell(String constell) {
            this.constell = constell;
        }

        public String getWork() {
            return work;
        }

        public void setWork(String work) {
            this.work = work;
        }

        public String getOrgans() {
            return organs;
        }

        public void setOrgans(String organs) {
            this.organs = organs;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getLike() {
            return like;
        }

        public void setLike(String like) {
            this.like = like;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public int getVipUid() {
            return vipUid;
        }

        public void setVipUid(int vipUid) {
            this.vipUid = vipUid;
        }

        public int getFansNum() {
            return fansNum;
        }

        public void setFansNum(int fansNum) {
            this.fansNum = fansNum;
        }

        public int getAttNum() {
            return attNum;
        }

        public void setAttNum(int attNum) {
            this.attNum = attNum;
        }

        public String getWeixin() {
            return weixin;
        }

        public void setWeixin(String weixin) {
            this.weixin = weixin;
        }

        public int getAttention() {
            return Attention;
        }

        public void setAttention(int Attention) {
            this.Attention = Attention;
        }

        public int getWeixinState() {
            return weixinState;
        }

        public void setWeixinState(int weixinState) {
            this.weixinState = weixinState;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public int getViplevel() {
            return viplevel;
        }

        public void setViplevel(int viplevel) {
            this.viplevel = viplevel;
        }
    }

    public static class DynamiclistBean {
        /**
         * data : [{"id":9360,"uid":313054,"content":"我喜欢搜房然后安乐死","images":"http://img1.tuwandata.com/v2/thumb/jpg/MmM0ZSw1MDAsNTAwLDcsOCwxLC0xLE5PTkUsLCwxMDA=/u/www.tuwan.com/uploads/play/201712/313054-16013622.jpeg","time":"2017-12-16 01:36","imgurl":"http://www.tuwan.com/uploads/play/201712/313054-16013622.jpeg","imgWidth":500,"imgHeight":500,"getUp":0,"myUp":0},{"id":9359,"uid":313054,"content":"吃鸡不存在的","images":"http://img.tuwandata.com/v2/thumb/jpg/ZWFiNiw1MDAsMzc1LDcsOCwxLC0xLE5PTkUsLCwxMDA=/u/www.tuwan.com/uploads/play/201712/313054-16013424.jpeg","time":"2017-12-16 01:34","imgurl":"http://www.tuwan.com/uploads/play/201712/313054-16013424.jpeg","imgWidth":500,"imgHeight":375,"getUp":0,"myUp":0},{"id":7752,"uid":313054,"content":"还有什么是你离开的借口","images":"http://img1.tuwandata.com/v2/thumb/jpg/NWZlMSw1MDAsNTAwLDcsOCwxLC0xLE5PTkUsLCwxMDA=/u/www.tuwan.com/uploads/play/201711/313054-26112803.jpeg","time":"2017-11-26 23:28","imgurl":"http://www.tuwan.com/uploads/play/201711/313054-26112803.jpeg","imgWidth":500,"imgHeight":500,"getUp":0,"myUp":0},{"id":7751,"uid":313054,"content":"冬天快乐","images":"http://img1.tuwandata.com/v2/thumb/jpg/MmIwOCw1MDAsNTAwLDcsOCwxLC0xLE5PTkUsLCwxMDA=/u/www.tuwan.com/uploads/play/201711/313054-26112736.jpeg","time":"2017-11-26 23:27","imgurl":"http://www.tuwan.com/uploads/play/201711/313054-26112736.jpeg","imgWidth":500,"imgHeight":500,"getUp":0,"myUp":0}]
         * page : 1
         * CountPage : 1
         */

        private int page;
        private int CountPage;
        private List<DataBean> data;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getCountPage() {
            return CountPage;
        }

        public void setCountPage(int CountPage) {
            this.CountPage = CountPage;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 9360
             * uid : 313054
             * content : 我喜欢搜房然后安乐死
             * images : http://img1.tuwandata.com/v2/thumb/jpg/MmM0ZSw1MDAsNTAwLDcsOCwxLC0xLE5PTkUsLCwxMDA=/u/www.tuwan.com/uploads/play/201712/313054-16013622.jpeg
             * time : 2017-12-16 01:36
             * imgurl : http://www.tuwan.com/uploads/play/201712/313054-16013622.jpeg
             * imgWidth : 500
             * imgHeight : 500
             * getUp : 0
             * myUp : 0
             */

            private int id;
            private int uid;
            private String content;
            private String images;
            private String time;
            private String imgurl;
            private int imgWidth;
            private int imgHeight;
            private int getUp;
            private int myUp;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getImages() {
                return images;
            }

            public void setImages(String images) {
                this.images = images;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getImgurl() {
                return imgurl;
            }

            public void setImgurl(String imgurl) {
                this.imgurl = imgurl;
            }

            public int getImgWidth() {
                return imgWidth;
            }

            public void setImgWidth(int imgWidth) {
                this.imgWidth = imgWidth;
            }

            public int getImgHeight() {
                return imgHeight;
            }

            public void setImgHeight(int imgHeight) {
                this.imgHeight = imgHeight;
            }

            public int getGetUp() {
                return getUp;
            }

            public void setGetUp(int getUp) {
                this.getUp = getUp;
            }

            public int getMyUp() {
                return myUp;
            }

            public void setMyUp(int myUp) {
                this.myUp = myUp;
            }
        }
    }

    public static class DevoterankBean {
        /**
         * uid : 318158
         * avatar : http://ucavatar.tuwan.com/data/avatar/000/31/81/58_avatar_middle.jpg
         * name : 丨SEVEN丨
         */

        private int uid;
        private String avatar;
        private String name;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "DevoterankBean{" +
                    "uid=" + uid +
                    ", avatar='" + avatar + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static class GivegiftBean {
        /**
         * pic : http://img3.tuwandata.com/uploads/play/1629711503976254.png
         * num : 1661
         */

        private String pic;
        private String num;

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }

    public static class ServiceBean {
        /**
         * sid : 1551
         * image : http://res.tuwan.com/templet/play/images/app/icon_lolup3x.png
         * title : 线上LOL
         * total : 914
         * dtid : 31540
         * grading : 钻石
         * pricestr : 42/小时
         */

        private int sid;
        private String image;
        private String title;
        private int total;
        private int dtid;
        private String grading;
        private String pricestr;

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getDtid() {
            return dtid;
        }

        public void setDtid(int dtid) {
            this.dtid = dtid;
        }

        public String getGrading() {
            return grading;
        }

        public void setGrading(String grading) {
            this.grading = grading;
        }

        public String getPricestr() {
            return pricestr;
        }

        public void setPricestr(String pricestr) {
            this.pricestr = pricestr;
        }
    }
}
