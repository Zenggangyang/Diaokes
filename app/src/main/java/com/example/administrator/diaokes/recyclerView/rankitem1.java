package com.example.administrator.diaokes.recyclerView;

/**
 * Created by Administrator on 2018/7/25.
 */

public class rankitem1 {
    String url;
    String name;
    String num;
    int path;

    public rankitem1(String url,String name,String num,int path){
        this.num = num;
        this.url = url;
        this.name = name;
        this.path = path;
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
    public void setNum(String num){
        this.num = num;
    }
    public String getNum(){
        return num;
    }
    public int getPath(){
        return path;
    }
    public void setPath(int path){
        this.path = path;
    }
}
