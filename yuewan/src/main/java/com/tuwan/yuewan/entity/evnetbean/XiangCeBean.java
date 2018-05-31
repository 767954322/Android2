package com.tuwan.yuewan.entity.evnetbean;

/**
 * Created by Administrator on 2017/12/14.
 */

public class XiangCeBean {

    private String name;
    private String dz;
    private String img;

    @Override
    public String toString() {
        return "XiangCeBean{" +
                "name='" + name + '\'' +
                ", dz='" + dz + '\'' +
                ", img=" + img +
                '}';
    }

    public XiangCeBean() {
        super();
    }

    public XiangCeBean(String name, String dz, String img) {
        this.name = name;
        this.dz = dz;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDz() {
        return dz;
    }

    public void setDz(String dz) {
        this.dz = dz;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
