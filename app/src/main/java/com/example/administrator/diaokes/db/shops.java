package com.example.administrator.diaokes.db;

import java.util.List;

/**
 * Created by Administrator on 2018/8/2.
 */

public class shops {
    List<shopitem> lists;
    String count;

    public shops(String count){
        this.count = count;
    }
    public List<shopitem> getList(){
        return lists;
    }

    public void setList(List<shopitem> lists) {
        this.lists = lists;
    }
    public void setCount(String count){
        this.count = count;
    }

    public String getCount(){
        return count;
    }
}
