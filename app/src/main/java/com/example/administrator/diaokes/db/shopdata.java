package com.example.administrator.diaokes.db;

import java.util.List;

/**
 * Created by Administrator on 2018/8/5.
 */

public class shopdata {
    List<caritem> list;
    int count;

    public shopdata(int count,List<caritem> list){
        this.count = count;
    }

    public int getCount(){
        return count;
    }
    public List<caritem> getList(){
        return list;
    }
    public void setList(List<caritem> list) {
        this.list = list;
    }
}
