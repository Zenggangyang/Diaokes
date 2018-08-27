package com.example.administrator.diaokes.Rank;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.recyclerView.viewpageAdapter;

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

public class rank extends AppCompatActivity{
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<Fragment> list1;
    private String[] titles = {"全站排行", "好友排行"};
    private String s = null;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    s = (String) msg.obj;
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rank_layout);
        toolbar = findViewById(R.id.rank_toolbar);
        viewPager = findViewById(R.id.rank_page);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        setSupportActionBar(toolbar);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.haokan));

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

        Intent intent = getIntent();

        list1 = new ArrayList<>();
        list1.add(Fragment1.newInstance(intent.getStringExtra("data")));
        list1.add(Fragment2.newInstance(intent.getStringExtra("data")));

        viewPager.setAdapter(new viewpageAdapter(getSupportFragmentManager(),list1,titles));
        tabLayout.setupWithViewPager(viewPager);
    }
}
