package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by TUWAN on 2017/12/4.
 */

public class InBean {


    private List<OnlinegBean> onlineg;
    private List<LinegBean> lineg;
    private List<OnlinepBean> onlinep;

    public List<OnlinegBean> getOnlineg() {
        return onlineg;
    }

    public void setOnlineg(List<OnlinegBean> onlineg) {
        this.onlineg = onlineg;
    }

    public List<LinegBean> getLineg() {
        return lineg;
    }

    public void setLineg(List<LinegBean> lineg) {
        this.lineg = lineg;
    }

    public List<OnlinepBean> getOnlinep() {
        return onlinep;
    }

    public void setOnlinep(List<OnlinepBean> onlinep) {
        this.onlinep = onlinep;
    }

    public static class OnlinegBean {
        /**
         * dtid : 31540
         * gameicon : http://res.tuwan.com/templet/play/teacher/images/lol.png
         * title : 线上LOL
         * state : 0
         */

        private int dtid;
        private String gameicon;
        private String title;
        private int state;

        public int getDtid() {
            return dtid;
        }

        public void setDtid(int dtid) {
            this.dtid = dtid;
        }

        public String getGameicon() {
            return gameicon;
        }

        public void setGameicon(String gameicon) {
            this.gameicon = gameicon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }

    public static class LinegBean {
        /**
         * dtid : 20009
         * gameicon : http://res.tuwan.com/templet/play/teacher/images/lol_ed.png
         * title : 线下LOL
         * state : 0
         */

        private int dtid;
        private String gameicon;
        private String title;
        private int state;

        public int getDtid() {
            return dtid;
        }

        public void setDtid(int dtid) {
            this.dtid = dtid;
        }

        public String getGameicon() {
            return gameicon;
        }

        public void setGameicon(String gameicon) {
            this.gameicon = gameicon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }

    public static class OnlinepBean {
        /**
         * dtid : 20002
         * gameicon : http://res.tuwan.com/templet/play/teacher/images/singer.png
         * title : 线上歌手
         * state : 0
         */

        private int dtid;
        private String gameicon;
        private String title;
        private int state;

        public int getDtid() {
            return dtid;
        }

        public void setDtid(int dtid) {
            this.dtid = dtid;
        }

        public String getGameicon() {
            return gameicon;
        }

        public void setGameicon(String gameicon) {
            this.gameicon = gameicon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}
