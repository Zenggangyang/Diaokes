package com.example.administrator.diaokes.db;

import java.util.List;

/**
 * Created by Administrator on 2018/8/3.
 */

public class addressdate {
    String count;
    List<addresss> list;

    public addressdate(String count){
        this.count = count;
    }
    public void setList(List<addresss> list){
        this.list = list;
    }
    public List<addresss> getList(){
        return list;
    }
}
