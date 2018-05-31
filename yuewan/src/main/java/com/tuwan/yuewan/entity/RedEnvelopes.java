package com.tuwan.yuewan.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/22.
 */

public class RedEnvelopes {

    private int error;
    private List<RedEnvelopsData> data = new ArrayList<RedEnvelopsData>();
    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<RedEnvelopsData> getData() {
        return data;
    }

    public void setData(List<RedEnvelopsData> data) {
        this.data = data;
    }

    public static class RedEnvelopsData{
        private int ucid;
        private String title;
        private double price;
        private String desc;
        private String sdate;
        private String edate;
        private int expire;
        private int used;
        private Boolean select;

        public int getUcid() {
            return ucid;
        }

        public void setUcid(int ucid) {
            this.ucid = ucid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getSdate() {
            return sdate;
        }

        public void setSdate(String sdate) {
            this.sdate = sdate;
        }

        public String getEdate() {
            return edate;
        }

        public void setEdate(String edate) {
            this.edate = edate;
        }

        public int getExpire() {
            return expire;
        }

        public void setExpire(int expire) {
            this.expire = expire;
        }

        public int getUsed() {
            return used;
        }

        public void setUsed(int used) {
            this.used = used;
        }

        public Boolean getSelect() {
            return select;
        }

        public void setSelect(Boolean select) {
            this.select = select;
        }
    }
}
