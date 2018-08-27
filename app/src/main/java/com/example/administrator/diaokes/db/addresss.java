package com.example.administrator.diaokes.db;

/**
 * Created by Administrator on 2018/8/3.
 */

public class addresss {
    String name;
    String address;
    String type;
    String costtype;
    String yu;
    String lianxi;
    String detail;
    String locate;

    public addresss(String name,String locate,String address,String type,String costtype,String yu,String lianxi,String detail){
        this.name = name;
        this.address = address;
        this.type = type;
        this.costtype = costtype;
        this.yu = yu;
        this.lianxi = lianxi;
        this.locate = locate;
        this.detail = detail;
    }

    public String getName(){
        return name;
    }
    public String getAddress(){
        return address;
    }
    public String getType(){
        return type;
    }
    public String getCosttype(){
        return costtype;
    }
    public String getYu(){
        return yu;
    }
    public String getLianxi(){
        return lianxi;
    }
    public String getDetail(){
        return detail;
    }
    public String getLocate(){
        return locate;
    }

}
