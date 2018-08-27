package com.example.administrator.diaokes.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.example.administrator.diaokes.Bean.CommentDetailBean;
import com.example.administrator.diaokes.Bean.DongTaiBean;
import com.example.administrator.diaokes.Bean.DtaiBean;
import com.example.administrator.diaokes.DongTai.Dongtai;
import com.example.administrator.diaokes.DongTai.Shipin;
import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.SQLServer.sqlConn;
import com.example.administrator.diaokes.db.FoundDate;
import com.example.administrator.diaokes.recyclerView.recyclerAdapter;
import com.example.administrator.diaokes.recyclerView.recyclerItem;
import com.google.gson.Gson;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.security.Key;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/6/27.
 */

public class Found extends Fragment implements View.OnClickListener {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private Dialog dialog;
    private View inflate;
    private TextView dongtai;
    private TextView shipin;
    private FloatingActionButton sendDT;
    private recyclerAdapter adapter;
    private String imagepath = null;
    private String imgtext = null;
    private RecyclerView recyclerView;
    private List<recyclerItem> list1 = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private static String username;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.found_layout,container,false);
        appBarLayout = view.findViewById(R.id.found_appBar2);
        collapsingToolbarLayout = view.findViewById(R.id.found_collapsing_toolbar2);
        sendDT = view.findViewById(R.id.sendDT);

        swipeRefreshLayout = view.findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.floatcolor,R.color.colorPrimaryDark);

        final ImageView imageView = view.findViewById(R.id.found_background_image2);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String date = prefs.getString("img",null);
        Glide.with(this).load(date).into(imageView);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset <= -imageView.getHeight() / 2){
                    collapsingToolbarLayout.setTitle("我的动态");
                    collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
                    Window window = getActivity().getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getActivity().getResources().getColor(R.color.floatcolor));
                }else {
                    collapsingToolbarLayout.setTitle("");
                    Window window = getActivity().getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getActivity().getResources().getColor(R.color.transparent));
                }
            }
        });
        sendDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                show(view);
            }
        });
        LitePal.initialize(getActivity());


        inits();
        recyclerView = view.findViewById(R.id.found_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new recyclerAdapter(getActivity(),list1);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                List<FoundDate> list = DataSupport.findAll(FoundDate.class);
                String text = list.size()-adapter.getItemCount()+"";
                for (int i =adapter.getItemCount();i<list.size();i++){
                    if(list.get(i).getType() == 0){
                        Bitmap bitmap = BitmapFactory.decodeFile(list.get(i).getPath());
                        adapter.addImageItem(0,username,list.get(i).getContent(),0,bitmap);
                        recyclerView.scrollToPosition(0);
                    }else {
                        Bitmap bitmap = getVideoThumbnail(list.get(i).getPath(),1200,650, MediaStore.Video.Thumbnails.MINI_KIND);
                        adapter.addVideoItem(0,username,list.get(i).getContent(),0,bitmap,list.get(i).getPath());
                        recyclerView.scrollToPosition(0);
                    }
                }
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(),"已更新"+text+"条数据",Toast.LENGTH_LONG).show();
                //DataSupport.deleteAll(FoundDate.class,"type == ?","1");
            }
        });
        return view;
    }

    public void show(View view){
        dialog = new Dialog(getActivity(),R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.dialog_layout, null);
        dialog.setContentView(inflate);
        Window dialogWin = dialog.getWindow();
        dialogWin.setGravity(Gravity.BOTTOM);
        dongtai = inflate.findViewById(R.id.takePhoto);
        shipin = inflate.findViewById(R.id.choosePhoto);
        dongtai.setOnClickListener(this);
        shipin.setOnClickListener(this);
        WindowManager.LayoutParams lp = dialogWin.getAttributes();
        //设置dialog离底部的距离
        lp.y = 50;
        dialogWin.setAttributes(lp);
        dialog.show();
    }

    public void inits(){
        list1.clear();
        List<FoundDate> list = DataSupport.findAll(FoundDate.class);
        recyclerItem[] recyclerItems = new recyclerItem[list.size()];
        int i = list.size() - 1;
        for(FoundDate date1 : list){
            if(date1.getType() == 0) {
                Bitmap bitmap = BitmapFactory.decodeFile(date1.getPath());
                recyclerItems[i] = new recyclerItem(date1.getName(), 0, date1.getContent(), 0, bitmap, date1.getPath(),date1.getTime());
            }else {
                Bitmap bitmap = getVideoThumbnail(date1.getPath(),1200,650, MediaStore.Video.Thumbnails.MINI_KIND);
                recyclerItems[i] = new recyclerItem(date1.getName(), 0, date1.getContent(), 1, bitmap, date1.getPath(),date1.getTime());
            }
            i--;
        }

        for(int i1 = 0;i1<recyclerItems.length;i1++){
            list1.add(i1,recyclerItems[i1]);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.takePhoto:
                Intent intent = new Intent(getActivity(), Dongtai.class);
                startActivityForResult(intent,1);
                break;
            case R.id.choosePhoto:
                Intent intent1 = new Intent(getActivity(),Shipin.class);
                startActivityForResult(intent1,2);
                break;
        }
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == -1){
                    Bitmap bitmap = null;
                    imgtext = data.getStringExtra("text");
                    imagepath = data.getStringExtra("path");
                    if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    }else {
                        bitmap = BitmapFactory.decodeFile(imagepath);
                        adapter.addImageItem(0,username,imgtext,0,bitmap);
                        recyclerView.scrollToPosition(0);

                        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                        String data1 = df.format(new Date()).toString();

                        List<FoundDate> list = DataSupport.findAll(FoundDate.class);
                        FoundDate date = new FoundDate();
                        date.setName(username);
                        date.setContent(imgtext);
                        date.setPath(imagepath);
                        date.setZan(0);
                        date.setId(list.size()+"");
                        date.setType(0);
                        date.setTime(data1);
                        date.save();
                    }
                }
                break;
            case 2:
                if(resultCode == -1){
                    Bitmap bitmap;
                    String content = data.getStringExtra("content");
                    String videoname = data.getStringExtra("name");
                    String path = data.getStringExtra("video");
                    if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    }else {
                        bitmap = getVideoThumbnail(path,1200,650, MediaStore.Video.Thumbnails.MINI_KIND);
                        adapter.addVideoItem(0,username,content,1,bitmap,path);
                        recyclerView.scrollToPosition(0);

                        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                        String data1 = df.format(new Date()).toString();

                        List<FoundDate> list = DataSupport.findAll(FoundDate.class);
                        FoundDate date = new FoundDate();
                        date.setName(username);
                        date.setContent(content);
                        date.setPath(path);
                        date.setZan(0);
                        date.setType(1);
                        date.setId(list.size()+"");
                        date.setTime(data1);
                        date.save();
                    }
                }
                break;
        }
    }


    private Bitmap getVideoThumbnail(String path,int width,int height,int kind){
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(path, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
                    adapter.addImageItem(0,username,imgtext,0,bitmap);
                    recyclerView.scrollToPosition(0);
                }else {
                    Toast.makeText(getActivity(),"You denied the permission",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public static Fragment newInstance(String username1){
        Found _found = new Found();
        username = username1;
        return _found;
    }
}
