package com.example.administrator.diaokes.db;

import android.content.Context;

/**
 * Created by Administrator on 2018/7/27.
 */

public class talkingMsg {
    public static final int TYPE_RECEIVE = 0;
    public static final int TYPE_SEND = 1;
    private String content;
    private int type;
    private String name;
    private String time;

    public talkingMsg(String content,int type,String time,String name){
        this.content = content;
        this.type = type;
        this.name = name;
        this.time = time;
    }
    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }
    public int getType(){
        return type;
    }
    public void setType(int type){
        this.type = type;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setTime(String time){
        this.time = time;
    }
    public String getTime(){
        return time;
    }

}
