package com.example.administrator.diaokes.tongzhi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.diaokes.OkHttp.HttpUtil;
import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.db.addressdate;
import com.example.administrator.diaokes.db.notifylist;
import com.example.administrator.diaokes.recyclerView.shopcarAdapter;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/30.
 */

public class tongzhi extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private tongzhiAdapter adapter;
    private List<tzitem> list = new ArrayList<>();
    private notifylist lists;
    private String datas = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tongzhi_layout);

        recyclerView = findViewById(R.id.tongzhi_recycler);
        toolbar = findViewById(R.id.tongzhi_msg);
        setSupportActionBar(toolbar);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.floatcolor));

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            //去除默认显示标题
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String date = prefs.getString("date",null);
        if(date != null){
            datas = date.substring(1,date.length() - 3);
            Gson gson1 = new Gson();
            lists = gson1.fromJson(datas, notifylist.class);
        }else {
            RequestDate();
        }

        inits();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new tongzhiAdapter(list,this);
        recyclerView.setAdapter(adapter);
    }

    private void inits(){
        list.clear();
        tzitem[] tzitems = new tzitem[lists.getList().size()];
        for (int i = 0;i<tzitems.length;i++){
            tzitems[i] = new tzitem(lists.getList().get(i).getName(),lists.getList().get(i).getContent(),lists.getList().get(i).getDate());
        }
        for(int k = tzitems.length-1;k>=0;k--){
            list.add(tzitems[k]);
        }
    }
    private void RequestDate(){
        String url = "http://192.168.1.103:8080/tomcats/notify";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String date = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(tongzhi.this).edit();
                editor.putString("date",date);
                editor.apply();
            }
        });
    }
}
