package com.example.administrator.diaokes.Rank;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.diaokes.Bean.CommentBean;
import com.example.administrator.diaokes.Fragment.Found;
import com.example.administrator.diaokes.Fragment.Personal;
import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.db.rankdata;
import com.example.administrator.diaokes.db.ranklist;
import com.example.administrator.diaokes.recyclerView.rankadapter;
import com.example.administrator.diaokes.recyclerView.rankadapter1;
import com.example.administrator.diaokes.recyclerView.rankitem;
import com.example.administrator.diaokes.recyclerView.rankitem1;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/25.
 */

public class Fragment1 extends Fragment {
    private RecyclerView recyclerView;
    private rankadapter adapter;
    private rankadapter1 adapter1;
    private List<rankitem> list = new ArrayList<>();
    private RecyclerView recyclerView1;
    //private rankitem[] items = {new rankitem("http://192.168.1.100:8080/tomcats/images/a.jpg","钱六","234"),new rankitem("http://192.168.1.100:8080/tomcats/images/b.jpg","魏聪","234"),new rankitem("http://192.168.1.100:8080/tomcats/images/c.jpg","马武","234"),new rankitem("http://192.168.1.100:8080/tomcats/images/a.jpg","汉斯","234"),new rankitem("http://192.168.1.100:8080/tomcats/images/b.jpg","魏聪","234"),new rankitem("http://192.168.1.100:8080/tomcats/images/c.jpg","马武","234")};
    private int[] items = {R.drawable.no1,R.drawable.no2,R.drawable.no3};
    private List<rankitem1> list1 = new ArrayList<>();
    private ranklist rlist;
    private static String s = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.ranklist_layout,container,false);

        Gson gson = new Gson();
        rlist = gson.fromJson(s, ranklist.class);
        int i1 = 0;
        int i2 =0;
        List<rankitem> rankitems1 = new ArrayList<>();
        List<rankitem> rankitems2 = new ArrayList<>();
        try {
            for(rankdata item : rlist.getList()) {
                if (item.getRank().equals("钓客江湖")) {
                    rankitem re = new rankitem(item.getImg(), item.getName(), Integer.parseInt(item.getNum()) * 60, Integer.parseInt(item.getFans()));
                    rankitems1.add(i1, re);
                    i1++;
                } else {
                    rankitem re = new rankitem(item.getImg(), item.getName(), Integer.parseInt(item.getNum()) * 60, Integer.parseInt(item.getFans()));
                    rankitems2.add(i2, re);
                    i2++;
                }
            }
        }catch (Exception e){
            Log.e("error",e.getMessage());
        }
        rankitem[] ss = new rankitem[rankitems1.size()];
        try {
            rankitems1.toArray(ss);
            for (int j = 0; j < rankitems1.size(); j++) {
                int q = j;
                for(int k = j+1;k < ss.length;k++ ){
                    if(ss[k].getNum() > ss[q].getNum()){
                        q = k;
                    }
                }
                rankitem a = ss[j];
                ss[j] = ss[q];
                ss[q] = a;
            }
        }catch (Exception e){
            Log.e("error",e.getMessage());
        }
        rankitem[] sss = new rankitem[rankitems2.size()];
        try {
            rankitems2.toArray(sss);
            for (int j = 0; j < rankitems2.size(); j++) {
                int q = j;
                for(int k = j+1;k < sss.length;k++ ){
                    if(sss[k].getNum() > sss[q].getNum()){
                        q = k;
                    }
                }
                rankitem a = sss[j];
                sss[j] = sss[q];
                sss[q] = a;
            }
        }catch (Exception e){
            Log.e("error",e.getMessage());
        }
        rankitem1[] aa = new rankitem1[3];
        for (int cc = 0;cc<aa.length;cc++){
            aa[cc] = new rankitem1(ss[cc].getUrl(),ss[cc].getName(),ss[cc].getNum()+"",items[cc]);
        }
        try {
            for (int k = 3; k < rlist.getList().size(); k++) {
                if (k <= ss.length - 1) {
                    list.add(ss[k]);
                } else {
                    list.add(sss[k-ss.length]);
                }
            }
            for(int c = 0;c<3;c++){
                list1.add(aa[c]);
            }
        }catch (Exception e){
            Log.e("error",e.getMessage());
        }
        recyclerView1 = view.findViewById(R.id.rank_recycler1);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this.getActivity());
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(linearLayoutManager1);
        adapter1 = new rankadapter1(this.getActivity(),list1);
        recyclerView1.setAdapter(adapter1);

        recyclerView = view.findViewById(R.id.rank_recycler);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this.getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager2);
        adapter = new rankadapter(this.getActivity(),list);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public static Fragment newInstance(String username1){
        Fragment1 _personal = new Fragment1();
        s = username1;
        return _personal;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("PERSONAL","ONSTART");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("PERSONAL","ONRESUME");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("PERSONAL","ONPAUSE");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("PERSONAL","ONSTOP");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v("PERSONAL","ONDESTORY");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("PERSONAL","ONDESTORY");
    }
}
