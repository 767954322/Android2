package com.tuwan.yuewan.entity.evnetbean;

/**
 * Created by Administrator on 2017/12/29.
 */

public class TeacherMainBean {


    /**
     * error : 0
     * service : {"icon":"http://res.tuwan.com/templet/play/images/app/icon_king3x.png","title":"王者荣耀","price":5,"unit":"局","id":957,"grading":"白金"}
     * info : {"attent":0,"name":"电竞奥巴马"}
     */

    private int error;
    private ServiceBean service;
    private InfoBean info;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public ServiceBean getService() {
        return service;
    }

    public void setService(ServiceBean service) {
        this.service = service;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class ServiceBean {
        /**
         * icon : http://res.tuwan.com/templet/play/images/app/icon_king3x.png
         * title : 王者荣耀
         * price : 5
         * unit : 局
         * id : 957
         * grading : 白金
         */

        private String icon;
        private String title;
        private int price;
        private String unit;
        private int id;
        private String grading;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGrading() {
            return grading;
        }

        public void setGrading(String grading) {
            this.grading = grading;
        }
    }

    public static class InfoBean {
        /**
         * attent : 0
         * name : 电竞奥巴马
         */

        private int attent;
        private String name;

        public int getAttent() {
            return attent;
        }

        public void setAttent(int attent) {
            this.attent = attent;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
