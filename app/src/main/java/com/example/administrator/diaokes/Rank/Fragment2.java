package com.example.administrator.diaokes.Rank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.recyclerView.hrankadapter;
import com.example.administrator.diaokes.recyclerView.hrankadpter1;
import com.example.administrator.diaokes.recyclerView.rankadapter;
import com.example.administrator.diaokes.recyclerView.rankadapter1;
import com.example.administrator.diaokes.recyclerView.rankitem;
import com.example.administrator.diaokes.recyclerView.rankitem1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/25.
 */

public class Fragment2 extends Fragment {
    private RecyclerView recyclerView;
    private hrankadapter adapter;
    private hrankadpter1 adapter1;
    private List<rankitem> list = new ArrayList<>();
    private RecyclerView recyclerView1;
    private rankitem[] items = {new rankitem("http://192.168.1.103:8080/tomcats/images/a.jpg","汉斯",123,123),new rankitem("http://192.168.1.103:8080/tomcats/images/b.jpg","魏聪",33,22),new rankitem("http://192.168.1.100:8080/tomcats/images/c.jpg","马武",13,23),new rankitem("http://192.168.1.100:8080/tomcats/images/a.jpg","汉斯",12,3),new rankitem("http://192.168.1.100:8080/tomcats/images/b.jpg","魏聪",23,8),new rankitem("http://192.168.1.100:8080/tomcats/images/c.jpg","马武",3,2)};
    private rankitem1[] rankitem1s = {new rankitem1("http://192.168.1.103:8080/tomcats/images/d.jpg","刘朝","456",R.drawable.no1),new rankitem1("http://192.168.1.103:8080/tomcats/images/e.jpg","马汉","356",R.drawable.no2),new rankitem1("http://192.168.1.100:8080/tomcats/images/f.jpg","唐尧","256",R.drawable.no3)};
    private List<rankitem1> list1 = new ArrayList<>();
    private static String s = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.ranklist_layout,container,false);
        inits();
        recyclerView1 = view.findViewById(R.id.rank_recycler1);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this.getActivity());
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(linearLayoutManager1);
        adapter1 = new hrankadpter1(this.getActivity(),list1);
        recyclerView1.setAdapter(adapter1);

        recyclerView = view.findViewById(R.id.rank_recycler);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this.getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager2);
        adapter = new hrankadapter(this.getActivity(),list);
        recyclerView.setAdapter(adapter);
        return view;
    }
    private void inits(){
        list.clear();
        list1.clear();
        for(int i1 = 0;i1<items.length;i1++){
            list.add(i1,items[i1]);
        }
        for (int i = 0; i<rankitem1s.length; i++){
            list1.add(i,rankitem1s[i]);
        }
    }
    public static Fragment newInstance(String username1){
        Fragment2 _personal = new Fragment2();
        s = username1;
        return _personal;
    }
}
