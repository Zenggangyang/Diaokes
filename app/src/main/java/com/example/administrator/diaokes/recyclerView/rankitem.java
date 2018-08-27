package com.example.administrator.diaokes.recyclerView;

/**
 * Created by Administrator on 2018/7/25.
 */

public class rankitem {
    String url;
    String name;
    int num;
    int fans;

    public rankitem(String url,String name,int num,int fans){
        this.num = num;
        this.url = url;
        this.name = name;
        this.fans = fans;
    }
    public String getUrl(){
        return url;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setNum(int num){
        this.num = num;
    }
    public int getNum(){
        return num;
    }
    public int getFans(){
        return fans;
    }
}
