package com.example.administrator.diaokes.db;

import com.example.administrator.diaokes.recyclerView.rankitem;

import java.util.List;

/**
 * Created by Administrator on 2018/8/5.
 */

public class ranklist {
    String count;
    List<rankdata> list;

    public ranklist(String count){
        this.count = count;
    }
    public void setList(List<rankdata> list){
        this.list = list;
    }
    public List<rankdata> getList(){
        return list;
    }
}
