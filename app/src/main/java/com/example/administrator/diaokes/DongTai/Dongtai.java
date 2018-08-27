package com.example.administrator.diaokes.DongTai;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.diaokes.Fragment.Found;
import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.recyclerView.ImageRecycler;
import com.example.administrator.diaokes.recyclerView.imageItem;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/7/12.
 */

public class Dongtai extends AppCompatActivity {
    public static final int CHOOSE_PHOTO = 2;
    private Toolbar toolbar;
    private TextView send;
    private ImageView chooseImage;
    private RelativeLayout relativeLayout;
    private ImageRecycler adapter;
    private List<imageItem> imagelist = new ArrayList<>();
    private imageItem[] imageItems = {new imageItem("test")};
    private CircleImageView weibo;
    private boolean click = true;
    private boolean bqclick = true;
    private boolean ddclick = true;
    private TextView didian;
    private TextView biaoqian;

    private EditText sendText;

    private String path =null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dongtai_layout);
        toolbar = findViewById(R.id.dttoolbar);
        setSupportActionBar(toolbar);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.floatcolor));

        send = findViewById(R.id.sendtext);
        chooseImage = findViewById(R.id.chooseImage);
        relativeLayout = findViewById(R.id.xuxian);
        weibo = findViewById(R.id.weibo);
        didian = findViewById(R.id.didian);
        biaoqian = findViewById(R.id.addbiaoqain);
        sendText = findViewById(R.id.sendText);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            //去除默认显示标题
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("发动态");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        inits();
        RecyclerView recyclerView = findViewById(R.id.imagerecycler);
        GridLayoutManager layoutManager = new GridLayoutManager(Dongtai.this,4);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ImageRecycler(Dongtai.this,imagelist);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent found = new Intent();
                found.putExtra("text",sendText.getText().toString());
                found.putExtra("path",path);
                setResult(RESULT_OK,found);
                finish();
            }
        });
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(Dongtai.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Dongtai.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    openAlbum();
                }
            }
        });

        weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click == true) {
                    weibo.setImageResource(R.drawable.weibo2);
                    click = false;
                }else {
                    weibo.setImageResource(R.drawable.weibo);
                    click = true;
                }
            }
        });

        didian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ddclick) {
                    didian.setBackgroundResource(R.drawable.recban);
                    ddclick = false;
                }else {
                    didian.setBackgroundResource(R.drawable.reccircle);
                    ddclick =true;
                }
            }
        });
        biaoqian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bqclick) {
                    biaoqian.setBackgroundResource(R.drawable.recban);
                    bqclick = false;
                }else {
                    biaoqian.setBackgroundResource(R.drawable.reccircle);
                    bqclick = true;
                }
            }
        });
    }

    private void inits(){
        imagelist.clear();
        imagelist.add(imageItems[0]);
    }
    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(Dongtai.this,"You denied the permission",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    if(Build.VERSION.SDK_INT >= 19){
                        handleImageOnKitKat(data);
                    }else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
        }
    }

    //4.4以上系统,选取相册中照片不返回图片真实的Uri,而是一个封装的Uri,需要进行解析
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            int i = adapter.getItemCount();
            adapter.addimage(i,imagePath);
        }else {
            Toast.makeText(Dongtai.this,"fail to get image",Toast.LENGTH_LONG).show();
        }
    }

    private int getWidth(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screen = dm.widthPixels;
        return screen;
    }
}
