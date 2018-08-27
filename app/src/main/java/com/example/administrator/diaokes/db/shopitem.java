package com.example.administrator.diaokes.db;

/**
 * Created by Administrator on 2018/8/2.
 */

public class shopitem {
    String name;
    String address;
    String locate;
    String detail;
    String lianxi;

    public shopitem(String name,String locate,String address,String lianxi,String detail) {
        this.name = name;
        this.address = address;
        this.locate = locate;
        this.detail = detail;
        this.lianxi = lianxi;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getLocate(){
        return locate;
    }
    public String getDetail(){
        return detail;
    }
    public String getLianxi(){
        return lianxi;
    }
}
