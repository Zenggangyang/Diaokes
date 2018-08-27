package com.example.administrator.diaokes.db;

import java.util.List;

/**
 * Created by Administrator on 2018/8/6.
 */

public class notifylist {
    int count;
    List<notifyitem> list;

    public notifylist(int count){
        this.count = count;
    }

    public void setList(List<notifyitem> list){
        this.list = list;
    }
    public List<notifyitem> getList(){
        return list;
    }


}
