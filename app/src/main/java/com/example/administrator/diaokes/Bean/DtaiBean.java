package com.example.administrator.diaokes.Bean;

/**
 * Created by Administrator on 2018/7/23.
 */

public class DtaiBean {
    private String name;
    private String content;
    private String zan;
    private String path;
    private String type;
    private String position;
    private String dtid;

    public DtaiBean(String name,String content,String zan,String path,String type) {
        this.name = name;
        this.content = content;
        this.zan = zan;
        this.path = path;
        this.type = type;
    }
    public String getDtId() {
        return dtid;
    }

    public void setDtId(String id) {
        this.dtid = id;
    }

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
    public String getZan(){
        return zan;
    }
    public void setZan(String zan){
        this.zan = zan;
    }
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }
    public void setId(String id){
        this.position = id;
    }
    public String getId(){
        return position;
    }
}
