package com.example.administrator.diaokes.db;

/**
 * Created by Administrator on 2018/8/6.
 */

public class notifyitem {
    String name;
    String content;
    String date;
    public notifyitem(String name,String content,String date){
        this.name = name;
        this.content = content;
        this.date = date;
    }
    public String getName(){
        return name;
    }

    public String getContent(){
        return content;
    }

    public String getDate(){
        return date;
    }
}
