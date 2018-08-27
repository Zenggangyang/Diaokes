package com.example.administrator.diaokes.db;

/**
 * Created by Administrator on 2018/8/5.
 */

public class rankdata {
    String name;
    String img;
    String num;
    String  fans;
    String rank;
    public rankdata(String name,String img,String num,String fans,String rank){
        this.name = name;
        this.img = img;
        this.num = num;
        this.fans = fans;
        this.rank = rank;
    }
    public String getName(){
        return name;
    }
    public String getImg(){
        return img;
    }
    public String getNum(){
        return num;
    }
    public String getFans(){
        return fans;
    }
    public String getRank(){
        return rank;
    }

}
