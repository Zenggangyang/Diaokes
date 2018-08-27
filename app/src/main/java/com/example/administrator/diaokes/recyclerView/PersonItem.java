package com.example.administrator.diaokes.recyclerView;

/**
 * Created by Administrator on 2018/7/4.
 */

public class PersonItem {
    String pername;
    int img;
    public PersonItem(String pername,int imageId){
        this.pername = pername;
        this.img = imageId;
    }
    public String getPername(){
        return pername;
    }
    public int getImageId(){
        return img;
    }
}
