package com.example.administrator.diaokes.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/7/23.
 */

public class PersonData extends DataSupport {
    String name;
    int path;
    public PersonData(String name,int path){
        this.name = name;
        this.path = path;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setPath(int path){
        this.path = path;
    }
    public int getPath(){
        return path;
    }
}
