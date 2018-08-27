package com.example.administrator.diaokes.Friends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.db.frienditem;
import com.example.administrator.diaokes.recyclerView.friendAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/8.
 */

public class friend extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private List<frienditem> list = new ArrayList<>();
    private friendAdapter adapter;
    private frienditem[] frienditems = {new frienditem("刘俊","钓客荣耀","我的未来不是梦",R.drawable.b),new frienditem("陈远芳","钓客小白","很喜欢在阳光下 吞云吐雾",R.drawable.f),
            new frienditem("余梦柔","钓客白银","我滴大脑100%处于无聊阶段",R.drawable.d),new frienditem("邹聪聪","钓客青铜","如果所有的衣服都懂得自己洗澡就好了。",R.drawable.h),
            new frienditem("王露","钓客门徒","空有一颗减肥的心，却生了一条吃货的命。",R.drawable.c),new frienditem("罗姗姗","钓客王者","星期三，我生日。我希望一切太平。",R.drawable.q),
            new frienditem("周成杰","钓客砖石","有着长发有着腰只不过是水桶腰",R.drawable.o),new frienditem("朱茜茜","钓客星耀","只有一直吃，才能留住我饱满得性格",R.drawable.l)};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_layout);

        recyclerView = findViewById(R.id.friend_recycler);
        toolbar = findViewById(R.id.friend_toolbar);
        setSupportActionBar(toolbar);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.floatcolor));
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("我的好友");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        inits();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(friend.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new friendAdapter(friend.this,list);
        recyclerView.setAdapter(adapter);
    }

    private void inits(){
        list.clear();
        for (int i = 0;i<frienditems.length;i++){
            list.add(frienditems[i]);
        }
    }
}
