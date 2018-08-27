package com.example.administrator.diaokes.recyclerView;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2018/6/29.
 */

public class recyclerItem {
    private String name;
    private int imageId;
    private String content;
    private int type;
    private Bitmap bitmap;
    private String path;
    private String time;

    public recyclerItem(String name,int imageId,String content,int type,Bitmap bitmap,String path,String time){
        this.name = name;
        this.imageId = imageId;
        this.content = content;
        this.type = type;
        this.bitmap = bitmap;
        this.path = path;
        this.time = time;
    }
    public String getName(){
        return name;
    }

    public int getImageId(){
        return imageId;
    }

    public String getContent(){
        return content;
    }

    public int getType(){
        return type;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public String getPath(){
        return path;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public String getTime(){
        return time;
    }
}
