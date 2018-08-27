package com.example.administrator.diaokes.Shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.diaokes.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/30.
 */

public class shopDetail extends AppCompatActivity implements OnBannerListener {
    private Toolbar toolbar;
    private Banner banner;
    private ArrayList<Integer> list_path;
    private ArrayList<String> list_title;
    private TextView name;
    private TextView cost;
    private Button pay;
    private Button shopcar;
    private String shopname;
    private String shopcost;
    private String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_detail_layout);
        toolbar = findViewById(R.id.shop_detail_toolbar);
        setSupportActionBar(toolbar);

        banner = (Banner) findViewById(R.id.shop_detail_banner);

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
        name = findViewById(R.id.shop_detail_name);
        cost = findViewById(R.id.shop_detail_cost);
        pay = findViewById(R.id.detail_goumai);
        shopcar = findViewById(R.id.detail_gouwu);

        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        shopname = intent.getStringExtra("name");
        shopcost = intent.getStringExtra("cost");
        name.setText(shopname);
        cost.setText("$"+shopcost);
        toolbar.setTitle(shopname);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        inits();

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(shopDetail.this,"购买成功",Toast.LENGTH_LONG).show();
            }
        });
        shopcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("name",shopname)
                                    .add("cost",shopcost)
                                    .add("path",path)
                                    .build();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://192.168.1.103:8080/tomcats/insertshop")
                                    .post(requestBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            final String re = response.body().string();
                            shopDetail.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(shopDetail.this,re.substring(0,4),Toast.LENGTH_LONG).show();
                                }
                            });
                        }catch (Exception e){
                            Log.e("error",e.getMessage());
                        }
                    }
                }).start();
            }
        });

    }
    private void inits(){
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();

        list_path.add(R.drawable.yuxian5);
        list_path.add(R.drawable.yuxian3);
        list_path.add(R.drawable.yuxian1);
        list_path.add(R.drawable.yuxian4);
        list_title.add("钓鱼好日子");
        list_title.add("赣州");
        list_title.add("万安水库");
        list_title.add("清塘");
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();

    }

    @Override
    public void OnBannerClick(int position) {
        Log.i("tag", "你点了第"+position+"张轮播图");
    }

    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }
}
