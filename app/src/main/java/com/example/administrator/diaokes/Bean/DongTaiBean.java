package com.example.administrator.diaokes.Bean;

import com.example.administrator.diaokes.recyclerView.recyclerItem;

import java.util.List;

/**
 * Created by Administrator on 2018/7/23.
 */

public class DongTaiBean {
    String message;
    String count;
    List<DtaiBean> lists;

    public DongTaiBean(String message,String count) {
        this.message = message;
        this.count = count;
    }

    public List<DtaiBean> getList() {
        return lists;
    }

    public void setLists(List<DtaiBean> items) {
        this.lists = items;
    }
}
