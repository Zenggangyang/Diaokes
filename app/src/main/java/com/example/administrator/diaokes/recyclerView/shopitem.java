package com.example.administrator.diaokes.recyclerView;

/**
 * Created by Administrator on 2018/7/28.
 */

public class shopitem {
    private int path;
    private String name;
    private int cost;

    public shopitem(int path,String name,int cost){
        this.path = path;
        this.name = name;
        this.cost = cost;
    }

    public int getPath(){
        return path;
    }
    public String getName(){
        return name;
    }
    public int getCost(){
        return cost;
    }

    public void setPath(int path){
        this.path = path;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setCost(int cost){
        this.cost = cost;
    }
}
