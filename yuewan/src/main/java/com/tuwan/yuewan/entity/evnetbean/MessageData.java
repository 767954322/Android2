package com.tuwan.yuewan.entity.evnetbean;

/**
 * Created by Administrator on 2017/12/15.
 */

public class MessageData {


    /**
     * error : 0
     * data : {"id":"6","img":"http://img1.tuwandata.com/v2/thumb/jpg/YWM2NSwyMDAsMjAwLDksMywxLC0xLE5PTkUsLCw5MA==/u/www.tuwan.com/uploads/user/20171218/20171218105819663.jpg"}
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

    public  class DataBean {
        /**
         * id : 6
         * img : http://img1.tuwandata.com/v2/thumb/jpg/YWM2NSwyMDAsMjAwLDksMywxLC0xLE5PTkUsLCw5MA==/u/www.tuwan.com/uploads/user/20171218/20171218105819663.jpg
         */

        private String id;
        private String img;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
