package com.example.administrator.diaokes.Shop;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.db.addressdate;
import com.example.administrator.diaokes.db.shopdata;
import com.example.administrator.diaokes.recyclerView.shopcarAdapter;
import com.example.administrator.diaokes.recyclerView.shopitem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/28.
 */

public class shopCar extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private shopcarAdapter adapter;
    private List<shopitem> list = new ArrayList<>();
    private TextView cost;
    private CheckBox checkBox;
    private Button button;
    /*
    private shopitem[] shopitems = {new shopitem(R.drawable.q,"中海鱼裤",16),new shopitem(R.drawable.y,"黄奕渔具",24),new shopitem(R.drawable.v,"亦云饵料",36),
            new shopitem(R.drawable.i,"古香鱼篓",19),new shopitem(R.drawable.x,"北忞鱼线",10),new shopitem(R.drawable.l,"南海神钩",90)};
            */
    private shopdata datas;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopcar_layout);
        toolbar = findViewById(R.id.shop_car_toolbar);
        recyclerView = findViewById(R.id.shop_car_recycler);
        cost = findViewById(R.id.shop_car_cost);
        button = findViewById(R.id.shop_car_jiesuan);
        checkBox = findViewById(R.id.shop_car_check);

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
        toolbar.setTitle("购物车");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        Gson gson1 = new Gson();
        datas = gson1.fromJson(intent.getStringExtra("data"), shopdata.class);

        inits();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new shopcarAdapter(list,this);
        recyclerView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(shopCar.this,"支付完成",Toast.LENGTH_LONG).show();
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox.isChecked()){
                    cost.setText("$"+adapter.getAllcost()+"");
                }
            }
        });
    }
    private void inits(){
        list.clear();
        shopitem[] shopitems = new shopitem[datas.getList().size()];

        for (int i = 0;i<shopitems.length;i++){
            shopitems[i] = new shopitem(R.drawable.a,datas.getList().get(i).getName(),Integer.parseInt(datas.getList().get(i).getCost()));
        }
        for (int k = 0;k<shopitems.length;k++){
            list.add(shopitems[k]);
        }
    }
}
