package com.example.administrator.diaokes.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/8/7.
 */

public class Routes extends DataSupport {
    String name;
    String time;
    String locate;
    String start;
    String content;
    public String getName(){
        return name;
    }

    public String getTime(){
        return time;
    }

    public String getLocate(){
        return locate;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setTime(String time){
        this.time = time;
    }

    public void setLocate(String locate){
        this.locate = locate;
    }
    public String getStart(){
        return start;
    }
    public void setStart(String start){
        this.start = start;
    }

    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }

}
