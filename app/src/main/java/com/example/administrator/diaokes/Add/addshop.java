package com.example.administrator.diaokes.Add;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.diaokes.Bean.CommentBean;
import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.db.shopitem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/8/2.
 */

public class addshop extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imageView;
    private TextView submit;
    private EditText name;
    private EditText locate;
    private EditText detail;
    private EditText lianxi;
    private String x = null;
    private String y = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_shop_layout);

        imageView = findViewById(R.id.choose_address);
        toolbar = findViewById(R.id.add_shop_toolbar);
        submit = findViewById(R.id.shop_submit);
        name = findViewById(R.id.shop_name);
        locate = findViewById(R.id.shop_address);
        detail = findViewById(R.id.shop_detail);
        lianxi = findViewById(R.id.shop_lianxi);

        setSupportActionBar(toolbar);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.haokan));
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("添加渔具店");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addshop.this,choose.class);
                startActivityForResult(intent,2);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("name",name.getText().toString())
                                    .add("address",locate.getText().toString())
                                    .add("locate",x+","+y)
                                    .add("lianxi",lianxi.getText().toString())
                                    .add("detail",detail.getText().toString())
                                    .build();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://192.168.1.103:8080/tomcats/AddShop")
                                    .post(requestBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            final String re = response.body().string();
                            addshop.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(addshop.this,re+"等待后台管理员审核",Toast.LENGTH_LONG).show();
                                }
                            });
                        }catch (Exception e){
                            Log.e("错误信息",e.getMessage());
                        }
                    }
                }).start();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 2:
                if(resultCode == -1){
                    x = data.getStringExtra("x");
                    y = data.getStringExtra("y");
                }

        }
    }
}
