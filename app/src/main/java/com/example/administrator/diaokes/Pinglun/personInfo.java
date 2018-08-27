package com.example.administrator.diaokes.Pinglun;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.diaokes.Bean.person;
import com.example.administrator.diaokes.DongTai.Dongtai;
import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.recyclerView.personinfo;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/24.
 */

public class personInfo extends AppCompatActivity {
    private Toolbar toolbar;
    private List<person> list = new ArrayList<>();
    private TextView submit;
    private CircleImageView chooseimg;
    private String path = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personinfo_layout);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.floatcolor));

        toolbar = findViewById(R.id.person_toolbar);
        setSupportActionBar(toolbar);
        submit = findViewById(R.id.person_submit);
        chooseimg = findViewById(R.id.person_chooseimg);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            //去除默认显示标题
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("我的资料");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String sex = intent.getStringExtra("sex");
        String birth = intent.getStringExtra("birth");
        String address = intent.getStringExtra("address");
        String person = intent.getStringExtra("person");
        String img = intent.getStringExtra("img");

        Glide.with(this).load(img).into(chooseimg);

        inits(name,sex,birth,address,person);
        final RecyclerView recyclerView = findViewById(R.id.person_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        final personinfo adapter = new personinfo(list,this);
        recyclerView.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String[] lists = new String[adapter.getItemCount()];
                            lists = adapter.getStrings().substring(1,adapter.getStrings().length()-2).split(",");
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("name",lists[0])
                                    .add("id","2")
                                    .add("sex",lists[1])
                                    .add("birth",lists[2])
                                    .add("address",lists[3])
                                    .add("person",lists[4])
                                    .build();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://192.168.1.103:8080/tomcats/updateperson")
                                    .post(requestBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String re = response.body().string();
                        }catch (Exception e){
                            Log.e("错误信息",e.getMessage());
                        }
                    }
                }).start();
                Intent found = new Intent();
                found.putExtra("text",adapter.getStrings().substring(1,adapter.getStrings().length()-2).split(",")[0]);
                found.putExtra("path",path);
                setResult(RESULT_OK,found);
                Toast.makeText(personInfo.this,"更新数据成功",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        chooseimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });
    }

    private void inits(String name,String sex,String birth,String address,String person){
        person person0 = new person("昵称",name);
        list.add(person0);
        person person1 = new person("性别",sex);
        list.add(person1);
        person person2 = new person("生日",birth);
        list.add(person2);
        person person3 = new person("地址",address);
        list.add(person3);
        person person4 = new person("签名",person);
        list.add(person4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
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
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }else {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                chooseimg.setImageBitmap(bitmap);
            }
        }else {
            Toast.makeText(this,"fail to get image",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    chooseimg.setImageBitmap(bitmap);
                }else {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
