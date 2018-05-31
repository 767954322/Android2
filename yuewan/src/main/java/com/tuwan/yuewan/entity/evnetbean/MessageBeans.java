package com.tuwan.yuewan.entity.evnetbean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class MessageBeans {

    /**
     * error : 0
     * data : {"teacher":1,"avatar":"http://ucavatar.tuwan.com/data/avatar/001/17/16/97_avatar_big.jpg","video":"http://img3.tuwandata.com/uploads/play/video/20171218/1f71ba0769b7f35cd8939b5c62236c68.mp4","photo":[{"id":3,"image":"/uploads/user/20171218/20171218105243484.jpg"},{"id":4,"image":"/uploads/user/20171218/20171218105244610.jpg"},{"id":5,"image":"/uploads/user/20171218/20171218105245806.jpg"},{"id":6,"image":"/uploads/user/20171218/20171218105819663.jpg"},{"id":7,"image":"/uploads/user/20171218/20171218111911706.jpg"},{"id":8,"image":"/uploads/user/20171218/20171218112122918.jpg"}],"nickname":"给沟沟壑壑","age":12,"qq":"1542486","height":"145","weight":"62","professionList":[{"id":1,"name":"学生"},{"id":2,"name":"主播"},{"id":3,"name":"医生"},{"id":4,"name":"护士"},{"id":5,"name":"模特"},{"id":6,"name":"教师"},{"id":7,"name":"公务员"},{"id":8,"name":"个体经营"},{"id":9,"name":"上班族"},{"id":10,"name":"其他"}],"profession":"","interest":"","glamour":[{"id":1,"name":"眼睛","selected":0},{"id":2,"name":"鼻子","selected":0},{"id":3,"name":"耳朵","selected":0},{"id":4,"name":"嘴唇","selected":0},{"id":5,"name":"胸部","selected":0},{"id":6,"name":"腰部","selected":0},{"id":7,"name":"腿","selected":0},{"id":8,"name":"臀部","selected":0},{"id":9,"name":"手","selected":0},{"id":10,"name":"其他","selected":0}]}
     */

    private int error;
    private DataBean data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * teacher : 1
         * avatar : http://ucavatar.tuwan.com/data/avatar/001/17/16/97_avatar_big.jpg
         * video : http://img3.tuwandata.com/uploads/play/video/20171218/1f71ba0769b7f35cd8939b5c62236c68.mp4
         * photo : [{"id":3,"image":"/uploads/user/20171218/20171218105243484.jpg"},{"id":4,"image":"/uploads/user/20171218/20171218105244610.jpg"},{"id":5,"image":"/uploads/user/20171218/20171218105245806.jpg"},{"id":6,"image":"/uploads/user/20171218/20171218105819663.jpg"},{"id":7,"image":"/uploads/user/20171218/20171218111911706.jpg"},{"id":8,"image":"/uploads/user/20171218/20171218112122918.jpg"}]
         * nickname : 给沟沟壑壑
         * age : 12
         * qq : 1542486
         * height : 145
         * weight : 62
         * professionList : [{"id":1,"name":"学生"},{"id":2,"name":"主播"},{"id":3,"name":"医生"},{"id":4,"name":"护士"},{"id":5,"name":"模特"},{"id":6,"name":"教师"},{"id":7,"name":"公务员"},{"id":8,"name":"个体经营"},{"id":9,"name":"上班族"},{"id":10,"name":"其他"}]
         * profession :
         * interest :
         * glamour : [{"id":1,"name":"眼睛","selected":0},{"id":2,"name":"鼻子","selected":0},{"id":3,"name":"耳朵","selected":0},{"id":4,"name":"嘴唇","selected":0},{"id":5,"name":"胸部","selected":0},{"id":6,"name":"腰部","selected":0},{"id":7,"name":"腿","selected":0},{"id":8,"name":"臀部","selected":0},{"id":9,"name":"手","selected":0},{"id":10,"name":"其他","selected":0}]
         */

        private String teacher;
        private String avatar;
        private String video;
        private int video_check;
        private String nickname;
        private String age;
        private String qq;
        private String height;
        private String weight;
        private String profession;
        private String interest;
        private List<PhotoBean> photo;
        private List<ProfessionListBean> professionList;
        private List<GlamourBean> glamour;

        public String getTeacher() {
            return teacher;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getVideo() {
            return video;
        }

        public void setVideoCheck(int videoCheck) {
            this.video_check = videoCheck;
        }

        public int getVideoCheck() {
            return video_check;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public List<PhotoBean> getPhoto() {
            return photo;
        }

        public void setPhoto(List<PhotoBean> photo) {
            this.photo = photo;
        }

        public List<ProfessionListBean> getProfessionList() {
            return professionList;
        }

        public void setProfessionList(List<ProfessionListBean> professionList) {
            this.professionList = professionList;
        }

        public List<GlamourBean> getGlamour() {
            return glamour;
        }

        public void setGlamour(List<GlamourBean> glamour) {
            this.glamour = glamour;
        }

        public static class PhotoBean {
            /**
             * id : 3
             * image : /uploads/user/20171218/20171218105243484.jpg
             */

            private String id;
            private String image;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }

        public static class ProfessionListBean {
            /**
             * id : 1
             * name : 学生
             */

            private String id;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class GlamourBean {
            /**
             * id : 1
             * name : 眼睛
             * selected : 0
             */

            private String id;
            private String name;
            private int selected;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getSelected() {
                return selected;
            }

            public void setSelected(int selected) {
                this.selected = selected;
            }
        }
    }
}