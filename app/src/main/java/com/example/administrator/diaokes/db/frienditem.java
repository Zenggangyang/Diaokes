package com.example.administrator.diaokes.db;

/**
 * Created by Administrator on 2018/8/8.
 */

public class frienditem {
    String name;
    String rank;
    String content;
    int img;

    public frienditem(String name,String rank,String content,int img){
        this.name = name;
        this.rank = rank;
        this.content = content;
        this.img = img;
    }

    public String getName(){
        return name;
    }

    public int getImg() {
        return img;
    }

    public String getRank() {
        return rank;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
