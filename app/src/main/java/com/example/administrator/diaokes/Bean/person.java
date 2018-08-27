package com.example.administrator.diaokes.Bean;

/**
 * Created by Administrator on 2018/7/24.
 */

public class person {
    private String name;
    private String content;

    public person(String name,String content){
        this.name = name;
        this.content = content;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }
}
