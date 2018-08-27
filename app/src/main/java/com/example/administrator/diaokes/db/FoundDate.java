package com.example.administrator.diaokes.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/7/18.
 */

public class FoundDate extends DataSupport {
    private String name;
    private String content;
    private int zan;
    private String path;
    private int type;
    private String position;
    private String time;

    public String getName(){
        return name;
    }
    public String getContent(){
        return content;
    }
    public String getPath(){
        return path;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setContent(String content){
        this.content = content;
    }
    public void setPath(String path){
        this.path = path;
    }
    public int getZan(){
        return zan;
    }
    public void setZan(int zan){
        this.zan = zan;
    }
    public int getType(){
        return type;
    }
    public void setType(int type){
        this.type = type;
    }
    public void setId(String id){
        this.position = id;
    }
    public String getId(){
        return position;
    }
    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time = time;
    }
}
