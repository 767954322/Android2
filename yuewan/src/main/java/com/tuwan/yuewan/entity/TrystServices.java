package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */

public class TrystServices {

    private int error;
    private List<ServicesData> data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<ServicesData> getData() {
        return data;
    }

    public void setData(List<ServicesData> data) {
        this.data = data;
    }

    public class ServicesData{
        private int dtid;
        private String name;
        private String unit;
        private List<TrystRemarks> grading;
        private List<TrystRemarks> remarks;

        public int getDtid() {
            return dtid;
        }

        public void setDtid(int dtid) {
            this.dtid = dtid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public List<TrystRemarks> getGrading() {
            return grading;
        }

        public void setGrading(List<TrystRemarks> grading) {
            this.grading = grading;
        }

        public List<TrystRemarks> getRemarks() {
            return remarks;
        }

        public void setRemarks(List<TrystRemarks> remarks) {
            this.remarks = remarks;
        }
    }
}
