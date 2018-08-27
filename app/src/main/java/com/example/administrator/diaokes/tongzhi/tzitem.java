package com.example.administrator.diaokes.tongzhi;

/**
 * Created by Administrator on 2018/7/30.
 */

public class tzitem {
    private String name;
    private String content;
    private String time;

    public tzitem(String name,String path,String time){
        this.name = name;
        this.content = path;
        this.time = time;
    }

    public String getName(){
        return name;
    }
    public String getPath(){
        return content;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setPath(String path){
        this.content = path;
    }

    public String getTime(){
        return time;
    }
    public void setTime(){
        this.time = time;
    }
}
