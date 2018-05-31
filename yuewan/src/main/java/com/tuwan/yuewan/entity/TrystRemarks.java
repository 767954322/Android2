package com.tuwan.yuewan.entity;

/**
 * Created by Administrator on 2018/4/28.
 */

public class TrystRemarks {

    private int id;
    private String name;
    private boolean type = false;

    public TrystRemarks(String name, boolean type) {
        this.name = name;
        this.type = type;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}
