package com.example.administrator.diaokes.db;

/**
 * Created by Administrator on 2018/8/5.
 */

public class caritem {
    String name;
    String cost;
    String path;

    public caritem(String name,String cost,String path){
        this.name = name;
        this.cost = cost;
        this.path = path;
    }
    public String getName(){
        return name;
    }
    public String getCost(){
        return cost;
    }
    public String getPath(){
        return path;
    }
}
